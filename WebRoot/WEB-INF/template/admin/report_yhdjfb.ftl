<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>首充用户等级分布报表 </title>
<link href="${base}/template/css/base.css" rel="stylesheet" type="text/css" />
<link href="${base}/template/js/lib/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
<link href="${base}/template/js/lib/ligerUI/skins/Gray/css/all.css" rel="stylesheet" type="text/css" />
<link href="${base}/template/css/list.css" rel="stylesheet" type="text/css" />
<script src="${base}/template/js/lib/jquery/jquery-1.3.2.min.js" type="text/javascript"></script>
<script src="${base}/template/js/lib/jquery/util.js" type="text/javascript"></script>
<script src="${base}/template/js/lib/jquery/jquery.form.js" type="text/javascript"></script>
<script src="${base}/template/js/lib/ligerUI/js/ligerui.all.js" type="text/javascript"></script>
<script src="${base}/template/js/lib/ligerUI/js/plugins/ligerMonthEditor.js" type="text/javascript"></script>
</head>
<body class="list" style=" overflow-x: hidden;">
	<div class="page">
		<#include "/WEB-INF/template/admin/menupath.ftl" />
		<form id="listForm" action="menu!list.action" method="post">
			<div class="pageHeader">
				<div class="searchtitle">查询条件</div>
				<div class="searchform">
				<table>
					<th>按月&nbsp;<input type="radio" name="dateType" value="0">&nbsp;&nbsp;&nbsp;&nbsp;自定义日期查询&nbsp;<input type="radio" name="dateType" value="1" checked="checked"></th>
					<th class="dateType0" style="display:none">选择月：</th>
					<td class="dateType0" style="display:none">
						<input type="text" name="month" id="month"/>
					</td>
					<th class="dateType1">开始日期：</th>
					<td class="dateType1">
						<input type="text" name="beginDate" id="beginDate"/>
					</td>
					<th class="dateType1">结束日期：</th>
					<td class="dateType1">
						<input type="text" name="endDate" id="endDate"/>
					</td>
				</table>
				<ul>
					<li>
						<label>渠道：</label>
						<span class="sli"><input name="channel" id="channel"/></span>
					</li>
					<li>
						<label>子渠道：</label>
						<span class="sli"><input name="subChannel" id="subChannel"/></span>
					</li>					
					<li>
						<label>游戏：</label>
						<span class="sli"><input name="games" id="games"/></span>
					</li>
					<li>
						<label>区服：</label>
						<span class="sli"><input name="server" id="server"/></span>
					</li>
					<li>
						<label>用户类型：</label>
						<span class="sli"><input name="userType" id="userType"/></span>
					</li>
					<li class="fr">
						<input type="button" class="formButton" value="查询" id="searchButton" href="${base}/admin/menu!add.action"/>
					</li>
				</ul> 
				</div>
			</div>
		</form>
		<div class="listBar">
			<h1>
			首充用户等级分布报表
			</h1>
		</div>
		<div id="maingrid"></div> 
	</div>
 	<script type="text/javascript">
        $(function (){
            var width=$(window).width()-50;
            $(".deleteAction").live("click",function(){
            	var url = $(this).attr("url");
            		 top.$.ligerDialog.confirm('提示内容', function (yes){
                          if(yes){
                          	$.ajax({
								url: url,
								dataType: "json",
								type: "POST" ,
								async: false,
								success: function(data) {
									$.tip(data.status, data.message);
								}
							});
                          }
                     });
            		return false;
            });
            $(".editAction").live("click",function(){
            	alert("等完善");
            	//top.$.ligerDialog.open({title:'编辑菜单', onClose:function(){},isHidden:false,url:$(this).attr("href"), width: 650, height: 550,showMax:true,load:false }); 
            	return false;
            });
            $("input[name='dateType']").get(1).checked = true;
            $("input[name='dateType']").change(function(){
            	var type = $(this).val();
            	var hideType = type==0?1:0;
            	$(".dateType"+hideType).hide();
            	$(".dateType"+type).show();
            });
            $("#month").ligerMonthEditor();
            var beginDate = $.date.getFirstDate();
            var endDate = $.date.getLastDate();
            $("#beginDate").ligerDateEditor({
            	initValue:$.date.getFormatDate(beginDate,'yyyy-MM-dd')
            });
            $("#endDate").ligerDateEditor({
            	initValue:$.date.getFormatDate(endDate,'yyyy-MM-dd')
            });
            $("#channel").ligerComboBox({
            	url:'${base}/admin/report!report.action?reportName=basedata_channel',
            	initValue:'-1',
            	onSelected:function(value,text){
            		if($("#subChannel").ligerGetComboBoxManager()){
            		    var subChannel=$("#subChannel").ligerGetComboBoxManager();
            		    subChannel.data=null;
            		    subChannel.options.url='${base}/admin/report!report.action?reportName=basedata_subChannel&parentId='+value;
            		    subChannel.bulidContent();
            		}else{
	            		$("#subChannel").ligerComboBox({
			            	url:'${base}/admin/report!report.action?reportName=basedata_subChannel&parentId='+value,
			            	initValue:'-1'
			            });
            		}
            		
            	}
            });
            $("#games").ligerComboBox({
            	url:'${base}/admin/report!report.action?reportName=basedata_games',
            	initValue:'-1'
            });
            $("#server").ligerComboBox({
            	url:'${base}/admin/report!report.action?reportName=basedata_server',
            	initValue:'-1'
            });
            $("#userType").ligerComboBox({
            	url:'${base}/admin/report!report.action?reportName=basedata_userType',
            	initValue:'-1'
            });
            
            
            var grid = $("#maingrid").ligerGrid({
                url: '${base}/admin/report!report.action', 
                parms:{
                	reportName:'FirstChargeReport',
                	beginDate:$.date.getFormatDate(beginDate,'yyyyMMdd'),
                	endDate:$.date.getFormatDate(endDate,'yyyyMMdd'),
                	server:-1,
                	channel:-1,
                	subCannel:-1,
                	games:-1,
                	userType:-1
                },
                sortName: 'id', 
                columns: [
                    { display: '用户等级', name: 'roleLevel',width: parseInt(width*0.1),title: '用户角色等级'},
                    { display: '首充人数', name: 'firstChargeUserCount',width: parseInt(width*0.1),title: '对应等级首充用户之和'},
	                { display: '充值次数', name: 'totalChargeCount',width: parseInt(width*0.1),title: '对应首充人数的充值次数'},
	                { display: '人数占比', name: 'chargeUserRate',width: parseInt(width*0.1),title: '（对应等级首充人数/所选日期首充人数之和）*100%'},
	                { display: '首充金额(元)', name: 'totalFirstChargeMoney',width: parseInt(width*0.1),title: '对应等级首充人数的充值金额'},
	                { display: '首充金额占比', name: 'firstChargeMoneyRate',width: parseInt(width*0.1),title: '（对应等级首充金额/所选日期首充金额之和）*100%'}
                ],
                width:'100%',
                height:'100%',
                heightDiff:-2,
                pageSizeOptions: [10, 15, 20] 
            });
            $("#searchButton").click(function(){
                var form={};
            	if($("input[name='dateType']:checked").val()==0){
            		var year = $("#month").ligerGetMonthEditorManager().selectedMonth.year;
            		var month = $("#month").ligerGetMonthEditorManager().selectedMonth.month;
            		var beginDate = $.date.getFormatFirstDateByMonth(year,month,'yyyyMMdd');
            		form.beginDate=beginDate;
            		var endDate = $.date.getFormatLastDateByMonth(year,month,'yyyyMMdd');
            		form.endDate=endDate;
            	}else{
            		form.beginDate=$.date.getFormatDate($("#beginDate").ligerGetDateEditorManager().usedDate,'yyyyMMdd');
            		form.endDate=$.date.getFormatDate($("#endDate").ligerGetDateEditorManager().usedDate,'yyyyMMdd');
            	}
            	form.reportName ='FirstChargeReport';
            	form.channel = $("#channel").ligerGetComboBoxManager().getValue();
            	form.subChannel = $("#subChannel").ligerGetComboBoxManager().getValue();
            	form.games = $("#games").ligerGetComboBoxManager().getValue();
            	form.server = $("#server").ligerGetComboBoxManager().getValue();
            	form.userType= $("#userType").ligerGetComboBoxManager().getValue();
            	grid.set('parms', form);
            	grid.loadData();
            });
            
        });
    </script>
</body>
</html>