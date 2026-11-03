<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="../common/header.jsp" %>

<div class="card">
    <div class="section-title">
        <h3>赛事详情 · <c:out value="${tournament.title}"/></h3>
        <span>
            <a class="btn btn-sm btn-default" href="${ctx}/tournaments?action=edit&id=${tournament.id}">编辑赛事</a>
            <c:choose>
                <c:when test="${tournament.status == 0 and tournament.remainingTeams > 0}">
                    <a class="btn btn-sm btn-success" href="${ctx}/registrations?action=add&tournamentId=${tournament.id}">报名该赛事</a>
                </c:when>
                <c:when test="${tournament.status == 0}">
                    <span class="badge badge-ended">名额已满</span>
                </c:when>
                <c:otherwise>
                    <span class="badge badge-ended">不接受报名</span>
                </c:otherwise>
            </c:choose>
            <a class="btn btn-sm btn-default" href="${ctx}/tournaments">返回列表</a>
        </span>
    </div>

    <div class="detail-grid">
        <div class="detail-item">
            <span>游戏项目</span>
            <strong><c:out value="${tournament.game}"/></strong>
        </div>
        <div class="detail-item">
            <span>比赛时间</span>
            <strong>${tournament.startDate} 至 ${tournament.endDate}</strong>
        </div>
        <div class="detail-item">
            <span>比赛地点</span>
            <strong><c:out value="${tournament.location}"/></strong>
        </div>
        <div class="detail-item">
            <span>报名进度</span>
            <strong>${tournament.registeredTeams} / ${tournament.maxTeams} 支战队</strong>
        </div>
        <div class="detail-item">
            <span>赛事状态</span>
            <strong><span class="badge ${tournament.statusClass}">${tournament.statusText}</span></strong>
        </div>
        <div class="detail-item">
            <span>发布时间</span>
            <strong>${tournament.createTime}</strong>
        </div>
    </div>

    <c:if test="${not empty tournament.description}">
        <div class="rules">
            <strong>赛事说明：</strong><c:out value="${tournament.description}"/>
        </div>
    </c:if>

    <div class="section-title">
        <h3>已报名战队（${tournament.registeredTeams} 支）</h3>
    </div>
    <table>
        <thead>
        <tr>
            <th>战队名称</th>
            <th>游戏项目</th>
            <th>报名时间</th>
            <th>备注</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="r" items="${registrations}">
            <tr>
                <td><a href="${ctx}/teams?action=detail&id=${r.teamId}"><c:out value="${r.teamName}"/></a></td>
                <td><span class="tag"><c:out value="${r.game}"/></span></td>
                <td>${r.createTime}</td>
                <td><c:out value="${r.remark}"/></td>
                <td>
                    <a class="btn btn-sm btn-danger" href="javascript:void(0)"
                       onclick="if(confirm('确定取消该战队的报名吗？'))location.href='${ctx}/registrations?action=delete&id=${r.id}'">取消报名</a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty registrations}">
            <tr><td colspan="5" class="empty">还没有战队报名该赛事</td></tr>
        </c:if>
        </tbody>
    </table>
</div>

<%@ include file="../common/footer.jsp" %>
