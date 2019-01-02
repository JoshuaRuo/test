package cn.cjsj.im.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.ApprovalPlanModel;
import cn.cjsj.im.gty.bean.ApprovalPlanResponse;

/**
 * Created by LuoYang on 2018/11/30 11:00
 * 审批历史
 */
public class ApprovalHistoryAdapter extends BGARecyclerViewAdapter<ApprovalPlanModel> {
    private String who;

    public ApprovalHistoryAdapter(RecyclerView recyclerView, String toWho) {
        super(recyclerView, R.layout.approval_his_adapter);
        this.who = toWho;
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, ApprovalPlanModel model) {
        if (model.getExeFullname().length() > 2) {
            String name = model.getExeFullname().substring(1, 3);
            helper.setText(R.id.approval_his_model_headimg, name);
        } else {
            helper.setText(R.id.approval_his_model_headimg, model.getExeFullname());
        }
        helper.setText(R.id.approval_his_model_name, model.getExeFullname());
        if (position == 0) {
            helper.setText(R.id.approval_his_do_what, "提交申请给");
            helper.setText(R.id.approval_his_to_who, who);
        } else {
            if (model.getStatus().contains("同意")) {
                helper.setText(R.id.approval_his_do_what, "同意该申请");
            } else if (model.getStatus().contains("驳回")) {
                helper.setText(R.id.approval_his_do_what, "驳回该申请");
            }
        }
        helper.setText(R.id.approval_his_model_time,model.getStartTimeStr());
        if (position == getItemCount() -1){
            helper.setVisibility(R.id.approval_his_model_line, View.GONE);
        }
    }
}
