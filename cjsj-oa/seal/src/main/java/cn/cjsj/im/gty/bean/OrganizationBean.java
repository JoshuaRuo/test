package cn.cjsj.im.gty.bean;
/**
 * Created by LuoYang on 2018/11/2.
 * 组织结构
 */
public class OrganizationBean{

    private long orgId;

    private String orgName;

    private String orgSupId;

    private String path;

    private int orgType;

    private String orgPathName;

    private String code;

    private int userCount;

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgSupId() {
        return orgSupId;
    }

    public void setOrgSupId(String orgSupId) {
        this.orgSupId = orgSupId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getOrgType() {
        return orgType;
    }

    public void setOrgType(int orgType) {
        this.orgType = orgType;
    }

    public String getOrgPathName() {
        return orgPathName;
    }

    public void setOrgPathName(String orgPathName) {
        this.orgPathName = orgPathName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    @Override
    public String toString() {
        return "OrganizationBean{" +
                "orgId=" + orgId +
                ", orgName='" + orgName + '\'' +
                ", orgSupId='" + orgSupId + '\'' +
                ", path='" + path + '\'' +
                ", orgType=" + orgType +
                ", orgPathName='" + orgPathName + '\'' +
                ", code='" + code + '\'' +
                ", userCount=" + userCount +
                '}';
    }
}
