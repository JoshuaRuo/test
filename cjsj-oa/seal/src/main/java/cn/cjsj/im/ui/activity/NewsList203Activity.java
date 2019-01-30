package cn.cjsj.im.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.cjsj.im.App;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.NewsListGenericityModel;
import cn.cjsj.im.gty.bean.NewsListResponse;
import cn.cjsj.im.gty.http.HttpMethods;
import cn.cjsj.im.gty.subscribers.ProgressSubscriber;
import cn.cjsj.im.gty.subscribers.SubscriberOnNextErrorListener;
import cn.cjsj.im.ui.adapter.NewsList203Adapter;

/**
 * Created by LuoYang on 2019/1/9 10:27
 * 新消息列表
 */
public class NewsList203Activity extends BaseActivity {

    @Bind(R.id.news_203_rv)
    RecyclerView mRv;

    @Bind(R.id.news_203_default_layout)
    RelativeLayout mDefaultLayout;

    private NewsList203Adapter mAdapter;
    private String mToken;
    private SubscriberOnNextErrorListener mSubscriber;
    private int mType;
    private String mTitle;
    private Intent mIntent;

    private static final int UPLOAD_UI = 401;
    private List<NewsListGenericityModel> mList;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPLOAD_UI:
                    mAdapter.setData(mList);
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_203_list);
        ButterKnife.bind(this);
        mIntent = getIntent();
        mType = mIntent.getIntExtra("type", 0);
        mTitle = mIntent.getStringExtra("titleName");
        mToken = App.getInstance().getToken();
        setTitle(mTitle);

        mRv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new NewsList203Adapter(mRv);
        mRv.setAdapter(mAdapter.getHeaderAndFooterAdapter());
        mAdapter.setOnRVItemClickListener(new BGAOnRVItemClickListener() {
            @Override
            public void onRVItemClick(ViewGroup parent, View itemView, int position) {
                if (mList != null) {
                    switch (mList.get(position).getType()) {
                        case 1:
//                            mIntent = new Intent(NewsList203Activity.this, AgendaDetailActivity.class);
//                            mIntent.putExtra("actDefId", mList.get(position).getActDefId());
//                            mIntent.putExtra("businessKey", mList.get(position).getBusinessKey() + "");
//                            mIntent.putExtra("status", mList.get(position).getPushType() + "");
//                            mIntent.putExtra("runId", mList.get(position).getBusinessKey() + "");
//                            startActivity(mIntent);
                            break;

                        case 2:

                            if ("【集团发文】".equals(mList.get(position).getTitle())) {
                                mIntent = new Intent(NewsList203Activity.this, NoticeDetailActivity.class);
                                mIntent.putExtra("id", mList.get(position).getBusinessKey());
                                mIntent.putExtra("dispatchType", "group");
                                startActivity(mIntent);
                            } else if ("【新闻公告】".equals(mList.get(position).getTitle())) {
                                Intent intent = new Intent(NewsList203Activity.this, NoticeDetailActivity.class);
                                intent.putExtra("id", mList.get(position).getBusinessKey());
                                intent.putExtra("dispatchType", "notices");
                                startActivity(intent);
                            } else if ("【部门公告】".equals(mList.get(position).getTitle())) {
                                mIntent = new Intent(NewsList203Activity.this, NoticeDetailActivity.class);
                                mIntent.putExtra("id", mList.get(position).getBusinessKey());
                                mIntent.putExtra("dispatchType", "department");
                                startActivity(mIntent);
                            }

                            break;
                    }
                }

            }
        });

        mSubscriber = new SubscriberOnNextErrorListener<NewsListResponse>() {
            @Override
            public void onNext(NewsListResponse model) {
                mList = model.getList();
                if (mList.size() != 0) {
                    mHandler.sendEmptyMessage(UPLOAD_UI);
                    mRv.setVisibility(View.VISIBLE);
                    mDefaultLayout.setVisibility(View.GONE);
                } else {
                    mRv.setVisibility(View.GONE);
                    mDefaultLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(String error) {

            }
        };

        if (mType != 0) {
            getNews203List(mToken, mType, 1, 100);
        }

    }

    private void getNews203List(String token, int type, int pageIndex, int pageSize) {
        HttpMethods.getInstance().getNews203List(new ProgressSubscriber<NewsListResponse>(mSubscriber, this, false), token, type, pageIndex, pageSize);
    }
}
