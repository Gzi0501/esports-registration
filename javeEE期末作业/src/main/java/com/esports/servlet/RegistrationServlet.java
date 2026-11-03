package com.esports.servlet;

import com.esports.dao.RegistrationDao;
import com.esports.dao.TeamDao;
import com.esports.dao.TournamentDao;
import com.esports.entity.Registration;
import com.esports.entity.Team;
import com.esports.entity.Tournament;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

/**
 * 赛事报名：提交报名、查看报名记录、取消报名
 */
@WebServlet("/registrations")
public class RegistrationServlet extends BaseServlet {

    private final RegistrationDao registrationDao = new RegistrationDao();
    private final TeamDao teamDao = new TeamDao();
    private final TournamentDao tournamentDao = new TournamentDao();

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
                    showForm(request, response);
                    break;
                case "save":
                    save(request, response);
                    break;
                case "delete":
                    cancel(request, response);
                    break;
                default:
                    list(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("报名模块数据库操作失败", e);
        }
    }

    /**
     * 报名记录列表，可按赛事筛选
     */
    private void list(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int tournamentId = getInt(request.getParameter("tournamentId"), 0);

        request.setAttribute("list", registrationDao.findByCondition(tournamentId));
        request.setAttribute("tournaments", tournamentDao.findByCondition("", -1));
        request.setAttribute("tournamentId", tournamentId);
        forward(request, response, "registration/list.jsp");
    }

    /**
     * 报名表单
     */
    private void showForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        Registration registration = new Registration();
        registration.setTeamId(getInt(request.getParameter("teamId"), 0));
        registration.setTournamentId(getInt(request.getParameter("tournamentId"), 0));

        request.setAttribute("registration", registration);
        request.setAttribute("teams", teamDao.findAll());
        request.setAttribute("openTournaments", tournamentDao.findOpen());
        forward(request, response, "registration/form.jsp");
    }

    /**
     * 提交报名
     */
    private void save(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int teamId = getInt(request.getParameter("teamId"), 0);
        int tournamentId = getInt(request.getParameter("tournamentId"), 0);
        String remark = getString(request, "remark");

        Registration registration = new Registration();
        registration.setTeamId(teamId);
        registration.setTournamentId(tournamentId);
        registration.setRemark(remark);

        String error = null;
        Team team = teamId > 0 ? teamDao.findById(teamId) : null;
        Tournament tournament = tournamentId > 0 ? tournamentDao.findById(tournamentId) : null;

        if (teamId <= 0) {
            error = "请选择报名战队";
        } else if (team == null) {
            error = "所选战队不存在";
        } else if (team.getPlayerCount() < 1) {
            error = "该战队还没有队员，无法报名";
        } else if (tournamentId <= 0) {
            error = "请选择报名赛事";
        } else if (tournament == null) {
            error = "所选赛事不存在";
        } else if (tournament.getStatus() != 0) {
            error = "该赛事当前不是报名中状态，无法报名";
        } else if (registrationDao.exists(teamId, tournamentId)) {
            error = "该战队已报名此赛事，请勿重复报名";
        } else if (registrationDao.countByTournament(tournamentId) >= tournament.getMaxTeams()) {
            error = "该赛事报名名额已满";
        } else if (remark.length() > 200) {
            error = "报名备注不能超过 200 个字符";
        }

        if (error != null) {
            request.setAttribute("error", error);
            request.setAttribute("registration", registration);
            request.setAttribute("teams", teamDao.findAll());
            request.setAttribute("openTournaments", tournamentDao.findOpen());
            forward(request, response, "registration/form.jsp");
            return;
        }

        registrationDao.insert(registration);
        flashSuccess(request, "报名成功：战队「" + team.getTeamName() + "」已报名「" + tournament.getTitle() + "」");
        redirect(request, response, "/registrations");
    }

    /**
     * 取消报名
     */
    private void cancel(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = getInt(request.getParameter("id"), 0);
        Registration registration = registrationDao.findById(id);
        if (registration == null) {
            flashError(request, "报名记录不存在");
        } else {
            registrationDao.delete(id);
            flashSuccess(request, "已取消战队「" + registration.getTeamName()
                    + "」对赛事「" + registration.getTournamentTitle() + "」的报名");
        }
        redirect(request, response, "/registrations");
    }
}
