<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>百诗登录</title>

    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <link href="/css/star-run.css" rel="stylesheet">
    <link href="/css/signin.css" rel="stylesheet">
    <style>
        .error_tip {
            text-align: center;
            margin-top: 15px;
            font-size: 16px;
        }
    </style>

    <script src="//apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>
</head>

<body>
<div id="alert" class="alert alert-info" style="display:none;position: absolute;top:1%;left:50%;transform: translateX(-50%)">
    <strong id="alert-strong"></strong> <span id="alert-text"></span>
</div>
<div class="container form-margin-top">

    <form class="form-signin" action="/doLogin" method="POST">

        <h2 class="form-signin-heading" align="center"><img src="/logo.png" style="position: relative; top:10%; right:10%;z-index: 999;"/>百诗</h2>

        <input type="text" name="username" class="form-control" placeholder="帐号" required autofocus>
        <input type="password" name="password" class="form-control" placeholder="密码" required>
        <div style="height: 50px">
            <input type="text" name="code" id="code" class="form-control" maxlength="4" placeholder="验证码" required/>
            <img src="/captcha" id="captcha" class="verify-code" onclick="changeCaptcha();" title="看不清？那换个吧"/>
        </div>

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <button class="btn btn-lg btn-primary btn-block" type="submit" style="margin-top:20px;">登 录</button>
        <div style="margin-top: 10px">
            <a href="javascript:void(0)" style="float: left" onclick="forgotPwd()">忘记密码？</a>
            <a href="/signUp" style="float: right">注册</a>
        </div>
        <!--登录错误提示信息-->
        <#if RequestParameters['error']??>
            <div class="error_tip">
                <span class="help-block"
                      style="color:red;">${(Session.SPRING_SECURITY_LAST_EXCEPTION.message)!""}</span>
            </div>
        </#if>

    </form>

    <#include "common/star-run.ftlh">


</div>
<footer>
    <p>support by: chouway</p>
    <p>email: 279704064@qq.com</p>
</footer>
</body>

<script>
    function error(alertStrong,alertText) {
        $('#alert-strong').text(alertStrong)
        $('#alert-text').text(alertText)
        $('#alert').show()
        setTimeout(function() {
            $("#alert").fadeOut('fast');
        }, 5000);
    }
    function forgotPwd() {
        error("暂未开放!","敬请期待")
        return false;
    }
    function changeCaptcha(){
        document.getElementById("captcha").src="/captcha?"+Math.random();
    }
    $(function () {

    });
</script>

</html>
