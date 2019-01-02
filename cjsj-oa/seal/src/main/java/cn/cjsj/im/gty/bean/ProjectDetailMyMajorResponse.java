package cn.cjsj.im.gty.bean;

/**
 * Created by LuoYang on 2018/12/10 16:50
 * 项目进度我的专业数据
 */
public class ProjectDetailMyMajorResponse {
    private long id;
    private String taskName;
    private String startDate;
    private String endDate;
    private String status;
    private String sjfw;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSjfw() {
        return sjfw;
    }

    public void setSjfw(String sjfw) {
        this.sjfw = sjfw;
    }
}
