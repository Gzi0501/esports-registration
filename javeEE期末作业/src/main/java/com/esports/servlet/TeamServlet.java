package com.esports.servlet;

import com.esports.dao.PlayerDao;
import com.esports.dao.RegistrationDao;
import com.esports.dao.TeamDao;
import com.esports.entity.Team;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * 战队管理：新增、修改、查询、查看详情、解散
 */
@WebServlet("/teams")
public class TeamServlet extends BaseServlet {

    private final TeamDao teamDao = new TeamDao();
    private final PlayerDao playerDao = new PlayerDao();
    private final RegistrationDao registrationDao = new RegistrationDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        process(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null || action.isEmpty()) {
            action = "list";
        }
        try {
            switch (action) {
                case "add":
                    showForm(request, response, 0);
                    break;
                case "edit":
                    showForm(request, response, getInt(request.getParameter("id"), 0));
                    break;
                case "detail":
                    showDetail(request, response);
                    break;
                case "save":
                    save(request, response);
                    break;
                case "delete":
                    delete(request, response);
                    break;
                default:
                    list(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("战队模块数据库操作失败", e);
        }
    }

    /**
     * 战队列表与关键字查询
     */
    private void list(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String keyword = getString(request, "keyword");
        List<Team> teams;
        if (keyword.isEmpty()) {
            teams = teamDao.findAll();
        } else {
            teams = teamDao.findByKeyword(keyword);
        }
        request.setAttribute("list", teams);
        request.setAttribute("keyword", keyword);
        forward(request, response, "team/list.jsp");
    }

    /**
     * 新增 / 编辑表单
     */
    private void showForm(HttpServletRequest request, HttpServletResponse response, int id)
            throws SQLException, ServletException, IOException {
        Team team = new Team();
        if (id > 0) {
            Team found = teamDao.findById(id);
            if (found == null) {
                flashError(request, "要编辑的战队不存在");
                redirect(request, response, "/teams");
                return;
            }
            team = found;
        }
        request.setAttribute("team", team);
        forward(request, response, "team/form.jsp");
    }

    /**
     * 保存新增或修改
     */
    private void save(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = getInt(request.getParameter("id"), 0);

        Team team = new Team();
        team.setId(id);
        team.setTeamName(getString(request, "teamName"));
        team.setGame(getString(request, "game"));
        team.setCaptain(getString(request, "captain"));
        team.setSlogan(getString(request, "slogan"));

        String error = null;
        if (team.getTeamName().isEmpty()) {
            error = "战队名称不能为空";
        } else if (team.getGame().isEmpty()) {
            error = "请填写游戏项目";
        } else if (team.getCaptain().isEmpty()) {
            error = "请填写队长名称";
        } else if (team.getTeamName().length() > 50) {
            error = "战队名称不能超过 50 个字符";
        } else if (teamDao.existsName(team.getTeamName(), id)) {
            error = "战队名称已被使用，请更换";
        }

        if (error != null) {
            request.setAttribute("error", error);
            request.setAttribute("team", team);
            forward(request, response, "team/form.jsp");
            return;
        }

        if (id > 0) {
            teamDao.update(team);
            flashSuccess(request, "战队信息已更新");
        } else {
            teamDao.insert(team);
            flashSuccess(request, "战队创建成功");
        }
        redirect(request, response, "/teams");
    }

    /**
     * 战队详情：展示队员和报名记录
     */
    private void showDetail(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = getInt(request.getParameter("id"), 0);
        Team team = teamDao.findById(id);
        if (team == null) {
            flashError(request, "战队不存在");
            redirect(request, response, "/teams");
            return;
        }
        request.setAttribute("team", team);
        request.setAttribute("players", playerDao.findByCondition("", id));
        request.setAttribute("registrations", registrationDao.findByTeamId(id));
        forward(request, response, "team/detail.jsp");
    }

    /**
     * 解散（删除）战队
     */
    private void delete(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = getInt(request.getParameter("id"), 0);
        Team team = teamDao.findById(id);
        if (team == null) {
            flashError(request, "战队不存在");
        } else {
            teamDao.delete(id);
            flashSuccess(request, "战队「" + team.getTeamName() + "」已解散，队员与报名记录已同步清除");
        }
        redirect(request, response, "/teams");
    }
}
