package cn.cjsj.im.gty.bean;

import java.util.List;

/**
 * Created by LuoYang on 2018/7/17 16:35
 * 集团发文
 */
public class GroupDispatchResponseBean {

    private List<GroupDispatchBean> groupNoticeList;

    public List<GroupDispatchBean> getGroupNoticeList() {
        return groupNoticeList;
    }

    public void setGroupNoticeList(List<GroupDispatchBean> groupNoticeList) {
        this.groupNoticeList = groupNoticeList;
    }
}
