package cn.cjsj.im.gty.bean;

import java.util.List;

/**
 * Created by LuoYang on 2018/11/26 15:13
 */
public class EarlyArrvalRankListResponse {

    private int userRankNum;
    private List<EarlyArrvalRank> checkEarlyRankList;
    private EarlyArrvalRank userRank;
    private SysUserBean user;

    public int getUserRankNum() {
        return userRankNum;
    }

    public void setUserRankNum(int userRankNum) {
        this.userRankNum = userRankNum;
    }

    public List<EarlyArrvalRank> getCheckEarlyRankList() {
        return checkEarlyRankList;
    }

    public void setCheckEarlyRankList(List<EarlyArrvalRank> checkEarlyRankList) {
        this.checkEarlyRankList = checkEarlyRankList;
    }

    public EarlyArrvalRank getUserRank() {
        return userRank;
    }

    public void setUserRank(EarlyArrvalRank userRank) {
        this.userRank = userRank;
    }

    public SysUserBean getUser() {
        return user;
    }

    public void setUser(SysUserBean user) {
        this.user = user;
    }
}
