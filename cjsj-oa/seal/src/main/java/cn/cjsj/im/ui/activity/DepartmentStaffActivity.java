package cn.cjsj.im.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.cjsj.im.App;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.DepartmentStaffBean;
import cn.cjsj.im.gty.http.HttpMethods;
import cn.cjsj.im.gty.subscribers.ProgressSubscriber;
import cn.cjsj.im.gty.subscribers.SubscriberOnNextErrorListener;
import cn.cjsj.im.ui.adapter.DepartmentStaffAdapter;
import cn.cjsj.im.ui.adapter.ProjectAdapter;
import cn.cjsj.im.ui.widget.DrawableCenterTextView;
import rx.functions.Action1;

/**
 * Created by LuoYang on 2018/11/5 14:48
 * 部门员工列表
 */
public class DepartmentStaffActivity extends BaseActivity {

    private String mToken;
    private SubscriberOnNextErrorListener mStaffSub;
    private Intent mIntent;
    private long orgId;
    private String mDeName;
    private DepartmentStaffAdapter mAdapter;
    private List<DepartmentStaffBean> mList;

    @Bind(R.id.department_staff_recyclerview)
    RecyclerView mRecyclerView;

    @Bind(R.id.department_staff_search_bt)
    DrawableCenterTextView mDrawableCenterTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_staff);
        ButterKnife.bind(this);
        mToken = App.getInstance().getToken();
        mIntent = getIntent();
        orgId = mIntent.getLongExtra("orgId",0);
        mDeName = mIntent.getStringExtra("deName");
        setTitle(mDeName);

        RxView.clicks(mDrawableCenterTextView)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(DepartmentStaffActivity.this, StaffSearchActivity.class));
                    }
                });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new DepartmentStaffAdapter(mRecyclerView,this);
        mRecyclerView.setAdapter(mAdapter.getHeaderAndFooterAdapter());

        mAdapter.setOnRVItemClickListener(new BGAOnRVItemClickListener() {
            @Override
            public void onRVItemClick(ViewGroup parent, View itemView, int position) {
                    Intent intent = new Intent(DepartmentStaffActivity.this,StaffDetailActivity.class);
                    intent.putExtra("userId",mList.get(position).getUserId());
                    intent.putExtra("deName",mDeName);
                    startActivity(intent);
            }
        });


        mStaffSub = new SubscriberOnNextErrorListener<List<DepartmentStaffBean>>() {
            @Override
            public void onNext(List<DepartmentStaffBean> list) {
                mList = list;
                mAdapter.setData(list);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {

            }
        };
        getDepartmentStaff(mToken,orgId);
    }


    private void getDepartmentStaff(String token,long orgId){
        HttpMethods.getInstance().getDepartmentStaff(new ProgressSubscriber<List<DepartmentStaffBean>>(mStaffSub,this,false),token,orgId);
    }
}
