package cn.cjsj.im.gty.bean;

/**
 * Created by LuoYang on 2018/11/26 15:11
 * 早到榜
 */
public class EarlyArrvalRank {
    private long userId;

    private String time;

    private String name;

    private String department;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
