package cn.cjsj.im.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSONObject;

import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.EarlyArrvalRank;

/**
 * Created by LuoYang on 2018/11/26 14:43
 * 早到排行榜适配器
 */
public class EarlyArrvalRankAdapter extends BGARecyclerViewAdapter<EarlyArrvalRank> {

    public EarlyArrvalRankAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.early_arrval_rank_model);
    }


    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, EarlyArrvalRank model) {
        if (position == 0) {
            helper.getImageView(R.id.early_arrval_rank_no_img).setVisibility(View.VISIBLE);
            helper.setImageResource(R.id.early_arrval_rank_no_img, R.mipmap.early_rank_no1);
            helper.getTextView(R.id.early_arrval_rank_no_tv).setVisibility(View.GONE);
            helper.setTextColorRes(R.id.early_arrval_rank_model_time,R.color.bg_yellow_color_deep);
        } else if (position == 1) {
            helper.getImageView(R.id.early_arrval_rank_no_img).setVisibility(View.VISIBLE);
            helper.setImageResource(R.id.early_arrval_rank_no_img, R.mipmap.early_rank_no2);
            helper.getTextView(R.id.early_arrval_rank_no_tv).setVisibility(View.GONE);
            helper.setTextColorRes(R.id.early_arrval_rank_model_time,R.color.bg_yellow_color_deep);
        } else if (position == 2) {
            helper.getImageView(R.id.early_arrval_rank_no_img).setVisibility(View.VISIBLE);
            helper.setImageResource(R.id.early_arrval_rank_no_img, R.mipmap.early_rank_no3);
            helper.getTextView(R.id.early_arrval_rank_no_tv).setVisibility(View.GONE);
            helper.setTextColorRes(R.id.early_arrval_rank_model_time,R.color.bg_yellow_color_deep);
        } else {
            helper.getImageView(R.id.early_arrval_rank_no_img).setVisibility(View.GONE);
            helper.getTextView(R.id.early_arrval_rank_no_tv).setVisibility(View.VISIBLE);
            helper.setText(R.id.early_arrval_rank_no_tv, String.valueOf(position + 1));
        }

        if (model.getName().length() > 2) {
            String name = model.getName().substring(1, 3);
            helper.setText(R.id.early_arrval_headimg, name);
        } else {
            helper.setText(R.id.early_arrval_headimg, model.getName());

        }
        helper.setText(R.id.early_arrval_name, model.getName());
        helper.setText(R.id.early_arrval_org_name, model.getDepartment());
        helper.setText(R.id.early_arrval_rank_model_time, model.getTime());

    }
}
