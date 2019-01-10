package cn.cjsj.im.gty.bean;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by LuoYang on 2019/1/9 15:42
 * 消息列表List泛型数据
 */
public class NewsListGenericityModel {
    private long id;
    private int type;
    private int isRead;
    private long businessKey;
    private String userId;
    private String title;
    private String subtitle;
    private String [] content;
    private JSONObject createTime;
    private String actDefId;
    private int pushType;

    public String getActDefId() {
        return actDefId;
    }

    public void setActDefId(String actDefId) {
        this.actDefId = actDefId;
    }

    public int getPushType() {
        return pushType;
    }

    public void setPushType(int pushType) {
        this.pushType = pushType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public long getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(long businessKey) {
        this.businessKey = businessKey;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String[] getContent() {
        return content;
    }

    public void setContent(String[] content) {
        this.content = content;
    }

    public JSONObject getCreateTime() {
        return createTime;
    }

    public void setCreateTime(JSONObject createTime) {
        this.createTime = createTime;
    }
}
