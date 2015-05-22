<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>提示信息 </title>
<link rel="icon" href="favicon.ico" type="image/x-icon" />
<link href="${base}/template/css/base.css" rel="stylesheet" type="text/css" />
</head>
<body class="message">
	<div class="body">
		<div class="messageBox">
			<div class="boxTop">
				<div class="boxTitle">提示信息&nbsp;</div>
				<a class="boxClose windowClose" href="#" hidefocus="true"></a>
			</div>
			<div class="boxMiddle">
				<div class="messageContent">
					<span class="icon success">&nbsp;</span>
					<span class="messageText">
						<#if (errorMessages?size > 0)!>
							<#list errorMessages as list>${list}<br></#list>
						<#elseif (actionMessages?size > 0)!>
							<#list actionMessages as list>${list}<br></#list>
						<#elseif (fieldErrors?size > 0)!>
							<#list (fieldErrors?keys)! as key>
								${fieldErrors[key]}<br>
							</#list>
						<#else>
							您的操作已成功!
						</#if>
					</span>
				</div>
				<input type="button" class="formButton messageButton" <#if redirectionUrl??>onclick="window.location.href='${redirectionUrl}'"<#else>onclick="window.history.back(); return false;"</#if> value="确  定" hidefocus="true" />
				<#if updateMainFrame>
				<script>
					top.$("#mainFrame")[0].contentWindow.location.reload();
				</script>
				</#if>
			</div>
			<div class="boxBottom"></div>
		</div>
	</div>
</body>
</html>