<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="../common/header.jsp" %>

<div class="card">
    <h2>战队管理</h2>

    <form class="search-bar" method="get" action="${ctx}/teams">
        <input type="text" name="keyword" value="<c:out value='${keyword}'/>"
               placeholder="战队名称 / 游戏项目 / 队长">
        <button type="submit" class="btn btn-sm">查询</button>
        <a class="btn btn-sm btn-default" href="${ctx}/teams">重置</a>
        <div class="spacer"></div>
        <a class="btn btn-sm btn-success" href="${ctx}/teams?action=add">+ 新增战队</a>
    </form>

    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>战队名称</th>
            <th>游戏项目</th>
            <th>队长</th>
            <th>队员人数</th>
            <th>创建时间</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="t" items="${list}">
            <tr>
                <td>${t.id}</td>
                <td><a href="${ctx}/teams?action=detail&id=${t.id}"><c:out value="${t.teamName}"/></a></td>
                <td><span class="tag"><c:out value="${t.game}"/></span></td>
                <td><c:out value="${t.captain}"/></td>
                <td>${t.playerCount} 人</td>
                <td>${t.createTime}</td>
                <td>
                    <a class="btn btn-sm btn-default" href="${ctx}/teams?action=detail&id=${t.id}">详情</a>
                    <a class="btn btn-sm btn-default" href="${ctx}/teams?action=edit&id=${t.id}">编辑</a>
                    <a class="btn btn-sm btn-danger" href="javascript:void(0)"
                       onclick="if(confirm('解散后该战队的队员与报名记录将一并删除，确定继续吗？'))location.href='${ctx}/teams?action=delete&id=${t.id}'">解散</a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty list}">
            <tr><td colspan="7" class="empty">暂无战队数据</td></tr>
        </c:if>
        </tbody>
    </table>
</div>

<%@ include file="../common/footer.jsp" %>
