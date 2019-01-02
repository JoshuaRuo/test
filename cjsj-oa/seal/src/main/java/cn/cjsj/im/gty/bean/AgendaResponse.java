package cn.cjsj.im.gty.bean;

import java.util.List;

/**
 * Created by LuoYang on 2018/1/3.
 */

public class AgendaResponse {
    private PageBean pageBean;
    private List<BackLogResponse> processFinishList;

    public PageBean getPageBean() {
        return pageBean;
    }

    public void setPageBean(PageBean pageBean) {
        this.pageBean = pageBean;
    }

    public List<BackLogResponse> getProcessFinishList() {
        return processFinishList;
    }

    public void setProcessFinishList(List<BackLogResponse> processFinishList) {
        this.processFinishList = processFinishList;
    }
}
