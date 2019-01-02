package cn.cjsj.im.gty.bean;

/**
 * Created by LuoYang on 2018/7/18 09:24
 * 部门发文
 */
public class DepartmentDispatchBean {
    private long id;
    private long orgid;
    private String wjffh;
    private String fblx;
    private String bt;
    private String zy;
    private String bz;
    private String fj;
    private long djr;
    private String orgName;
    private String sorgName;
    private String djrq;
    private String approvals;
    private String fblxval;
    private String wjcode;
    private String djrName;
    private long runId;
    private int isRead;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrgid() {
        return orgid;
    }

    public void setOrgid(long orgid) {
        this.orgid = orgid;
    }

    public String getWjffh() {
        return wjffh;
    }

    public void setWjffh(String wjffh) {
        this.wjffh = wjffh;
    }

    public String getFblx() {
        return fblx;
    }

    public void setFblx(String fblx) {
        this.fblx = fblx;
    }

    public String getBt() {
        return bt;
    }

    public void setBt(String bt) {
        this.bt = bt;
    }

    public String getZy() {
        return zy;
    }

    public void setZy(String zy) {
        this.zy = zy;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getFj() {
        return fj;
    }

    public void setFj(String fj) {
        this.fj = fj;
    }

    public long getDjr() {
        return djr;
    }

    public void setDjr(long djr) {
        this.djr = djr;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getSorgName() {
        return sorgName;
    }

    public void setSorgName(String sorgName) {
        this.sorgName = sorgName;
    }

    public String getDjrq() {
        return djrq;
    }

    public void setDjrq(String djrq) {
        this.djrq = djrq;
    }

    public String getApprovals() {
        return approvals;
    }

    public void setApprovals(String approvals) {
        this.approvals = approvals;
    }

    public String getFblxval() {
        return fblxval;
    }

    public void setFblxval(String fblxval) {
        this.fblxval = fblxval;
    }

    public String getWjcode() {
        return wjcode;
    }

    public void setWjcode(String wjcode) {
        this.wjcode = wjcode;
    }

    public String getDjrName() {
        return djrName;
    }

    public void setDjrName(String djrName) {
        this.djrName = djrName;
    }

    public long getRunId() {
        return runId;
    }

    public void setRunId(long runId) {
        this.runId = runId;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    @Override
    public String toString() {
        return "DepartmentDispatchBean{" +
                "id=" + id +
                ", orgid=" + orgid +
                ", wjffh='" + wjffh + '\'' +
                ", fblx='" + fblx + '\'' +
                ", bt='" + bt + '\'' +
                ", zy='" + zy + '\'' +
                ", bz='" + bz + '\'' +
                ", fj='" + fj + '\'' +
                ", djr=" + djr +
                ", orgName='" + orgName + '\'' +
                ", sorgName='" + sorgName + '\'' +
                ", djrq='" + djrq + '\'' +
                ", approvals='" + approvals + '\'' +
                ", fblxval='" + fblxval + '\'' +
                ", wjcode='" + wjcode + '\'' +
                ", djrName='" + djrName + '\'' +
                ", runId=" + runId +
                ", isRead='" + isRead + '\'' +
                '}';
    }
}
