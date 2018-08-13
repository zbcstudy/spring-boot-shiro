<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=utf-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    <h1>登陆成功跳转的页面</h1>

    <a href="/user">user jsp</a>
    <br>
    <shiro:hasRole name="admin">
        <a href="/admin">admin jsp</a>
    </shiro:hasRole>
    <br>
    <a href="/logout">登出</a>
</body>
</html>
