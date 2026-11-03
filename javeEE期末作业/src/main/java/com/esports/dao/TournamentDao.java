package com.esports.dao;

import com.esports.entity.Tournament;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 赛事数据访问类
 */
public class TournamentDao {

    // 公共查询语句：同时统计每个赛事已报名的战队数
    private static final String BASE_SELECT =
            "SELECT t.id, t.title, t.game, "
                    + "DATE_FORMAT(t.start_date, '%Y-%m-%d') AS start_date, "
                    + "DATE_FORMAT(t.end_date, '%Y-%m-%d') AS end_date, "
                    + "t.location, t.max_teams, t.status, t.description, "
                    + "DATE_FORMAT(t.create_time, '%Y-%m-%d %H:%i') AS create_time, "
                    + "(SELECT COUNT(*) FROM registration r WHERE r.tournament_id = t.id) AS registered_teams "
                    + "FROM tournament t ";

    /**
     * 按条件查询赛事：status 为 -1 表示全部状态
     */
    public List<Tournament> findByCondition(String keyword, int status) throws SQLException {
        List<Tournament> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(BASE_SELECT);
        List<String> conditions = new ArrayList<>();

        if (keyword != null && !keyword.isEmpty()) {
            conditions.add("(t.title LIKE ? OR t.game LIKE ? OR t.location LIKE ?)");
        }
        if (status >= 0) {
            conditions.add("t.status = ?");
        }
        if (!conditions.isEmpty()) {
            sql.append("WHERE ").append(String.join(" AND ", conditions)).append(" ");
        }
        sql.append("ORDER BY t.start_date ASC, t.id DESC");

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int index = 1;
            if (keyword != null && !keyword.isEmpty()) {
                String like = "%" + keyword + "%";
                ps.setString(index++, like);
                ps.setString(index++, like);
                ps.setString(index++, like);
            }
            if (status >= 0) {
                ps.setInt(index, status);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    /**
     * 查询正在报名的赛事，供报名页下拉框使用
     */
    public List<Tournament> findOpen() throws SQLException {
        List<Tournament> list = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE t.status = 0 ORDER BY t.start_date ASC, t.id DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    /**
     * 根据 ID 查询赛事
     */
    public Tournament findById(int id) throws SQLException {
        String sql = BASE_SELECT + "WHERE t.id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    /**
     * 发布赛事
     */
    public void insert(Tournament tournament) throws SQLException {
        String sql = "INSERT INTO tournament (title, game, start_date, end_date, location, "
                + "max_teams, status, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setCommonFields(ps, tournament);
            ps.executeUpdate();
        }
    }

    /**
     * 修改赛事
     */
    public void update(Tournament tournament) throws SQLException {
        String sql = "UPDATE tournament SET title = ?, game = ?, start_date = ?, end_date = ?, "
                + "location = ?, max_teams = ?, status = ?, description = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setCommonFields(ps, tournament);
            ps.setInt(9, tournament.getId());
            ps.executeUpdate();
        }
    }

    /**
     * 删除（下架）赛事，报名记录由数据库外键级联删除
     */
    public int delete(int id) throws SQLException {
        String sql = "DELETE FROM tournament WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }

    /**
     * 赛事总数
     */
    public int countAll() throws SQLException {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tournament");
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    /**
     * 报名中赛事数量
     */
    public int countOpen() throws SQLException {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tournament WHERE status = 0");
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private void setCommonFields(PreparedStatement ps, Tournament tournament) throws SQLException {
        ps.setString(1, tournament.getTitle());
        ps.setString(2, tournament.getGame());
        ps.setString(3, tournament.getStartDate());
        ps.setString(4, tournament.getEndDate());
        ps.setString(5, tournament.getLocation());
        ps.setInt(6, tournament.getMaxTeams());
        ps.setInt(7, tournament.getStatus());
        ps.setString(8, tournament.getDescription());
    }

    private Tournament mapRow(ResultSet rs) throws SQLException {
        Tournament tournament = new Tournament();
        tournament.setId(rs.getInt("id"));
        tournament.setTitle(rs.getString("title"));
        tournament.setGame(rs.getString("game"));
        tournament.setStartDate(rs.getString("start_date"));
        tournament.setEndDate(rs.getString("end_date"));
        tournament.setLocation(rs.getString("location"));
        tournament.setMaxTeams(rs.getInt("max_teams"));
        tournament.setStatus(rs.getInt("status"));
        tournament.setDescription(rs.getString("description"));
        tournament.setCreateTime(rs.getString("create_time"));
        tournament.setRegisteredTeams(rs.getInt("registered_teams"));
        return tournament;
    }
}
