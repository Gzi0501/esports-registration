<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="../common/header.jsp" %>

<div class="card">
    <h2>队员管理</h2>

    <form class="search-bar" method="get" action="${ctx}/players">
        <input type="text" name="keyword" value="<c:out value='${keyword}'/>"
               placeholder="选手姓名 / 游戏ID / 位置">
        <select name="teamId">
            <option value="0">全部战队</option>
            <c:forEach var="t" items="${teams}">
                <option value="${t.id}" <c:if test="${teamId == t.id}">selected</c:if>><c:out value="${t.teamName}"/></option>
            </c:forEach>
        </select>
        <button type="submit" class="btn btn-sm">查询</button>
        <a class="btn btn-sm btn-default" href="${ctx}/players">重置</a>
        <div class="spacer"></div>
        <a class="btn btn-sm btn-success" href="${ctx}/players?action=add">+ 添加队员</a>
    </form>

    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>选手姓名</th>
            <th>游戏ID</th>
            <th>场上位置</th>
            <th>年龄</th>
            <th>联系电话</th>
            <th>所属战队</th>
            <th>加入时间</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="p" items="${list}">
            <tr>
                <td>${p.id}</td>
                <td><c:out value="${p.playerName}"/></td>
                <td><c:out value="${p.gameId}"/></td>
                <td>${p.position}</td>
                <td>${p.age}</td>
                <td><c:out value="${p.phone}"/></td>
                <td><a href="${ctx}/teams?action=detail&id=${p.teamId}"><c:out value="${p.teamName}"/></a></td>
                <td>${p.joinTime}</td>
                <td>
                    <a class="btn btn-sm btn-default" href="${ctx}/players?action=edit&id=${p.id}">编辑</a>
                    <a class="btn btn-sm btn-danger" href="javascript:void(0)"
                       onclick="if(confirm('确定删除该队员吗？'))location.href='${ctx}/players?action=delete&id=${p.id}'">删除</a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty list}">
            <tr><td colspan="9" class="empty">暂无队员数据</td></tr>
        </c:if>
        </tbody>
    </table>
</div>

<%@ include file="../common/footer.jsp" %>
