<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>游戏战队与赛事报名系统</title>
    <link rel="stylesheet" href="${ctx}/css/style.css">
</head>
<body>
<div class="navbar">
    <span class="logo">GAME ARENA · 电竞战队赛事系统</span>
    <a href="${ctx}/home">首页</a>
    <a href="${ctx}/teams">战队管理</a>
    <a href="${ctx}/players">队员管理</a>
    <a href="${ctx}/tournaments">赛事管理</a>
    <a href="${ctx}/registrations">赛事报名</a>
</div>
<div class="container">
<c:if test="${not empty sessionScope.flashMsg}">
    <div class="msg msg-${sessionScope.flashType}"><c:out value="${sessionScope.flashMsg}"/></div>
    <c:remove var="flashMsg" scope="session"/>
    <c:remove var="flashType" scope="session"/>
</c:if>
