<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>添加/编辑菜单 </title>
<link href="${base}/template/css/base.css" rel="stylesheet" type="text/css" />
<link href="${base}/template/css/input.css" rel="stylesheet" type="text/css" />
<link href="${base}/template/js/lib/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" /><link href="${base}/template/js/lib/ligerUI/skins/Gray/css/all.css" rel="stylesheet" type="text/css" />  
<script src="${base}/template/js/lib/jquery/jquery-1.3.2.min.js" type="text/javascript"></script>
<script src="${base}/template/js/lib/ligerUI/js/ligerui.all.js" type="text/javascript"></script>  
<script type="text/javascript" src="${base}/template/js/lib/jquery/jquery.form.js"></script>
<script type="text/javascript" src="${base}/template/js/lib/jquery/jquery.metadata.js"></script>
<script type="text/javascript" src="${base}/template/js/lib/jquery/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/template/js/lib/jquery/jquery.validate.methods.js"></script>
<script type="text/javascript" src="${base}/template/js/lib/jquery/jquery.validate.cn.js"></script>
<script type="text/javascript" src="${base}/template/js/lib/jquery/base.js"></script>
<#if !id??>
	<#assign isAdd = true />
<#else>
	<#assign isEdit = true />
</#if>
</head>
<body class="input">
	<div class="body">
		<form id="inputForm" class="validate" action="<#if isAdd??>menu!save.action<#else>menu!update.action</#if>" method="post">
			<input type="hidden" name="id" value="${id}" />
			
			<table class="inputTable">
				<tr>
					<th>
						菜单名称:
					</th>
					<td>
						<input type="text" name="menu.title" class="formText {required: true}" value="${(menu.title)!}" />
						<label class="requireField">*</label>
					</td>
				</tr>
				<tr>
					<th>
						上级菜单:
					</th>
					<td>
						<select name="parentId">
							<option value="">顶级菜单</option>
							<#list menuTable as list>
								<option value="${list.id}" <#if (list.id == menu.parentId)!> selected</#if>>
									<#if list.level != 0>
										<#list 1..list.level as i>
											------
										</#list>
									</#if>
								${list.title}
								</option>
							</#list>
						</select>
					</td>
				</tr>
				<tr>
					<th>
						排序:
					</th>
					<td>
						<input type="text" name="menu.orderList" class="formText {required: true, digits: true}" value="${(menu.orderList)!50}" title="只允许输入零或正整数" />
						<label class="requireField">*</label>
					</td>
				</tr>
				<tr>
					<th>
						路径标识:
					</th>
					<td>
						<input type="text" name="menu.url" class="formText {required: true}" value="${(menu.url)!}" />
						<label class="requireField">* （如果为导航目录级菜单输入###）</label>
					</td>
				</tr>
				<tr>
					<th>
						是否首页:
					</th>
					<td>
						<label><input type="radio" name="menu.isHome" value="true"<#if (menu.isHome == true)!> checked</#if> />是</label>
						<label><input type="radio" name="menu.isHome" value="false"<#if (isAdd || menu.isHome == false)!> checked</#if> />否</label>
					</td>
				</tr>
				<tr>
					<th>
						是否显示:
					</th>
					<td>
						<label><input type="radio" name="menu.isVisible" value="true"<#if (isAdd || menu.isVisible == true)!> checked</#if> />是</label>
						<label><input type="radio" name="menu.isVisible" value="false"<#if (menu.isVisible == false)!> checked</#if> />否</label>
					</td>
				</tr>
				<tr>
					<th>
						是否有效:
					</th>
					<td>
						<label><input type="radio" name="menu.isEnable" value="true"<#if (isAdd || menu.isEnable == true)!> checked</#if> />是</label>
						<label><input type="radio" name="menu.isEnable" value="false"<#if (menu.isEnable == false)!> checked</#if> />否</label>
					</td>
				</tr>
				<tr>
					<th>
						菜单描述:
					</th>
					<td>
						<textarea name="menu.remark" class="formTextarea">${(menu.remark)!}</textarea>
					</td>
				</tr>
			</table>
			<div class="buttonArea">
				<input type="submit" class="formButton" value="确  定" hidefocus="true" />&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button" class="formButton" onclick="top.$.ligerDialog.close(); return false;" value="返  回" hidefocus="true" />
			</div>
		</form>
	</div>
</body>
</html>