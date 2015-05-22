<div class="listBar">
	<h1>
	<span class="locationBar">当前页面:</span>
	<a href="${homeUrl}" target="mainFrame">首页</a>
	<#if menuPathList?size gt 0>
	->
	<#list menuPathList as list>
		<a <#if list.url.indexOf("###") lt 0 >href="${base}${list.url}<#if list.url.indexOf("&") gt 0 >&<#else>?</#if>menuId=${list.id}"</#if> target="mainFrame">${list.title}</a><#if list_index<menuPathList.size()-1>-></#if>
	</#list>
	</#if>
	</h1>
</div>