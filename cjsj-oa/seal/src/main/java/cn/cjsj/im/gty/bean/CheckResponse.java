package cn.cjsj.im.gty.bean;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * Created by LuoYang on 2018/5/15.
 */

public class CheckResponse {
    private CheckLogResultResponse checkLogResult;
    private JSONObject checkClass;
    private JSONObject checkGroup;
    private long nowTime;
    private boolean inRange;
    private List<JSONObject> checkAskLeaveList;


    public List<JSONObject> getCheckAskLeaveList() {
        return checkAskLeaveList;
    }

    public void setCheckAskLeaveList(List<JSONObject> checkAskLeaveList) {
        this.checkAskLeaveList = checkAskLeaveList;
    }

    public boolean isInRange() {
        return inRange;
    }

    public void setInRange(boolean inRange) {
        this.inRange = inRange;
    }

    public long getNowTime() {
        return nowTime;
    }

    public void setNowTime(long nowTime) {
        this.nowTime = nowTime;
    }

    public JSONObject getCheckGroup() {
        return checkGroup;
    }

    public void setCheckGroup(JSONObject checkGroup) {
        this.checkGroup = checkGroup;
    }

    public JSONObject getCheckClass() {
        return checkClass;
    }

    public void setCheckClass(JSONObject checkClass) {
        this.checkClass = checkClass;
    }

    public CheckLogResultResponse getCheckLogResult() {
        return checkLogResult;
    }

    public void setCheckLogResult(CheckLogResultResponse checkLogResult) {
        this.checkLogResult = checkLogResult;
    }
}
