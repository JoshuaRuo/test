package cn.cjsj.im.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.cjsj.im.App;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.ReWorkDetailResponse;
import cn.cjsj.im.gty.http.HttpMethods;
import cn.cjsj.im.gty.subscribers.ProgressSubscriber;
import cn.cjsj.im.gty.subscribers.SubscriberOnNextErrorListener;
import rx.functions.Action1;

/**
 * Created by LuoYang on 2018/11/29 15:28
 * 补卡详情
 */
public class ReWorkAttDetailActivity extends BaseActivity {
    @Bind(R.id.rework_detail_status_tv)
    TextView mStatus;

    @Bind(R.id.rework_detial_time)
    TextView mTime;

    @Bind(R.id.rework_detail_app_name)
    TextView mAppName;

    @Bind(R.id.rework_detail_type_tv)
    TextView mType;

    @Bind(R.id.rework_detail_start_time)
    TextView mTopTime;

    @Bind(R.id.rework_detail_end_time)
    TextView mBottomTime;

    @Bind(R.id.rework_detail_end_layout)
    LinearLayout mEndLayout;

    @Bind(R.id.rework_detail_reason)
    TextView mReason;

    @Bind(R.id.rework_detail_headimg)
    TextView mHeadImg;

    @Bind(R.id.rework_detail_name)
    TextView mName;

    @Bind(R.id.rework_detail_app_todo)
    TextView mTodo;

    private Intent mIntent;
    private long mId;
    private SubscriberOnNextErrorListener mSubscriber;
    private String mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_work_att_detail);
        ButterKnife.bind(this);
        setTitle("补卡详情");
        mToken = App.getInstance().getToken();
        mIntent = getIntent();
        mId = mIntent.getLongExtra("id", 0);
        mHeadRightText.setText("审批进度");
        mHeadRightText.setVisibility(View.VISIBLE);
        mSubscriber = new SubscriberOnNextErrorListener<ReWorkDetailResponse>() {
            @Override
            public void onNext(ReWorkDetailResponse model) {
                mId = model.getCheckRemakeCard().getId();
                switch (model.getStatus()) {
                    case 1:
                        mStatus.setText("待审批");
                        mStatus.setTextColor(ContextCompat.getColor(mContext, R.color.color_2293ff));
                        mAppName.setText(model.getExecutor());
                        mTodo.setText("提交给了 " + model.getNextExecutor());
                        break;
                    case 2:
                        mStatus.setText("已完成");
                        mStatus.setTextColor(ContextCompat.getColor(mContext, R.color.color_04b600));
                        mAppName.setText(model.getNextExecutor());
                        mTodo.setText("同意了您的申请");
                        break;
                    case 3:
                        mStatus.setText("已驳回");
                        mStatus.setTextColor(ContextCompat.getColor(mContext, R.color.color_FD1575));
                        mAppName.setText(model.getNextExecutor());
                        mTodo.setText("驳回了您的申请");
                        break;
                    default:
                        break;
                }

                mTime.setText(model.getDate());
                mType.setText(model.getCheckRemakeCard().getTypeStr());

                if ("旷工补卡".equals(model.getCheckRemakeCard().getTypeStr())) {
                    mTopTime.setText(model.getCheckRemakeCard().getTimeStr());
                    mBottomTime.setText(model.getCheckRemakeCard().getTimeEndStr());
                    mEndLayout.setVisibility(View.VISIBLE);
                } else {
                    if (model.getCheckRemakeCard().getTimeStr() != null) {
                        mTopTime.setText(model.getCheckRemakeCard().getTimeStr());
                    } else {
                        mTopTime.setText(model.getCheckRemakeCard().getTimeEndStr());
                    }
                    mEndLayout.setVisibility(View.GONE);
                }

                mReason.setText(model.getCheckRemakeCard().getReason());
                if (model.getNextExecutor().length() > 2) {
                    String name = model.getNextExecutor().substring(1, 3);
                    mHeadImg.setText(name);
                } else {
                    mHeadImg.setText(model.getNextExecutor());
                }
                mName.setText(model.getNextExecutor());
            }

            @Override
            public void onError(String error) {

            }
        };
        getDetail(mToken, mId);
        RxView.clicks(mHeadRightText)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(ReWorkAttDetailActivity.this, ApprovalPlanActivity.class);
                        intent.putExtra("id", mId);
                        startActivity(intent);
                    }
                });
    }

    private void getDetail(String token, long id) {
        HttpMethods.getInstance().getReWorkDetail(new ProgressSubscriber<ReWorkDetailResponse>(mSubscriber, this, false), token, id);
    }
}
