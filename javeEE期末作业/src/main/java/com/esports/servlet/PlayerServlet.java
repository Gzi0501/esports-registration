package com.esports.servlet;

import com.esports.dao.PlayerDao;
import com.esports.dao.TeamDao;
import com.esports.entity.Player;
import com.esports.entity.Team;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * 队员管理：添加、修改、按战队查询、删除
 */
@WebServlet("/players")
public class PlayerServlet extends BaseServlet {

    private final PlayerDao playerDao = new PlayerDao();
    private final TeamDao teamDao = new TeamDao();

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
            throw new ServletException("队员模块数据库操作失败", e);
        }
    }

    /**
     * 队员列表：支持关键字和所属战队筛选
     */
    private void list(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String keyword = getString(request, "keyword");
        int teamId = getInt(request.getParameter("teamId"), 0);

        request.setAttribute("list", playerDao.findByCondition(keyword, teamId));
        request.setAttribute("keyword", keyword);
        request.setAttribute("teamId", teamId);
        request.setAttribute("teams", teamDao.findAll());
        forward(request, response, "player/list.jsp");
    }

    /**
     * 新增 / 编辑表单
     */
    private void showForm(HttpServletRequest request, HttpServletResponse response, int id)
            throws SQLException, ServletException, IOException {
        Player player = new Player();
        if (id > 0) {
            Player found = playerDao.findById(id);
            if (found == null) {
                flashError(request, "要编辑的队员不存在");
                redirect(request, response, "/players");
                return;
            }
            player = found;
        } else {
            // 从战队详情页“添加队员”进入时，可以带上默认战队
            player.setTeamId(getInt(request.getParameter("teamId"), 0));
        }
        request.setAttribute("player", player);
        request.setAttribute("teams", teamDao.findAll());
        forward(request, response, "player/form.jsp");
    }

    /**
     * 保存新增或修改
     */
    private void save(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = getInt(request.getParameter("id"), 0);

        Player player = new Player();
        player.setId(id);
        player.setPlayerName(getString(request, "playerName"));
        player.setGameId(getString(request, "gameId"));
        player.setPosition(getString(request, "position"));
        player.setPhone(getString(request, "phone"));
        player.setTeamId(getInt(request.getParameter("teamId"), 0));

        String error = null;
        String ageText = getString(request, "age");
        if (!ageText.isEmpty()) {
            int age = getInt(ageText, -1);
            if (age < 10 || age > 60) {
                error = "年龄需在 10 - 60 之间";
            } else {
                player.setAge(age);
            }
        }

        if (error == null && player.getPlayerName().isEmpty()) {
            error = "选手姓名不能为空";
        }
        if (error == null && player.getGameId().isEmpty()) {
            error = "游戏ID不能为空";
        }
        if (error == null && player.getPosition().isEmpty()) {
            error = "请选择场上位置";
        }
        if (error == null && player.getPhone().length() > 20) {
            error = "联系电话不能超过 20 个字符";
        }

        Team team = player.getTeamId() > 0 ? teamDao.findById(player.getTeamId()) : null;
        if (error == null && team == null) {
            error = "请选择所属战队";
        }

        if (error != null) {
            request.setAttribute("error", error);
            request.setAttribute("player", player);
            request.setAttribute("teams", teamDao.findAll());
            forward(request, response, "player/form.jsp");
            return;
        }

        if (id > 0) {
            playerDao.update(player);
            flashSuccess(request, "队员资料已更新");
        } else {
            playerDao.insert(player);
            flashSuccess(request, "队员添加成功");
        }
        redirect(request, response, "/players");
    }

    /**
     * 删除队员
     */
    private void delete(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = getInt(request.getParameter("id"), 0);
        Player player = playerDao.findById(id);
        if (player == null) {
            flashError(request, "队员不存在");
        } else {
            playerDao.delete(id);
            flashSuccess(request, "队员「" + player.getPlayerName() + "」已删除");
        }
        redirect(request, response, "/players");
    }
}
