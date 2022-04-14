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
<div class="container form-margin-top">

    <form class="form-signin" onsubmit="return false;">

        <h2 class="form-signin-heading" align="center"><img src="logo.png" style="position: relative; top:10%; right:10%;z-index: 999;"/>百诗</h2>

        <input type="text" name="username" class="form-control" placeholder="帐号 (英文字母、数字、下划线)" required autofocus>
        <input type="password" name="password" class="form-control" placeholder="密码 (至少6位)" required>
        <input type="password" name="password2" class="form-control" placeholder="请再次输入密码" required>
        <div style="height: 50px">
            <input type="text" name="code" id="code" class="form-control" maxlength="4" placeholder="验证码" required/>
            <img src="/captcha" id="captcha" class="verify-code" onclick="changeCaptcha();" title="看不清？那换个吧"/>
        </div>

        <button class="btn btn-lg btn-primary btn-block" type="submit" style="margin-top:20px;">注 册</button>
        <button class="btn btn-lg btn-primary btn-block" type="submit" onclick="returnLogin()" style="margin-top:15px;">返 回</button>


    </form>

    <#include "common/star-run.ftlh">


</div>
<footer>
    <p>support by: chouway</p>
    <p>email: 279704064@qq.com</p>
</footer>
</body>

<script>
    $(function () {

        function changeCaptcha(){
            document.getElementById("captcha").src="/captcha?"+Math.random();
        }

        function returnLogin(){
            window.location.href = "/login";
            return false;
        }
    });
</script>

</html>
