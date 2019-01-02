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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.cjsj.im.App;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.OrganizationBean;
import cn.cjsj.im.gty.http.HttpMethods;
import cn.cjsj.im.gty.subscribers.ProgressSubscriber;
import cn.cjsj.im.gty.subscribers.SubscriberOnNextErrorListener;
import cn.cjsj.im.ui.activity.StaffSearchActivity;
import cn.cjsj.im.ui.viewholder.TreeViewHolder;
import cn.cjsj.im.ui.widget.DrawableCenterTextView;
import cn.cjsj.im.ui.widget.treeview.model.TreeNode;
import cn.cjsj.im.ui.widget.treeview.view.AndroidTreeView;
import rx.functions.Action1;

/**
 * Created by LuoYang on 2018/7/31 15:03
 * 通讯录/部门组织架构
 */
public class ContactAddressFragment extends Fragment {

    private View mView;

    private AndroidTreeView mTreeView;

    private SubscriberOnNextErrorListener mOrgSub;

    private String mToken;

    private static final int INIT_DATE = 1002;

    private List<OrganizationBean> mList;

    private ViewGroup mContainerView;
    private TreeNode mRootNode;
    private DrawableCenterTextView mDrawableCenterTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null != mView) {
            ViewGroup viewGroup = (ViewGroup) mView.getParent();
            if (null != viewGroup) {
                viewGroup.removeView(mView);
            }
        } else {
            mView = inflater.inflate(R.layout.fragment_contact_address, null, false);
        }

        mToken = App.getInstance().getToken();
        mDrawableCenterTextView = mView.findViewById(R.id.contact_search_bt);
        RxView.clicks(mDrawableCenterTextView)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getActivity(), StaffSearchActivity.class));
                    }
                });

//        TreeNode test1 = new TreeNode(new TreeViewHolder.IconTreeItem("张毅", "(1人)")).setViewHolder(new TreeViewHolder(getActivity()));
//        TreeNode test3 = new TreeNode(new TreeViewHolder.IconTreeItem("招投标部", "(5人)")).setViewHolder(new TreeViewHolder(getActivity()));
//
//        TreeNode test4 = new TreeNode(new TreeViewHolder.IconTreeItem("交通一分院", "(12人)")).setViewHolder(new TreeViewHolder(getActivity()));
//        TreeNode test5 = new TreeNode(new TreeViewHolder.IconTreeItem("行政部", "(5人)")).setViewHolder(new TreeViewHolder(getActivity()));
//        TreeNode test6 = new TreeNode(new TreeViewHolder.IconTreeItem("经营部", "(7人)")).setViewHolder(new TreeViewHolder(getActivity()));
//
//        TreeNode myProfile = new TreeNode(new TreeViewHolder.IconTreeItem("董事长", "(1人)")).setViewHolder(new TreeViewHolder(getActivity()));
//        TreeNode bruce = new TreeNode(new TreeViewHolder.IconTreeItem("交通院", "(1人)")).setViewHolder(new TreeViewHolder(getActivity()));
//
//        test4.addChildren(test5, test6);
//
//        myProfile.addChildren(test1);
//        bruce.addChildren(test3, test4);

//        mRootNode.addChildren(myProfile, bruce);


        mOrgSub = new SubscriberOnNextErrorListener<List<OrganizationBean>>() {
            @Override
            public void onNext(List<OrganizationBean> list) {
                mList = list;
                mContainerView = mView.findViewById(R.id.contact_relativelayout);
                mRootNode = TreeNode.root();
                TreeNode top;
                TreeNode sec;

                for (int i = 0; i < mList.size(); i++) {
                    if ("1".equals(mList.get(i).getOrgSupId())) {
                        int secTotal = 0;
                        for (int j = 0; j < mList.size(); j++){
                            if (mList.get(j).getPath().contains(mList.get(i).getPath())){
                                secTotal += mList.get(j).getUserCount();
                            }
                        }

                        top = new TreeNode(new TreeViewHolder.IconTreeItem(mList.get(i).getOrgName(), "(" + secTotal + "人)")).setViewHolder(new TreeViewHolder(getActivity()));

                        for (int k = 0; k < mList.size(); k++) {
                            if (mList.get(k).getPath().contains(mList.get(i).getPath())) {
                                sec = new TreeNode(new TreeViewHolder.IconTreeItem(mList.get(k).getOrgName(), "(" + mList.get(k).getUserCount() + "人)",mList.get(k).getOrgId())).setViewHolder(new TreeViewHolder(getActivity()));
                                top.addChildren(sec);
                            }
                        }
                        mRootNode.addChildren(top);
                    }
                }
                mTreeView = new AndroidTreeView(getActivity(), mRootNode);
                mTreeView.setDefaultAnimation(true);
                mTreeView.setDefaultContainerStyle(R.style.TreeNodeStyleDivided, true);
                mContainerView.addView(mTreeView.getView());

            }

            @Override
            public void onError(String error) {

            }
        };

//        if (savedInstanceState != null) {
//            String state = savedInstanceState.getString("tState");
//            if (!TextUtils.isEmpty(state)) {
//                mTreeView.restoreState(state);
//            }
//        }
        getOrgList(mToken);
        return mView;
    }

//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//            outState.putString("tState", mTreeView.getSaveState());
//    }


    /**
     * 获取组织
     *
     * @param token
     */
    private void getOrgList(String token) {
        HttpMethods.getInstance().getOrgList(new ProgressSubscriber<List<OrganizationBean>>(mOrgSub, getActivity(), false), token);
    }

}
