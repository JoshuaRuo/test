package cn.cjsj.im.gty.bean;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by LuoYang on 2019/1/9 09:18
 * 消息外部列表value数据
 */
public class NewsStatisticsResponse {
    private int type;
    private String typeName;
    private String content;
    private int unread;
    private JSONObject createTime;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public JSONObject getCreateTime() {
        return createTime;
    }

    public void setCreateTime(JSONObject createTime) {
        this.createTime = createTime;
    }
}
