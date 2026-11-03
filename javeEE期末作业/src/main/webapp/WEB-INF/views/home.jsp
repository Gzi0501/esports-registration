<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="common/header.jsp" %>

<div class="card">
    <h2>系统概览</h2>
    <div class="stat-grid">
        <div class="stat-card">
            <div class="num">${teamCount}</div>
            <div class="lbl">战队总数</div>
        </div>
        <div class="stat-card cyan">
            <div class="num">${playerCount}</div>
            <div class="lbl">队员总数</div>
        </div>
        <div class="stat-card green">
            <div class="num">${tournamentCount}</div>
            <div class="lbl">赛事总数</div>
        </div>
        <div class="stat-card orange">
            <div class="num">${registrationCount}</div>
            <div class="lbl">报名记录</div>
        </div>
    </div>
</div>

<div class="card">
    <h2>报名中的赛事</h2>
    <table>
        <thead>
        <tr>
            <th>赛事名称</th>
            <th>游戏项目</th>
            <th>开始时间</th>
            <th>报名进度</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="t" items="${openTournaments}">
            <tr>
                <td><a href="${ctx}/tournaments?action=detail&id=${t.id}"><c:out value="${t.title}"/></a></td>
                <td><span class="tag"><c:out value="${t.game}"/></span></td>
                <td>${t.startDate}</td>
                <td>${t.registeredTeams} / ${t.maxTeams} 支战队</td>
                <td><a class="btn btn-sm btn-default" href="${ctx}/tournaments?action=detail&id=${t.id}">查看</a></td>
            </tr>
        </c:forEach>
        <c:if test="${empty openTournaments}">
            <tr><td colspan="5" class="empty">暂无报名中的赛事</td></tr>
        </c:if>
        </tbody>
    </table>
</div>

<div class="card">
    <h2>最新报名记录</h2>
    <table>
        <thead>
        <tr>
            <th>战队</th>
            <th>赛事</th>
            <th>游戏项目</th>
            <th>报名时间</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="r" items="${recentRegistrations}">
            <tr>
                <td><c:out value="${r.teamName}"/></td>
                <td><c:out value="${r.tournamentTitle}"/></td>
                <td><span class="tag"><c:out value="${r.game}"/></span></td>
                <td>${r.createTime}</td>
            </tr>
        </c:forEach>
        <c:if test="${empty recentRegistrations}">
            <tr><td colspan="4" class="empty">暂无报名记录</td></tr>
        </c:if>
        </tbody>
    </table>
</div>

<%@ include file="common/footer.jsp" %>
