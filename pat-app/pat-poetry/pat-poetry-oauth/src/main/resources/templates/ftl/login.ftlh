<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>微服务统一认证</title>

    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <link href="/css/signin.css" rel="stylesheet">
    <style>
        .error_tip {
            text-align:center
        }
    </style>


</head>

<body>
<div class="container form-margin-top">
    <!-- 这里请求方法为post，配置的url跟formLogin().loginPage("/login")的login一致，如果配formLogin().loginPage("/login").loginProcessingUrl("/form") 那么配置form -->
    <form class="form-signin" action="/login" method="POST">
        <h2 class="form-signin-heading" align="center">统一认证系统</h2>
        <input type="text" name="username" class="form-control form-margin-top" placeholder="账号" required autofocus>
        <input type="password" name="password" class="form-control" placeholder="密码" required>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <button class="btn btn-lg btn-primary btn-block" type="submit">登 录</button>
        <!--登录错误提示信息-->
        <#if RequestParameters['error']??>
            <div class="error_tip">
                <span class="help-block" style="color:red;">${(Session.SPRING_SECURITY_LAST_EXCEPTION.message)!""}</span>
            </div>
        </#if>
    </form>
</div>
<footer>
    <p>support by: chouway</p>
    <p>email: 279704064@qq.com</p>
</footer>
</body>
</html>
