package com.esports.servlet;

import com.esports.dao.PlayerDao;
import com.esports.dao.RegistrationDao;
import com.esports.dao.TeamDao;
import com.esports.dao.TournamentDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

/**
 * 首页：展示各模块统计数据、报名中的赛事和最新报名记录
 */
@WebServlet("/home")
public class HomeServlet extends BaseServlet {

    private final TeamDao teamDao = new TeamDao();
    private final PlayerDao playerDao = new PlayerDao();
    private final TournamentDao tournamentDao = new TournamentDao();
    private final RegistrationDao registrationDao = new RegistrationDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("teamCount", teamDao.countAll());
            request.setAttribute("playerCount", playerDao.countAll());
            request.setAttribute("tournamentCount", tournamentDao.countAll());
            request.setAttribute("openTournamentCount", tournamentDao.countOpen());
            request.setAttribute("registrationCount", registrationDao.countAll());
            request.setAttribute("openTournaments", tournamentDao.findOpen());
            request.setAttribute("recentRegistrations", registrationDao.findRecent(5));
            forward(request, response, "home.jsp");
        } catch (SQLException e) {
            throw new ServletException("加载首页数据失败", e);
        }
    }
}
