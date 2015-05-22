<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>玩家留存率统计报表 </title>
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
				<table>
					<th>渠道：</th>
					<td>
						<input name="platform" id="platform"/>
					</td>
					<th>游戏：</th>
					<td>
						<input name="games" id="games"/>
					</td>
					<th>用户类型：</th>
					<td>
						<input name="userType" id="userType"/>
					</td>
					<td>
						<input type="button" class="formButton" value="查询" id="searchButton" href="${base}/admin/menu!add.action"/>
					</td>
				</table>
				</div>
			</div>
		</form>
		<div class="listBar">
			<h1>
			玩家留存率统计报表列表
			</h1>
		</div>
		<div id="maingrid"></div> 
	</div>
 	<script type="text/javascript">
        $(function (){
            var width=$(window).width()-50;
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
            $("#platform").ligerComboBox({
            	url:'${base}/admin/report!report.action?reportName=basedata_platform',
            	initValue:'-1'
            });
            $("#games").ligerComboBox({
            	url:'${base}/admin/report!report.action?reportName=basedata_games',
            	initValue:'-1'
            });
            $("#userType").ligerComboBox({
            	url:'${base}/admin/report!report.action?reportName=basedata_userType',
            	initValue:'-1'
            });
            
            
            var grid = $("#maingrid").ligerGrid({
                url: '${base}/admin/report!report.action', 
                parms:{
                	reportName:'RoleLevelReport',
                	beginDate:$.date.getFormatDate(beginDate,'yyyyMMdd'),
                	endDate:$.date.getFormatDate(endDate,'yyyyMMdd'),
                	platform:-1
                },
                sortName: 'id', 
                columns: [
                    { display: '日期', name: 'reportDate',width: parseInt(width*0.1),title: '等级'},
	                { display: '注册帐号数', name: 'produceCopper',width: parseInt(width*0.1),title: '所选日期当前等级的角色总数'},
	                { display: '创建角色数', name: 'consumeCopper',width: parseInt(width*0.1),title: '（当前等级人数分布/总角色数）*100%'},
	                { display: '等级', name: 'reportDate',width: parseInt(width*0.1),title: '等级'},
	                { display: '当前等级人数分布', name: 'produceCopper',width: parseInt(width*0.1),title: '所选日期当前等级的角色总数'},
	                { display: '当前等级分布比率', name: 'consumeCopper',width: parseInt(width*0.1),title: '（当前等级人数分布/总角色数）*100%'},
	                { display: '等级', name: 'reportDate',width: parseInt(width*0.1),title: '等级'},
	                { display: '当前等级人数分布', name: 'produceCopper',width: parseInt(width*0.1),title: '所选日期当前等级的角色总数'},
	                { display: '当前等级分布比率', name: 'consumeCopper',width: parseInt(width*0.1),title: '（当前等级人数分布/总角色数）*100%'}
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
            	form.reportName ='RoleLevelReport';
            	form.platform = $("#platform").ligerGetComboBoxManager().getValue();
            	form.games = $("#games").ligerGetComboBoxManager().getValue();
            	form.userType= $("#userType").ligerGetComboBoxManager().getValue();
            	grid.set('parms', form);
            	grid.loadData();
            });
            
        });
    </script>
</body>
</html>