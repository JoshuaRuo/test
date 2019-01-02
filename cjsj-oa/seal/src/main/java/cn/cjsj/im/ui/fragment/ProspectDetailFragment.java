package cn.cjsj.im.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.ProspectDataBean;
import cn.cjsj.im.ui.adapter.ProspectRecyclerAdapter;

/**
 * Created by LuoYang on 2018/8/6 15:23
 * 勘察详情
 */
public class ProspectDetailFragment extends Fragment {

    private View mView;

    @Bind(R.id.prospect_recycler)
    RecyclerView mRv;

    private ProspectRecyclerAdapter mAdapter;

    private List<ProspectDataBean> mList;

    private List<ProspectDataBean> mChildList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null != mView) {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (null != parent) {
                parent.removeView(mView);
            }
        } else {
            mView = inflater.inflate(R.layout.fragment_prospect_detail, container, false);
        }
        ButterKnife.bind(this, mView);
        initData();
        initView();
        return mView;
    }


    private void initData() {
        mList = new ArrayList<>();
        ProspectDataBean bean = null;
        for (int i = 0; i < 5; i++){
            bean = new ProspectDataBean();
            bean.setID(i + "");
            bean.setType(0);
            bean.setParentLeftTxt("测量详情");
            bean.setTitle("标题");
            bean.setTime("2018-03-12至2019-03-12");
            bean.setPrincipal("张三");
            bean.setStatus("1");
            bean.setChildBean(bean);
            mList.add(bean);
        }

        mChildList = new ArrayList<>();
        ProspectDataBean childBean = null;

        childBean = new ProspectDataBean();
        childBean.setType(1);
        childBean.setTitle("标题");
        childBean.setTime("2018-03-12至2019-03-12");
        childBean.setPrincipal("张三");
        childBean.setStatus("1");
        childBean.setChildBean(childBean);
        mChildList.add(childBean);

        childBean = new ProspectDataBean();
        childBean.setType(1);
        childBean.setTitle("标题");
        childBean.setTime("2018-03-12至2019-03-13");
        childBean.setPrincipal("李四");
        childBean.setStatus("2");
        childBean.setChildBean(childBean);
        mChildList.add(childBean);

        childBean = new ProspectDataBean();
        childBean.setType(1);
        childBean.setTitle("标题");
        childBean.setTime("2018-03-12至2019-03-11");
        childBean.setPrincipal("王五");
        childBean.setStatus("3");
        childBean.setChildBean(childBean);
        mChildList.add(childBean);
    }

    private void initView() {
        mRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ProspectRecyclerAdapter(getActivity(),mList,mChildList);
        mRv.setAdapter(mAdapter);
        mAdapter.setOnScrollListener(new ProspectRecyclerAdapter.OnScrollListener() {
            @Override
            public void scrollTo(int pos) {
                mRv.scrollToPosition(pos);
            }
        });
    }

}
