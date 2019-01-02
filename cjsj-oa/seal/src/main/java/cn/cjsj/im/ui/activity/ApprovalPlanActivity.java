package cn.cjsj.im.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jakewharton.rxbinding.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.cjsj.im.App;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.ApprovalPlanModel;
import cn.cjsj.im.gty.bean.ApprovalPlanResponse;
import cn.cjsj.im.gty.http.HttpMethods;
import cn.cjsj.im.gty.subscribers.ProgressSubscriber;
import cn.cjsj.im.gty.subscribers.SubscriberOnNextErrorListener;
import cn.cjsj.im.ui.adapter.ApprovalPlanAdapter;
import rx.functions.Action1;

/**
 * Created by LuoYang on 2018/11/29 16:41
 * 审批进度
 */
public class ApprovalPlanActivity extends BaseActivity {
    @Bind(R.id.approval_plan_rv)
    RecyclerView mRv;

    private Intent mIntent;

    private long mId;
    private String mToken;
    private SubscriberOnNextErrorListener mSubscriber;
    private static final int SET_DATA = 105;
    private ApprovalPlanAdapter mApprovalPlanAdapter;
    private List<ApprovalPlanModel> mList;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SET_DATA:
                    initView();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_plan);
        ButterKnife.bind(this);
        mIntent = getIntent();
        mId = mIntent.getLongExtra("id", 0);
        mToken = App.getInstance().getToken();
        setTitle("审批进度");
        mHeadRightText.setText("审批历史");
        mHeadRightText.setVisibility(View.VISIBLE);
        mSubscriber = new SubscriberOnNextErrorListener<ApprovalPlanResponse>() {
            @Override
            public void onNext(ApprovalPlanResponse list) {
                mList = list.getTaskOpinionList();
              mHandler.sendEmptyMessage(SET_DATA);
            }

            @Override
            public void onError(String error) {

            }
        };

        getApprovalPlan(mToken, mId);

        RxView.clicks(mHeadRightText)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                            Intent intent = new Intent(ApprovalPlanActivity.this,ApprovalHistoryActivity.class);
                            intent.putExtra("id",mId);
                            startActivity(intent);
                    }
                });
    }

    private void initView() {
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mApprovalPlanAdapter = new ApprovalPlanAdapter(this, mList);
        mRv.setAdapter(mApprovalPlanAdapter);
        mApprovalPlanAdapter.notifyDataSetChanged();
    }

    private void getApprovalPlan(String token, long businessKey) {
        HttpMethods.getInstance().getApprovalPlan(new ProgressSubscriber<ApprovalPlanResponse>(mSubscriber, this, false), token, businessKey);
    }

}
