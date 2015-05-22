/**
* jQuery ligerUI 1.1.9
* 
* http://ligerui.com
*  
* Author daomi 2012 [ gd_star@163.com ] 
* 
*/
(function ($)
{
    $.fn.ligerMonthEditor = function ()
    {
        return $.ligerui.run.call(this, "ligerMonthEditor", arguments);
    };

    $.fn.ligerGetMonthEditorManager = function ()
    {
        return $.ligerui.run.call(this, "ligerGetMonthEditorManager", arguments);
    };

    $.ligerDefaults.MonthEditor = {
        format: "yyyy-MM",
        showTime: false,
        onChangeMonth: false,
        absolute: true,  //选择框是否在附加到body,并绝对定位
        awidth:30
    };
    $.ligerDefaults.MonthEditorString = {
        dayMessage: ["日", "一", "二", "三", "四", "五", "六"],
        monthMessage: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
        todayMessage: "确定",
        closeMessage: "关闭"
    };
    $.ligerMethos.MonthEditor = {};

    $.ligerui.controls.MonthEditor = function (element, options)
    {
        $.ligerui.controls.MonthEditor.base.constructor.call(this, element, options);
    };
    $.ligerui.controls.MonthEditor.ligerExtend($.ligerui.controls.Input, {
        __getType: function ()
        {
            return 'MonthEditor';
        },
        __idPrev: function ()
        {
            return 'MonthEditor';
        },
        _extendMethods: function ()
        {
            return $.ligerMethos.MonthEditor;
        },
        _render: function ()
        {
            var g = this, p = this.options;
            if (this.element.tagName.toLowerCase() != "input" || this.element.type != "text")
                return;
            g.inputText = $(this.element);
            if (!g.inputText.hasClass("l-text-field"))
                g.inputText.addClass("l-text-field");
            g.link = $('<div class="l-trigger"><div class="l-trigger-icon"></div></div>');
            g.text = g.inputText.wrap('<div class="l-text l-text-date"></div>').parent();
            g.text.append('<div class="l-text-l"></div><div class="l-text-r"></div>');
            g.text.append(g.link);
            //添加个包裹，
            g.textwrapper = g.text.wrap('<div class="l-text-wrapper"></div>').parent();
            var montheditorHTML =   '<div class="l-box-montheditor"><div class="l-box-montheditor-body"><ul class="l-box-montheditor-monthselector"></ul>  <div class="l-box-montheditor-header"><div class="l-box-montheditor-header-btn l-box-montheditor-header-prevyear"><span></span></div><div class="l-box-montheditor-header-btn l-box-montheditor-header-nextyear"><span></span></div></div><ul class="l-box-montheditor-yearselector"><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li></ul></div><div class="l-box-montheditor-toolbar"><div class="l-button l-button-today">关闭</div><div class="l-button l-button-close">关闭</div><div class="l-clear"></div></div></div>';
            g.montheditor = $(montheditorHTML);
            if (p.absolute)
                g.montheditor.appendTo('body').addClass("l-box-montheditor-absolute");
            else
                g.textwrapper.append(g.montheditor);
            g.header = $(".l-box-montheditor-header", g.montheditor);
            g.body = $(".l-box-montheditor-body", g.montheditor);
            g.toolbar = $(".l-box-montheditor-toolbar", g.montheditor);

            g.body.monthselector = $(".l-box-montheditor-monthselector", g.body);
            g.body.yearselector = $(".l-box-montheditor-yearselector", g.body);

            g.buttons = {
                btnPrevYear: $(".l-box-montheditor-header-prevyear", g.header),
                btnNextYear: $(".l-box-montheditor-header-nextyear", g.header),
                btnToday: $(".l-button-today", g.toolbar),
                btnClose: $(".l-button-close", g.toolbar)
            };
            var nowDate = new Date();
            g.now = {
                year: nowDate.getFullYear(),
                month: nowDate.getMonth() + 1
            };
            //当前的时间
            g.currentMonth = {
                year: nowDate.getFullYear(),
                month: nowDate.getMonth() + 1
            };
            //选择的时间
            g.selectedMonth = null;
            //使用的时间
            g.usedMonth = null;

            //初始化数据
            //设置一月到十一二月
            for(var i=0;i<12;i++){
            	g.body.monthselector.append("<li>"+p.monthMessage[i]+"</li>")
            }
            //设置按钮
            g.buttons.btnToday.html(p.todayMessage);
            g.buttons.btnClose.html(p.closeMessage);
            //设置主体
            g.bulidContent();
            //初始化   
            if (g.inputText.val() != "")
                g.onTextChange();
            /**************
            **bulid evens**
            *************/
            //toggle even
            g.link.hover(function ()
            {
                if (p.disabled) return;
                this.className = "l-trigger-hover";
            }, function ()
            {
                if (p.disabled) return;
                this.className = "l-trigger";
            }).mousedown(function ()
            {
                if (p.disabled) return;
                this.className = "l-trigger-pressed";
            }).mouseup(function ()
            {
                if (p.disabled) return;
                this.className = "l-trigger-hover";
            }).click(function ()
            {
                if (p.disabled) return;
                g.bulidContent();
                g.toggleMonthEditor(g.montheditor.is(":visible"));
            });
            //不可用属性时处理
            if (p.disabled)
            {
                g.inputText.attr("readonly", "readonly");
                //g.text.addClass('l-text-disabled');
            }
            //初始值
            if (p.initValue)
            {
                g.inputText.val(p.initValue);
            }else{
           		g.selectedMonth = {
                    year: g.currentMonth.year,
                    month: g.currentMonth.month
                };
                g.showMonth();
                g.onTextChange();
            }
            //今天
            g.buttons.btnToday.click(function (){
                g.selectedMonth = {
                    year: g.currentMonth.year,
                    month: g.currentMonth.month
                };
                g.showMonth();
                g.montheditor.slideToggle("fast");
            });
            g.buttons.btnClose.click(function ()
            {
                g.toggleMonthEditor(true);
            });

            $(".l-box-montheditor-header-btn", g.header).hover(function ()
            {
                $(this).addClass("l-box-montheditor-header-btn-over");
            }, function ()
            {
                $(this).removeClass("l-box-montheditor-header-btn-over");
            });
            //选择月份
            $("li", g.body.monthselector).click(function ()
            {
                var index = $("li", g.body.monthselector).index(this);
                g.currentMonth.month = index + 1;
                $(this).siblings("li.l-selected").removeClass("l-selected");
                $(this).addClass("l-selected");
                g.selectedMonth = {
                    year: g.currentMonth.year,
                    month: g.currentMonth.month
                };
                g.showMonth();
            });
            //选择年份
            $("li", g.body.yearselector).click(function ()
            {
                g.currentMonth.year = parseInt($(this).html());
                $(this).siblings("li.l-selected").removeClass("l-selected");
                $(this).addClass("l-selected");
                g.selectedMonth = {
                    year: g.currentMonth.year,
                    month: g.currentMonth.month
                };
                g.showMonth();
            });
             

            //上一年
            g.buttons.btnPrevYear.click(function ()
            {
                g.currentMonth.year=g.currentMonth.year-5;
                g.bulidContent();
            });
            //下一年
            g.buttons.btnNextYear.click(function ()
            {
                g.currentMonth.year=g.currentMonth.year+5;
                g.bulidContent();
            });
            //文本框
            g.inputText.change(function ()
            {
                g.onTextChange();
            }).blur(function ()
            {
                g.text.removeClass("l-text-focus");
            }).focus(function ()
            {
                g.text.addClass("l-text-focus");
            });
            g.text.hover(function ()
            {
                g.text.addClass("l-text-over");
            }, function ()
            {
                g.text.removeClass("l-text-over");
            });
            //LEABEL 支持
            if (p.label)
            {
                g.labelwrapper = g.textwrapper.wrap('<div class="l-labeltext"></div>').parent();
                g.labelwrapper.prepend('<div class="l-text-label" style="float:left;display:inline;">' + p.label + ':&nbsp</div>');
                g.textwrapper.css('float', 'left');
                if (!p.labelWidth)
                {
                    p.labelWidth = $('.l-text-label', g.labelwrapper).outerWidth();
                } else
                {
                    $('.l-text-label', g.labelwrapper).outerWidth(p.labelWidth);
                }
                $('.l-text-label', g.labelwrapper).width(p.labelWidth);
                $('.l-text-label', g.labelwrapper).height(g.text.height());
                g.labelwrapper.append('<br style="clear:both;" />');
                if (p.labelAlign)
                {
                    $('.l-text-label', g.labelwrapper).css('text-align', p.labelAlign);
                }
                g.textwrapper.css({ display: 'inline' });
                g.labelwrapper.width((p.width||g.text.outerWidth())  + p.labelWidth + 2);
            }

            g.set(p);
        },
        destroy: function ()
        {
            if (this.textwrapper) this.textwrapper.remove();
            if (this.montheditor) this.montheditor.remove();
            this.options = null;
            $.ligerui.remove(this);
        },
        bulidContent: function (){
        	var g = this, p = this.options;
        	$("li", g.body.monthselector).each(function (i, item){
                if (i+1 == g.currentMonth.month)
                    $(this).addClass("l-selected");
                else
                    $(this).removeClass("l-selected");
            });
        	$("li", g.body.yearselector).each(function (i, item){
                var currentYear = g.currentMonth.year + (i - 4);
                if (currentYear == g.currentMonth.year)
                    $(this).addClass("l-selected");
                else
                    $(this).removeClass("l-selected");
                $(this).html(currentYear);
            });
        },
        updateSelectBoxPosition: function ()
        {
            var g = this, p = this.options;
            if (p.absolute){
            	if(g.text.offset().left +g.text.width() + p.awidth>$(window).width()){
            		g.montheditor.css({ right:5, top: g.text.offset().top + 1 + g.text.outerHeight() });
            	}else{
            		g.montheditor.css({ left: g.text.offset().left, top: g.text.offset().top + 1 + g.text.outerHeight() });
            	}
                
            }
            else
            {
                if (g.text.offset().top + 4 > g.montheditor.height() && g.text.offset().top + g.montheditor.height() + textHeight + 4 - $(window).scrollTop() > $(window).height())
                {
                    g.montheditor.css("marginTop", -1 * (g.montheditor.height() + textHeight + 5));
                    g.showOnTop = true;
                }
                else
                {
                    g.showOnTop = false;
                }
            }
        },
        toggleMonthEditor: function (isHide)
        {
            var g = this, p = this.options;
            var textHeight = g.text.height();
            g.editorToggling = true;
            if (isHide)
            {
                g.montheditor.hide('fast', function ()
                {
                    g.editorToggling = false;
                });
            }
            else
            {
                g.updateSelectBoxPosition();
                g.montheditor.slideDown('fast', function ()
                {
                    g.editorToggling = false;
                });
            }
        },
        showMonth: function ()
        {
            var g = this, p = this.options;
            if (!this.selectedMonth) return;
            var dateStr = g.selectedMonth.year + "/" + g.selectedMonth.month;
            this.inputText.val(dateStr);
            this.inputText.trigger("change").focus();
        },
        isMonthTime: function (dateStr)
        {
            var g = this, p = this.options;
            var r = dateStr.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/);
            if (r == null) return false;
            var d = new Month(r[1], r[3] - 1, r[4]);
            if (d == "NaN") return false;
            return (d.getFullYear() == r[1] && (d.getMonth() + 1) == r[3] && d.getMonth() == r[4]);
        },
        isLongMonthTime: function (dateStr)
        {
            var g = this, p = this.options;
            var reg = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2})$/;
            var r = dateStr.match(reg);
            if (r == null) return false;
            var d = new Month(r[1], r[3] - 1, r[4], r[5], r[6]);
            if (d == "NaN") return false;
            return (d.getFullYear() == r[1] && (d.getMonth() + 1) == r[3] && d.getMonth() == r[4] && d.getHours() == r[5] && d.getMinutes() == r[6]);
        },
        getFormatMonthValue:function(){
        	return this.getFormatMonth(this.getValue());
        },
        getFormatMonth: function (date)
        {
            var g = this, p = this.options;
            if (date == "NaN") return null;
            var format = p.format;
            var o = {
                "M+": date.getMonth() + 1,
                "d+": date.getMonth()
            }
            if (/(y+)/.test(format))
            {
                format = format.replace(RegExp.$1, (date.getFullYear() + "")
                .substr(4 - RegExp.$1.length));
            }
            for (var k in o)
            {
                if (new RegExp("(" + k + ")").test(format))
                {
                    format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
                    : ("00" + o[k]).substr(("" + o[k]).length));
                }
            }
            return format;
        },
        onTextChange: function ()
        {
            var g = this, p = this.options;
            var val = g.inputText.val();
            if (val == "")
            {
                g.selectedMonth = null;
                return true;
            }
            val = val.replace(/-/g, "/")+"/01";
            var formatVal = g.getFormatMonth(new Date(val));
            if (formatVal == null)
            {
                //恢复
                if (!g.usedMonth)
                {
                    g.inputText.val("");
                } else
                {
                    g.inputText.val(g.getFormatMonth(g.usedMonth));
                }
            }
            g.usedMonth = new Date(val); //记录
            g.selectedMonth = {
                year: g.usedMonth.getFullYear(),
                month: g.usedMonth.getMonth() + 1
            };
            g.currentMonth = {
                year: g.usedMonth.getFullYear(),
                month: g.usedMonth.getMonth() + 1
            };
            g.inputText.val(formatVal);
            g.trigger('changeMonth', [formatVal]);
            if (!$(g.montheditor).is(":visible"))
                g.bulidContent();
        },
        _setHeight: function (value)
        {
            var g = this;
            if (value > 4)
            {
                g.text.css({ height: value });
                g.inputText.css({ height: value });
                g.textwrapper.css({ height: value });
            }
        },
        _setWidth: function (value)
        {
            var g = this;
            if (value > 20)
            {
                g.text.css({ width: value });
                g.inputText.css({ width: value - 20 });
                g.textwrapper.css({ width: value });
            }
        },
        _setValue: function (value)
        {
            var g = this;
            if (!value) g.inputText.val('');
            if (typeof value == "string")
            {
                g.inputText.val(value);
            }
            else if (typeof value == "object")
            {
                if (value instanceof Date)
                {
                    g.inputText.val(g.getFormatMonth(value));
                    g.onTextChange();
                }
            }
        },
        _getValue: function ()
        {
            return this.usedMonth;
        },
        setEnabled: function ()
        {
            var g = this, p = this.options;
            this.inputText.removeAttr("readonly");
            this.text.removeClass('l-text-disabled');
            p.disabled = false;
        },
        setDisabled: function ()
        {
            var g = this, p = this.options;
            this.inputText.attr("readonly", "readonly");
            this.text.addClass('l-text-disabled');
            p.disabled = true;
        }
    });


})(jQuery);