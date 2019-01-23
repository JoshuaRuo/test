package cn.cjsj.im.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.CheckResponse;
import cn.cjsj.im.utils.DensityUtil;

/**
 * Created by LuoYang on 2018/11/9 10:23
 * 打卡页面适配器
 */
public class CheckWorkAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private CheckResponse mCheckResponse;
    private boolean isShowDown = false;
    private String mType;
    private String mAddress;
    public CheckWorkAdapter(Context context, CheckResponse checkResponse,String address) {
        this.mContext = context;
        this.mCheckResponse = checkResponse;
        this.mAddress = address;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.check_work_model, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;

        switch (position) {
            case 0:
                viewHolder.titleTime.setText(mCheckResponse.getCheckLogResult().getClassName() + "  " + "上班时间"
                        + " " + mCheckResponse.getCheckClass().getString("startTimeStr"));
                setView(viewHolder, position);
                break;
            case 1:
                viewHolder.titleTime.setText(mCheckResponse.getCheckLogResult().getClassName() + "  " + "下班时间"
                        + " " + mCheckResponse.getCheckClass().getString("endTimeStr"));

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 60, 0, 0);
                viewHolder.titleLayout.setLayoutParams(params);
//                setView(viewHolder, position);

                if (mCheckResponse.getCheckLogResult().getOnTime() == null
                        && mCheckResponse.getCheckLogResult().getOnAddress() == null
                        && mCheckResponse.getCheckLogResult().getOnStatus() == null) {
                    isShowDown = false;
                    viewHolder.timeLineDot.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.circle_gray));

                } else {
                    if (mCheckResponse.getCheckLogResult().getOnStatus() != null && mCheckResponse.getCheckLogResult().getOffStatus() != null) {
                        isShowDown = false;
                    } else {
                        isShowDown = true;
                    }
                    viewHolder.timeLineDot.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ongoing));
                    setView(viewHolder, position);
                }


                break;

        }
        viewHolder.setPosition(position);

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View line;
        private TextView titleTime;
        private LinearLayout layoutButton;
        private TextView checkTypeTv;
        private TextClock buttonTime;
        private TextView checkLocation;
        private TextView checkLocationError;
        private TextView checkLocationIn;
        private RelativeLayout titleLayout;
        private TextView checkOnTime;
        private TextView statusNormal, statusLate, statusEarly, statusLack, statusError, statusOutside;
        private LinearLayout statusLayout;
        private TextView mCheckStatusLocationValue;
        private RelativeLayout allLayout;
        private ImageView timeLineDot;
        private TextView clockOutHint;
        private TextView mLeave;

        public ViewHolder(View view) {
            super(view);
            titleLayout = view.findViewById(R.id.check_work_title);
            line = view.findViewById(R.id.check_work_line);
            titleTime = view.findViewById(R.id.txt_date_time);
            layoutButton = view.findViewById(R.id.check_work_button);
            checkTypeTv = view.findViewById(R.id.check_work_button_title);
            buttonTime = view.findViewById(R.id.check_work_button_time);
            checkLocation = view.findViewById(R.id.check_work_location_ok);
            checkLocationError = view.findViewById(R.id.check_work_location_error);
            checkLocationIn = view.findViewById(R.id.check_work_location_in);
            checkOnTime = view.findViewById(R.id.check_work_time_value);
            statusNormal = view.findViewById(R.id.check_status_normal);
            statusLate = view.findViewById(R.id.check_status_late);
            statusEarly = view.findViewById(R.id.check_status_early);
            statusLack = view.findViewById(R.id.check_status_lack);
            statusError = view.findViewById(R.id.check_status_error);
            statusOutside = view.findViewById(R.id.check_status_outside);
            statusLayout = view.findViewById(R.id.check_work_timestatus_layout);
            mCheckStatusLocationValue = view.findViewById(R.id.check_status_location_in);
            allLayout = view.findViewById(R.id.check_all_layout);
            timeLineDot = view.findViewById(R.id.check_work_progress_icon);
            clockOutHint = view.findViewById(R.id.check_onwork_clockout_hint);
            mLeave = view.findViewById(R.id.check_status_leave);

        }

        public void setPosition(int position) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) line.getLayoutParams();

            if (position == 0) {
                layoutParams.setMargins(DensityUtil.dip2px(line.getContext(), 20), DensityUtil.dip2px(line.getContext(), 15), 0,
                        DensityUtil.dip2px(line.getContext(), -150));
                layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.check_work_title);
                layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.check_work_location_ok);

            } else {
                if (isShowDown) {
                    layoutParams.setMargins(DensityUtil.dip2px(line.getContext(), 20), -150, 0, 400);
                } else {
                    layoutParams.setMargins(DensityUtil.dip2px(line.getContext(), 20), -200, 0, 100);
                }

                layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.check_work_title);
                layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.check_work_location_ok);
                line.setVisibility(View.VISIBLE);
            }
            line.setLayoutParams(layoutParams);
        }

    }

    private void setView(ViewHolder viewHolder, int position) {
        switch (position) {
            case 0:
                if (mCheckResponse.getCheckLogResult().getOnStatus() == null) {
                    if (!mCheckResponse.isInRange()) {
                        viewHolder.layoutButton.setVisibility(View.VISIBLE);
                        viewHolder.layoutButton.setBackgroundResource(R.drawable.check_work_button_outsite);
                        viewHolder.layoutButton.setVisibility(View.VISIBLE);
                        viewHolder.checkTypeTv.setText("外勤打卡");
                        viewHolder.checkLocationIn.setVisibility(View.VISIBLE);
                        viewHolder.checkLocationIn.setText("当前位置:" + mAddress);
                        mType = "FIELD_CLOCK";
                        if ("外勤".equals(mCheckResponse.getCheckLogResult().getOffStatus())) {
                            viewHolder.statusOutside.setVisibility(View.VISIBLE);
                            viewHolder.mCheckStatusLocationValue.setVisibility(View.VISIBLE);
                            viewHolder.mCheckStatusLocationValue.setText(mCheckResponse.getCheckLogResult().getOffAddress());
                        }
                        if (mCheckResponse.getCheckLogResult().getOffTime() == null) {
                            viewHolder.checkOnTime.setText("打卡时间 无");
                        } else {
                            viewHolder.checkOnTime.setText("打卡时间" + " " + mCheckResponse.getCheckLogResult().getOffTime());
                        }
                    } else {
                        mType = "PUNCHING_TIME_CARD";
                        viewHolder.layoutButton.setVisibility(View.VISIBLE);
                        viewHolder.timeLineDot.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ongoing));
                        viewHolder.statusLayout.setVisibility(View.GONE);
                        viewHolder.mCheckStatusLocationValue.setVisibility(View.GONE);
                        viewHolder.layoutButton.setBackgroundResource(R.drawable.check_work_button_normal);
                        viewHolder.checkTypeTv.setText("上班打卡");
                        viewHolder.checkLocation.setVisibility(View.VISIBLE);
                    }
                } else {
                    viewHolder.layoutButton.setVisibility(View.GONE);
                    viewHolder.timeLineDot.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.circle_gray));
                    viewHolder.statusLayout.setVisibility(View.VISIBLE);
                    if (mCheckResponse.getCheckLogResult().getOnTime() == null) {
                        viewHolder.checkOnTime.setText("打卡时间 无");
                    } else {
                        viewHolder.checkOnTime.setText("打卡时间" + " " + mCheckResponse.getCheckLogResult().getOnTime());
                    }
                    if ("正常".equals(mCheckResponse.getCheckLogResult().getOnStatus())) {
                        viewHolder.statusNormal.setVisibility(View.VISIBLE);
                        viewHolder.mCheckStatusLocationValue.setVisibility(View.VISIBLE);
                        viewHolder.mCheckStatusLocationValue.setText(mCheckResponse.getCheckLogResult().getOnAddress());
                    } else if ("迟到".equals(mCheckResponse.getCheckLogResult().getOnStatus())) {
                        viewHolder.statusLate.setVisibility(View.VISIBLE);
                        viewHolder.mCheckStatusLocationValue.setVisibility(View.VISIBLE);
                        viewHolder.mCheckStatusLocationValue.setText(mCheckResponse.getCheckLogResult().getOnAddress());
                    } else if ("早退".equals(mCheckResponse.getCheckLogResult().getOnStatus())) {
                        viewHolder.statusEarly.setVisibility(View.VISIBLE);
                        viewHolder.mCheckStatusLocationValue.setVisibility(View.VISIBLE);
                        viewHolder.mCheckStatusLocationValue.setText(mCheckResponse.getCheckLogResult().getOnAddress());
                    } else if ("缺卡".equals(mCheckResponse.getCheckLogResult().getOnStatus())) {
                        viewHolder.statusLack.setVisibility(View.VISIBLE);
                        viewHolder.mCheckStatusLocationValue.setVisibility(View.GONE);
                    } else if ("旷工".equals(mCheckResponse.getCheckLogResult().getOnStatus())) {
                        viewHolder.statusError.setVisibility(View.VISIBLE);
                        viewHolder.mCheckStatusLocationValue.setVisibility(View.GONE);
                    } else if ("外勤".equals(mCheckResponse.getCheckLogResult().getOnStatus())) {
                        viewHolder.statusOutside.setVisibility(View.VISIBLE);
                        viewHolder.mCheckStatusLocationValue.setVisibility(View.VISIBLE);
                        viewHolder.mCheckStatusLocationValue.setText(mCheckResponse.getCheckLogResult().getOnAddress());
                    } else if ("请假".equals(mCheckResponse.getCheckLogResult().getOnStatus())) {
                        viewHolder.mLeave.setVisibility(View.VISIBLE);
                        viewHolder.mCheckStatusLocationValue.setVisibility(View.VISIBLE);
                        viewHolder.mCheckStatusLocationValue.setText(mCheckResponse.getCheckLogResult().getOnAddress());
                    }
                }

                viewHolder.layoutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postCheckData.postData(mType, 0);
                    }
                });
                break;
            case 1:
                if (mCheckResponse.getCheckLogResult().getOffStatus() == null) {
                    if (!mCheckResponse.isInRange()) {
                        viewHolder.layoutButton.setVisibility(View.VISIBLE);
                        viewHolder.layoutButton.setBackgroundResource(R.drawable.check_work_button_outsite);
                        viewHolder.checkTypeTv.setText("外勤打卡");
                        viewHolder.checkLocationIn.setVisibility(View.VISIBLE);
                        viewHolder.checkLocationIn.setText("当前位置:" + mAddress);
                        mType = "FIELD_CLOCK";
                        if ("外勤".equals(mCheckResponse.getCheckLogResult().getOffStatus())) {
                            viewHolder.statusOutside.setVisibility(View.VISIBLE);
                            viewHolder.mCheckStatusLocationValue.setVisibility(View.VISIBLE);
                            viewHolder.mCheckStatusLocationValue.setText(mCheckResponse.getCheckLogResult().getOffAddress());
                        }
                        if (mCheckResponse.getCheckLogResult().getOffTime() == null) {
                            viewHolder.checkOnTime.setText("打卡时间 无");
                        } else {
                            viewHolder.checkOnTime.setText("打卡时间" + " " + mCheckResponse.getCheckLogResult().getOffTime());
                        }
                    } else {
                        mType = "PUNCHING_TIME_CARD";
                        viewHolder.layoutButton.setVisibility(View.VISIBLE);
//                        viewHolder.timeLineDot.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ongoing));
                        viewHolder.statusLayout.setVisibility(View.GONE);
                        viewHolder.mCheckStatusLocationValue.setVisibility(View.GONE);
                        viewHolder.layoutButton.setBackgroundResource(R.drawable.check_work_button_normal);
                        viewHolder.checkTypeTv.setText("下班打卡");
                        viewHolder.checkLocation.setVisibility(View.VISIBLE);
                    }
                } else {
                    viewHolder.layoutButton.setVisibility(View.GONE);
//                        viewHolder.timeLineDot.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.circle_gray));
                    viewHolder.statusLayout.setVisibility(View.VISIBLE);
                    if (mCheckResponse.getCheckLogResult().getOffTime() == null) {
                        viewHolder.checkOnTime.setText("打卡时间 无");
                    } else {
                        viewHolder.checkOnTime.setText("打卡时间" + " " + mCheckResponse.getCheckLogResult().getOffTime());
                    }
                    if ("正常".equals(mCheckResponse.getCheckLogResult().getOffStatus())) {
                        viewHolder.statusNormal.setVisibility(View.VISIBLE);
                        viewHolder.mCheckStatusLocationValue.setVisibility(View.VISIBLE);
                        viewHolder.mCheckStatusLocationValue.setText(mCheckResponse.getCheckLogResult().getOffAddress());
                    } else if ("迟到".equals(mCheckResponse.getCheckLogResult().getOffStatus())) {
                        viewHolder.statusLate.setVisibility(View.VISIBLE);
                        viewHolder.mCheckStatusLocationValue.setVisibility(View.VISIBLE);
                        viewHolder.mCheckStatusLocationValue.setText(mCheckResponse.getCheckLogResult().getOffAddress());
                    } else if ("早退".equals(mCheckResponse.getCheckLogResult().getOffStatus())) {
                        viewHolder.statusEarly.setVisibility(View.VISIBLE);
                        viewHolder.mCheckStatusLocationValue.setVisibility(View.VISIBLE);
                        viewHolder.mCheckStatusLocationValue.setText(mCheckResponse.getCheckLogResult().getOffAddress());
                    } else if ("缺卡".equals(mCheckResponse.getCheckLogResult().getOffStatus())) {
                        viewHolder.statusLack.setVisibility(View.VISIBLE);
                        viewHolder.mCheckStatusLocationValue.setVisibility(View.GONE);
                    } else if ("旷工".equals(mCheckResponse.getCheckLogResult().getOffStatus())) {
                        viewHolder.statusError.setVisibility(View.VISIBLE);
                        viewHolder.mCheckStatusLocationValue.setVisibility(View.GONE);
                    } else if ("请假".equals(mCheckResponse.getCheckLogResult().getOffStatus())) {
                        viewHolder.mLeave.setVisibility(View.VISIBLE);
                        viewHolder.mCheckStatusLocationValue.setVisibility(View.VISIBLE);
                        viewHolder.mCheckStatusLocationValue.setText(mCheckResponse.getCheckLogResult().getOnAddress());
                    }

                    if (mCheckResponse.getCheckLogResult().getOffStatus() != null && mCheckResponse.getCheckLogResult().getOnStatus() != null) {
                        viewHolder.timeLineDot.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.circle_gray));
                    } else {
                    }

                    if (mCheckResponse.getCheckLogResult().getOffStatus() != null && !"缺卡".equals(mCheckResponse.getCheckLogResult().getOffStatus())
                            && !"旷工".equals(mCheckResponse.getCheckLogResult().getOffStatus())) {
                        viewHolder.clockOutHint.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.clockOutHint.setVisibility(View.GONE);
                    }
                }

                viewHolder.layoutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postCheckData.postData(mType, 1);
                    }
                });
                break;
        }

    }

    private PostCheckData postCheckData;

    public interface PostCheckData {
        void postData(String type, int operation);
    }

    public void setPostCheckData(PostCheckData postCheckData) {
        this.postCheckData = postCheckData;
    }
}
