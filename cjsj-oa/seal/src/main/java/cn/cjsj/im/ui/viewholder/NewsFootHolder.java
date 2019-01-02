package cn.cjsj.im.ui.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import cn.cjsj.im.R;
import cn.cjsj.im.gty.common.NewsLoadingFooter;
import cn.cjsj.im.server.utils.NLog;

/**
 * 消息列表底部加载布局
 * Created by LuoYang on 2018/1/5.
 */

public class NewsFootHolder extends RecyclerView.ViewHolder {

    View footLoadView;
    View footTheEnd;

    private static String TAG = "NewsFootHolder";

    public NewsFootHolder(View itemView) {
        super(itemView);
        footLoadView = itemView.findViewById(R.id.news_load_view);
        footTheEnd = itemView.findViewById(R.id.news_load_end);
    }

    /**
     * 根据传过来的status控制哪个状态可见
     * @param status
     */
    public void setData(NewsLoadingFooter.LoadingFooter status){
        NLog.e(TAG,"reduAdapter" + status + "");
        switch (status){
            case Normal:
                    setAllGone();
                break;

            case Loading:
                setAllGone();
                footLoadView.setVisibility(View.VISIBLE);
                break;

            case TheEnd:
                setAllGone();
                footTheEnd.setVisibility(View.VISIBLE);
                break;
        }
    }

    //全部Foot不可见
    public void setAllGone() {
        if (footLoadView != null) {
            footLoadView.setVisibility(View.GONE);
        }
        if (footTheEnd != null) {
            footTheEnd.setVisibility(View.GONE);
        }
    }
}
