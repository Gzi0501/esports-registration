package com.esports.dao;

import com.esports.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * 队员数据访问类
 */
public class PlayerDao {

    // 关联战队表，查询出队员所属战队名称
    private static final String BASE_SELECT =
            "SELECT p.id, p.player_name, p.game_id, p.position, p.age, p.phone, p.team_id, "
                    + "DATE_FORMAT(p.join_time, '%Y-%m-%d %H:%i') AS join_time, t.team_name "
                    + "FROM player p LEFT JOIN team t ON p.team_id = t.id ";

    /**
     * 按条件查询队员：关键字匹配姓名/游戏ID/位置，teamId 为 0 表示全部战队
     */
    public List<Player> findByCondition(String keyword, int teamId) throws SQLException {
        List<Player> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(BASE_SELECT);
        List<Object> params = new ArrayList<>();
        List<String> conditions = new ArrayList<>();

        if (keyword != null && !keyword.isEmpty()) {
            conditions.add("(p.player_name LIKE ? OR p.game_id LIKE ? OR p.position LIKE ?)");
            String like = "%" + keyword + "%";
            params.add(like);
            params.add(like);
            params.add(like);
        }
        if (teamId > 0) {
            conditions.add("p.team_id = ?");
            params.add(teamId);
        }
        if (!conditions.isEmpty()) {
            sql.append("WHERE ").append(String.join(" AND ", conditions)).append(" ");
        }
        sql.append("ORDER BY p.id DESC");

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
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
     * 根据 ID 查询队员
     */
    public Player findById(int id) throws SQLException {
        String sql = BASE_SELECT + "WHERE p.id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    /**
     * 新增队员
     */
    public void insert(Player player) throws SQLException {
        String sql = "INSERT INTO player (player_name, game_id, position, age, phone, team_id) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setCommonFields(ps, player);
            ps.executeUpdate();
        }
    }

    /**
     * 修改队员
     */
    public void update(Player player) throws SQLException {
        String sql = "UPDATE player SET player_name = ?, game_id = ?, position = ?, age = ?, "
                + "phone = ?, team_id = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setCommonFields(ps, player);
            ps.setInt(7, player.getId());
            ps.executeUpdate();
        }
    }

    /**
     * 删除队员
     */
    public int delete(int id) throws SQLException {
        String sql = "DELETE FROM player WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }

    /**
     * 队员总数
     */
    public int countAll() throws SQLException {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM player");
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    /**
     * 插入和更新共用的字段赋值
     */
    private void setCommonFields(PreparedStatement ps, Player player) throws SQLException {
        ps.setString(1, player.getPlayerName());
        ps.setString(2, player.getGameId());
        ps.setString(3, player.getPosition());
        if (player.getAge() == null) {
            ps.setNull(4, Types.INTEGER);
        } else {
            ps.setInt(4, player.getAge());
        }
        ps.setString(5, player.getPhone());
        ps.setInt(6, player.getTeamId());
    }

    /**
     * 把 ResultSet 的一行封装成 Player 对象
     */
    private Player mapRow(ResultSet rs) throws SQLException {
        Player player = new Player();
        player.setId(rs.getInt("id"));
        player.setPlayerName(rs.getString("player_name"));
        player.setGameId(rs.getString("game_id"));
        player.setPosition(rs.getString("position"));
        int age = rs.getInt("age");
        player.setAge(rs.wasNull() ? null : age);
        player.setPhone(rs.getString("phone"));
        player.setTeamId(rs.getInt("team_id"));
        player.setJoinTime(rs.getString("join_time"));
        player.setTeamName(rs.getString("team_name"));
        return player;
    }
}
