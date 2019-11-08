function setTip(id, str, isValidate) {
    var sNname = "";
    if (isValidate) {
        sNname = "none";
        str = "";
    }
    else {
        sNname = "inline-block";
    }
    $("#ico" + id).css("display", sNname);
    $("#info" + id).html(str);
}
function getValidate(opt) {
    var str;
    switch (opt) {
        case "uName": //用户名
            str = /^[a-zA-Z][a-zA-Z0-9_]*$/g;
            break;
        case "eMail": //邮箱
            str = /^[\w-]+(\.[\w-]+)*@([\w-]+\.)+[a-zA-Z0-9]+$/;
            break;
        case "uTel": //电话
            str = /^[\d\-+\()]+$/;
            break;
        case "nName": //真实姓名
            str = /^[\u4e00-\u9fa5_a-zA-Z0-9]+$/;
            break;
        default:
    }
    return str
}
/******************************文本框验证脚本***********************************/
//controlid    要验证的控件id
//isEmptyTip   为空提示
//isMinLen     是否验证最小长度
//isMinLenTip  最小长度验证失败提示
//testStr      正则表达式
//testTip      正则表达式验证不通过提示
function jsValidate(controlid, isEmptyTip, minLen, minLenTip, testStr, testTip) {
    var validate = true; //默认验证通过
    var textValue = $("#" + controlid).val();
    if (textValue == null || textValue == "") {
        validate = false;
        setTip(controlid, isEmptyTip, validate);
    }
    else {
        if (minLen != 0 && textValue.length < minLen) {
            validate = false;
            setTip(controlid, minLenTip, validate);
        }
        else if (testStr != "") {
            var s = getValidate(testStr);
            if (!s.test(textValue)) {
                validate = false;
                setTip(controlid, testTip, validate);
            }
        }
    }
    if (validate) setTip(controlid, "", true); //验证通过隐藏提示
    return validate;
}

function compareInput(controlid1, controlid2, str) {
    var validate = true;
    if ($("#" + controlid1).val() != $("#" + controlid2).val()) {
        setTip(controlid2, str, false);
        $("#info" + controlid2).css("min-width", "150px");
        validate = false;
    }
    if (validate) setTip(controlid2, "", true); //验证通过隐藏提示
    return validate;
}

var uTip = "请输入用户名";
var uTipErr = "用户名输入错误";
var exisTip = "用户名已存在";
var pTip = "请输入密码";
var pTipErr = "密码长度不符";
var pcTip = "请重输密码";
var pcTipErr = "两次输入的密码不一致";
var nTip = "请输入真实姓名";
var nTipErr = "真实姓名只能中英文和数字";
var tTip = "请输入联系电话";
var tTipErr = "电话号码输入有误";
var eTip = "请输入电子邮箱";
var eTipErr = "电子邮箱输入有误";
var exisMail = "邮箱已注册";
var empVerifyCode = "请填写验证码";
var empLicense = "请选择许可协议";

function bindblur() {
    $("#regUserName").blur(function () {
        checkUserName();
    });
    $("#regPassword").blur(function () {
        jsValidate('regPassword', pTip, 6, pTipErr, "", "");
        if ($("#regConfirmPassword").val() != "") {
            var vcPwd = jsValidate('regConfirmPassword', pcTip, 6, pTipErr, '', '');
            if (vcPwd) {
                compareInput('regPassword', 'regConfirmPassword', pcTipErr);
            }
        }
    });
    $("#regConfirmPassword").blur(function () {
        var vcPwd = jsValidate('regConfirmPassword', pcTip, 6, pTipErr, '', '');
        if (vcPwd) {
            compareInput('regPassword', 'regConfirmPassword', pcTipErr);
        }
    });
    $("#regNickName").blur(function () {
        jsValidate('regNickName', nTip, 0, nTipErr, 'nName', nTipErr);
    });
    $("#regTel").blur(function () {
        jsValidate('regTel', tTip, 0, "", "uTel", tTipErr);
    });
    $("#regEmail").blur(function () {
        checkMail();
    });
    $("#regVerifyCode").blur(function () {
        var textValue = this.value;
        if (textValue == null || textValue == "" || textValue == "验证码" || textValue == "驗證碼") {
            setTip('regVerifyCode', empVerifyCode, false);
        }
        else {
            setTip('regVerifyCode', '', true);
        }
    });
    $(".checkbox").on("click", "#chkLicense", function () {
        if (!$(this).hasClass("disable")) {
            if ($(this).hasClass("chkItem")) {
                setTip('chkLicense', empLicense, false);
            }
            else {
                setTip('chkLicense', '', true);
            }
        }
    });
    $(".checkbox").on("blur", ".aLicense", function () {
        if (!$('#chkLicense').hasClass("disable")) {
            if ($('#chkLicense').hasClass("chkItem")) {
                setTip('chkLicense', empLicense, false);
            }
            else {
                setTip('chkLicense', '', true);
            }
        }
    });
}


//检测用户名
function checkUserName() {
    var validate = true;
    validate = jsValidate('regUserName', uTip, 2, uTipErr, "uName", uTipErr);
    if (validate) {
        var userName = $("#regUserName").val();
        $.ajax({
            url: '/Register/CheckUserName',
            type: 'POST',
            async: false,
            data: { userName: userName },
            dataType: 'text',
            success: function (data) {
                if (data == "isexis") {
                    validate = false;
                    setTip("regUserName", exisTip, validate);
                }
            },
            error: function (data) {

            }
        });
    }
    return validate;
}
//检测邮箱是否存在
function checkMail() {
    var validate = true;
    validate = jsValidate('regEmail', eTip, 0, '', 'eMail', eTipErr);
    if (validate) {
        var usermail = $("#regEmail").val();
        $.ajax({
            url: '/Register/CheckUserMail',
            type: 'POST',
            data: { mail: usermail },
            async: false,
            dataType: 'text',
            success: function (data) {
                if (data == "isexis") {
                    validate = false;
                    setTip("regEmail", exisMail, validate);
                }
            },
            error: function (data) {

            }
        });
    }
    return validate;
}


function inputValidate() {
    var validate = true;
    var vUname = checkUserName();
    var vPwd = jsValidate('regPassword', pTip, 6, pTipErr, "", "");
    var vcPwd = jsValidate('regConfirmPassword', pcTip, 6, pTipErr, '', '');
    if (vcPwd) {
        var isEqual = compareInput('regPassword', 'regConfirmPassword', pcTipErr);
    }
    //var vNiname = jsValidate('regNickName', nTip, 0, "", "", "");
    //var vTel = jsValidate('regTel', tTip, 0, "", "uTel", tTipErr);
    var vMail = checkMail();
    $("#regVerifyCode").blur();
    $(".checkbox .aLicense").blur();
    //if (!vUname || !vPwd || !vcPwd || !isEqual || !vNiname || !vTel || !vMail) {
    if (!vUname || !vPwd || !vcPwd || !isEqual || !vMail) {
        validate = false;
    }
    return validate;
}


//匿名登录
function doAnonymousLogin() {

    $("#dWaiting").css("display", "block");
    $.post("/Login/AnonymousLogin", {},
            function (data) {
                if (data) {
                    if (data.success) {
                        window.location.href = '/Home/Index';
                    }
                    else {
                        $("#dWaiting").css("display", "none");
                        layer.alert(data.msg, { icon: 0, title: "信息", btn: ['确定 '] });
                    }
                }
                else {
                    
                    layer.alert('登录失败', { icon: 0, title: "信息", btn: ['确定 '] });
                }
            }, "json");
}

var inputPassword = "请输入密码";
var inputUserName = "请输入账号";
var inputVerifyCode = "验证码";
var tryLoginTimes = 0; //尝试登陆次数，如果登陆成功了则设为0，否则每次登陆+1。
// 登录
function doLogin() {
    tryLoginTimes = tryLoginTimes + 1;

    var userName = $("#userName").val();
    var password = $("#password").val();
    var verifyCode = $("#txtVerifyCode").val();

    if (userName == inputUserName)
        userName = "";
    if (password == inputPassword)
        password = "";

    if (verifyCode == inputVerifyCode)
        verifyCode = "";
    if (userName == "" || userName == null) {
        blurLoginInput();
        layer.alert('用户名不能为空', { icon: 0 });
        return;
    }
    if (password == "" || password == null) {
        blurLoginInput();
        layer.alert('密码不能为空', { icon: 0 });
        return;
    }
    var isShowCode = $("#verifyCodeBox").css("display");
    if (isShowCode != "none") {
        if (verifyCode == "" || verifyCode == null) {
            blurLoginInput();
            layer.alert('验证码不能为空', { icon: 0 });
            return;
        }
    }
    var isOpen = $("#txtisDialogBox").val();
    $("#dWaiting").css("display", "block");
    //$.post("/Login/Login", { UserName: userName, Password: password, VerifyCode: verifyCode },
    $.ajax({
        type: "POST",
        url: "/Login/Login",
        dataType: "json",
        data: { UserName: userName, Password: password, VerifyCode: verifyCode },
        async: false,
        success: function (data) {
            if (data) {
                if (data.success) {
                    tryLoginTimes = 0;
                    if (mark_action != null && mark_action != "") {
                        setTopInfo(data);
                        var index = parent.layer.getFrameIndex(window.name);
                        if (isTopLogin == "true") {
                            parent.location.reload();
                        }
                        parent.layer.close(index);
                    }
                    else {
                        if (isOpen == "showDialog") {
                            parent.location.reload();
                        }
                        else {
                            window.top.location.href = '/Home/Index';
                        }
                    }
                }
                else {

                    
                    if (data.msg == "0") {
                        blurLoginInput();
                        layer.open({
                            content: '你的账号已经在另外一个地点登录，是否强制性将对方逼下线！'
                              , btn: ['是', '否']
                              , title: "信息"
                              , yes: function (index, layero) {
                                  $.post("/Login/LoginOff", {},
                                function (data) {
                                    $("#dWaiting").css("display", "none");
                                    if (data.success) {
                                        if (mark_action != null && mark_action != "") {
                                            setTopInfo(data);
                                            var index = parent.layer.getFrameIndex(window.name);
                                            parent.layer.close(index);
                                        }
                                        else {
                                            if (isOpen == "showDialog") {
                                                parent.location.reload();
                                            }
                                            else {
                                                window.top.location.href = '/Home/Index';
                                            }
                                        }
                                    }
                                })
                              }, btn2: function (index, layero) {
                                  $("#userName").val("请输入账号").css("color", "#999");
                                  $("#password").hide().val("");
                                  $('#password1').show();
                              }
                              , cancel: function () {
                                  $("#userName").val("请输入账号").css("color", "#999");
                                  $("#password").hide().val("");
                                  $('#password1').show();
                                  //右上角x关闭时候回调
                              }
                        });
                    }
                    else if (data.msg.indexOf("未激活") > 1) {
                        $("#dWaiting").css("display", "none");
                        data.msg = data.msg.replace("激活帐号", "<a class='red weight' onclick='gotoActivate()' href=\"javascript:\" style='text-decoration: underline'>激活帐号</a>");
                        data.msg = data.msg.replace("重新发送激活邮件", "<a class='red weight' onclick='sendMail()' href=\"javascript:\" style='text-decoration: underline'>重新发送激活邮件</a>");

                        //showVerifyCode(data.isShowVerifyCode);//调用doLogin()的地方自行判断是否要显示验证码
                        if (data.isShowVerifyCode) {
                            //如果服务器认为要显示验证码，则把服务器认为的登陆错误次数赋值给客户端记录的次数
                            tryLoginTimes = data.loginFailureTimes;
                        }

                        blurLoginInput();
                        layer.alert(data.msg, { icon: 0 });
                    }
                    else {
                        $("#dWaiting").css("display", "none");
                        blurLoginInput();

                        //showVerifyCode(data.isShowVerifyCode);//调用doLogin()的地方自行判断是否要显示验证码
                        if (data.isShowVerifyCode) {
                            //如果服务器认为要显示验证码，则把服务器认为的登陆错误次数赋值给客户端记录的次数
                            tryLoginTimes = data.loginFailureTimes;
                        }

                        layer.alert(data.msg, { icon: 0 });
                    }
                }
            }
            else {

                $("#dWaiting").css("display", "none");
                layer.alert('登录失败', { icon: 0 });
            }
                
        }
    });
}

//取得服务器上配置的【登录失败多少次数后，显示验证码】的次数
var configLoginFailureTimes = -1;
function getConfigLoginFailureTimes() {
    if (configLoginFailureTimes != -1) {
        return configLoginFailureTimes;
    }

    $.ajax({
        type: "POST",
        url: "/Login/GetConfigLoginFailureTimes",
        dataType: "json",
        data: {},
        async: false,
        success: function (data) {
            if (data.Result == 1) {
                configLoginFailureTimes = data.Data;
            } else {
                configLoginFailureTimes = -1;
            }
        },
        error: function (msg) {
            configLoginFailureTimes = -1;
        }
    });

    if (configLoginFailureTimes != -1) {
        return configLoginFailureTimes;
    } else {
        return 1;
    }
}