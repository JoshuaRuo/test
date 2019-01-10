package cn.cjsj.im.gty.bean;

import java.util.List;

/**
 * Created by LuoYang on 2019/1/9 15:40
 * 消息列表数据
 */
public class NewsListResponse {
    private int totalCount;
    private int pageIndex;

    private int pageSize;
    private List<NewsListGenericityModel> list;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<NewsListGenericityModel> getList() {
        return list;
    }

    public void setList(List<NewsListGenericityModel> list) {
        this.list = list;
    }
}
