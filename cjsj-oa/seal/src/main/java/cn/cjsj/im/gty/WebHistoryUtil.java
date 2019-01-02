package cn.cjsj.im.gty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LuoYang on 2018/1/2.
 */

public class WebHistoryUtil {
    private WebHistoryUtil mWebHistoryUtil;
    private List<String> mHisUrl = new ArrayList<>();
    //    public synchronized static WebHistoryUtil getInstance(){
//        if(mWebHistoryUtil == null){
//            return new WebHistoryUtil();
//        }else {
//            return mWebHistoryUtil;
//        }
//    }
    private boolean hasUrl = false;

    public String getUrl(int index) {

        return mHisUrl.get(index - 1);
    }

    public void setUrl(String url) {
        mHisUrl.add(url);
    }

    public void remove(int index) {
        mHisUrl.remove(index);
    }

    public void clearHis() {
        mHisUrl.clear();
    }

    public boolean isHasUrl(String url) {

        for (int i = 0; i < mHisUrl.size(); i++) {
            if (mHisUrl.get(i).contains(url)) {
                hasUrl = true;
            }
            if (mHisUrl.get(i).contains("daily_log.html")){
                mHisUrl.remove(i);
            }
        }
        return hasUrl;
    }

}
