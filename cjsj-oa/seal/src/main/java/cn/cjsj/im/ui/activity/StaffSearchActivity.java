package cn.cjsj.im.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.cjsj.im.App;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.DepartmentStaffBean;
import cn.cjsj.im.gty.bean.SysUserBean;
import cn.cjsj.im.gty.http.HttpMethods;
import cn.cjsj.im.gty.subscribers.ProgressSubscriber;
import cn.cjsj.im.gty.subscribers.SubscriberOnNextErrorListener;
import cn.cjsj.im.server.widget.ClearWriteEditText;
import cn.cjsj.im.ui.adapter.SearchStaffAdapter;
import rx.functions.Action1;

/**
 * Created by LuoYang on 2018/11/7 09:54
 * 搜索员工
 */
public class StaffSearchActivity extends AppCompatActivity {
    @Bind(R.id.staff_search_btn_left)
    Button mBc;

    @Bind(R.id.staff_search_et)
    ClearWriteEditText mSearchEt;

    @Bind(R.id.staff_search_recyclerview)
    RecyclerView mRecyclerView;

    private SearchStaffAdapter mAdapter;

    private String mToken;

    private SubscriberOnNextErrorListener mSearchSub;

    private List<DepartmentStaffBean> mList;
    private String mSearchParameter;

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            searchStaff(mToken,mSearchParameter);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_staff_search);
        ButterKnife.bind(this);
        mToken = App.getInstance().getToken();

        RxView.clicks(mBc)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });

        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (mRunnable != null){
                    mHandler.removeCallbacks(mRunnable);
                }
                if (!s.toString().isEmpty() && !"".equals(s.toString())) {
                    mSearchParameter = s.toString();

                    mHandler.postDelayed(mRunnable,1000);
                }
            }
        };

        mSearchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return true;
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SearchStaffAdapter(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter.getHeaderAndFooterAdapter());

        mAdapter.setOnRVItemClickListener(new BGAOnRVItemClickListener() {
            @Override
            public void onRVItemClick(ViewGroup parent, View itemView, int position) {
                Intent intent = new Intent(StaffSearchActivity.this,StaffDetailActivity.class);
                intent.putExtra("userId",mList.get(position).getUserId());
                intent.putExtra("deName",mList.get(position).getOrgName());
                startActivity(intent);
            }
        });

        mSearchSub = new SubscriberOnNextErrorListener<List<DepartmentStaffBean>>() {
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
        mSearchEt.addTextChangedListener(tw);
    }


    public void searchStaff(String token, String searchP) {
        HttpMethods.getInstance().searchStaff(new ProgressSubscriber<List<DepartmentStaffBean>>(mSearchSub, this, false), token, searchP);
    }


}
