<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>菜单列表 </title>
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
		<div class="listBar">
			<h1>
			菜单列表
			<input style="float:right;margin-right:5px;" type="button" class="formButton" value="增加" id="addButton" href="${base}/admin/menu!add.action"/>
			</h1>
		</div>
		<div id="maingrid"></div> 
	</div>
 	<script type="text/javascript">
        $(function (){
            var jsonObj = {};
            var width=$(window).width()-50;
            $("#maingrid").ligerGrid({
               url: '${base}/admin/menu!grid.action', sortName: 'id', 
                columns: [
                { display: '菜单名称', name: 'title',width: parseInt(width*0.3)},
                { display: '路径', name: 'url',width: parseInt(width*0.5)},
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
                height:'100%',
                heightDiff:-2,
                data: jsonObj, 
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
            	top.$.ligerDialog.open({title:'编辑菜单', onClose:function(){},isHidden:false,url:$(this).attr("href"), width: 650, height: 550,showMax:true,load:false }); 
            	return false;
            })
            $("#addButton").click(function(){
             	top.$.ligerDialog.open({title:'新增菜单', onClose:function(){},isHidden:false,url:$(this).attr("href"), width: 650, height: 550,showMax:true,load:false }); 
            })
        });
    </script>
</body>
</html>