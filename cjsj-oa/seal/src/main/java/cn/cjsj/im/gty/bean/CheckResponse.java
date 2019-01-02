package cn.cjsj.im.gty.bean;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by LuoYang on 2018/5/15.
 */

public class CheckResponse {
    private CheckLogResultResponse checkLogResult;
    private JSONObject checkClass;
    private JSONObject checkGroup;
    private long nowTime;
    private boolean inRange;


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
