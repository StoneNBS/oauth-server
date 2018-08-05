<%--
  Created by IntelliJ IDEA.
  User: wxyuan
  Date: 2018/8/3
  Describe: 授权认证页面
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>授权页面</title>
</head>
<body>
   <form action="dealAuth" method="post">
        用户名：<input type="text" value="tom" name="username"/><br/>
        密&nbsp;&nbsp;码：<input type="password" value="12345678" name="password"/><br/>
        <input type="submit" value="同意"/>&nbsp;&nbsp;
        <input type="button" value="拒绝"/>
   </form>
</body>
</html>
