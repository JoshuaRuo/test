package cn.cjsj.im.gty.bean;

import java.util.List;

/**
 * Created by LuoYang on 2018/11/28 09:06\
 * 抄送人员列表
 */
public class SelectPeopleCcResponse {

    private PageBean pageBean;
    private List<SysUserBean> sysUserList;


    public PageBean getPageBean() {
        return pageBean;
    }

    public void setPageBean(PageBean pageBean) {
        this.pageBean = pageBean;
    }

    public List<SysUserBean> getSysUserList() {
        return sysUserList;
    }

    public void setSysUserList(List<SysUserBean> sysUserList) {
        this.sysUserList = sysUserList;
    }
}
