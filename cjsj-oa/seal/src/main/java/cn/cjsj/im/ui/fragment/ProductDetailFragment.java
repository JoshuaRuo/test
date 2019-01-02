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

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.cjsj.im.R;
import cn.cjsj.im.ui.adapter.ProductRecyclerAdapter;

/**
 * Created by LuoYang on 2018/8/6 15:22
 * 生产详情
 */
public class ProductDetailFragment extends Fragment {

    @Bind(R.id.product_recycler)
    RecyclerView mRv;

    private View mView;

    private ProductRecyclerAdapter mAdapter;

    private List<JSONObject> mList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null != mView) {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (null != parent) {
                parent.removeView(mView);
            }
        } else {
            mView = inflater.inflate(R.layout.fragment_product_detail, container, false);
        }
        ButterKnife.bind(this, mView);
        initDate();
        initView();
        return mView;
    }

    private void initDate() {
        mList = new ArrayList<>();
        JSONObject json = new JSONObject();
        json.put("title","这里是专业名称这里是专业名称");
        json.put("time","2018-03-01至2019-03-01");
        json.put("principal","负责人:张三");
        json.put("status","1");
        mList.add(json);

        JSONObject json1 = new JSONObject();
        json1.put("title","这里是专业名称这里是专业名称");
        json1.put("time","2018-03-01至2019-03-01");
        json1.put("principal","负责人:老王");
        json1.put("status","2");
        mList.add(json1);

        JSONObject json2 = new JSONObject();
        json2.put("title","这里是专业名称这里是专业名称");
        json2.put("time","2018-03-01至2019-03-01");
        json2.put("principal","负责人:李四");
        json2.put("status","3");
        mList.add(json2);



    }

    private void initView() {
        mRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ProductRecyclerAdapter(mRv,getActivity());
        mRv.setAdapter(mAdapter.getHeaderAndFooterAdapter());
        mAdapter.setData(mList);
    }
}
