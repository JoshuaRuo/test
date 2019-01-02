package cn.cjsj.im.gty.bean;

import java.util.List;

/**
 * Created by LuoYang on 2018/7/18 09:27
 * 部门发文
 */
public class DepartmentDispatchListBean {

    private List<DepartmentDispatchBean> deptNoticeList;

    public List<DepartmentDispatchBean> getDeptNoticeList() {
        return deptNoticeList;
    }

    public void setDeptNoticeList(List<DepartmentDispatchBean> deptNoticeList) {
        this.deptNoticeList = deptNoticeList;
    }
}
