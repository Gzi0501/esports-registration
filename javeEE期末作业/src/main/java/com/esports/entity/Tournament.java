package com.esports.entity;

/**
 * 赛事实体类，对应 tournament 表
 */
public class Tournament {

    private int id;
    private String title;
    private String game;
    private String startDate;
    private String endDate;
    private String location;
    private int maxTeams;
    private int status; // 0 报名中，1 进行中，2 已结束
    private String description;
    private String createTime;
    private int registeredTeams; // 查询时统计出的已报名战队数

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getMaxTeams() {
        return maxTeams;
    }

    public void setMaxTeams(int maxTeams) {
        this.maxTeams = maxTeams;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getRegisteredTeams() {
        return registeredTeams;
    }

    public void setRegisteredTeams(int registeredTeams) {
        this.registeredTeams = registeredTeams;
    }

    /**
     * 在 JSP 中可以通过 ${tournament.statusText} 直接输出中文状态
     */
    public String getStatusText() {
        if (status == 0) {
            return "报名中";
        } else if (status == 1) {
            return "进行中";
        } else if (status == 2) {
            return "已结束";
        }
        return "未知";
    }

    /**
     * 对应 CSS 中的状态徽章样式
     */
    public String getStatusClass() {
        if (status == 0) {
            return "badge-open";
        } else if (status == 1) {
            return "badge-running";
        }
        return "badge-ended";
    }

    /**
     * 剩余可报名名额
     */
    public int getRemainingTeams() {
        return Math.max(maxTeams - registeredTeams, 0);
    }
}
