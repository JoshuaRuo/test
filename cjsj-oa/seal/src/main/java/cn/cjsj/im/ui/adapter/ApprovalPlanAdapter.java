package cn.cjsj.im.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.ApprovalPlanModel;
import cn.cjsj.im.utils.DensityUtil;

/**
 * Created by LuoYang on 2018/11/29 16:45
 * 审批进度适配器
 */
public class ApprovalPlanAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<ApprovalPlanModel> mList;

    public ApprovalPlanAdapter(Context context, List<ApprovalPlanModel> list) {
        this.mContext = context;
        this.mList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.approval_plan_adapter, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        if (mList.get(position).getExeFullname().length() > 2) {
            String name = mList.get(position).getExeFullname().substring(1, 3);
            viewHolder.mHeadImg.setText(name);
        } else {
            viewHolder.mHeadImg.setText(mList.get(position).getExeFullname());
        }
        if (mList.get(position).getStatus().contains("驳回")) {
            viewHolder.mHeadImg.setBackgroundResource(R.drawable.approval_head_bluebg);
        } else {
            viewHolder.mHeadImg.setBackgroundResource(R.drawable.approval_head_gray_bg);
        }
        if (position == 0) {
            viewHolder.mFromType.setText("发起人");
            viewHolder.mStatus.setText("发起申请");
            viewHolder.mStatus.setTextColor(ContextCompat.getColor(mContext, R.color.color_66));
            viewHolder.mDot.setImageResource(R.drawable.approval_complete_icon);
            viewHolder.line.setBackgroundResource(R.color.color_2293ff);
        } else {
            viewHolder.mFromType.setText(mList.get(position).getTaskName());
            if (mList.get(position).getStatus().contains("同意")){
                viewHolder.mStatus.setText("同意");
                viewHolder.mStatus.setTextColor(ContextCompat.getColor(mContext,R.color.color_04b600));
                viewHolder.line.setBackgroundResource(R.color.color_2293ff);
                viewHolder.mDot.setImageResource(R.drawable.approval_complete_icon);
            }else if (mList.get(position).getStatus().contains("待审批")){
                viewHolder.mStatus.setText("待审批");
                viewHolder.mDot.setImageResource(R.drawable.ongoing);
                viewHolder.mStatus.setTextColor(ContextCompat.getColor(mContext,R.color.color_2293ff));
            }else if (mList.get(position).getStatus().contains("驳回")){
                viewHolder.mStatus.setText("驳回");
                viewHolder.mDot.setImageResource(R.drawable.ongoing);
                viewHolder.mStatus.setTextColor(ContextCompat.getColor(mContext,R.color.color_FD1575));
            }else {
                viewHolder.mDot.setImageResource(R.drawable.circle_gray);
            }
        }
        viewHolder.mName.setText(mList.get(position).getExeFullname());
        viewHolder.setPosition(position);
    }

    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        } else {
            return 0;
        }

    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private View line;
        private TextView mHeadImg;
        private TextView mFromType;
        private TextView mName;
        private TextView mStatus;
        private RelativeLayout layout;
        private ImageView mDot;

        public ViewHolder(View view) {
            super(view);
            line = view.findViewById(R.id.approval_plan_progress_line);
            mHeadImg = view.findViewById(R.id.approval_plan_headimg);
            mFromType = view.findViewById(R.id.approval_plan_from_type);
            mName = view.findViewById(R.id.approval_plan_name);
            mStatus = view.findViewById(R.id.approval_plan_status);
            layout = view.findViewById(R.id.approval_plan_layout);
            mDot = view.findViewById(R.id.approval_plan_progress_icon);
        }


        public void setPosition(int position) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) line.getLayoutParams();

            if (position == 0) {
                layoutParams.setMargins(DensityUtil.dip2px(line.getContext(), 20), DensityUtil.dip2px(line.getContext(), 18), 0,
                        DensityUtil.dip2px(line.getContext(), -30));
                layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.approval_plan_layout);
                layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.approval_plan_layout);

            } else {
                layoutParams.setMargins(DensityUtil.dip2px(line.getContext(), 20), DensityUtil.dip2px(line.getContext(), -150), 0, DensityUtil.dip2px(line.getContext(), 20));
                layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.approval_plan_layout);
                layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.approval_plan_layout);
            }
            line.setLayoutParams(layoutParams);


        }
    }
}
