<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>管理菜单 </title>
<link rel="icon" href="favicon.ico" type="image/x-icon" />
<link href="${base}/template/css/base.css" rel="stylesheet" type="text/css" />
<link href="${base}/template/css/menu.css" rel="stylesheet" type="text/css" />
</head>
<body class="menu">
	<div class="menuContent">
		 <@menuList ; menuList>
		 <#list menuList as list>
		 	<dl>
			<dt><span>${list.name}</span></dt>
			<#list (list.sortChildren)! as list>
				<dd>
					<a href="${base}${list.url}<#if list.url.indexOf("&") gt 0 >&<#else>?</#if>menuId=${list.id}" target="mainFrame">${list.name}</a>
				</dd>
			</#list>
			</dl> 
		 </#list>
		 </@menuList>
	</div>
</body>
</html>