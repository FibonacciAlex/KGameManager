<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>登录次数统计 </title>
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
					<th>游戏：</th>
					<td>
						<input name="games" id="games"/>
					</td>
					<th>区服：</th>
					<td>
						<input name="server" id="server"/>
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
			登录次数统计
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
                	reportName:'LoginInfoReport',
                	beginDate:$.date.getFormatDate(beginDate,'yyyyMMdd'),
                	endDate:$.date.getFormatDate(endDate,'yyyyMMdd'),
                	server:-1,
                	games:-1,
                	userType:-1
                },
                sortName: 'id', 
                columns: [
                 { display: '日期', name: 'reportDate',width: parseInt(width*0.08),title: '日期'},
				 { display: '登录1次', name: 'login1Count',width: parseInt(width*0.08),title: '登录1次'},
                { display: '登录2次', name: 'login2Count',width: parseInt(width*0.08),title: '登录2次'},
                { display: '登录多次', name: 'loginNCount',width: parseInt(width*0.08),title: '登录多次'}
                ],
                width:'100%',
                height:'100%',
                heightDiff:-2,
                pageSizeOptions: [10,30,50,100] 
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
            	form.reportName ='LoginInfoReport';
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