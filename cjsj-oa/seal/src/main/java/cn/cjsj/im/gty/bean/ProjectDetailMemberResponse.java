package cn.cjsj.im.gty.bean;

/**
 * Created by LuoYang on 2018/12/11 11:39
 * 项目详情成员数据
 */
public class ProjectDetailMemberResponse {
    private String createBy;
    private String createtime;
    private String updatetime;
    private String updateBy;
    private int id;
    private long groupID;
    private long userID;
    private String userName;
    private String roleName;
    private String projectID;
    private String headIcon;
    private String lastSeeTime;


    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getGroupID() {
        return groupID;
    }

    public void setGroupID(long groupID) {
        this.groupID = groupID;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }

    public String getLastSeeTime() {
        return lastSeeTime;
    }

    public void setLastSeeTime(String lastSeeTime) {
        this.lastSeeTime = lastSeeTime;
    }

    @Override
    public String toString() {
        return "ProjectDetailMemberResponse{" +
                "createBy='" + createBy + '\'' +
                ", createtime='" + createtime + '\'' +
                ", updatetime='" + updatetime + '\'' +
                ", updateBy='" + updateBy + '\'' +
                ", id=" + id +
                ", groupID=" + groupID +
                ", userID=" + userID +
                ", userName='" + userName + '\'' +
                ", roleName='" + roleName + '\'' +
                ", projectID='" + projectID + '\'' +
                ", headIcon='" + headIcon + '\'' +
                ", lastSeeTime='" + lastSeeTime + '\'' +
                '}';
    }
}
