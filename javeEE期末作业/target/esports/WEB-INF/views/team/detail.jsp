<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="../common/header.jsp" %>

<div class="card">
    <div class="section-title">
        <h3>战队详情 · <c:out value="${team.teamName}"/></h3>
        <span>
            <a class="btn btn-sm btn-default" href="${ctx}/teams?action=edit&id=${team.id}">编辑战队</a>
            <a class="btn btn-sm btn-success" href="${ctx}/players?action=add&teamId=${team.id}">+ 添加队员</a>
            <a class="btn btn-sm btn-default" href="${ctx}/teams">返回列表</a>
        </span>
    </div>

    <div class="detail-grid">
        <div class="detail-item">
            <span>游戏项目</span>
            <strong><c:out value="${team.game}"/></strong>
        </div>
        <div class="detail-item">
            <span>队长</span>
            <strong><c:out value="${team.captain}"/></strong>
        </div>
        <div class="detail-item">
            <span>队员人数</span>
            <strong>${team.playerCount} 人</strong>
        </div>
        <div class="detail-item">
            <span>创建时间</span>
            <strong>${team.createTime}</strong>
        </div>
        <div class="detail-item">
            <span>战队口号</span>
            <strong><c:out value="${team.slogan}"/></strong>
        </div>
    </div>

    <div class="section-title">
        <h3>战队队员</h3>
        <span><a class="btn btn-sm btn-success" href="${ctx}/players?action=add&teamId=${team.id}">+ 添加队员</a></span>
    </div>
    <table>
        <thead>
        <tr>
            <th>选手姓名</th>
            <th>游戏ID</th>
            <th>场上位置</th>
            <th>年龄</th>
            <th>联系电话</th>
            <th>加入时间</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="p" items="${players}">
            <tr>
                <td><c:out value="${p.playerName}"/></td>
                <td><c:out value="${p.gameId}"/></td>
                <td>${p.position}</td>
                <td>${p.age}</td>
                <td><c:out value="${p.phone}"/></td>
                <td>${p.joinTime}</td>
            </tr>
        </c:forEach>
        <c:if test="${empty players}">
            <tr><td colspan="6" class="empty">该战队还没有队员，请先添加队员</td></tr>
        </c:if>
        </tbody>
    </table>

    <div class="section-title">
        <h3>战队报名记录</h3>
        <span><a class="btn btn-sm btn-success" href="${ctx}/registrations?action=add&teamId=${team.id}">+ 报名赛事</a></span>
    </div>
    <table>
        <thead>
        <tr>
            <th>赛事名称</th>
            <th>游戏项目</th>
            <th>比赛开始时间</th>
            <th>报名时间</th>
            <th>备注</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="r" items="${registrations}">
            <tr>
                <td><a href="${ctx}/tournaments?action=detail&id=${r.tournamentId}"><c:out value="${r.tournamentTitle}"/></a></td>
                <td><span class="tag"><c:out value="${r.game}"/></span></td>
                <td>${r.startDate}</td>
                <td>${r.createTime}</td>
                <td><c:out value="${r.remark}"/></td>
                <td>
                    <a class="btn btn-sm btn-danger" href="javascript:void(0)"
                       onclick="if(confirm('确定取消该赛事的报名吗？'))location.href='${ctx}/registrations?action=delete&id=${r.id}'">取消报名</a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty registrations}">
            <tr><td colspan="6" class="empty">该战队暂无报名记录</td></tr>
        </c:if>
        </tbody>
    </table>
</div>

<%@ include file="../common/footer.jsp" %>
