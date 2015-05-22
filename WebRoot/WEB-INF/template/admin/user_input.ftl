<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>添加/编辑用户 </title>
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
		<form id="inputForm" onsubmit="return validate()"  action="<#if isAdd??>user!save.action<#else>user!update.action</#if>" method="post">
			<input type="hidden" name="id" value="${id}" />
			<input type="hidden" id="menuIds" name="user.menuIds" value="${(user.menuIds)!}" />
			<div id="navtab1">
				<div tabid="home" title="基本信息" lselected="true"  style="height:150px" >
					 <table class="inputTable">
						<tr>
							<th>
								用户名:
							</th>
							<td>
								<#if isAdd??>
									<input type="text" name="user.username" class="formText {required: true, minlength: 2, maxlength: 20, remote: 'user!checkUsername.action', messages: {remote: '用户名已存在!'}}" />
									<label class="requireField">*</label>
								<#else>
									${user.username}
									<input type="hidden" name="user.username" value="${(user.username)!}" />
								</#if>
							</td>
						</tr>
						<tr>
							<th>
								真实名:
							</th>
							<td>
								<input type="text" name="user.truename" class="formText {required: true}" value="${(user.truename)!}"/>
								<label class="requireField">*</label>
							</td>
						</tr>
						<tr>
							<th>
								密码:
							</th>
							<td>
								<input type="text" name="user.password" class="formText {required: true}" value="${(user.password)!}" />
								<label class="requireField">*</label>
							</td>
						</tr>
					</table>
				</div>
				<div  title="分配菜单" style="height:150px">
					 <table class="inputTable">
						<tr>
							<th>
								菜单:
							</th>
							<td style="padding:0;">
								<ul id="tree1" style="height:148px;overflow:auto;"></ul>
							</td>
						</tr>
					</table>
				</div>
			</div>
			<div class="buttonArea">
				<input type="submit" class="formButton" value="确  定" hidefocus="true" />&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button" class="formButton" onclick="top.$.ligerDialog.close(); return false;" value="关  闭" hidefocus="true" />
			</div>
		</form>
	</div>
	<script type="text/javascript">
		function validate(){
			var checkNodes=$("#tree1").ligerGetTreeManager().getCheckedDatas();
			var menuIds=[];
			for(var i=0;i<checkNodes.length;i++){
				menuIds.push(checkNodes[i].id);
			}
			$("#menuIds").val(menuIds.toString());
			return $("#inputForm").valid();
		}
        $(function (){
            $("#navtab1").ligerTab({tabType:1}); 
            $("#tree1").ligerTree({checkbox: true, url: '${base}/admin/user!getUserCheckMenu.action?id=${id}' });
        	$("#inputForm").validate({
				errorClass: "validateError",
				ignore: ".ignoreValidate",
				errorPlacement: function(error, element) {
					var messagePosition = element.metadata().messagePosition;
					if("undefined" != typeof messagePosition && messagePosition != "") {
						var $messagePosition = $(messagePosition);
						if ($messagePosition.size() > 0) {
							error.insertAfter($messagePosition).fadeOut(300).fadeIn(300);
						} else {
							error.insertAfter(element).fadeOut(300).fadeIn(300);
						}
					} else {
						error.insertAfter(element).fadeOut(300).fadeIn(300);
					}
				},
				submitHandler: function(form) {
					form.submit();
				}
			});
        
        });
    </script>
</body>
</html>