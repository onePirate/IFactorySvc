// JavaScript Document
/*TreType enum*/
var TreeTypes = {
股票代码信息树:1,
债券代码信息树 : 2,
权证代码信息树 : 3,
期货代码信息树 : 4,
指数代码信息树 : 5,
银行代码信息树 : 6,
基金代码信息树 : 7,
股票自定义查询树 : 8,
单表查询树 : 9,
债券自定义查询树 : 10,
权证自定义查询树 : 11,
期货自定义查询树 : 12,
银行自定义查询树 : 13,
回购代码信息树 : 14,
工业代码信息树 : 16,
世界经济自定义查询树 : 17,
国家代码树 : 18,
省份代码树 : 19,
城市代码树 : 20,
工业年度自定义查询树 : 21,
区域城市自定义查询树 : 22,
区域省份自定义查询树 : 23,
宏观季度自定义查询树 : 24,
宏观年度自定义查询树 : 25,
宏观月度自定义查询树 : 26,
工业月度自定义查询树 : 27,

    //add by weitang 2015-10-16 基金新树
    //基金自定义查询树 = 28,
基金自定义查询树 : 45,

新闻索引信息树 : 29,
公告索引信息树 : 30,
研究报告索引信息树 : 31,
港股代码信息树 : 32,

    //唐伟
    //财务交易数据跨表查询树
股票日频交易数据树 : 42,
股票周频交易数据树 : 43,
股票月频交易数据树 : 44,

基金日频交易数据树 : 51,
基金周频交易数据树 : 52,
基金月频交易数据树 : 53,

国债日频交易数据树 : 34,
可转债日频交易数据树 : 35,
可分离日频交易数据树 : 36,
债券回购日频交易数据树 : 37,

国债月频交易数据树 : 38,
可转债月频交易数据树 : 39,
可分离月频交易数据树 : 40,
债券回购月频交易数据树 : 41,

    //涂小丽 2015/4/16
    //模型定制树
模型定制Matlab信息树 : 48,
模型定制SAS信息树 : 49,
模型定制R信息树: 50,
    股票代碼信息樹: 1,
    債券代碼信息樹: 2,
權證代碼信息樹: 3,
期貨代碼信息樹: 4,
指數代碼信息樹: 5,
銀行代碼信息樹: 6,
基金代碼信息樹: 7,
股票自定義查詢樹: 8,
單表查詢樹: 9,
債券自定義查詢樹: 10,
權證自定義查詢樹: 11,
期貨自定義查詢樹: 12,
銀行自定義查詢樹: 13,
回購代碼信息樹: 14,
工業代碼信息樹: 16,
世界經濟自定義查詢樹: 17,
國家代碼樹: 18,
省份代碼樹: 19,
城市代碼樹: 20,
工業年度自定義查詢樹: 21,
區域城市自定義查詢樹: 22,
區域省份自定義查詢樹: 23,
宏觀季度自定義查詢樹: 24,
宏觀年度自定義查詢樹: 25,
宏觀月度自定義查詢樹: 26,
工業月度自定義查詢樹: 27,

//add by weitang 2015-10-16 基金新樹
//基金自定義查詢樹 = 28,
基金自定義查詢樹: 45,

新聞索引信息樹: 29,
公告索引信息樹: 30,
研究報告索引信息樹: 31,
港股代碼信息樹: 32,

//唐偉
//財務交易數據跨表查詢樹
股票日頻交易數據樹: 42,
股票周頻交易數據樹: 43,
股票月頻交易數據樹: 44,

基金日頻交易數據樹: 51,
基金周頻交易數據樹: 52,
基金月頻交易數據樹: 53,

國債日頻交易數據樹: 34,
可轉債日頻交易數據樹: 35,
可分離日頻交易數據樹: 36,
債券回購日頻交易數據樹: 37,

國債月頻交易數據樹: 38,
可轉債月頻交易數據樹: 39,
可分離月頻交易數據樹: 40,
債券回購月頻交易數據樹: 41,

//塗小麗 2015/4/16
//模型定制樹
模型定制Matlab信息樹: 48,
模型定制SAS信息樹: 49,
模型定制R信息樹: 50
};
/*代码enum*/
var CodeType = {
    不是代码:0,
    股票代码信息:1,
    债券代码信息:2,
    权证代码信息:3,
    期货代码信息:4,
    指数代码信息:5,
    银行代码信息:6,
    基金代码信息:7,
    回购代码信息:8,
    工业代码信息:9,
    国家代码信息:10,
    城市代码信息:11,
    省份代码信息:12,
    港股代码信息:13,
    用户自定义代码:14
}
/*语言类型*/
var LanguageType={
    Zh_cn:1,
    Zh_hw:2,
    Zh_tw:3,
    En_us:4    

}
/*把1、2、3转换为一、二、三    
   */
var N = [
           "零", "一", "二", "三", "四", "五", "六", "七", "八", "九"
];
function ConvertToChinese(num) {
    var str = num.toString();
    var len = num.toString().length;
    var C_Num = [];
    for (var i = 0; i < len; i++) {
        C_Num.push(N[str.charAt(i)]);
    }
    return C_Num.join('');
}
/* 传类型输出选择项下拉框
 maxvalue:最大值
 minvalue:最小值
 type：1 年份 2月 3季度
*/
function OptionSele(type, maxvalue, minvalue) {
    var option = "";
    switch (type) {
        case 1:
        case 2:
            for (var i = minvalue; i <= maxvalue; i++) {
                //添加option
                option += "<option value='" + i + "'>" + i + "</option>";
            }
            break;
        case 3:
            for (var i = minvalue; i <= maxvalue; i++) {

                option += "<option value='0" + i + "'>第" + ConvertToChinese(i) + "季度</option>";
            }
            break;
        default:

    }
    return option;

}
/*控制月份，季度函数*/
function returnOption(length, type, elentid, month, quarter) {
    var option = "";
    if (type == 1) {
        for (var i = 1; i <= length; i++) {
            option += "<option value='0" + i + "'>第" + ConvertToChinese(i) + "季度</option>";
        }
    }
    else {
        for (var i = 1; i <= length; i++) {
            option += "<option value='" + i + "'>" + i + "</option>";
        }
    }
    $(elentid).empty().append(option);
    var curQuarter = "0" + quarter;
    var maxvalue = "0" + length;
    if (type == 1) {
        $(elentid).val(curQuarter);
    }
    else {
        $(elentid).val(month);

    }
    $(elentid).data("maxValue", maxvalue)
}
$(function () {

    //调用语句
   try{
    $(".checkbox").checkbox();
    $(".radioGroup").radio();
    $(".select-box").simSelect();
   }catch(e){}

    //输入框交互
    $(".txt,textarea").focus(function () {
        $(this).addClass("focus");
    });
    $(".txt,textarea").blur(function () {
        $(this).removeClass("focus");
    });
    //返回顶部
    $(window).scroll(function () {
        var t = $(this).scrollTop();
        if (t > 100) {
            $(".go-top").stop().show();
        } else {
            $(".go-top").stop().hide();
        };
    });
    $(".go-top").click(function () {
        $("body,html").stop().animate({ scrollTop: 0 }, 300)
    });

    //语言切换
    $(".language  a").click(function () {
        $(this).addClass("cur").siblings().removeClass("cur");
    })

    //登录页错误提示
    $(".login-form .tips i").hover(function () {
        $(this).prev().fadeIn();
    }, function () {
        $(this).prev().fadeOut();
    });
    //Tab切换
    var $tab = $(".js-tab"),
		$tabLi = $tab.children(".tab-hd").find("li"),
		$tabItem = $tab.children(".tab-cont").find(".tab-item");
    $tabItem.eq(0).show();
    $tabLi.each(function () {
        $(this).click(function () {
            var index = $(this).index();
            $(this).addClass("cur").siblings("li").removeClass();
            $(this).parents(".js-tab").find($tabItem).hide().eq(index).stop().fadeIn();
        });
    });

    //首页订阅模块
    $(".subscriber-list dt a").click(function () {
        $(this).parents("dl").children("dd").slideToggle();
        $(this).children("i").toggleClass("open");
    });

    //淡入淡出轮播图
    $(".scroll").each(function () {
        var $imgList = $(".img"),
			$imgLi = $imgList.children("li"),
			len = $imgLi.length,
			k = 0,
			t = setInterval(move, 5000);
        $imgLi.eq(0).show();

        for (var i = 1; i <= len; i++) {
            var li = "<li>" + i + "</li>";
            $(".num").append(li);
        };
        $(".num li").eq(0).addClass("cur");
        function moveL() {
            k--;
            if (k == -1) {
                k = len - 1;
            };
            $(".num li").eq(k).addClass("cur").siblings().removeClass("cur");
            $(".img li").eq(k).fadeIn().siblings().fadeOut();
        };
        function move() {
            k++;
            if (k == len) {
                k = 0;
            };
            $(".num li").eq(k).addClass("cur").siblings().removeClass("cur");
            $(".img li").eq(k).fadeIn().siblings().fadeOut();
        };
        $(".scroll").hover(function () {
            clearInterval(t);
        }, function () {
            t = setInterval(move, 5000);
        });
        $(".num li").click(function () {
            $(this).addClass("cur").siblings().removeClass("cur");
            var index = $(this).index();
            k = index;
            $(".img li").eq(index).stop().fadeIn().siblings().stop().fadeOut();
        });
        $(".scroll .prev").click(function () {
            moveL();
        })
        $(".scroll .next").click(function () {
            move();
        })
    });

    //数据查询-条件筛选
    $(".condition-box .pub-btn").click(function () {
        $(this).toggleClass("open");
        $(this).next().stop().slideToggle();
    });

    //树结构
    $('.js-tree li:has(ul)').addClass('parent_li');
    $('.js-tree li.parent_li >a >i').on('click', function (e) {
        var children = $(this).parent().parent('li').children(' ul');
        if (children.is(":visible")) {
            children.slideUp();
            $(this).text("+");
        } else {
            children.slideDown();
            $(this).text("-");
        }
        e.stopPropagation();
    });
    $('.Allclick li:has(ul)').addClass('parent_li');
    $('.Allclick li.parent_li >a').on('click', function (e) {
        var children = $(this).parent('li').children(' ul');
        if (children.is(":visible")) {
            children.slideUp();
            $(this).children("i").text("+");
        } else {
            children.slideDown();
            $(this).children("i").text("-");
        }
        e.stopPropagation();
    });
    $(".pub-tree li a").click(function () {
        //$(this).toggleClass("cur");
    });
    //$(".Allclick .parent_li ul a").click(function () {
    //    if ($(this).next().is("ul")) {
    //        $(this).removeClass("cur");
    //    }
    //    else {
    //        $(this).addClass("cur").parents("li").siblings().find("a").removeClass("cur");
    //        var $li = $(this).parent("li").parent().parent("li");
    //        var text = "";
    //        if ($li.parent().attr("class") != "first-item") {
    //            text = $li.parent().parent("li").attr("nodename") + " — ";
    //        }
    //        text += $li.attr("nodename")
    //        $("#dbinfo_node").text(text);
    //        $("#dbinfo_tb").text(" — " + $(this).text());
    //        loadFileds($(this).parent("li").attr("tbid"));
    //    }
    //});
    $(".code-tree a").click(function () {
        $(this).toggleClass("cur");
    });

    //左侧菜单
    $(".menu-list li").click(function () {
        $(this).addClass("cur").siblings().removeClass("cur");
    });

    //支付方式
    $(".pay li").click(function () {
        $(this).addClass("cur").siblings("li").removeClass("cur");
    });
    //发票
    $(".invoice #true").on('click', function () {
        //slideDown后overflow变成了hidden，这里把它修正回来。
        var overflowValue = $(".invoice-cont").css("overflow");
        $(".invoice-cont").slideDown("normal", function () {
            $(".invoice-cont").css("overflow", overflowValue);
        });
        $(".invoice_money").show();
    });
    $(".invoice #false").on('click', function () {
        $(".invoice-cont").slideUp();
        $(".invoice_money").hide();
    });
    //优惠码
    $("#Codetrue").on('click', function () {
        //slideDown后overflow变成了hidden，这里把它修正回来。
        var overflowValue = $(".code-cont").css("overflow");
        $(".code-cont").slideDown("normal", function () {
            $(".code-cont").css("overflow", overflowValue);
        });
        $(".p_code").show();
    });
    $("#Codefalse").on('click', function () {
        $(".code-cont").slideUp();
        $(".p_code").hide();
    });
    ////左右数据交互
    //$(".choose-left .code-list").on("click", "li", function () {
    //    $(this).addClass("cur");
    //    $(this).clone(false).appendTo($(".choose-right .code-list"));
    //    $(this).remove();

    //});
    //$(".choose-right .code-list").on("click", "li", function () {
    //    $(this).removeClass("cur");
    //    $(this).clone(false).appendTo($(".choose-left .code-list"));
    //    $(this).remove();
    //});
    //$(".choose-left .pub-btn").on("click", function () {
    //    $(".choose-left .code-list li").addClass("cur").clone(false).appendTo($(".choose-right .code-list"));
    //    $(".choose-left .code-list li").remove();
    //});
    //$(".choose-right .pub-btn").on("click", function () {
    //    $(".choose-right .code-list li").removeClass("cur").clone(false).appendTo($(".choose-left .code-list"));
    //    $(".choose-right .code-list li").remove();
    //});



}); //结束


$('#exit').on('click', function () {
    layer.confirm('您确定要退出吗？', {
        btn: ['确定', '取消'], //按钮
        icon: 3,
    }, function () {
        window.location.href = "/home/Logout";
    });
});

$('#login').on('click', function () {
    layer.open({
        type: 2,
        title: '用户登录',
        maxmin: false,
        scrollbar: true,
        shadeClose: false, //点击遮罩关闭层
        area: ['440px', '420px'],
        content: '/Login/LoginBoxPartial?topclick=1',
        success: function (layer) {
        }
     });
     
});
$('#search-popup').on('click', function () {
    layer.open({
        type: 2,
        title: '数据搜索',
        maxmin: false,
        scrollbar: false,
        shadeClose: true, //点击遮罩关闭层
        area: ['750px', '520px'],
        content: 'popup/search-list.html'
    });
});


(function (jQuery) {
    //string的扩展方法
    $.extend(String.prototype, {
        //超出长度截取
        ellipsis: function (length) {
            if (this.toString().length > length)
                return this.toString().substr(0, length) + "..";
            else
                return this.toString();
        }
    });
})(jQuery);


function validateMial(__email) {
    var s = /^[\w-]+(\.[\w-]+)*@([\w-]+\.)+[a-zA-Z0-9]+$/
    return s.test(__email)
}

/*数据中心页面更改结构后增加的扩展jq方法（滚动后浮动定位）*/
$.fn.stickyfloat = function (options, lockBottom) {
    var $obj = this;
    var parentPaddingTop = parseInt($obj.parent().css('padding-top'));
    var startOffset = $obj.parent().offset().top;
    var opts = $.extend({ startOffset: startOffset, offsetY: parentPaddingTop, duration: 200, lockBottom: true }, options);
    $obj.css({ position: 'absolute' });
    if (opts.lockBottom) {
        var bottomPos = $obj.parent().height() - $obj.height() + parentPaddingTop;
        if (bottomPos < 0)
            bottomPos = 0;
    }
    $(window).scroll(function () {
        $obj.stop();
        var pastStartOffset = $(document).scrollTop() > opts.startOffset;
        var objFartherThanTopPos = $obj.offset().top > startOffset;
        var objBiggerThanWindow = $obj.outerHeight() < $(window).height();
        if ((pastStartOffset || objFartherThanTopPos) && objBiggerThanWindow) {
            var newpos = ($(document).scrollTop() - startOffset + opts.offsetY);
            if (newpos > bottomPos)
                newpos = bottomPos;
            if ($(document).scrollTop() < opts.startOffset)
                newpos = parentPaddingTop;

            $obj.animate({ top: newpos }, opts.duration);
        }
    });
};