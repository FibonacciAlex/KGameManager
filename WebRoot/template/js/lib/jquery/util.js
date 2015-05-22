(function($) {
	$.extend({
	 	date:{
	 		getFirstDateByMath:function(year,month){
	 		   var new_year = year;    //取当前的年份         
               var new_month = month;//取下一个月的第一天，方便计算（最后一天不固定）         
               if(month>12) {        
                 new_month -=12;        //月份减         
                 new_year++;            //年份增         
               }        
               var new_date = new Date(new_year,new_month,1);     
               return new_date;
	 		},
	 		getLastDateByMath:function(year,month){
	 		   var new_date =this.getFirstDateByMath(year,month);
               return (new Date(new_date.getTime()-1000*60*60*24));//获取当月最后一天日期          
	 		},
	 		getFormatFirstDateByMonth:function(year,month,format){
	 			return this.getFormatDate(this.getFirstDateByMath(year,month-1),format);
	 		},
	 		getFormatLastDateByMonth:function(year,month,format){
	 			return this.getFormatDate(this.getLastDateByMath(year,month),format);
	 		},
	 		getFirstDate:function(){
	 			var date = new Date();
	 			return this.getFirstDateByMath(date.getFullYear(),date.getMonth());
	 		},
	 		getLastDate:function(){
	 			var date = new Date();
	 			return this.getLastDateByMath(date.getFullYear(),date.getMonth()+1);
	 		},
	 		getFormatDate:function(date,format){
 			    var o = {
	                "M+": date.getMonth() + 1,
	                "d+": date.getDate(),
	                "h+": date.getHours(),
	                "m+": date.getMinutes(),
	                "s+": date.getSeconds(),
	                "q+": Math.floor((date.getMonth() + 3) / 3),
	                "S": date.getMilliseconds()
	            }
	            if (/(y+)/.test(format)){
	                format = format.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
	            }
	            for (var k in o){
	                if (new RegExp("(" + k + ")").test(format))
	                {
	                    format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]: ("00" + o[k]).substr(("" + o[k]).length));
	                }
	            }
	            return format;
	 		}
	 	},
	 	vtip : function () {
		    var xOffset = -10;
		    var yOffset = 10;
		    $(".title-tip").live("mouseover",function(a){
		        this.t = this.title;
		        this.title = "";
		        var top = (a.pageY + yOffset);
		        var left = (a.pageX + xOffset);
		        var vtip = $('<div id="vtip"></div>');
		        $("body").append(vtip);
		        vtip.html('<div id="vtipArrow"></div>'+this.t);
		        if((left+200)>$(window).width()){
		        	//$("#vtipArrow").css({left:200-($(window).width()-left)});
		        	vtip.css("top", top + "px").css("right", "0px").fadeIn("slow",function(){
		        		$("#vtipArrow").css({left:$("#vtip").outerWidth()-($(window).width()-left)});
		        	});
		        }else{
		        	vtip.css("top", top + "px").css("left", left + "px").fadeIn("slow",function(){
		        		$("#vtipArrow").css({left:5});
		        	});
		        }
		    	
		    }).live("mouseout",function(){
		    	this.title = this.t;
		        $("#vtip").fadeOut("slow").remove();		    	
		    }).live("mousemove",function(a){
		    	var top = (a.pageY + yOffset);
		        var left = (a.pageX + xOffset);
		        if((left+200)>$(window).width()){
		        	$("#vtipArrow").css({left:$("#vtip").outerWidth()-($(window).width()-left)});
		        	$("#vtip").css("top", top + "px").css("right", "0px");
		        }else{
		        	$("#vtipArrow").css({left:5});
		        	$("#vtip").css("top", top + "px").css("left", left + "px");
		        }    	
		    })
		}
	 });
	 $.vtip();
})(jQuery);