<%--
  Created by IntelliJ IDEA.
  User: Laptop88
  Date: 10/6/2021
  Time: 9:55 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Title</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<body>
<form action="/vendors?action=create" method="post">
    <label for="name"></label>
    <input type="text" id="name" name="name" placeholder="Enter vendor name...">
    <button>Create</button>
</form>
</body>
</html>
