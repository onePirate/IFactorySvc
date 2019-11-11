// JavaScript Document

//单选复选框重写
(function ($) {
    $.fn.extend({
        checkbox: function () {
            $(".checkbox").on("click", ".chkAll", function () {
                $(this).addClass("chkAll-ed").removeClass("chkAll");
                $(this).closest(".checkbox").find(".chkItem").addClass("chkItem-ed").removeClass("chkItem");
            });
            $(".checkbox").on("click", ".chkAll-ed", function () {
                $(this).addClass("chkAll").removeClass("chkAll-ed");
                $(this).closest(".checkbox").find(".chkItem-ed").addClass("chkItem").removeClass("chkItem-ed");
            });
            $(".checkbox").on("click", ".chkItem", function (event) {
                if (!$(this).hasClass("disable")) {
                    $(this).addClass("chkItem-ed").removeClass("chkItem");
                    var $parentObj = $(this).closest(".checkbox");
                    var len = $parentObj.find(".chkItem").length;
                    if (len == 0) {
                        $parentObj.find(".chkAll").addClass("chkAll-ed").removeClass("chkAll");
                    }
                }
            });
            $(".checkbox").on("click", ".chkItem-ed", function () {
                if (!$(this).hasClass("disable")) {
                    $(this).addClass("chkItem").removeClass("chkItem-ed");
                    $(this).closest(".checkbox").find(".chkAll-ed").addClass("chkAll").removeClass("chkAll-ed");
                }
            });
        },
        getCheckValue: function () {
            var chkVals = "";
            $(this).find(".chkItem-ed").each(function () {
                chkVals += $(this).attr("value") + ",";
            });
            return chkVals.substring(0, chkVals.length - 1);
        },
        radio: function () {
            $(".radioGroup").on("click", ".radio", function () {
                if (!$(this).hasClass("disable")) {
                    $(this).closest(".radioGroup").find(".radio-ed").addClass("radio").removeClass("radio-ed");
                    $(this).addClass("radio-ed").removeClass("radio");
                }
            });
        },
        getRadioValue: function () {
            var chkVals = "";
            $(this).find(".radio-ed").each(function () {
                chkVals += $(this).attr("value") + ",";
            });
            return chkVals.substring(0, chkVals.length - 1);
        },
        select: function (options) {
            if (options != null & options != "") {
                return this.each(function () {
                    new $.SelectBox($(this).find("select"), options);
                });
            } else {
                return this.each(function () {
                   // new $.SelectBox($(this).find("select"));
                });
            }
        },
        addSelectOption: function (option) {//增加选项
            $(this).append(option);
            var opnlist = $(this).parent(".select-box").find(".opn-list");
            opnlist.append($("<li title='" + $(option).text() + "'>" + $(option).text() + "</li>"));
            $(this).trigger("optionchange");
        },
        addSelectOptionByFilter: function (option, datatype, values, unitid, mergerunitname, fldname, iscode, mergerdatatype) {//条件筛选扩展
            $(this).append(option);
            var opnlist = $(this).parent(".select-box").find(".opn-list");
            opnlist.append($("<li mergerdatatype='" + mergerdatatype + "' iscode='" + iscode + "'  fldname='" + fldname + "' mergerunitname='" + mergerunitname + "' unitid='" + unitid + "' value='" + values + "' datatype='" + datatype + "' title='" + $(option).text() + "'>" + $(option).text() + "</li>"));
            $(this).trigger("optionchange");
        },
        addSelectOptionByOperator: function (option,value) {//条件筛选运算符扩展
            $(this).append(option);
            var opnlist = $(this).parent(".select-box").find(".opn-list");
            opnlist.append($("<li title='" + value + "'>" + $(option).text() + "</li>"));
            $(this).trigger("optionchange");
        },
        clearSelectOption: function (option) {//清空选项
            $(this).children().remove();
            var opnlist = $(this).parent(".select-box").find(".opn-list");
            opnlist.children().remove();

            $(this).trigger("optionchange");
        },
        setSelectedIndex: function (index) {//选中项
            $(this).find("option").eq(index).attr("selected", "selected");
            $(this).trigger("change");
        },
        setSelectedOption: function (value) {//选中项
            $(this).find("option[value=" + value + "]").attr("selected", "selected");
            $(this).trigger("change");
        }

    });
})(jQuery);

//下拉框重写
(function (jQuery) {
    $.fn.simSelect = function (o) {
        o = $.extend({									//设置默认参数 
            maxNum: 5,								    //最大显示5个
            width: 200,									//默认宽200px。为避免过多的设置宽度，尽量依照项目中最常见的宽度设定css样式。
            direction: "down",							//向下拉，另一个是up	
            disabled: false								//不可用时为true
        }, o || {});
        return this.each(function () {					//构造开始
            if ($(this).children(".slt-wrap")) {			//去重复	
                $(this).children(".slt-wrap").remove();
            };
            var $ts = $(this),
				$select = $ts.find("select").eq(0),
				wid = parseFloat($ts.attr("width")),
				num = parseFloat($ts.attr("max-num")),
				$sltWrap = $("<div class='slt-wrap'></div>").prependTo($ts),
				$sltTit = $("<a class='slt-title' hidefocus='true' href='javascript:void(0);'><span class='slt-text'></span><i></i></a>").prependTo($sltWrap),
				$sltText = $(".slt-text", $sltTit),
				$opnBox = $("<div class='opn-box'><ul class='opn-list'></ul></div>").appendTo($sltWrap),
				$opnList = $(".opn-list", $opnBox);
            $ts.addClass("select-style");                               //增加一个class专门作为写css样式用
            $select.find("option").each(function (i) {					//循环生成li标签	
                var text = $(this).text(),
					$li = $("<li title='" + text + "'>" + text + "</li>").appendTo($opnList);
                if (this.selected) {
                    $li.addClass("selected");
                    $sltText.text(text).attr("title", text);
                };
                if (this.disabled) {
                    $li.addClass("disabled");
                    return;
                };
            });
            var $li = $("li", $opnList),
				hei = $li.height();
            if (wid) {													//设置宽度
                $ts.css("width", wid + "px");							//兼容IE6、7
                $sltWrap.css("width", wid - 2 + "px");
            } else {
                $ts.css("width", o.width + "px");						//兼容IE6、7
                $sltWrap.css("width", o.width - 2 + "px");
            };
            if (num) {													//设置高度
                $opnList.css("max-height", hei * num + "px");
            }
            else {
                $opnList.css("max-height", hei * o.maxNum + "px");
            };
            if (o.direction == "up") {									//设置上、下拉方向
                $ts.addClass("up");
            };

            //修改为注册到父节点的li，当option动态改变时才能触发事件。lkf
            $opnList.on("click", "li", function () {									//li标签的点击事件，传给原生select
                var index = $opnList.find("li").index(this),
					text = $(this).text();
                if ($(this).hasClass("disabled")) {
                    return false;
                };
                $(this).addClass("selected").siblings().removeClass("selected");
                $select.find("option").prop("selected", false).eq(index).prop("selected", true);
                $sltText.text(text).attr("title", text);
                $opnBox.hide();
                $ts.removeClass("focus");
                $select.trigger("change");
            });
            $sltTit.on("click", function (e) { 							//a标签的点击下拉事件
                e.stopPropagation();									//阻止a标签的点击冒泡		
                if ($opnBox.is(":hidden")) {
                    $(".select-style .opn-box").hide();
                    $(".select-style").removeClass("focus");
                    $opnBox.show();
                    $ts.addClass("focus");
                }
                else {
                    $opnBox.hide();
                    $ts.removeClass("focus");
                }
            });
            $select.on("change", function () {								//原生select的点击事件，传给ul
                var index = $(this).find("option:selected").index(),
					text = $li.eq(index).text();
                $li.eq(index).addClass("selected").siblings().removeClass("selected");
                $sltText.text(text).attr("title", text);

            });
            $(document).on("click", function (e) {							//点击其他地方收起下拉框
                if ($opnBox.is(":visible")) {
                    $opnBox.hide();
                    $ts.removeClass("focus");
                }
            });
            if ($select.prop("disabled") == true || o.disabled == true || $ts.hasClass("disabled")) {
                $sltTit.off("click");									//设置禁用状态
                $select.prop("disabled", true);
                $ts.addClass("disabled");
            };


            $select.on("optionchange", function () {//选择改变
                $li = $("li", $opnList);
                hei = $li.height();
            });
        });
    };
})(jQuery);
