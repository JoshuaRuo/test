package cn.cjsj.im.gty.bean;

import java.util.List;
import java.util.Map;

/**
 * Created by LuoYang on 2018/11/21 10:11
 */
public class CheckStatisticsResponse {

    //旷工天数
    private List<String> absenteeismDaysList;
    //补卡次数
    private List<String> remakeCardDaysList;
    //考勤天数
    private List<String> checkInDaysList;
    //早退
    private List<String> leaveEarlyDaysList;
    //请假
    private List<String> askLeaveDaysList;
    //缺卡
    private List<Map<String, String>> lackCardDaysList;
    //迟到
    private List<String> lateDaysList;
    //外勤
    private List<String> fieldClockDaysList;
    //休息日
    private List<String> restDaysList;

    private int avgMinute;
    //出差
    private List<String> businessTripDaysList;

    private String currentCheckDate;

    public String getCurrentCheckDate() {
        return currentCheckDate;
    }

    public void setCurrentCheckDate(String currentCheckDate) {
        this.currentCheckDate = currentCheckDate;
    }

    public List<String> getAbsenteeismDaysList() {
        return absenteeismDaysList;
    }

    public void setAbsenteeismDaysList(List<String> absenteeismDaysList) {
        this.absenteeismDaysList = absenteeismDaysList;
    }

    public List<String> getRemakeCardDaysList() {
        return remakeCardDaysList;
    }

    public void setRemakeCardDaysList(List<String> remakeCardDaysList) {
        this.remakeCardDaysList = remakeCardDaysList;
    }

    public List<String> getCheckInDaysList() {
        return checkInDaysList;
    }

    public void setCheckInDaysList(List<String> checkInDaysList) {
        this.checkInDaysList = checkInDaysList;
    }

    public List<String> getLeaveEarlyDaysList() {
        return leaveEarlyDaysList;
    }

    public void setLeaveEarlyDaysList(List<String> leaveEarlyDaysList) {
        this.leaveEarlyDaysList = leaveEarlyDaysList;
    }

    public List<String> getAskLeaveDaysList() {
        return askLeaveDaysList;
    }

    public void setAskLeaveDaysList(List<String> askLeaveDaysList) {
        this.askLeaveDaysList = askLeaveDaysList;
    }

    public List<Map<String, String>> getLackCardDaysList() {
        return lackCardDaysList;
    }

    public void setLackCardDaysList(List<Map<String, String>> lackCardDaysList) {
        this.lackCardDaysList = lackCardDaysList;
    }

    public List<String> getLateDaysList() {
        return lateDaysList;
    }

    public void setLateDaysList(List<String> lateDaysList) {
        this.lateDaysList = lateDaysList;
    }

    public List<String> getFieldClockDaysList() {
        return fieldClockDaysList;
    }

    public void setFieldClockDaysList(List<String> fieldClockDaysList) {
        this.fieldClockDaysList = fieldClockDaysList;
    }

    public List<String> getRestDaysList() {
        return restDaysList;
    }

    public void setRestDaysList(List<String> restDaysList) {
        this.restDaysList = restDaysList;
    }

    public int getAvgMinute() {
        return avgMinute;
    }

    public void setAvgMinute(int avgMinute) {
        this.avgMinute = avgMinute;
    }

    public List<String> getBusinessTripDaysList() {
        return businessTripDaysList;
    }

    public void setBusinessTripDaysList(List<String> businessTripDaysList) {
        this.businessTripDaysList = businessTripDaysList;
    }
}
