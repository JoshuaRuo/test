package cn.cjsj.im.gty.bean;

import java.util.List;

/**
 * Created by LuoYang on 2018/11/30 09:43
 * 审批进度
 */
public class ApprovalPlanResponse {
    private List<ApprovalPlanModel> taskOpinionList;
    private String sendToUser;

    public String getSendToUser() {
        return sendToUser;
    }

    public void setSendToUser(String sendToUser) {
        this.sendToUser = sendToUser;
    }

    public List<ApprovalPlanModel> getTaskOpinionList() {
        return taskOpinionList;
    }

    public void setTaskOpinionList(List<ApprovalPlanModel> taskOpinionList) {
        this.taskOpinionList = taskOpinionList;
    }
}
