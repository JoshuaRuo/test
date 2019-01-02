package cn.cjsj.im.gty.bean;

import java.util.List;

/**
 * Created by LuoYang on 2018/5/17.
 */

public class DailyPaperModel {
    private List<CjsjDailyPaper> cjsjDailyPaper;

    private List draftList;

    public List<CjsjDailyPaper> getCjsjDailyPaper() {
        return cjsjDailyPaper;
    }

    public void setCjsjDailyPaper(List<CjsjDailyPaper> cjsjDailyPaper) {
        this.cjsjDailyPaper = cjsjDailyPaper;
    }

    public List getDraftList() {
        return draftList;
    }

    public void setDraftList(List draftList) {
        this.draftList = draftList;
    }
}
