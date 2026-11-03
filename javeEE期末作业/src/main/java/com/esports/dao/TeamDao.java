package com.esports.dao;

import com.esports.entity.Team;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 战队数据访问类
 */
public class TeamDao {

    // 公共查询语句：同时统计每个战队的队员人数
    private static final String BASE_SELECT =
            "SELECT t.id, t.team_name, t.game, t.captain, t.slogan, "
                    + "DATE_FORMAT(t.create_time, '%Y-%m-%d %H:%i') AS create_time, "
                    + "(SELECT COUNT(*) FROM player p WHERE p.team_id = t.id) AS player_count "
                    + "FROM team t ";

    /**
     * 查询全部战队
     */
    public List<Team> findAll() throws SQLException {
        List<Team> list = new ArrayList<>();
        String sql = BASE_SELECT + "ORDER BY t.id DESC";
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
     * 按关键字查询战队（名称 / 游戏项目 / 队长）
     */
    public List<Team> findByKeyword(String keyword) throws SQLException {
        List<Team> list = new ArrayList<>();
        String sql = BASE_SELECT
                + "WHERE t.team_name LIKE ? OR t.game LIKE ? OR t.captain LIKE ? "
                + "ORDER BY t.id DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String like = "%" + keyword + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    /**
     * 根据 ID 查询战队
     */
    public Team findById(int id) throws SQLException {
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
     * 判断战队名称是否已被其他战队使用
     */
    public boolean existsName(String teamName, int excludeId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM team WHERE team_name = ? AND id <> ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, teamName);
            ps.setInt(2, excludeId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    /**
     * 新增战队
     */
    public void insert(Team team) throws SQLException {
        String sql = "INSERT INTO team (team_name, game, captain, slogan) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, team.getTeamName());
            ps.setString(2, team.getGame());
            ps.setString(3, team.getCaptain());
            ps.setString(4, team.getSlogan());
            ps.executeUpdate();
        }
    }

    /**
     * 修改战队
     */
    public void update(Team team) throws SQLException {
        String sql = "UPDATE team SET team_name = ?, game = ?, captain = ?, slogan = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, team.getTeamName());
            ps.setString(2, team.getGame());
            ps.setString(3, team.getCaptain());
            ps.setString(4, team.getSlogan());
            ps.setInt(5, team.getId());
            ps.executeUpdate();
        }
    }

    /**
     * 删除战队（队员与报名记录由数据库外键级联删除）
     */
    public int delete(int id) throws SQLException {
        String sql = "DELETE FROM team WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }

    /**
     * 战队总数
     */
    public int countAll() throws SQLException {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM team");
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    /**
     * 把 ResultSet 的一行封装成 Team 对象
     */
    private Team mapRow(ResultSet rs) throws SQLException {
        Team team = new Team();
        team.setId(rs.getInt("id"));
        team.setTeamName(rs.getString("team_name"));
        team.setGame(rs.getString("game"));
        team.setCaptain(rs.getString("captain"));
        team.setSlogan(rs.getString("slogan"));
        team.setCreateTime(rs.getString("create_time"));
        team.setPlayerCount(rs.getInt("player_count"));
        return team;
    }
}
