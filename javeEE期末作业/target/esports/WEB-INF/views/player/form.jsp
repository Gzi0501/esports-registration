<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="../common/header.jsp" %>

<div class="card">
    <h2>
        <c:choose>
            <c:when test="${player.id > 0}">编辑队员资料</c:when>
            <c:otherwise>添加队员</c:otherwise>
        </c:choose>
    </h2>

    <c:if test="${not empty error}">
        <div class="msg msg-error"><c:out value="${error}"/></div>
    </c:if>

    <c:if test="${empty teams}">
        <div class="msg msg-warning">
            系统中还没有战队，请先 <a href="${ctx}/teams?action=add">创建战队</a> 后再添加队员。
        </div>
    </c:if>

    <form method="post" action="${ctx}/players?action=save">
        <input type="hidden" name="id" value="${player.id}">

        <div class="form-row">
            <label>选手姓名 <span class="required">*</span></label>
            <input type="text" name="playerName" maxlength="30" required
                   value="<c:out value='${player.playerName}'/>" placeholder="真实姓名或昵称">
        </div>

        <div class="form-row">
            <label>游戏ID <span class="required">*</span></label>
            <input type="text" name="gameId" maxlength="50" required
                   value="<c:out value='${player.gameId}'/>" placeholder="游戏内使用的ID">
        </div>

        <div class="form-row">
            <label>场上位置 <span class="required">*</span></label>
            <select name="position" required>
                <option value="">请选择位置</option>
                <option value="上单" <c:if test="${player.position == '上单'}">selected</c:if>>上单</option>
                <option value="打野" <c:if test="${player.position == '打野'}">selected</c:if>>打野</option>
                <option value="中单" <c:if test="${player.position == '中单'}">selected</c:if>>中单</option>
                <option value="射手" <c:if test="${player.position == '射手'}">selected</c:if>>射手</option>
                <option value="辅助" <c:if test="${player.position == '辅助'}">selected</c:if>>辅助</option>
                <option value="自由人" <c:if test="${player.position == '自由人'}">selected</c:if>>自由人</option>
                <option value="指挥" <c:if test="${player.position == '指挥'}">selected</c:if>>指挥</option>
                <option value="突破手" <c:if test="${player.position == '突破手'}">selected</c:if>>突破手</option>
                <option value="狙击手" <c:if test="${player.position == '狙击手'}">selected</c:if>>狙击手</option>
            </select>
        </div>

        <div class="form-row">
            <label>年龄</label>
            <input type="number" name="age" min="10" max="60"
                   value="${player.age}" placeholder="10 - 60（选填）">
        </div>

        <div class="form-row">
            <label>联系电话</label>
            <input type="text" name="phone" maxlength="20"
                   value="<c:out value='${player.phone}'/>" placeholder="选填">
        </div>

        <div class="form-row">
            <label>所属战队 <span class="required">*</span></label>
            <select name="teamId" required>
                <option value="">请选择所属战队</option>
                <c:forEach var="t" items="${teams}">
                    <option value="${t.id}" <c:if test="${player.teamId == t.id}">selected</c:if>><c:out value="${t.teamName}"/>（<c:out value="${t.game}"/>）</option>
                </c:forEach>
            </select>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn">保存</button>
            <a class="btn btn-default" href="${ctx}/players">返回列表</a>
        </div>
    </form>
</div>

<%@ include file="../common/footer.jsp" %>
