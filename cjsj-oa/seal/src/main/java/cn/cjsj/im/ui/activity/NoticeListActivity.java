package cn.cjsj.im.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.cjsj.im.App;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.NoticeAndIntegralBean;
import cn.cjsj.im.gty.http.HttpMethods;
import cn.cjsj.im.gty.subscribers.ProgressSubscriber;
import cn.cjsj.im.gty.subscribers.SubscriberOnNextErrorListener;
import cn.cjsj.im.model.NoticeDetailBean;
import cn.cjsj.im.ui.adapter.NoticeListAdapter;
import cn.cjsj.im.ui.adapter.NoticeRecyclerViewAdapter;

/**
 * 公告列表
 * Created by LuoYang on 2017/9/30.
 */

public class NoticeListActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.notice_list_lv)
    RecyclerView mListView;


    private List<NoticeDetailBean> mList;


    private NoticeRecyclerViewAdapter mNoticeListAdapter;

    private SubscriberOnNextErrorListener mNoticesSubscriber;

    private String mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_list_activity);
        ButterKnife.bind(this);
        setTitle(R.string.notice_title);

        mToken = App.getInstance().getToken();

//        mNoticeListAdapter = new NoticeListAdapter(this,mList);
//        mListView.setAdapter(mNoticeListAdapter);
        mList = new ArrayList<>();
        mNoticeListAdapter = new NoticeRecyclerViewAdapter(this,mList, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag() != null) {
                    NoticeDetailBean noticeDetailBean = (NoticeDetailBean) v.getTag();
                    Intent intent = new Intent(NoticeListActivity.this, NoticeDetailActivity.class);
                    intent.putExtra("id",noticeDetailBean.getId());
                    intent.putExtra("dispatchType","notices");
                    startActivity(intent);
                }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mListView.setLayoutManager(layoutManager);
        mListView.setAdapter(mNoticeListAdapter);

        mNoticesSubscriber = new SubscriberOnNextErrorListener<NoticeAndIntegralBean>() {
            @Override
            public void onNext(NoticeAndIntegralBean o) {
                if (o.getNoticeList() != null){
                    mList.addAll(o.getNoticeList());
                    mNoticeListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String error) {

            }
        };
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mList.clear();
        getnotice(mToken);
        mNoticeListAdapter.notifyDataSetChanged();

    }



    public void getnotice(String token){
        HttpMethods.getInstance().getNotice(new ProgressSubscriber<NoticeAndIntegralBean>(mNoticesSubscriber,this,false),token,1,100,1);
    }
}
