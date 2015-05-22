<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>实时在线统计</title>
<link href="${base}/template/css/base.css" rel="stylesheet" type="text/css" />
<link href="${base}/template/js/lib/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
<link href="${base}/template/js/lib/ligerUI/skins/Gray/css/all.css" rel="stylesheet" type="text/css" />
<link href="${base}/template/css/list.css" rel="stylesheet" type="text/css" />
<script src="${base}/template/js/lib/jquery/jquery-1.3.2.min.js" type="text/javascript"></script>
<script src="${base}/template/js/lib/jquery/util.js" type="text/javascript"></script>
<script src="${base}/template/js/lib/ligerUI/js/ligerui.all.js" type="text/javascript"></script>
<script language="JavaScript" src="${base}/template/js/lib/chart/FusionCharts.js"></script>
</head>
<body class="list" style=" overflow-x: hidden;">
	<div class="page">
		<#include "/WEB-INF/template/admin/menupath.ftl" />
		<form id="listForm" action="menu!list.action" method="post">
			<div class="pageHeader">
				<div class="searchtitle">查询条件</div>
				<div class="searchform">
				<table>
					<th class="dateType1">开始日期：</th>
					<td class="dateType1">
						<input type="text" name="beginDate" id="beginDate"/>
					</td>
					<th>区服：</th>
					<td>
						<input name="server" id="server"/>
					</td>
					<td>
						<input type="button" class="formButton" value="查询" id="searchButton" href="${base}/admin/menu!add.action"/>
					</td>
				</table>
				</div>
			</div>
		</form>
		<div id="zsdiv" align="center"></div> 
	</div>
 	<script type="text/javascript">
        $(function (){
            var beginDate = new Date();
            $("#beginDate").ligerDateEditor({
            	initValue:$.date.getFormatDate(beginDate,'yyyy-MM-dd')
            });
            $("#server").ligerComboBox({
            	url:'${base}/admin/report!report.action?reportName=basedata_server',
            	initValue:'-1'
            });
            function getChartData(params){
                 var data={reportName:'OnlineReport',server:-1,beginDate:$.date.getFormatDate(beginDate,'yyyyMMdd'),}
            	 $.extend(data,params||{});
	             $.ajax({ 
					type:"POST",
				    url: "${base}/admin/report!report.action",
				    data:data,
				    success: function(json) { 
			    	    var chart = new FusionCharts("${base}/template/js/lib/chart/Line.swf", "ChartId", $(window).width()-20, $(window).height()-105, "0", "1");
					    chart.setDataXML(json);		   
					    chart.render("zsdiv");
				    }
				}); 
            }
            getChartData();
			$("#searchButton").click(function(){
                var form={};
            	form.beginDate=$.date.getFormatDate($("#beginDate").ligerGetDateEditorManager().usedDate,'yyyyMMdd');
            	form.server = $("#server").ligerGetComboBoxManager().getValue();
            	getChartData(form)
            });
        });
    </script>
</body>
</html>