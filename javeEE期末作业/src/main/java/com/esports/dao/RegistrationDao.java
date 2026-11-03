package com.esports.dao;

import com.esports.entity.Registration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 赛事报名数据访问类
 */
public class RegistrationDao {

    private static final String BASE_SELECT =
            "SELECT r.id, r.team_id, r.tournament_id, r.remark, "
                    + "DATE_FORMAT(r.create_time, '%Y-%m-%d %H:%i') AS create_time, "
                    + "t.team_name, t.game, tr.title AS tournament_title, "
                    + "DATE_FORMAT(tr.start_date, '%Y-%m-%d') AS start_date "
                    + "FROM registration r "
                    + "JOIN team t ON r.team_id = t.id "
                    + "JOIN tournament tr ON r.tournament_id = tr.id ";

    /**
     * 查询报名记录，tournamentId 为 0 表示全部赛事
     */
    public List<Registration> findByCondition(int tournamentId) throws SQLException {
        List<Registration> list = new ArrayList<>();
        String sql = BASE_SELECT;
        if (tournamentId > 0) {
            sql += "WHERE r.tournament_id = ? ";
        }
        sql += "ORDER BY r.id DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (tournamentId > 0) {
                ps.setInt(1, tournamentId);
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
     * 查询某个战队的全部报名
     */
    public List<Registration> findByTeamId(int teamId) throws SQLException {
        List<Registration> list = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE r.team_id = ? ORDER BY r.id DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, teamId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    /**
     * 查询最新的报名记录，用于首页展示
     */
    public List<Registration> findRecent(int limit) throws SQLException {
        List<Registration> list = new ArrayList<>();
        String sql = BASE_SELECT + "ORDER BY r.id DESC LIMIT ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    /**
     * 根据 ID 查询报名记录
     */
    public Registration findById(int id) throws SQLException {
        String sql = BASE_SELECT + "WHERE r.id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    /**
     * 判断某战队是否已经报名某赛事
     */
    public boolean exists(int teamId, int tournamentId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM registration WHERE team_id = ? AND tournament_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, teamId);
            ps.setInt(2, tournamentId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    /**
     * 统计某个赛事的报名战队数
     */
    public int countByTournament(int tournamentId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM registration WHERE tournament_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tournamentId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    /**
     * 新增报名
     */
    public void insert(Registration registration) throws SQLException {
        String sql = "INSERT INTO registration (team_id, tournament_id, remark) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, registration.getTeamId());
            ps.setInt(2, registration.getTournamentId());
            ps.setString(3, registration.getRemark());
            ps.executeUpdate();
        }
    }

    /**
     * 删除报名（取消报名）
     */
    public int delete(int id) throws SQLException {
        String sql = "DELETE FROM registration WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }

    /**
     * 报名记录总数
     */
    public int countAll() throws SQLException {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM registration");
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private Registration mapRow(ResultSet rs) throws SQLException {
        Registration registration = new Registration();
        registration.setId(rs.getInt("id"));
        registration.setTeamId(rs.getInt("team_id"));
        registration.setTournamentId(rs.getInt("tournament_id"));
        registration.setRemark(rs.getString("remark"));
        registration.setCreateTime(rs.getString("create_time"));
        registration.setTeamName(rs.getString("team_name"));
        registration.setGame(rs.getString("game"));
        registration.setTournamentTitle(rs.getString("tournament_title"));
        registration.setStartDate(rs.getString("start_date"));
        return registration;
    }
}
