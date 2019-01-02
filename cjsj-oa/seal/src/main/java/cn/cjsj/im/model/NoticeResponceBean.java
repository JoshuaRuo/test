package cn.cjsj.im.model;

import java.util.List;

/**
 * Created by LuoYang on 2017/9/29.
 * 公告数据
 */

public class NoticeResponceBean {

    private int elseReadCount;

    private int newsCount;

    private List<NoticeDetailBean> newsInfo;


    public int getElseReadCount() {
        return elseReadCount;
    }

    public void setElseReadCount(int elseReadCount) {
        this.elseReadCount = elseReadCount;
    }

    public int getNewsCount() {
        return newsCount;
    }

    public void setNewsCount(int newsCount) {
        this.newsCount = newsCount;
    }

    public List<NoticeDetailBean> getNewsInfo() {
        return newsInfo;
    }

    public void setNewsInfo(List<NoticeDetailBean> newsInfo) {
        this.newsInfo = newsInfo;
    }

    @Override
    public String toString() {
        return "NoticeResponceBean{" +
                "elseReadCount=" + elseReadCount +
                ", newsCount=" + newsCount +
                ", newsInfo=" + newsInfo +
                '}';
    }
}
