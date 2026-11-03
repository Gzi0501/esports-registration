<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="../common/header.jsp" %>

<div class="card">
    <h2>赛事管理</h2>

    <form class="search-bar" method="get" action="${ctx}/tournaments">
        <input type="text" name="keyword" value="<c:out value='${keyword}'/>"
               placeholder="赛事名称 / 游戏项目 / 比赛地点">
        <select name="status">
            <option value="-1" <c:if test="${status == -1}">selected</c:if>>全部状态</option>
            <option value="0" <c:if test="${status == 0}">selected</c:if>>报名中</option>
            <option value="1" <c:if test="${status == 1}">selected</c:if>>进行中</option>
            <option value="2" <c:if test="${status == 2}">selected</c:if>>已结束</option>
        </select>
        <button type="submit" class="btn btn-sm">查询</button>
        <a class="btn btn-sm btn-default" href="${ctx}/tournaments">重置</a>
        <div class="spacer"></div>
        <a class="btn btn-sm btn-success" href="${ctx}/tournaments?action=add">+ 发布赛事</a>
    </form>

    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>赛事名称</th>
            <th>游戏项目</th>
            <th>比赛时间</th>
            <th>比赛地点</th>
            <th>报名进度</th>
            <th>状态</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="t" items="${list}">
            <tr>
                <td>${t.id}</td>
                <td><a href="${ctx}/tournaments?action=detail&id=${t.id}"><c:out value="${t.title}"/></a></td>
                <td><span class="tag"><c:out value="${t.game}"/></span></td>
                <td>${t.startDate} 至 ${t.endDate}</td>
                <td><c:out value="${t.location}"/></td>
                <td>${t.registeredTeams} / ${t.maxTeams} 支</td>
                <td><span class="badge ${t.statusClass}">${t.statusText}</span></td>
                <td>
                    <a class="btn btn-sm btn-default" href="${ctx}/tournaments?action=detail&id=${t.id}">详情</a>
                    <a class="btn btn-sm btn-default" href="${ctx}/tournaments?action=edit&id=${t.id}">编辑</a>
                    <a class="btn btn-sm btn-danger" href="javascript:void(0)"
                       onclick="if(confirm('下架后该赛事的报名记录将一并删除，确定继续吗？'))location.href='${ctx}/tournaments?action=delete&id=${t.id}'">下架</a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty list}">
            <tr><td colspan="8" class="empty">暂无赛事数据</td></tr>
        </c:if>
        </tbody>
    </table>
</div>

<%@ include file="../common/footer.jsp" %>
