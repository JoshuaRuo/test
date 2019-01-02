package cn.cjsj.im.ui.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.ReWorkHistoryResponse;

/**
 * Created by LuoYang on 2018/11/29 10:14
 * 补卡历史适配器
 */
public class ReWorkHisAdapter extends BGARecyclerViewAdapter<ReWorkHistoryResponse> {
    public ReWorkHisAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.re_work_history_model);
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, ReWorkHistoryResponse model) {
        helper.setText(R.id.re_his_type_name_tv, model.getTypeStr());
        helper.setText(R.id.re_his_create_time_tv, model.getCreateTimeStr());
        if ("旷工补卡".equals(model.getTypeStr())) {
            helper.setText(R.id.re_his_time_tv, "补卡班次"  + model.getTimeStr() + "\n" + "补卡班次"  +  model.getTimeEndStr());
        } else {
            if (model.getTimeStr() != null){
                helper.setText(R.id.re_his_time_tv, "补卡班次"  + model.getTimeStr());
            }else {
                helper.setText(R.id.re_his_time_tv, "补卡班次"  + model.getTimeEndStr());
            }
        }

        switch (model.getApprovals()) {
            case 1:
                helper.setText(R.id.re_his_status_tv,"待审批");
                helper.setTextColor(R.id.re_his_status_tv, ContextCompat.getColor(mContext,R.color.color_2293ff));
                break;
            case 2:
                helper.setText(R.id.re_his_status_tv,"已完成");
                helper.setTextColor(R.id.re_his_status_tv, ContextCompat.getColor(mContext,R.color.color_04b600));
                break;
            case 3:
                helper.setText(R.id.re_his_status_tv,"已驳回");
                helper.setTextColor(R.id.re_his_status_tv, ContextCompat.getColor(mContext,R.color.color_FD1575));
                break;
            default:
                break;
        }


    }
}
