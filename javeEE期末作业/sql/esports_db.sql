-- ============================================================
-- 游戏战队与赛事报名系统 数据库初始化脚本
-- 数据库：esports_db   字符集：utf8mb4
-- 使用方式：mysql -u root -p < esports_db.sql
-- ============================================================

CREATE DATABASE IF NOT EXISTS esports_db
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE esports_db;

SET NAMES utf8mb4;

-- 先删除子表，再删除主表，避免外键约束报错
DROP TABLE IF EXISTS registration;
DROP TABLE IF EXISTS player;
DROP TABLE IF EXISTS tournament;
DROP TABLE IF EXISTS team;

-- ---------------------------- 战队表 ----------------------------
CREATE TABLE team (
    id          INT AUTO_INCREMENT PRIMARY KEY COMMENT '战队ID',
    team_name   VARCHAR(50)  NOT NULL COMMENT '战队名称',
    game        VARCHAR(30)  NOT NULL COMMENT '游戏项目',
    captain     VARCHAR(30)  NOT NULL COMMENT '队长',
    slogan      VARCHAR(200) DEFAULT NULL COMMENT '战队口号',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_team_name (team_name)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '战队表';

-- ---------------------------- 队员表 ----------------------------
CREATE TABLE player (
    id          INT AUTO_INCREMENT PRIMARY KEY COMMENT '队员ID',
    player_name VARCHAR(30)  NOT NULL COMMENT '选手姓名',
    game_id     VARCHAR(50)  NOT NULL COMMENT '游戏ID',
    position    VARCHAR(20)  NOT NULL COMMENT '场上位置',
    age         INT          DEFAULT NULL COMMENT '年龄',
    phone       VARCHAR(20)  DEFAULT NULL COMMENT '联系电话',
    team_id     INT          NOT NULL COMMENT '所属战队',
    join_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
    KEY idx_player_team (team_id),
    CONSTRAINT fk_player_team FOREIGN KEY (team_id) REFERENCES team (id) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '队员表';

-- ---------------------------- 赛事表 ----------------------------
CREATE TABLE tournament (
    id          INT AUTO_INCREMENT PRIMARY KEY COMMENT '赛事ID',
    title       VARCHAR(80)  NOT NULL COMMENT '赛事名称',
    game        VARCHAR(30)  NOT NULL COMMENT '游戏项目',
    start_date  DATE         NOT NULL COMMENT '比赛开始日期',
    end_date    DATE         NOT NULL COMMENT '比赛结束日期',
    location    VARCHAR(80)  NOT NULL COMMENT '比赛地点',
    max_teams   INT          NOT NULL DEFAULT 8 COMMENT '可容纳战队数',
    status      TINYINT      NOT NULL DEFAULT 0 COMMENT '状态：0报名中 1进行中 2已结束',
    description VARCHAR(500) DEFAULT NULL COMMENT '赛事说明',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间'
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '赛事表';

-- ---------------------------- 报名表 ----------------------------
CREATE TABLE registration (
    id            INT AUTO_INCREMENT PRIMARY KEY COMMENT '报名ID',
    team_id       INT          NOT NULL COMMENT '报名战队',
    tournament_id INT          NOT NULL COMMENT '报名赛事',
    remark        VARCHAR(200) DEFAULT NULL COMMENT '报名备注',
    create_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '报名时间',
    UNIQUE KEY uk_team_tournament (team_id, tournament_id),
    KEY idx_reg_tournament (tournament_id),
    CONSTRAINT fk_reg_team FOREIGN KEY (team_id) REFERENCES team (id) ON DELETE CASCADE,
    CONSTRAINT fk_reg_tournament FOREIGN KEY (tournament_id) REFERENCES tournament (id) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '赛事报名表';

-- ============================================================
-- 示例数据
-- ============================================================

INSERT INTO team (team_name, game, captain, slogan) VALUES
('NOVA 电竞',   '英雄联盟',   '陈昊',   '以新星之名，燃赛场之光'),
('星辰战队',     '王者荣耀',   '周雨桐', '星光不问赶路人'),
('NEON 霓虹',    '无畏契约',   '李明轩', '点亮每一次精准射击'),
('破晓之翼',     'CS2',       '王梓豪', '黎明之前，先赢下这一局'),
('逐鹿电竞',     'DOTA2',     '赵一鸣', '逐鹿中原，问鼎峡谷');

INSERT INTO player (player_name, game_id, position, age, phone, team_id) VALUES
('陈昊',   'NOVA_Top',       '上单',   21, '13800000001', 1),
('徐子豪', 'NOVA_Jungle',    '打野',   20, '13800000002', 1),
('韩雨',   'NOVA_Mid',       '中单',   22, '13800000003', 1),
('林小北', 'NOVA_ADC',       '射手',   19, '13800000004', 1),
('郑凯',   'NOVA_Support',   '辅助',   21, '13800000005', 1),
('周雨桐', 'Star_Mid',       '中单',   20, '13900000001', 2),
('孙悦',   'Star_ADC',       '射手',   19, '13900000002', 2),
('何俊',   'Star_Jungle',    '打野',   21, '13900000003', 2),
('唐糖',   'Star_Support',   '辅助',   20, '13900000004', 2),
('李明轩', 'NEON_IGL',       '指挥',   23, '13700000001', 3),
('丁一',   'NEON_Duelist',   '突破手', 21, '13700000002', 3),
('苏晓',   'NEON_Sniper',    '狙击手', 22, '13700000003', 3),
('吴越',   'NEON_Flex',      '自由人', 20, '13700000004', 3),
('王梓豪', 'Dawn_IGL',       '指挥',   24, '13600000001', 4),
('刘畅',   'Dawn_Entry',     '突破手', 22, '13600000002', 4),
('孙浩',   'Dawn_AWP',       '狙击手', 23, '13600000003', 4),
('周杰',   'Dawn_Flex',      '自由人', 21, '13600000004', 4),
('赵一鸣', 'ZL_Mid',         '中单',   23, '13500000001', 5),
('钱多多', 'ZL_Support',     '辅助',   22, '13500000002', 5);

INSERT INTO tournament (title, game, start_date, end_date, location, max_teams, status, description) VALUES
('高校电竞联赛 · 英雄联盟', '英雄联盟',
 DATE_ADD(CURDATE(), INTERVAL 14 DAY), DATE_ADD(CURDATE(), INTERVAL 16 DAY),
 '线上赛（官方客户端）', 8, 0, '面向在校大学生的英雄联盟团队赛，采用 BO3 单败淘汰赛制，决赛 BO5。'),
('王者荣耀高校争霸赛', '王者荣耀',
 DATE_ADD(CURDATE(), INTERVAL 30 DAY), DATE_ADD(CURDATE(), INTERVAL 32 DAY),
 '大学城电竞馆', 8, 0, '五人制王者荣耀赛事，现场大屏直播，冠亚季军均有奖金与证书。'),
('无畏契约新生杯', '无畏契约',
 DATE_SUB(CURDATE(), INTERVAL 12 DAY), DATE_SUB(CURDATE(), INTERVAL 10 DAY),
 '线上赛', 8, 2, '面向新生的无畏契约邀请赛，已结束。'),
('CS2 校园邀请赛', 'CS2',
 CURDATE(), DATE_ADD(CURDATE(), INTERVAL 1 DAY),
 '学校机房（线下）', 8, 1, 'CS2 校园线下邀请赛，比赛进行中。'),
('DOTA2 冬季挑战赛', 'DOTA2',
 DATE_ADD(CURDATE(), INTERVAL 45 DAY), DATE_ADD(CURDATE(), INTERVAL 47 DAY),
 '线上赛', 4, 0, 'DOTA2 冬季线上挑战赛，限定 4 支战队报名。');

INSERT INTO registration (team_id, tournament_id, remark, create_time) VALUES
(1, 1, '全员可按时参赛', DATE_SUB(NOW(), INTERVAL 3 DAY)),
(2, 1, '申请安排周末场次', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(4, 1, '', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(2, 2, '需要线下参赛指引', DATE_SUB(NOW(), INTERVAL 5 DAY)),
(3, 2, '', DATE_SUB(NOW(), INTERVAL 4 DAY)),
(5, 2, '队长负责对接', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(3, 3, '新生杯已完赛', DATE_SUB(NOW(), INTERVAL 20 DAY)),
(4, 3, '', DATE_SUB(NOW(), INTERVAL 19 DAY)),
(4, 4, '现场参赛', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(5, 5, '希望安排晚间比赛', DATE_SUB(NOW(), INTERVAL 1 DAY));
