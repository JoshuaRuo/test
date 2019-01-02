package cn.cjsj.im.gty.bean;

/**
 * Created by LuoYang on 2017/12/29.
 */

public class BackLogResponse {

    /** 流程执行id */
    private Long runId;
    /** ACT流程实例ID */
    private Long actInstId;
    /** ACT流程定义ID */
    private String actDefId;
    /** 任务id */
    private Long taskId;
    /** 状态审批状态 1=同意 2=反对 3=驳回 0=弃权 4=追回 */
    private Short status;
    /** 主题 */
    private String subject;
    /** 创建人 */
    private String creator;
    /** 业务流程实例id */
    private String businessKey;
    /** 事项名称 */
    private String matterName;

    public Long getRunId() {
        return runId;
    }

    public void setRunId(Long runId) {
        this.runId = runId;
    }

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }

    public String getActDefId() {
        return actDefId;
    }

    public void setActDefId(String actDefId) {
        this.actDefId = actDefId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getMatterName() {
        return matterName;
    }

    public void setMatterName(String matterName) {
        this.matterName = matterName;
    }
}
