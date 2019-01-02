package cn.cjsj.im.ui.adapter;


import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.cjsj.im.R;

/**
 * Created by LuoYang on 2018/8/8 09:58
 * 参与人列表适配器
 */
public class ParticipantAdapter extends BGARecyclerViewAdapter<JSONObject> {

    public ParticipantAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.participant_list_model);
    }


    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, JSONObject model) {
        helper.setText(R.id.participant_model_name_title, model.getString("name"));
        helper.setText(R.id.participant_name_tv, model.getString("name"));
        helper.setText(R.id.participant_progress, model.getString("progress"));
        setStatus(helper, model.getString("status"));
    }


    private void setStatus(BGAViewHolderHelper helper, String status) {
        TextView tv = helper.getTextView(R.id.participant_status);
        switch (status) {
            case "1":
                tv.setText("进行中");
                tv.setTextColor(ContextCompat.getColor(mContext, R.color.color_2293ff));
                break;

            case "2":
                tv.setText("已延迟");
                tv.setTextColor(ContextCompat.getColor(mContext, R.color.color_fc472b));
                break;

            case "3":
                tv.setText("已完成");
                tv.setTextColor(ContextCompat.getColor(mContext, R.color.color_00cc05));
                break;


        }
    }

}
