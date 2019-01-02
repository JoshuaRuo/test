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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.cjsj.im.App;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.DepartmentDispatchBean;
import cn.cjsj.im.gty.bean.DepartmentDispatchListBean;
import cn.cjsj.im.gty.http.HttpMethods;
import cn.cjsj.im.gty.subscribers.ProgressSubscriber;
import cn.cjsj.im.gty.subscribers.SubscriberOnNextErrorListener;
import cn.cjsj.im.ui.activity.NoticeDetailActivity;
import cn.cjsj.im.ui.adapter.DepartmentDispatchAdapter;

/**
 * Created by LuoYang on 2018/7/17 09:31
 */
public class DepartmentPostFragment extends Fragment implements BGARefreshLayout.BGARefreshLayoutDelegate {

    private View rootView;


    private BGARefreshLayout bgaRefreshLayout;

    private DepartmentDispatchAdapter mDepartmentDispatchAdapter;

    private SubscriberOnNextErrorListener mDepartmentSubscriber;

    private String mToken;
    private List<DepartmentDispatchBean> mList;
    private int mCurrentPage = 1;
    private int mPageSize = 7;

    private boolean isRefresh = false;
    private boolean isLoading = false;


    private final static int DEPARTMENT_LOADING = 101;

    private final static int DEPARTMENT_REFRESH = 102;

    private int FOR_DETAIL_REQUEST_CODE = 100;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case DEPARTMENT_LOADING:
                    bgaRefreshLayout.endLoadingMore();
                    isLoading = false;
                    break;

                case DEPARTMENT_REFRESH:
                    bgaRefreshLayout.endRefreshing();
                    isRefresh = false;
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.fragment_department_post, container, false);


        }

        mToken = App.getInstance().getToken();
        mList = new ArrayList<>();
        bgaRefreshLayout = rootView.findViewById(R.id.department_modulename_refresh);

        bgaRefreshLayout.setDelegate(this);
        BGANormalRefreshViewHolder mRefreshViewHolder = new BGANormalRefreshViewHolder(getContext(), true);
        bgaRefreshLayout.setRefreshViewHolder(mRefreshViewHolder);
        RecyclerView mRecyclerView = rootView.findViewById(R.id.department_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDepartmentDispatchAdapter = new DepartmentDispatchAdapter(mRecyclerView);
        mRecyclerView.setAdapter(mDepartmentDispatchAdapter.getHeaderAndFooterAdapter());

        mDepartmentDispatchAdapter.setOnRVItemClickListener(new BGAOnRVItemClickListener() {
            @Override
            public void onRVItemClick(ViewGroup parent, View itemView, int position) {
                Intent intent = new Intent(getActivity(), NoticeDetailActivity.class);
                intent.putExtra("id",mList.get(position).getId());
                intent.putExtra("dispatchType","department");
                startActivityForResult(intent,100);
            }
        });
        mDepartmentSubscriber = new SubscriberOnNextErrorListener<DepartmentDispatchListBean>() {
            @Override
            public void onNext(DepartmentDispatchListBean departmentDispatchListBean) {
                if(isRefresh){
                    mHandler.sendEmptyMessage(DEPARTMENT_REFRESH);
                }else if (isLoading){
                    mHandler.sendEmptyMessage(DEPARTMENT_LOADING);
                }

                if (departmentDispatchListBean.getDeptNoticeList().size() == 0 && mCurrentPage > 1){
                    mCurrentPage--;
                }else {
                    mList.addAll(departmentDispatchListBean.getDeptNoticeList()) ;
                    mDepartmentDispatchAdapter.setData(mList);
                }
            }

            @Override
            public void onError(String error) {

            }
        };

        getDepartmentDispatch(mToken, 7, 1);
        return rootView;


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FOR_DETAIL_REQUEST_CODE){
            mList.clear();
        getDepartmentDispatch(mToken, 7, 1);
        }
    }
    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mCurrentPage = 1;
                mList.clear();
                getDepartmentDispatch(mToken, 7, 1);
                isRefresh = true;
            }
        }, 1000);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mCurrentPage++;
                getDepartmentDispatch(mToken, mPageSize, mCurrentPage);
                isLoading = true;
            }
        }, 1000);
        return true;
    }


    /**
     * 获取部门发文
     *
     * @param token
     * @param pageSize
     * @param currentPage
     */
    private void getDepartmentDispatch(String token, int pageSize, int currentPage) {
        HttpMethods.getInstance().getDepartmentDispatch(new ProgressSubscriber<DepartmentDispatchListBean>(mDepartmentSubscriber, getActivity(), false), token, pageSize, currentPage);
    }
}
