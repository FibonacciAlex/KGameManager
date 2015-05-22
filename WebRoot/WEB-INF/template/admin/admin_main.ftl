<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head id="Head1">
<title>欢迎使用 KOLA管理系统</title>
    <link href="${base}/template/js/lib/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
    <link href="${base}/template/js/lib/ligerUI/skins/Gray/css/all.css" rel="stylesheet" type="text/css" />  
    <script src="${base}/template/js/lib/jquery/jquery-1.3.2.min.js" type="text/javascript"></script>
    <script src="${base}/template/js/lib/ligerUI/js/ligerui.all.js" type="text/javascript"></script>  
    <link href="${base}/template/css/common.css" rel="stylesheet" type="text/css" />
    <link href="${base}/template/css/index.css" rel="stylesheet" type="text/css" />
</head>
<body style="text-align:center; background:#F0F0F0; overflow:hidden;">
    <div id="topmenu" class="l-topmenu">
        <div class="l-topmenu-logo">欢迎使用 KOLA管理系统</div>
        <div class="l-topmenu-welcome"> 
        	<div class="l-top-welcome-r">
        		<span class="home"><a href="${homeUrl}" target="mainFrame">首页</a></span>
        		<span class="loginuserl">用户:</span>
        		<span class="loginuser">${loginUsername}</span>
        		<span class="ligionout"><a href="${base}/admin/admin!logout.action">退出登录</a></span>
        	</div>
        </div> 
    </div> 
     <div id="mainbody" class="l-mainbody" style="width:100%; margin:0 auto;" >
        <div position="left">
         <div class="left_logo"></div>
         <div id="mainmenu">
	         <@menuList userId='${loginId}'; menuList>
			 <#list menuList as list>
				<div title="${list.title}">
				<ul class="menulist">
				<#list (list.children)! as list>
					<li>
						<a hidefocus="true" href="${base}${list.url}<#if list.url.indexOf("?") gt 0 >&<#else>?</#if>menuId=${list.id}" target="mainFrame">${list.title}</a>
					</li>
				</#list>
				</ul>
				</div>
			 </#list>
			 </@menuList>
         </div>
        </div>  
        <div position="center" id="framecenter"> 
            <iframe frameborder="0" name="mainFrame" width="100%" height="100%" id="mainFrame" src="${homeUrl}"></iframe>
        </div> 
        <div position="bottom">
        	<div class="l-bottom">广州考拉信息技术有限公司</div>
        </div>
    </div>
    <div class="l-hidden"></div>
    <script type="text/javascript">
        //几个布局的对象
        var layout, tab, accordion;
        //tabid计数器，保证tabid不会重复
        var tabidcounter = 0;
        //窗口改变时的处理函数
        function f_heightChanged(options) {
            if (accordion && options.middleHeight-123  > 0)
                accordion.setHeight(options.middleHeight-123);
        }
        //增加tab项的函数
        function f_addTab(tabid, text, url) {
            if (!tab) return;
            if (!tabid)
            {
                tabidcounter++;
                tabid = "tabid" + tabidcounter; 
            }
            tab.addTabItem({ tabid: tabid, text: text, url: url });
        }
        $(document).ready(function ()
        {

 			
 			$("#pageloading").hide();
            //布局初始化 
            //layout
            layout = $("#mainbody").ligerLayout({ allowLeftCollapse:false,height: '100%', heightDiff: 0, leftWidth: 160,bottomHeight:30,space:0,allowLeftResize:false,allowBottomResize:false, onHeightChanged: f_heightChanged, minLeftWidth: 120 });
            var bodyHeight = $(".l-layout-center:first").height();
            $("#mainmenu").ligerAccordion({ height: bodyHeight-123, speed: null });
            $("#mainmenu").find("li").click(function(){
                var $this = $(this);
            	$("#mainmenu").find("li.current").removeClass("current");
            	$this.addClass("current");
            	$("#mainFrame")[0].src=$this.find("a:first").attr("href");
            });
            $("#mainmenu").find("a").click(function(){
                 $(this).parent().click();
            	 return false;
            })
            accordion = $("#mainmenu").ligerGetAccordionManager();

        });
    </script>
</body>
</html>
