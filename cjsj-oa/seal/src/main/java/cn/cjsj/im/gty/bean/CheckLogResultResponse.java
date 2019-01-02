package cn.cjsj.im.gty.bean;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by LuoYang on 2018/5/15.
 */

public class CheckLogResultResponse {
    private long id;
    private String groupName;
    private String className;
    private String userName;
    private JSONObject date;
    private String dayStatus;
    private String onType;
    private String onTime;
    private String onAddress;
    private String onStatus;
    private String offType;
    private String offTime;
    private String offAddress;
    private String offStatus;
    private String dateStr;
    private String week;
    private String duration;
    private String checkLog;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public JSONObject getDate() {
        return date;
    }

    public void setDate(JSONObject date) {
        this.date = date;
    }

    public String getDayStatus() {
        return dayStatus;
    }

    public void setDayStatus(String dayStatus) {
        this.dayStatus = dayStatus;
    }

    public String getOnType() {
        return onType;
    }

    public void setOnType(String onType) {
        this.onType = onType;
    }

    public String getOnTime() {
        return onTime;
    }

    public void setOnTime(String onTime) {
        this.onTime = onTime;
    }

    public String getOnAddress() {
        return onAddress;
    }

    public void setOnAddress(String onAddress) {
        this.onAddress = onAddress;
    }

    public String getOnStatus() {
        return onStatus;
    }

    public void setOnStatus(String onStatus) {
        this.onStatus = onStatus;
    }

    public String getOffType() {
        return offType;
    }

    public void setOffType(String offType) {
        this.offType = offType;
    }

    public String getOffTime() {
        return offTime;
    }

    public void setOffTime(String offTime) {
        this.offTime = offTime;
    }

    public String getOffAddress() {
        return offAddress;
    }

    public void setOffAddress(String offAddress) {
        this.offAddress = offAddress;
    }

    public String getOffStatus() {
        return offStatus;
    }

    public void setOffStatus(String offStatus) {
        this.offStatus = offStatus;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCheckLog() {
        return checkLog;
    }

    public void setCheckLog(String checkLog) {
        this.checkLog = checkLog;
    }

    @Override
    public String toString() {
        return "CheckLogResultResponse{" +
                "id=" + id +
                ", groupName='" + groupName + '\'' +
                ", className='" + className + '\'' +
                ", userName='" + userName + '\'' +
                ", date=" + date +
                ", dayStatus='" + dayStatus + '\'' +
                ", onType='" + onType + '\'' +
                ", onTime='" + onTime + '\'' +
                ", onAddress='" + onAddress + '\'' +
                ", onStatus='" + onStatus + '\'' +
                ", offType='" + offType + '\'' +
                ", offTime='" + offTime + '\'' +
                ", offAddress='" + offAddress + '\'' +
                ", offStatus='" + offStatus + '\'' +
                ", dateStr='" + dateStr + '\'' +
                ", week='" + week + '\'' +
                ", duration='" + duration + '\'' +
                ", checkLog='" + checkLog + '\'' +
                '}';
    }
}
