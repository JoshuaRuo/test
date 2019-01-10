package cn.cjsj.im.ui.adapter;

import android.support.v7.widget.RecyclerView;


import java.util.Calendar;
import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.NewsListGenericityModel;

/**
 * Created by LuoYang on 2019/1/9 10:29
 * 消息列表适配器
 */
public class NewsList203Adapter extends BGARecyclerViewAdapter<NewsListGenericityModel> {
    private int mThisYear;
    private int mThisMonth;
    private int mToday;
    private StringBuilder mSb;

    public NewsList203Adapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.news_203_list_model);
        getToday();
    }


    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, NewsListGenericityModel model) {
        if (model.getCreateTime() != null) {
            if (mThisYear == model.getCreateTime().getInteger("year")
                    && mThisMonth == model.getCreateTime().getInteger("monthValue")
                    && mToday == model.getCreateTime().getInteger("dayOfMonth")) {

                helper.setText(R.id.new_list_time_tv, "今天" + model.getCreateTime().getInteger("hour") + ":" + model.getCreateTime().getInteger("minute"));
            } else if (mThisYear == model.getCreateTime().getInteger("year")
                    && mThisMonth == model.getCreateTime().getInteger("monthValue")
                    && (mToday - model.getCreateTime().getInteger("dayOfMonth") == 1)) {
                helper.setText(R.id.new_list_time_tv, "昨天" + model.getCreateTime().getInteger("hour") + ":" + model.getCreateTime().getInteger("minute"));
            } else {
                helper.setText(R.id.new_list_time_tv, model.getCreateTime().getInteger("monthValue") + "月" + model.getCreateTime().getInteger("dayOfMonth") + "日");
            }
        }
        mSb = new StringBuilder();
        helper.setText(R.id.news_list_title_tv, model.getTitle());
        helper.setText(R.id.news_list_title_value_tv, model.getSubtitle());
        if (model.getContent() != null) {
            if (model.getContent().length != 0) {
                for (int i = 0; i < model.getContent().length; i++) {
                    mSb.append(model.getContent()[i]);
                    if (i < model.getContent().length) {
                        mSb.append("\n");
                    }
                }
                helper.setText(R.id.news_list_value_tv, mSb.toString());
            }
        }
    }


    public void getToday() {
        Calendar now = Calendar.getInstance();
        mThisYear = now.get(Calendar.YEAR);
        mThisMonth = now.get(Calendar.MONTH) + 1;
        mToday = now.get(Calendar.DAY_OF_MONTH);
    }
}
