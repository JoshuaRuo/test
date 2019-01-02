package cn.cjsj.im.gty.bean;


import java.util.Date;

/**
 * Created by LuoYang on 2018/11/5.
 */
public class DepartmentStaffBean {
    private long userId;

    private String fullName;

    private String account;

    private long entryDate;

    private String userStatus;

    private String head_portrait;

    private String posName;

    private String orgName;


    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getPosName() {
        return posName;
    }

    public void setPosName(String posName) {
        this.posName = posName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public long getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(long entryDate) {
        this.entryDate = entryDate;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getHeadPortrait() {
        return head_portrait;
    }

    public void setHeadPortrait(String head_portrait) {
        this.head_portrait = head_portrait;
    }

    @Override
    public String toString() {
        return "DepartmentStaffBean{" +
                "userId=" + userId +
                ", fullName='" + fullName + '\'' +
                ", account='" + account + '\'' +
                ", entryDate=" + entryDate +
                ", userStatus='" + userStatus + '\'' +
                ", headPortrait='" + head_portrait + '\'' +
                ", posName='" + posName + '\'' +
                '}';
    }
}
