<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>管理登录 </title>
<link rel="icon" href="favicon.ico" type="image/x-icon" />
<script type="text/javascript" src="${base}/template/js/jquery.js"></script>
<script type="text/javascript" src="${base}/template/js/jquery.cookie.js"></script>
<link href="${base}/template/css/login.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">

// 登录页面若在框架内，则跳出框架
if (self != top) {
	top.location = self.location;
};

$().ready( function() {
	$.tip = function (messageText,second,tipClose) {
		second=second||1;
		var $tipWindow = $(".tipWindow");
		if($tipWindow.length<1){
			$tipWindow=$("<div class='tipWindow'><div class='tipClose'></div><div class='messageText'></div></div>");
			$tipWindow.appendTo($("body"));
			$(".tipWindow .tipClose").click(function(){
				$tipWindow.animate({left: 0, opacity: "hide"}, "slow");
			});
		}
		if(tipClose){
			$(".tipWindow .tipClose").css({display:'block'});
		}else{
			$(".tipWindow .tipClose").css({display:'none'});
		}
		var $messageText = $(".tipWindow .messageText");
		$messageText.html(messageText);
		$tipWindow.css({"margin-left": "-" + parseInt($tipWindow.width() / 2) + "px", "left": "50%"});
		setTimeout(function() {
			$tipWindow.animate({left: 0, opacity: "hide"}, "slow");
		}, second*1000);
		$tipWindow.show();
	}
	var $username = $("#username");
	var $password = $("#password");
	var $isSaveUsername = $("#isSaveUsername");


	// 提交表单验证,记住登录用户名
	$("#loginForm").submit( function() {
		if ($username.val() == "") {
			$username.focus();
			$.tip("请输入您的用户名!");
			return false;
		}
		if ($password.val() == "") {
			$password.focus();
			$.tip("请输入您的密码!");
			return false;
		}
	});


	<#if (actionErrors?size > 0)>
		$.tip("<#list errorMessages as list>${list}<br></#list>", 1);
	</#if>

});
</script>
</head>
<body class="login">
	<div class="bg">
		<form id="loginForm" class="form" action="${base}/admin/admin!login.action" method="post">
			<input type="text" id="username" name="username" value=""/>
			<input type="password" id="password" name="password" value="" />
			<input type="submit" id="submitButton" value="登 陆" hidefocus="true" />
		</form>
	</div>
	<div class="copyright">
			<a href="http://www.kl321.com" target="_blank"><img src="${base}/template/images/admin-logo.jpg"/></a>
	</div>
</body>
</html>