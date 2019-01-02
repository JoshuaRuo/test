package cn.cjsj.im.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.cjsj.im.App;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.ProjectDetailMemberResponse;
import cn.cjsj.im.gty.bean.ProjectDetailResponse;
import cn.cjsj.im.gty.http.HttpMethods;
import cn.cjsj.im.gty.subscribers.ProgressSubscriber;
import cn.cjsj.im.gty.subscribers.SubscriberOnNextErrorListener;
import cn.cjsj.im.ui.adapter.ProjectMemberAdapter;
import rx.functions.Action1;

/**
 * Created by LuoYang on 2018/8/6 10:28
 * 项目成员
 */
public class ProjectMemberActivity extends BaseActivity {
    @Bind(R.id.member_recycler)
    RecyclerView mRv;

    @Bind(R.id.member_total_title)
    TextView mTotalTV;


    private Button mBackBtn;

    private ProjectMemberAdapter mAdapter;
    private List<ProjectDetailMemberResponse> mList;
    private Intent mIntent;
    private long mId;
    private String mToken;

    private SubscriberOnNextErrorListener mSubscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_member);
        ButterKnife.bind(this);
        setTitle("项目成员");
        mToken = App.getInstance().getToken();
        mIntent = getIntent();
        mId = mIntent.getLongExtra("id", 0);

        mBackBtn = getHeadLeftButton();
        RxView.clicks(mBackBtn)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });

        mRv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ProjectMemberAdapter(mRv);
        mRv.setAdapter(mAdapter);
        mAdapter.setData(mList);

        mSubscriber = new SubscriberOnNextErrorListener<ProjectDetailResponse>() {
            @Override
            public void onNext(ProjectDetailResponse model) {
                mList = model.getMemberList();
                mAdapter.setData(mList);
                mAdapter.notifyDataSetChanged();

                if (mList != null) {
                    mTotalTV.setText("共" + mList.size() + "人");
                }else {
                    mTotalTV.setText("共0人");
                }
            }

            @Override
            public void onError(String error) {

            }
        };

        getMemberList(mToken, mId);
    }


    /**
     * 获取成员列表
     *
     * @param token
     * @param id
     */
    private void getMemberList(String token, long id) {
        HttpMethods.getInstance().getMemberList(new ProgressSubscriber<ProjectDetailResponse>(mSubscriber, this, false), token, id);
    }


}
