<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>日常数据报表 </title>
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
					<th><input type="radio" name="dateType" value="0">&nbsp;按月查询&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="dateType" value="1" checked="checked">&nbsp;自定义日期查询</th>
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
			日常数据报表列表
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
                	reportName:'DailyReport',
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
                 { display: '日期', name: 'reportDate',width: parseInt(width*0.05),title: '日期'},
                { display: '下载量', name: 'downloadCount',width: parseInt(width*0.05),title: '下载客户端数'},
                { display: '激活用户数', name: 'firstConnectUserCount',width: parseInt(width*0.05),title: '首次连上服务器的客户端数'},
                { display: '新增用户', name: 'registerUserCount',width: parseInt(width*0.05),title: '当天新增的注册账号数'},
                { display: '新增有角用户', name: 'registerCreateRoleUserCount',width: parseInt(width*0.05),title: '当天新增注册并有角色的用户数'},
                { display: '新增有效用户', name: 'registerEffectUserCount',width: parseInt(width*0.05),title: '有角色且等距>3的新增用户'},
                { display: '登陆用户', name: 'loginUserCount',width: parseInt(width*0.05),title: '当天所有登录过的用户数'},
                { display: '次日留存用户', name: 'keepUserCount',width: parseInt(width*0.05),title: '昨天注册的用户今天再次登录'},
                { display: '次日留存率', name: 'keepUserRate',width: parseInt(width*0.05),title: '次日留存用户/昨天注册用户'},
                { display: '首充用户', name: 'firstChargeUserCount',width: parseInt(width*0.05),title: '在游戏中第一次充值的用户'},
                { display: '充值用户', name: 'todayChargeUser',width: parseInt(width*0.05),title: '当天充值的用户数'},
                { display: '充值金额', name: 'todayChargeMoney',width: parseInt(width*0.05),title: '当天充值总额'},
                { display: '付费ARPU', name: 'arpu',width: parseInt(width*0.05),title: '当天消费金额/当天充值人数'},
                { display: '日活跃用户', name: 'activeUserCount',width: parseInt(width*0.05),title: '当天登陆一次的用户，不包括新增用户'},
				{ display: '周活跃用户', name: 'weekActiveUserCount',width: parseInt(width*0.05),title: '最近7天内有2天登录或总登录时间>=2小时'},
                { display: '活跃用户ARPU', name: 'activeArpu',width: parseInt(width*0.05),title: '日收入/活跃用户'},
                { display: '忠诚用户', name: 'loyalUserCount',width: parseInt(width*0.05),title: '连续三天登录的用户数'},
                { display: 'DAU/MAU', name: 'DAU',width: parseInt(width*0.05),title: '日登陆用户数/月登录用户数'},
                { display: '回流用户', name: 'comebackUserCount',width: parseInt(width*0.05),title: '当天前3天未登陆过游戏的旧注册账号'},
                { display: '最高在线', name: 'highestOnlineCount',width: parseInt(width*0.05),title: '当天在线的人数峰值'},
                { display: '平均在线', name: 'avgOnlineCount',width: parseInt(width*0.05),title: '平均当天各时间段的在线用户数'},
                { display: '人均次数', name: 'perUserAvgLoginCount',width: parseInt(width*0.05),title: '当天每人平均登陆次数'},
                { display: '人均时长', name: 'perUserAvgOnlineTime',width: parseInt(width*0.05),title: '当天每人平均在线时长(单位:小时)'},
                { display: '活跃用户流失率', name: 'activeUserLossRate',width: parseInt(width*0.05),title: '(前一天登陆过的活跃用户数-今天再次登陆用户数)/前一天活跃用户数'},
                { display: '付费渗透率', name: 'payRate',width: parseInt(width*0.05),title: '当天充值人数/当天登陆人数'},
                { display: '创角率', name: 'createRoleRate',width: parseInt(width*0.05),title: '新增用户/激活用户数'}
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
            	form.reportName ='DailyReport';
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