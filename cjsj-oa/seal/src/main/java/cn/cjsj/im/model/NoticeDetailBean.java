package cn.cjsj.im.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LuoYang on 2017/9/30.
 */

public class NoticeDetailBean implements Serializable{
    /** 主键 */
    private long id;
    /** 标题 */
    private String title;
    /** 内容 */
    private String context;
    /** 发布时间 */
    private String time;
    /** 是否置顶(0不置顶 1置顶) */
    private byte stick;
    /** 是否删除(0未删除 1删除) */
    private byte status;
    /** 创建人ID */
    private Long creatorId;
    /** 创建人 */
    private String creatorName;
    /** 1已读 0未读 */
    private byte isRead;

    private byte type;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public byte getStick() {
        return stick;
    }

    public void setStick(byte stick) {
        this.stick = stick;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public byte getIsRead() {
        return isRead;
    }

    public void setIsRead(byte isRead) {
        this.isRead = isRead;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }
}