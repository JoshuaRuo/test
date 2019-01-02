package cn.cjsj.im.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.cjsj.im.App;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.LogUtils;
import cn.cjsj.im.gty.bean.ProjectListBeanResponse;
import cn.cjsj.im.gty.http.HttpMethods;
import cn.cjsj.im.gty.subscribers.ProgressSubscriber;
import cn.cjsj.im.gty.subscribers.SubscriberOnNextErrorListener;
import cn.cjsj.im.ui.activity.FilterActivity;
import cn.cjsj.im.ui.activity.ProjectDetailActivity;
import cn.cjsj.im.ui.activity.SearchActivity;
import cn.cjsj.im.ui.adapter.ProjectAdapter;
import cn.cjsj.im.ui.widget.DrawableCenterTextView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.functions.Action1;

/**
 * Created by LuoYang on 2018/7/31 15:02
 * 项目组
 */
public class ProjectFragment extends Fragment {

    @Bind(R.id.project_manager_refresh)
    BGARefreshLayout mBGARefreshLayout;

    @Bind(R.id.project_manager_recyclerview)
    RecyclerView mRecyclerView;

    @Bind(R.id.project_search_bt)
    DrawableCenterTextView mSearchEt;

    private View mView;
    private static final int LIST_RESULT = 11;//项目详情

    private ProjectAdapter mProjectAdapter;

    private String mToken;
    private TextView mFilterButton;

    private SubscriberOnNextErrorListener mSubscriber;

    private SubscriberOnNextErrorListener mPutAttentionSubscriber;

    private static final int GET_LIST = 1000;

    private List<ProjectListBeanResponse> mList;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case GET_LIST:
                    mProjectAdapter.setData(mList);
                    mProjectAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null != mView) {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (null != parent) {
                parent.removeView(mView);
            }
        } else {
            mView = inflater.inflate(R.layout.fragment_project, container, false);
        }
        ButterKnife.bind(this, mView);

        initData();
        initView();
        return mView;
    }


    private void initView() {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mProjectAdapter = new ProjectAdapter(mRecyclerView);
        mRecyclerView.setAdapter(mProjectAdapter.getHeaderAndFooterAdapter());

        mProjectAdapter.setOnRVItemClickListener(new BGAOnRVItemClickListener() {
            @Override
            public void onRVItemClick(ViewGroup parent, View itemView, int position) {
                Intent intent = new Intent(getActivity(),ProjectDetailActivity.class);
                intent.putExtra("id",mList.get(position).getId());
                startActivityForResult(intent,LIST_RESULT);
            }
        });
        mProjectAdapter.setProjectAtt(new ProjectAdapter.ProjectAttention() {
            @Override
            public void setAttention(long projectId, String action) {
                putAttention(mToken, projectId, action);
                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).getId() == projectId){
                        if ("del".equals(action)){
                            mList.get(i).setAttention(0);
                        }else if ("add".equals(action)){
                            mList.get(i).setAttention(1);
                        }
                    }
                }
                mProjectAdapter.notifyDataSetChanged();
            }
        });
        mFilterButton = getActivity().findViewById(R.id.home_filtrate);
        RxView.clicks(mFilterButton)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
//                        Intent intent = new Intent(getActivity(), FilterActivity.class);
//                        startActivityForResult(intent, 10);
                    }
                });

        RxView.clicks(mSearchEt)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
//                        startActivity(new Intent(getActivity(), SearchActivity.class));
                    }
                });

    }

    private void initData() {
        mToken = App.getInstance().getToken();

        mSubscriber = new SubscriberOnNextErrorListener<List<ProjectListBeanResponse>>() {
            @Override
            public void onNext(List<ProjectListBeanResponse> list) {
                mList = list;
                mHandler.sendEmptyMessage(GET_LIST);

            }

            @Override
            public void onError(String error) {
                LogUtils.debug("projectList_error:" + error);
            }
        };

        mPutAttentionSubscriber = new SubscriberOnNextErrorListener<String>() {
            @Override
            public void onNext(String arg) {

            }

            @Override
            public void onError(String error) {

            }
        };

        getProjectList(mToken);

    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser){
//            getProjectList(mToken);
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            switch (requestCode){
                case 10:
                    Bundle bundle = data.getBundleExtra("bundle");
                    String test = bundle.getString("test");
                    Toast.makeText(getActivity(), test, Toast.LENGTH_SHORT).show();
                case LIST_RESULT:
                    getProjectList(mToken);
                    break;
            }

        } catch (NullPointerException npException) {

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 获取项目列表
     *
     * @param token
     */
    public void getProjectList(String token) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pageIndex", 1);
        jsonObject.put("pageSize", 100);
        jsonObject.put("query", "");
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toJSONString());
        HttpMethods.getInstance().getProjectList(new ProgressSubscriber<List<ProjectListBeanResponse>>(mSubscriber, getActivity(), false), token, body);
    }

    /**
     * 编辑项目关注状态
     *
     * @param token
     * @param projectId
     * @param action
     */
    public void putAttention(String token, long projectId, String action) {
        HttpMethods.getInstance().putAttention(new ProgressSubscriber<String>(mPutAttentionSubscriber, getActivity(), false), token, projectId, action);
    }


}
