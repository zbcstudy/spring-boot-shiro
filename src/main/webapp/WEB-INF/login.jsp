<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=utf-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <style>
        input {
            margin-top: 5px;
        }
    </style>
</head>
<body>
<h1>欢迎登录!${user.userName}</h1>
<form action="${pageContext.request.contextPath }/loginUser" method="post">
    <input type="text" name="username"><br>
    <input type="password" name="password"><br>
    <input type="submit" value="提交">
</form>
</body>
</html>

