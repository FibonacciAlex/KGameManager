<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>分区管理 </title>
<link href="${base}/template/css/base.css" rel="stylesheet" type="text/css" />
<link href="${base}/template/js/lib/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
<link href="${base}/template/js/lib/ligerUI/skins/Gray/css/all.css" rel="stylesheet" type="text/css" />
<link href="${base}/template/css/list.css" rel="stylesheet" type="text/css" />
<script src="${base}/template/js/lib/jquery/jquery-1.3.2.min.js" type="text/javascript"></script>
<script src="${base}/template/js/lib/ligerUI/js/ligerui.all.js" type="text/javascript"></script>
</head>
<body class="list" style=" overflow-x: hidden;">
	<div class="page">
		<#include "/WEB-INF/template/admin/menupath.ftl" />
		<form id="listForm" action="menu!list.action" method="post">
			<div class="pageHeader">
				<div class="searchtitle">查询条件</div>
				<div class="searchform">
				<table>
				<th>平台查询：</th>
				<td>
					<input name="platform" id="platform"/>
				</td>
				<th>游戏查询：</th>
				<td>
					<input name="games" id="games"/>
				</td>
				<th>状态：</th>
				<td>
					<input name="state" id="state"/>
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
			分区管理列表
			<input style="float:right;margin-right:5px;" type="button" class="formButton" value="增加分区" id="addButton" href="${base}/admin/menu!add.action"/>
			</h1>
		</div>
		<div id="maingrid"></div> 
	</div>
 	<script type="text/javascript">
        $(function (){
            var width=$(window).width()-50;
            $("#maingrid").ligerGrid({
                url: '${base}/admin/menu!grid.action', 
                sortName: 'id', 
                columns: [
                { display: '分区名', name: 'title',width: parseInt(width*0.15)},
                { display: '描述', name: 'url',width: parseInt(width*0.15)},
                { display: '平台名', name: 'url',width: parseInt(width*0.15)},
                { display: '游戏名', name: 'url',width: parseInt(width*0.15)},
                { display: '添加时间', name: 'url',width: parseInt(width*0.15)},
                { display: '分区状态', name: 'url',width: parseInt(width*0.15)},
                { display: '操作', isSort: false, width:parseInt(width*0.1), render: function (rowdata, rowindex, value)
                {
                    var h = "";
                    h += '<a href="${base}/admin/menu!delete.action?id='+rowdata.id+'" class="deleteAction" title="删除">删除</a>&nbsp;&nbsp;&nbsp;&nbsp;';
                    h += '<a href="${base}/admin/menu!edit.action?id='+rowdata.id+'" class="editAction" title="编辑">编辑</a>' 
                    return h;
                }
                }
                ],
                width:'100%',
                pageSizeOptions: [10, 15, 20] 
            });
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
            $("#addButton").click(function(){
            	alert("等完善");
             	//top.$.ligerDialog.open({title:'新增菜单', onClose:function(){},isHidden:false,url:$(this).attr("href"), width: 650, height: 550,showMax:true,load:false }); 
            });
            $("#platform").ligerComboBox({
            	url:'${base}/admin/report!report.action?reportName=basedata_platform',
            	initValue:'-1'
            });
            $("#games").ligerComboBox({
            	url:'${base}/admin/report!report.action?reportName=basedata_games',
            	initValue:'-1'
            });
            $("#state").ligerComboBox({
            	url:'${base}/admin/report!report.action?reportName=basedata_state',
            	initValue:'-1'
            });
        });
    </script>
</body>
</html>