<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="../common/header.jsp" %>

<div class="card">
    <h2>
        <c:choose>
            <c:when test="${team.id > 0}">编辑战队</c:when>
            <c:otherwise>新增战队</c:otherwise>
        </c:choose>
    </h2>

    <c:if test="${not empty error}">
        <div class="msg msg-error"><c:out value="${error}"/></div>
    </c:if>

    <form method="post" action="${ctx}/teams?action=save">
        <input type="hidden" name="id" value="${team.id}">

        <div class="form-row">
            <label>战队名称 <span class="required">*</span></label>
            <input type="text" name="teamName" maxlength="50" required
                   value="<c:out value='${team.teamName}'/>" placeholder="例如：NOVA 电竞">
        </div>

        <div class="form-row">
            <label>游戏项目 <span class="required">*</span></label>
            <input type="text" name="game" list="gameOptions" maxlength="30" required
                   value="<c:out value='${team.game}'/>" placeholder="选择或输入游戏项目">
            <datalist id="gameOptions">
                <option value="英雄联盟"></option>
                <option value="王者荣耀"></option>
                <option value="无畏契约"></option>
                <option value="CS2"></option>
                <option value="DOTA2"></option>
            </datalist>
            <div class="hint">可从下拉选项中选择，也可以直接输入其他游戏名称。</div>
        </div>

        <div class="form-row">
            <label>队长 <span class="required">*</span></label>
            <input type="text" name="captain" maxlength="30" required
                   value="<c:out value='${team.captain}'/>" placeholder="队长姓名">
        </div>

        <div class="form-row">
            <label>战队口号</label>
            <textarea name="slogan" rows="3" maxlength="200"
                      placeholder="一句话介绍你的战队（选填）"><c:out value="${team.slogan}"/></textarea>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn">保存</button>
            <a class="btn btn-default" href="${ctx}/teams">返回列表</a>
        </div>
    </form>
</div>

<%@ include file="../common/footer.jsp" %>
