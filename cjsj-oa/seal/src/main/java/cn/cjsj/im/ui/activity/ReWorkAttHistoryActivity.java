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
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.cjsj.im.App;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.ReWorkHistoryResponse;
import cn.cjsj.im.gty.http.HttpMethods;
import cn.cjsj.im.gty.subscribers.ProgressSubscriber;
import cn.cjsj.im.gty.subscribers.SubscriberOnNextErrorListener;
import cn.cjsj.im.ui.adapter.ReWorkHisAdapter;

/**
 * Created by LuoYang on 2018/11/29 10:10
 * 补卡历史
 */
public class ReWorkAttHistoryActivity extends BaseActivity {
    @Bind(R.id.re_work_history_rv)
    RecyclerView mRv;

    private ReWorkHisAdapter mAdapter;

    private SubscriberOnNextErrorListener mSubscriber;

    private String mToken;
    private List<ReWorkHistoryResponse> mList;
    private static final int SET_DATA = 102;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SET_DATA:
                    mAdapter.setData(mList);
                    mAdapter.notifyDataSetChanged();
                break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_work_history);
        ButterKnife.bind(this);
        setTitle("补卡历史");
        mToken = App.getInstance().getToken();
        initView();

        mSubscriber = new SubscriberOnNextErrorListener<List<ReWorkHistoryResponse>>() {
            @Override
            public void onNext(List<ReWorkHistoryResponse> list) {
                mList = list;
                mHandler.sendEmptyMessage(SET_DATA);
            }

            @Override
            public void onError(String error) {

            }
        };


        getHisList(mToken, 0, 100, 1);
    }


    private void initView() {
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ReWorkHisAdapter(mRv);
        mRv.setAdapter(mAdapter);
        mAdapter.setOnRVItemClickListener(new BGAOnRVItemClickListener() {
            @Override
            public void onRVItemClick(ViewGroup parent, View itemView, int position) {
                Intent intent = new Intent(ReWorkAttHistoryActivity.this,ReWorkAttDetailActivity.class);
                intent.putExtra("id",mList.get(position).getId());
                startActivity(intent);
            }
        });
    }

    private void getHisList(String token, int type, int pageSize, int currentPage) {
        HttpMethods.getInstance().getReWorkHisList(new ProgressSubscriber<List<ReWorkHistoryResponse>>(mSubscriber, this, false), token, type, pageSize, currentPage);
    }
}
