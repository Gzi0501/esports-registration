package com.esports.servlet;

import com.esports.dao.RegistrationDao;
import com.esports.dao.TournamentDao;
import com.esports.entity.Tournament;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * 赛事管理：发布、编辑、查询、查看详情、下架
 */
@WebServlet("/tournaments")
public class TournamentServlet extends BaseServlet {

    private final TournamentDao tournamentDao = new TournamentDao();
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
            throw new ServletException("赛事模块数据库操作失败", e);
        }
    }

    /**
     * 赛事列表：支持关键字和状态筛选
     */
    private void list(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String keyword = getString(request, "keyword");
        int status = getInt(request.getParameter("status"), -1);

        request.setAttribute("list", tournamentDao.findByCondition(keyword, status));
        request.setAttribute("keyword", keyword);
        request.setAttribute("status", status);
        forward(request, response, "tournament/list.jsp");
    }

    /**
     * 发布 / 编辑表单
     */
    private void showForm(HttpServletRequest request, HttpServletResponse response, int id)
            throws SQLException, ServletException, IOException {
        Tournament tournament = new Tournament();
        if (id > 0) {
            Tournament found = tournamentDao.findById(id);
            if (found == null) {
                flashError(request, "要编辑的赛事不存在");
                redirect(request, response, "/tournaments");
                return;
            }
            tournament = found;
        } else {
            tournament.setStatus(0);
            tournament.setMaxTeams(8);
        }
        request.setAttribute("tournament", tournament);
        forward(request, response, "tournament/form.jsp");
    }

    /**
     * 保存赛事
     */
    private void save(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = getInt(request.getParameter("id"), 0);

        Tournament tournament = new Tournament();
        tournament.setId(id);
        tournament.setTitle(getString(request, "title"));
        tournament.setGame(getString(request, "game"));
        tournament.setStartDate(getString(request, "startDate"));
        tournament.setEndDate(getString(request, "endDate"));
        tournament.setLocation(getString(request, "location"));
        tournament.setMaxTeams(getInt(request.getParameter("maxTeams"), 8));
        tournament.setStatus(getInt(request.getParameter("status"), 0));
        tournament.setDescription(getString(request, "description"));

        String error = validate(tournament, id);

        if (error != null) {
            request.setAttribute("error", error);
            request.setAttribute("tournament", tournament);
            forward(request, response, "tournament/form.jsp");
            return;
        }

        if (id > 0) {
            tournamentDao.update(tournament);
            flashSuccess(request, "赛事信息已更新");
        } else {
            tournamentDao.insert(tournament);
            flashSuccess(request, "赛事发布成功");
        }
        redirect(request, response, "/tournaments");
    }

    /**
     * 校验赛事表单数据
     */
    private String validate(Tournament tournament, int id) throws SQLException {
        if (tournament.getTitle().isEmpty()) {
            return "赛事名称不能为空";
        }
        if (tournament.getGame().isEmpty()) {
            return "请填写游戏项目";
        }
        if (tournament.getStartDate().isEmpty() || tournament.getEndDate().isEmpty()) {
            return "请选择比赛开始和结束日期";
        }
        try {
            LocalDate start = LocalDate.parse(tournament.getStartDate());
            LocalDate end = LocalDate.parse(tournament.getEndDate());
            if (end.isBefore(start)) {
                return "结束日期不能早于开始日期";
            }
        } catch (DateTimeParseException e) {
            return "比赛日期格式不正确";
        }
        if (tournament.getLocation().isEmpty()) {
            return "请填写比赛地点";
        }
        if (tournament.getMaxTeams() < 2) {
            return "可容纳战队数至少为 2";
        }
        if (tournament.getStatus() < 0 || tournament.getStatus() > 2) {
            return "赛事状态不正确";
        }
        if (id > 0) {
            int registered = registrationDao.countByTournament(id);
            if (tournament.getMaxTeams() < registered) {
                return "已有 " + registered + " 支战队报名，可容纳战队数不能小于该值";
            }
        }
        return null;
    }

    /**
     * 赛事详情：展示已报名战队
     */
    private void showDetail(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = getInt(request.getParameter("id"), 0);
        Tournament tournament = tournamentDao.findById(id);
        if (tournament == null) {
            flashError(request, "赛事不存在");
            redirect(request, response, "/tournaments");
            return;
        }
        request.setAttribute("tournament", tournament);
        request.setAttribute("registrations", registrationDao.findByCondition(id));
        forward(request, response, "tournament/detail.jsp");
    }

    /**
     * 下架（删除）赛事
     */
    private void delete(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = getInt(request.getParameter("id"), 0);
        Tournament tournament = tournamentDao.findById(id);
        if (tournament == null) {
            flashError(request, "赛事不存在");
        } else {
            tournamentDao.delete(id);
            flashSuccess(request, "赛事「" + tournament.getTitle() + "」已下架，相关报名记录已一并删除");
        }
        redirect(request, response, "/tournaments");
    }
}
