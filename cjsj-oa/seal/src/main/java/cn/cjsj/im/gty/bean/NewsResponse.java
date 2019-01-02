package cn.cjsj.im.gty.bean;

/**
 * Created by LuoYang on 2018/1/4.
 */

public class NewsResponse {
    private long id;
    private String actDefId;
    private long businessKey;
    private int pushType;
    private String title;
    private long senderId;
    private String senderName;
    private long addresseeId;
    private String addresseeName;
    private String context;
    private String json;
    private int subclass;
    private String time;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getActDefId() {
        return actDefId;
    }

    public void setActDefId(String actDefId) {
        this.actDefId = actDefId;
    }

    public long getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(long businessKey) {
        this.businessKey = businessKey;
    }

    public int getPushType() {
        return pushType;
    }

    public void setPushType(int pushType) {
        this.pushType = pushType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public long getAddresseeId() {
        return addresseeId;
    }

    public void setAddresseeId(long addresseeId) {
        this.addresseeId = addresseeId;
    }

    public String getAddresseeName() {
        return addresseeName;
    }

    public void setAddresseeName(String addresseeName) {
        this.addresseeName = addresseeName;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public int getSubclass() {
        return subclass;
    }

    public void setSubclass(int subclass) {
        this.subclass = subclass;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
