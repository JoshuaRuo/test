package cn.cjsj.im.gty.bean;

/**
 * Created by LuoYang on 2018/7/17 15:51
 * 集团发文
 */
public class GroupDispatchBean {

    private long id;
    private String fileCode;
    private String fileIssueNo;
    private int releaseType;
    private String title;
    private String summary;
    private String memo;
    private String attachment;
    private String makerId;
    private String makerName;
    private String makerDeptName;
    private String makeTime;
    private String releaseTypeVal;
    private String approvals;
    private int isRead;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFileCode() {
        return fileCode;
    }

    public void setFileCode(String fileCode) {
        this.fileCode = fileCode;
    }

    public String getFileIssueNo() {
        return fileIssueNo;
    }

    public void setFileIssueNo(String fileIssueNo) {
        this.fileIssueNo = fileIssueNo;
    }

    public int getReleaseType() {
        return releaseType;
    }

    public void setReleaseType(int releaseType) {
        this.releaseType = releaseType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getMakerId() {
        return makerId;
    }

    public void setMakerId(String makerId) {
        this.makerId = makerId;
    }

    public String getMakerName() {
        return makerName;
    }

    public void setMakerName(String makerName) {
        this.makerName = makerName;
    }

    public String getMakerDeptName() {
        return makerDeptName;
    }

    public void setMakerDeptName(String makerDeptName) {
        this.makerDeptName = makerDeptName;
    }

    public String getMakeTime() {
        return makeTime;
    }

    public void setMakeTime(String makeTime) {
        this.makeTime = makeTime;
    }

    public String getReleaseTypeVal() {
        return releaseTypeVal;
    }

    public void setReleaseTypeVal(String releaseTypeVal) {
        this.releaseTypeVal = releaseTypeVal;
    }

    public String getApprovals() {
        return approvals;
    }

    public void setApprovals(String approvals) {
        this.approvals = approvals;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    @Override
    public String toString() {
        return "GroupDispatchBean{" +
                "id=" + id +
                ", fileCode=" + fileCode +
                ", fileIssueNo=" + fileIssueNo +
                ", releaseType=" + releaseType +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", memo='" + memo + '\'' +
                ", attachment='" + attachment + '\'' +
                ", makerId='" + makerId + '\'' +
                ", makerName='" + makerName + '\'' +
                ", makerDeptName='" + makerDeptName + '\'' +
                ", makeTime='" + makeTime + '\'' +
                ", releaseTypeVal='" + releaseTypeVal + '\'' +
                ", approvals='" + approvals + '\'' +
                ", isRead=" + isRead +
                '}';
    }
}
