<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>菜单列表 </title>
<link href="${base}/template/css/base.css" rel="stylesheet" type="text/css" />
<link href="${base}/template/css/list.css" rel="stylesheet" type="text/css" />
<link href="${base}/template/js/lib/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
<link href="${base}/template/js/lib/ligerUI/skins/Gray/css/all.css" rel="stylesheet" type="text/css" />
<script src="${base}/template/js/lib/jquery/jquery-1.3.2.min.js" type="text/javascript"></script>
<script src="${base}/template/js/lib/ligerUI/js/core/base.js" type="text/javascript"></script>
<script src="${base}/template/js/lib/jquery/util.js" type="text/javascript"></script>
<script src="${base}/template/js/lib/ligerUI/js/plugins/ligerGrid.js" type="text/javascript"></script>
<script src="${base}/template/js/lib/ligerUI/js/plugins/ligerResizable.js" type="text/javascript"></script>
<script src="${base}/template/js/lib/ligerUI/js/plugins/ligerDrag.js" type="text/javascript"></script>
<script src="${base}/template/js/lib/ligerUI/js/plugins/ligerWindow.js" type="text/javascript"></script>
<script src="${base}/template/js/lib/ligerUI/js/plugins/ligerDateEditor.js" type="text/javascript"></script>
<script src="${base}/template/js/lib/ligerUI/js/plugins/ligerMonthEditor.js" type="text/javascript"></script>
<script language="JavaScript" src="${base}/template/js/lib/chart/FusionCharts.js"></script>
</head>
<body class="list" style=" overflow-x: hidden;">
	<div class="page">
		<#include "/WEB-INF/template/admin/menupath.ftl" />
		<div class="body">
		<div class="bodyLeft">
			<table class="listTable indexTable">
				<tr class="srtr">
					<td>
					今日收入：<span id="jrsr"></span>元
					</td>
					<td>
					昨日收入：<span id="zrsr"></span>元
					</td>
					<td>
					本月收入：<span id="bysr"></span>元
					</td>
					<td>
					上月收入：<span id="sysr"></span>元
					</td>
					<td>
					总收入：<span id="zsr"></span>元
					</td>
					<td style="border-right:0;">
					在线人数：<span id="zxrs"></span>人
					</td>
				</tr>
				<tr>
					<th colspan="3">
						最新充值
					</th>
					<th colspan="3" style="border-right:0;">
						今日充值排行
					</th>
				</tr>
				<tr>
					<td colspan="3" id="zxcz" valign="top">
					</td>
					<td colspan="3" style="border-right:0;" id="czph"  valign="top">
					</th>
				</tr>
				<tr>
					<th colspan="6" style="border-right:0;">
						<div style="float:left;margin:2px 5px 0 10px;">玩家实时在线曲线图</div>
						<div style="float:right;margin:2px 5px 0 0">
						<input type="text" id="date" style="width:80px;"/>
						</div>
					</th>
				</tr>
				<tr>
					<td colspan="6" style="border-right:0;">
					   <div id="zsdiv" align="center"></div>
					</td>
				</tr>
				<tr>
					<th colspan="6" style="position:relative:">
						<div style="float:left;margin:2px 5px 0 10px;">月收入趋势</div>
						<div style="float:right;margin:2px 5px 0 0">
						<input type="text" id="month">
						</div>
					</th>
				</tr>
				<tr>
					<td colspan="6">
					 	<div id="ysrtdiv" align="center"></div>
					</td>
				</tr>
			</table>
		</div>
	</div>
	</div>
	<script type="text/javascript">
		var index={
		    //获取最新充值以及充值排行
			getZxcz:function(){
				 $.ajax({ 
					type:"POST",
				    url: "${base}/admin/report!report.action",
				    data:{
				    	reportName:'index_zxcz' ,
				    	month:month
				    },
				    success: function(json) { 
				        var arr = json.split("`~");
				        var json = eval("["+arr[0]+"]")[0];
				        for(key in json){
				    		if(json[key]=="")json[key]=0;
				    		$("#"+key).html(json[key]);
				    	}
			    	    $("#zxcz").html(arr[1]);//最新充值赋值
			    	    $("#czph").html(arr[2]);//排行充值赋值
				    }
				});
			},
			//获取月收入
			getYsrt:function(month){
				$.ajax({ 
					type:"POST",
				    url: "${base}/admin/report!report.action",
				    data:{
				    	reportName:'index_ysrt' ,
				    	month:month
				    },
				    success: function(json) { 
			    	    var chart = new FusionCharts("${base}/template/js/lib/chart/Line.swf", "ChartId", $("#ysrtdiv").parent().width()-20, "200", "0", "1");
					    chart.setDataXML(json);		   
					    chart.render("ysrtdiv");
				    }
				});
			},
			//获取在线人数
			getZxrs:function(date){
				$.ajax({ 
					type:"POST",
				    url: "${base}/admin/report!report.action",
				    data:{
				    	reportName:'index_zxrs' ,
				    	date:date
				    },
				    success: function(json) { 
			    	   var chart = new FusionCharts("${base}/template/js/lib/chart/Line.swf", "zsdivChartId", $("#zsdiv").parent().width()-20, "200", "0", "1");
					   chart.setDataXML(json);		   
					   chart.render("zsdiv");
				    }
				});
			},
			init:function(){
				this.getZxcz();
				this.getYsrt($("#month").ligerGetMonthEditorManager().getFormatMonthValue());
            	this.getZxrs($.date.getFormatDate(new Date(),'yyyy-MM-dd'));      
			}
		}
        $(function (){
            $("#month").ligerMonthEditor({onChangeMonth:function(){
            	index.getYsrt($("#month").ligerGetMonthEditorManager().getFormatMonthValue());
            }, width:80, label: '时间', labelWidth: 40, labelAlign: 'left' });  
            $("#date").ligerDateEditor({onChangeDate:function(){
            	index.getZxrs($.date.getFormatDate($("#date").ligerGetDateEditorManager().usedDate,'yyyy-MM-dd'));      
            },initValue:$.date.getFormatDate(new Date(),'yyyy-MM-dd'),width:120, label: '时间', labelWidth: 40, labelAlign: 'left'});  
            index.init();  
        });
    </script>
</body>
</html>

 