<#assign sec=JspTaglibs["http://www.springframework.org/security/tags"] /> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>管理中心 </title>
<link href="${base}/template/css/base.css" rel="stylesheet" type="text/css" />
<link href="${base}/template/css/header.css" rel="stylesheet" type="text/css" />
<!--[if lte IE 6]>
<script type="text/javascript">
	DD_belatedPNG.fix(".headerLogo");
</script>
<![endif]-->
</head>
<body class="header">
	<div class="body">
		<div class="headerLogo"></div>
		<div class="headerTop">
			<div class="headerLink">
				<span class="welcome">
					<strong>&nbsp;您好!&nbsp;
				</span>
			</div>
		</div>
		<div class="headerBottom">
			<div class="headerMenu">
			</div>
			<div class="userInfo">
				<a href="user_profile!edit.action" target="mainFrame">
					<span class="profileIcon">&nbsp;</span>个人资料
				</a>
				<a href="${base}/j_spring_security_logout" target="_top">
					<span class="logoutIcon">&nbsp;</span>退出&nbsp
				</a>
			</div>
		</div>
	</div>
</body>
</html>