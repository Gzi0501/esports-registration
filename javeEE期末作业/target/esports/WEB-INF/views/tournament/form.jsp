<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="../common/header.jsp" %>

<div class="card">
    <h2>
        <c:choose>
            <c:when test="${tournament.id > 0}">编辑赛事</c:when>
            <c:otherwise>发布赛事</c:otherwise>
        </c:choose>
    </h2>

    <c:if test="${not empty error}">
        <div class="msg msg-error"><c:out value="${error}"/></div>
    </c:if>

    <form method="post" action="${ctx}/tournaments?action=save">
        <input type="hidden" name="id" value="${tournament.id}">

        <div class="form-row">
            <label>赛事名称 <span class="required">*</span></label>
            <input type="text" name="title" maxlength="80" required
                   value="<c:out value='${tournament.title}'/>" placeholder="例如：高校电竞联赛 · 英雄联盟">
        </div>

        <div class="form-row">
            <label>游戏项目 <span class="required">*</span></label>
            <input type="text" name="game" list="gameOptions" maxlength="30" required
                   value="<c:out value='${tournament.game}'/>" placeholder="选择或输入游戏项目">
            <datalist id="gameOptions">
                <option value="英雄联盟"></option>
                <option value="王者荣耀"></option>
                <option value="无畏契约"></option>
                <option value="CS2"></option>
                <option value="DOTA2"></option>
            </datalist>
        </div>

        <div class="form-row">
            <label>比赛开始日期 <span class="required">*</span></label>
            <input type="date" name="startDate" required value="${tournament.startDate}">
        </div>

        <div class="form-row">
            <label>比赛结束日期 <span class="required">*</span></label>
            <input type="date" name="endDate" required value="${tournament.endDate}">
            <div class="hint">结束日期不能早于开始日期。</div>
        </div>

        <div class="form-row">
            <label>比赛地点 <span class="required">*</span></label>
            <input type="text" name="location" maxlength="80" required
                   value="<c:out value='${tournament.location}'/>" placeholder="例如：线上赛 / 大学城电竞馆">
        </div>

        <div class="form-row">
            <label>可容纳战队数 <span class="required">*</span></label>
            <input type="number" name="maxTeams" min="2" max="99" required value="${tournament.maxTeams}">
            <div class="hint">报名战队数达到该上限后，系统将不再接受新的报名。</div>
        </div>

        <div class="form-row">
            <label>赛事状态 <span class="required">*</span></label>
            <select name="status" required>
                <option value="0" <c:if test="${tournament.status == 0}">selected</c:if>>报名中</option>
                <option value="1" <c:if test="${tournament.status == 1}">selected</c:if>>进行中</option>
                <option value="2" <c:if test="${tournament.status == 2}">selected</c:if>>已结束</option>
            </select>
        </div>

        <div class="form-row">
            <label>赛事说明</label>
            <textarea name="description" rows="4" maxlength="500"
                      placeholder="赛制、奖项、注意事项等（选填）"><c:out value="${tournament.description}"/></textarea>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn">保存</button>
            <a class="btn btn-default" href="${ctx}/tournaments">返回列表</a>
        </div>
    </form>
</div>

<%@ include file="../common/footer.jsp" %>
