<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="../common/header.jsp" %>

<div class="card">
    <h2>赛事报名记录</h2>

    <form class="search-bar" method="get" action="${ctx}/registrations">
        <select name="tournamentId">
            <option value="0">全部赛事</option>
            <c:forEach var="t" items="${tournaments}">
                <option value="${t.id}" <c:if test="${tournamentId == t.id}">selected</c:if>><c:out value="${t.title}"/></option>
            </c:forEach>
        </select>
        <button type="submit" class="btn btn-sm">查询</button>
        <a class="btn btn-sm btn-default" href="${ctx}/registrations">重置</a>
        <div class="spacer"></div>
        <a class="btn btn-sm btn-success" href="${ctx}/registrations?action=add">+ 新增报名</a>
    </form>

    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>报名战队</th>
            <th>报名赛事</th>
            <th>游戏项目</th>
            <th>比赛开始时间</th>
            <th>报名时间</th>
            <th>备注</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="r" items="${list}">
            <tr>
                <td>${r.id}</td>
                <td><a href="${ctx}/teams?action=detail&id=${r.teamId}"><c:out value="${r.teamName}"/></a></td>
                <td><a href="${ctx}/tournaments?action=detail&id=${r.tournamentId}"><c:out value="${r.tournamentTitle}"/></a></td>
                <td><span class="tag"><c:out value="${r.game}"/></span></td>
                <td>${r.startDate}</td>
                <td>${r.createTime}</td>
                <td><c:out value="${r.remark}"/></td>
                <td>
                    <a class="btn btn-sm btn-danger" href="javascript:void(0)"
                       onclick="if(confirm('确定取消该报名吗？'))location.href='${ctx}/registrations?action=delete&id=${r.id}'">取消报名</a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty list}">
            <tr><td colspan="8" class="empty">暂无报名记录</td></tr>
        </c:if>
        </tbody>
    </table>
</div>

<%@ include file="../common/footer.jsp" %>
