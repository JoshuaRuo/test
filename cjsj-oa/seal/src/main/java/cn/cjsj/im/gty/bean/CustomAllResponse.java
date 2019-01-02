package cn.cjsj.im.gty.bean;

import com.alibaba.fastjson.JSONObject;

/**
 * 模块信息对象
 * Created by LuoYang on 2018/1/11.
 */

public class CustomAllResponse {

    private String authorizeType;
    private String defKey;
    private String managementEdit;
    private String managementDel;
    private String managementStart;
    private String managementSet;
    private String managementInternational;
    private String managementClean;
    private String instanceDel;
    private String instanceLog;
    private String rightContent;
    private JSONObject rightJsonObj;
    private String actDefId;

    public String getAuthorizeType() {
        return authorizeType;
    }

    public void setAuthorizeType(String authorizeType) {
        this.authorizeType = authorizeType;
    }

    public String getDefKey() {
        return defKey;
    }

    public void setDefKey(String defKey) {
        this.defKey = defKey;
    }

    public String getManagementEdit() {
        return managementEdit;
    }

    public void setManagementEdit(String managementEdit) {
        this.managementEdit = managementEdit;
    }

    public String getManagementDel() {
        return managementDel;
    }

    public void setManagementDel(String managementDel) {
        this.managementDel = managementDel;
    }

    public String getManagementStart() {
        return managementStart;
    }

    public void setManagementStart(String managementStart) {
        this.managementStart = managementStart;
    }

    public String getManagementSet() {
        return managementSet;
    }

    public void setManagementSet(String managementSet) {
        this.managementSet = managementSet;
    }

    public String getManagementInternational() {
        return managementInternational;
    }

    public void setManagementInternational(String managementInternational) {
        this.managementInternational = managementInternational;
    }

    public String getManagementClean() {
        return managementClean;
    }

    public void setManagementClean(String managementClean) {
        this.managementClean = managementClean;
    }

    public String getInstanceDel() {
        return instanceDel;
    }

    public void setInstanceDel(String instanceDel) {
        this.instanceDel = instanceDel;
    }

    public String getInstanceLog() {
        return instanceLog;
    }

    public void setInstanceLog(String instanceLog) {
        this.instanceLog = instanceLog;
    }

    public String getRightContent() {
        return rightContent;
    }

    public void setRightContent(String rightContent) {
        this.rightContent = rightContent;
    }

    public JSONObject getRightJsonObj() {
        return rightJsonObj;
    }

    public void setRightJsonObj(JSONObject rightJsonObj) {
        this.rightJsonObj = rightJsonObj;
    }

    public String getActDefId() {
        return actDefId;
    }

    public void setActDefId(String actDefId) {
        this.actDefId = actDefId;
    }

    @Override
    public String toString() {
        return "CustomAllResponse{" +
                "authorizeType='" + authorizeType + '\'' +
                ", defKey='" + defKey + '\'' +
                ", managementEdit='" + managementEdit + '\'' +
                ", managementDel='" + managementDel + '\'' +
                ", managementStart='" + managementStart + '\'' +
                ", managementSet='" + managementSet + '\'' +
                ", managementInternational='" + managementInternational + '\'' +
                ", managementClean='" + managementClean + '\'' +
                ", instanceDel='" + instanceDel + '\'' +
                ", instanceLog='" + instanceLog + '\'' +
                ", rightContent='" + rightContent + '\'' +
                ", rightJsonObj=" + rightJsonObj +
                ", actDefId='" + actDefId + '\'' +
                '}';
    }
}
