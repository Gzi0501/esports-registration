<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="../common/header.jsp" %>

<div class="card">
    <h2>赛事报名</h2>

    <c:if test="${not empty error}">
        <div class="msg msg-error"><c:out value="${error}"/></div>
    </c:if>

    <c:if test="${empty teams}">
        <div class="msg msg-warning">
            系统中还没有战队，请先 <a href="${ctx}/teams?action=add">创建战队</a> 并添加队员后再报名。
        </div>
    </c:if>
    <c:if test="${empty openTournaments}">
        <div class="msg msg-warning">
            当前没有处于「报名中」状态的赛事，请先到 <a href="${ctx}/tournaments?action=add">赛事管理</a> 发布赛事。
        </div>
    </c:if>

    <div class="rules">
        <strong>报名须知：</strong>
        <ul>
            <li>只有至少拥有一名队员的战队才能报名；</li>
            <li>同一支战队不能重复报名同一项赛事；</li>
            <li>仅「报名中」状态的赛事可以报名，名额满后不再接受报名；</li>
            <li>报名后如无法参赛，可在报名记录列表中取消报名。</li>
        </ul>
    </div>

    <form method="post" action="${ctx}/registrations?action=save">

        <div class="form-row">
            <label>报名战队 <span class="required">*</span></label>
            <select name="teamId" required>
                <option value="">请选择报名战队</option>
                <c:forEach var="t" items="${teams}">
                    <option value="${t.id}" <c:if test="${registration.teamId == t.id}">selected</c:if>><c:out value="${t.teamName}"/>（<c:out value="${t.game}"/> · ${t.playerCount} 名队员）</option>
                </c:forEach>
            </select>
        </div>

        <div class="form-row">
            <label>报名赛事 <span class="required">*</span></label>
            <select name="tournamentId" required>
                <option value="">请选择报名赛事</option>
                <c:forEach var="tr" items="${openTournaments}">
                    <option value="${tr.id}" <c:if test="${registration.tournamentId == tr.id}">selected</c:if>><c:out value="${tr.title}"/>（<c:out value="${tr.game}"/> · 已报名 ${tr.registeredTeams}/${tr.maxTeams}）</option>
                </c:forEach>
            </select>
        </div>

        <div class="form-row">
            <label>报名备注</label>
            <textarea name="remark" rows="3" maxlength="200"
                      placeholder="例如：希望安排周末场次（选填）"><c:out value="${registration.remark}"/></textarea>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn">提交报名</button>
            <a class="btn btn-default" href="${ctx}/registrations">返回报名记录</a>
        </div>
    </form>
</div>

<%@ include file="../common/footer.jsp" %>
