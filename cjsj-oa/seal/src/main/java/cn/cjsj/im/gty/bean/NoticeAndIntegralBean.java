package cn.cjsj.im.gty.bean;

import java.util.List;

import cn.cjsj.im.model.NoticeDetailBean;

/**
 * Created by LuoYang on 2018/4/12.
 * 公告及积分
 */

public class NoticeAndIntegralBean {

    private List<NoticeDetailBean> noticeList;

    private List<NoticeIntegralBean> integralList;

    public List<NoticeDetailBean> getNoticeList() {
        return noticeList;
    }

    public void setNoticeList(List<NoticeDetailBean> noticeList) {
        this.noticeList = noticeList;
    }

    public List<NoticeIntegralBean> getIntegralList() {
        return integralList;
    }

    public void setIntegralList(List<NoticeIntegralBean> integralList) {
        this.integralList = integralList;
    }
}

