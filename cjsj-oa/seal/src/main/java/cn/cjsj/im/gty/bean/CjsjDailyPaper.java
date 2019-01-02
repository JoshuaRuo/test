package cn.cjsj.im.gty.bean;


/**
 * Created by LuoYang on 2018/5/14.
 * 日报数据模型
 */
public class CjsjDailyPaper {

    /**
     * 主键
     */
    protected Long id;

    /**
     * 日报时间
     */
    protected String time;

    /**
     * 今日完成工作
     */
    protected String todayTodoDetail;

    /**
     * 需要协调的工作
     */
    protected String needCoordinate;

    /**
     * 创建时间
     */
    protected String createTime;

    /**
     * 创建人
     */
    protected String creator;

    /**
     * 创建人ID
     */
    protected String creatorID;

    /**
     * 状态0提交，1草稿
     */
    protected String reception;

    /**
     * 发给谁
     */
    protected String sendTo;

    /**
     * 发给谁ID
     */
    protected String sendToID;


    /**
     * 草稿状态0正式1草稿
     */
    protected int draft;

    /**
     * 主题
     */
    protected String subject;

    /**
     * 接收人ID
     */
    protected String receptionID;

    /** 审批进度(1等待审批 2审批结束 3已经驳回) */
    private Byte approvals;

    /** 0未读 1已读 */
    private Byte hasRead;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CjsjDailyPaper)) return false;

        CjsjDailyPaper that = (CjsjDailyPaper) o;

        if (draft != that.draft) return false;
        if (!approvals.equals(that.approvals)) return false;
        if (!createTime.equals(that.createTime)) return false;
        if (!creator.equals(that.creator)) return false;
        if (!creatorID.equals(that.creatorID)) return false;
        if (!id.equals(that.id)) return false;
        if (!needCoordinate.equals(that.needCoordinate)) return false;
        if (!reception.equals(that.reception)) return false;
        if (!receptionID.equals(that.receptionID)) return false;
        if (!sendTo.equals(that.sendTo)) return false;
        if (!sendToID.equals(that.sendToID)) return false;
        if (!subject.equals(that.subject)) return false;
        if (!time.equals(that.time)) return false;
        if (!todayTodoDetail.equals(that.todayTodoDetail)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + time.hashCode();
        result = 31 * result + todayTodoDetail.hashCode();
        result = 31 * result + needCoordinate.hashCode();
        result = 31 * result + createTime.hashCode();
        result = 31 * result + creator.hashCode();
        result = 31 * result + creatorID.hashCode();
        result = 31 * result + reception.hashCode();
        result = 31 * result + sendTo.hashCode();
        result = 31 * result + sendToID.hashCode();
        result = 31 * result + draft;
        result = 31 * result + subject.hashCode();
        result = 31 * result + receptionID.hashCode();
        result = 31 * result + approvals.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CjsjDailyPaper{" +
                "id=" + id +
                ", time='" + time + '\'' +
                ", todayTodoDetail='" + todayTodoDetail + '\'' +
                ", needCoordinate='" + needCoordinate + '\'' +
                ", createTime='" + createTime + '\'' +
                ", creator='" + creator + '\'' +
                ", creatorID='" + creatorID + '\'' +
                ", reception='" + reception + '\'' +
                ", sendTo='" + sendTo + '\'' +
                ", sendToID='" + sendToID + '\'' +
                ", draft=" + draft +
                ", subject='" + subject + '\'' +
                ", receptionID=" + receptionID +
                ", approvals=" + approvals +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTodayTodoDetail() {
        return todayTodoDetail;
    }

    public void setTodayTodoDetail(String todayTodoDetail) {
        this.todayTodoDetail = todayTodoDetail;
    }

    public String getNeedCoordinate() {
        return needCoordinate;
    }

    public void setNeedCoordinate(String needCoordinate) {
        this.needCoordinate = needCoordinate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }

    public String getReception() {
        return reception;
    }

    public void setReception(String reception) {
        this.reception = reception;
    }

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public String getSendToID() {
        return sendToID;
    }

    public void setSendToID(String sendToID) {
        this.sendToID = sendToID;
    }

    public int getDraft() {
        return draft;
    }

    public void setDraft(int draft) {
        this.draft = draft;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getReceptionID() {
        return receptionID;
    }

    public void setReceptionID(String receptionID) {
        this.receptionID = receptionID;
    }

    public Byte getApprovals() {
        return approvals;
    }

    public void setApprovals(Byte approvals) {
        this.approvals = approvals;
    }

    public Byte getHasRead() {
        return hasRead;
    }

    public void setHasRead(Byte hasRead) {
        this.hasRead = hasRead;
    }
}