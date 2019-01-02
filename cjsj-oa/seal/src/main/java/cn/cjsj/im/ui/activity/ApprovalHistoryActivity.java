package cn.cjsj.im.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.cjsj.im.App;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.ApprovalPlanModel;
import cn.cjsj.im.gty.bean.ApprovalPlanResponse;
import cn.cjsj.im.gty.http.HttpMethods;
import cn.cjsj.im.gty.subscribers.ProgressSubscriber;
import cn.cjsj.im.gty.subscribers.SubscriberOnNextErrorListener;
import cn.cjsj.im.ui.adapter.ApprovalHistoryAdapter;

/**
 * Created by LuoYang on 2018/11/30 10:43
 * 审批历史
 */
public class ApprovalHistoryActivity extends BaseActivity {
    @Bind(R.id.approval_his_rv)
    RecyclerView mRv;

    private SubscriberOnNextErrorListener mSubscriber;
    private String mToken;
    private Intent mIntent;
    private long mId;
    private ApprovalHistoryAdapter mAdapter;
    private List<ApprovalPlanModel> mList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_history);
        ButterKnife.bind(this);
        setTitle("审批历史");
        mToken = App.getInstance().getToken();
        mIntent = getIntent();
        mId = mIntent.getLongExtra("id", 0);
        mSubscriber = new SubscriberOnNextErrorListener<ApprovalPlanResponse>() {
            @Override
            public void onNext(ApprovalPlanResponse model) {
                mList = model.getTaskOpinionList();
                initView(model.getSendToUser());
            }

            @Override
            public void onError(String error) {

            }
        };

        getApprovalHis(mToken, mId);

    }

    private void initView(String who) {
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ApprovalHistoryAdapter(mRv,who);
        mRv.setAdapter(mAdapter);
        mAdapter.setData(mList);
        mAdapter.notifyDataSetChanged();
    }

    public void getApprovalHis(String token, long key) {
        HttpMethods.getInstance().getApprovalHistory(new ProgressSubscriber<ApprovalPlanResponse>(mSubscriber, this, false), token, key);
    }
}
