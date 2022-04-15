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


    <script src="//apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>
</head>

<body>
    <div id="alert" class="alert alert-danger" style="display:none;position: absolute;top:1%;left:50%;transform: translateX(-50%)">
        <strong id="alert-strong"></strong> <span id="alert-text"></span>
    </div>
<div class="container form-margin-top">

    <form class="form-signin" onsubmit="return false;">

        <h2 class="form-signin-heading" align="center"><img src="logo.png" style="position: relative; top:10%; right:10%;z-index: 999;"/>百诗</h2>

        <input type="text" name="username" class="form-control" placeholder="帐号 (英文字母、数字、下划线)" autofocus>
        <input type="password" name="password" class="form-control" placeholder="密码 (至少6位)" >
        <input type="password" name="password2" class="form-control" placeholder="密码确认" >
        <div style="height: 50px">
            <input type="text" name="code" id="code" class="form-control" maxlength="4" placeholder="验证码"/>
            <img src="/captcha" id="captcha" class="verify-code" onclick="changeCaptcha();" title="看不清？那换个吧"/>
        </div>

        <button class="btn btn-lg btn-primary btn-block" type="submit" onclick="signUp()" style="margin-top:20px;">注 册</button>

        <div style="margin-top: 10px">
            <a href="javascript:void(0)" style="float: left" onclick="reset()">重置</a>
            <a href="/login" style="float: right">返回登录</a>
        </div>
    </form>

    <#include "common/star-run.ftlh">


</div>
<footer>
    <p>support by: chouway</p>
    <p>email: 279704064@qq.com</p>
</footer>
</body>

<script  type="text/javascript">
    function changeCaptcha(){
        document.getElementById("captcha").src="/captcha?"+Math.random();
    }

    function error(alertStrong,alertText) {
        $('#alert-strong').text(alertStrong)
        $('#alert-text').text(alertText)
        $('#alert').show()
        setTimeout(function() {
            $("#alert").fadeOut('fast');
        }, 5000);
    }
    function success() {
        var seconds = 3;
        $('#alert-strong').text("注册成功！")
        $('#alert-text').text("稍候自动跳转(" + seconds + "秒）")
        $('#alert').removeClass("alert-danger").addClass("alert-success").show();
        setInterval(function () {
            if (seconds>1){
                $('#alert-text').text("稍候自动跳转(" + --seconds + "秒）")
            }else{
                $('#alert-text').text("跳转登录页...")
                window.location.href = "/login";
            }
        },1000)
    }

    function reset() {
        $("input[name='username']").val('');
        $("input[name='password']").val('');
        $("input[name='password2']").val('');
    }

    function signUp(){
        var username = $("input[name='username']").val();
        if (username == ''){
            error("帐号为空!","请填写。")
            return false;
        }
        if(!/^[a-zA-Z0-9_]+$/.test(username)){
            error("帐号无效!","请输入英文、数字、下划线组成的帐号。")
            return false;
        }
        var password = $("input[name='password']").val();
        if (password == ''){
            error("密码为空!","请填写。")
            return false;
        }
        if (password.length<6){
            error("密码过短!","至少6位。")
            return false;
        }
        var password2 = $("input[name='password2']").val();
        if (password2 == ''){
            error("密码确认为空!","请填写。")
            return false;
        }
        if (password!=password2){
            error("两次密码不一致!","请重新填写。")
            return false;
        }

        var code = $("input[name='code']").val();
        if (code == ''){
            error("验证码为空!","请填写。")
            return false;
        }
        var data = {
            username:username,
            password:password
        }
        //请求注册
        var url = "/signUp.json?code=" + code;
        var setting = {
            url:url,
            type:'POST',
            contentType:'application/json',
            data: JSON.stringify(data),
            success:function (res) {
                // console.info(JSON.stringify(res));
                if (!res.success){
                    error(res.code,res.message);
                }else{
                    success();
                }
            },
            error:function (res) {
                error("请求失败!","请稍候再试。")
            }
        };
        $.ajax(url,setting);
    }
    $(function () {

    });
</script>

</html>
