package cn.cjsj.im.gty.bean;

/**
 * Created by LuoYang on 2018/11/29 16:06
 * 补卡详情数据
 */
public class ReWorkDetailResponse {
    private String date;
    private String executor;
    private String rejectName;
    private String nextExecutor;
    private int zt;
    private ReWorkHistoryResponse checkRemakeCard;
    private int status;
    private String opinion;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    public String getRejectName() {
        return rejectName;
    }

    public void setRejectName(String rejectName) {
        this.rejectName = rejectName;
    }

    public String getNextExecutor() {
        return nextExecutor;
    }

    public void setNextExecutor(String nextExecutor) {
        this.nextExecutor = nextExecutor;
    }

    public int getZt() {
        return zt;
    }

    public void setZt(int zt) {
        this.zt = zt;
    }

    public ReWorkHistoryResponse getCheckRemakeCard() {
        return checkRemakeCard;
    }

    public void setCheckRemakeCard(ReWorkHistoryResponse checkRemakeCard) {
        this.checkRemakeCard = checkRemakeCard;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }
}
