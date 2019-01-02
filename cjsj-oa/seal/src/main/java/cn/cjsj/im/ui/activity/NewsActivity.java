package cn.cjsj.im.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.cjsj.im.App;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.NewsResponse;
import cn.cjsj.im.gty.common.NewsLoadingFooter;
import cn.cjsj.im.gty.http.HttpMethods;
import cn.cjsj.im.gty.subscribers.ProgressSubscriber;
import cn.cjsj.im.gty.subscribers.SubscriberOnNextErrorListener;
import cn.cjsj.im.server.utils.NLog;
import cn.cjsj.im.ui.adapter.EndLessOnScrollListener;
import cn.cjsj.im.ui.adapter.NewsAdapter;

/**
 * 消息通知
 * Created by LuoYang on 2018/1/4.
 */

public class NewsActivity extends BaseActivity {
    @Bind(R.id.news_lv)
    RecyclerView mLv;

    private String mToken;

    private NewsAdapter mNewsAdapter;

    private List<NewsResponse> mNewsList;

    private SubscriberOnNextErrorListener mGetNewsSubscriber;

    private int mIndex = 1;

    protected NewsLoadingFooter.LoadingFooter mStatus = NewsLoadingFooter.LoadingFooter.Normal;

    private Intent mIntent;

    private EndLessOnScrollListener mOnScrollListener = new EndLessOnScrollListener() {
        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);
            if (mStatus == NewsLoadingFooter.LoadingFooter.Loading) {
                return;
            }
            mIndex++;
            getNews(mToken, 10, mIndex);
            setStatus(NewsLoadingFooter.LoadingFooter.Loading);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);
        setTitle("消息通知");

        mToken = App.getInstance().getToken();
        mNewsList = new ArrayList<>();
        mNewsAdapter = new NewsAdapter(this, mNewsList, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag() != null) {
                    NewsResponse newsResponse = (NewsResponse) v.getTag();
                    switch (newsResponse.getActDefId()) {

                        case "0":
                            mIntent = new Intent(NewsActivity.this, CheckWorkTabActivity.class);
                            mIntent.putExtra("actDefId", "bksq:1:10000002508861");
                            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                            startActivity(mIntent);
                            break;

                        case "1":
                            mIntent = new Intent(NewsActivity.this, TaskActivity.class);

                            startActivity(mIntent);
                            break;

                        case "2":
                            mIntent = new Intent(NewsActivity.this, WeeklyLogActivity.class);

                            startActivity(mIntent);
                            break;
                        case "3":
                            mIntent = new Intent(NewsActivity.this, MonthlyActivity.class);

                            startActivity(mIntent);
                            break;
                        case "4":
                            mIntent = new Intent(NewsActivity.this, CallOnRecordActivity.class);

                            startActivity(mIntent);
                            break;
                        case "5":
                            mIntent = new Intent(NewsActivity.this, ProductionLogActivity.class);
                            startActivity(mIntent);
                            break;

                        case "6":
                            mIntent = new Intent(NewsActivity.this, DailyPaperActivity.class);
                            startActivity(mIntent);
                            break;

                        case "7":
                            mIntent = new Intent(NewsActivity.this, NoticeDetailActivity.class);
                            mIntent.putExtra("id", newsResponse.getBusinessKey());
                            mIntent.putExtra("dispatchType", "department");
                            startActivity(mIntent);

                            break;

                        case "8":

                            mIntent = new Intent(NewsActivity.this, NoticeDetailActivity.class);
                            mIntent.putExtra("id", newsResponse.getBusinessKey());
                            mIntent.putExtra("dispatchType", "group");
                            startActivity(mIntent);
                            break;

                        case "-1":

                            break;

                        case "9"://投票

                            JSONObject jsonObject = JSONObject.parseObject(newsResponse.getJson());

                            mIntent = new Intent(NewsActivity.this, AgendaDetailActivity.class);
                            mIntent.putExtra("businessKey", newsResponse.getBusinessKey() + "");
                            mIntent.putExtra("actDefId", "vote:100001");
                            mIntent.putExtra("status", newsResponse.getPushType() + "");
                            mIntent.putExtra("runId", newsResponse.getBusinessKey() + "");
                            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(mIntent);
                            break;
                        default:
                            mIntent = new Intent(NewsActivity.this, AgendaDetailActivity.class);
                            mIntent.putExtra("actDefId", newsResponse.getActDefId());
                            mIntent.putExtra("businessKey", newsResponse.getBusinessKey() + "");
                            mIntent.putExtra("status", newsResponse.getPushType() + "");
                            mIntent.putExtra("runId", newsResponse.getBusinessKey() + "");
                            startActivity(mIntent);
                            break;
                    }
                }
            }
        });

//        瀑布流StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
//       表格流 GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLv.setLayoutManager(layoutManager);
        mLv.addOnScrollListener(mOnScrollListener);
        mLv.setAdapter(mNewsAdapter);


        mGetNewsSubscriber = new SubscriberOnNextErrorListener<List<NewsResponse>>() {
            @Override
            public void onNext(List<NewsResponse> newsList) {

                if (newsList == null) {
                    mIndex--;
                    setStatus(NewsLoadingFooter.LoadingFooter.TheEnd);
                }

                if (newsList != null) {
                    mNewsList.addAll(newsList);
                    mNewsAdapter.notifyDataSetChanged();
                }

                setStatus(NewsLoadingFooter.LoadingFooter.Normal);
            }

            @Override
            public void onError(String error) {

            }
        };

        getNews(mToken, 10, mIndex);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNewsList.clear();
        mNewsAdapter.notifyDataSetChanged();
    }


    //改变底部bottom的样式
    protected void changeAdaperState() {
        if (mNewsAdapter != null && mNewsAdapter.mNewsFootHolder != null) {
            mNewsAdapter.mNewsFootHolder.setData(mStatus);
        }
    }

    public void setStatus(NewsLoadingFooter.LoadingFooter status) {
        this.mStatus = status;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                changeAdaperState();
            }
        });
    }

    /**
     * 获取消息列表
     *
     * @param token
     * @param pageSize
     * @param currentPage
     */
    public void getNews(String token, int pageSize, int currentPage) {
        HttpMethods.getInstance().getNewS(new ProgressSubscriber<List<NewsResponse>>(mGetNewsSubscriber, this, true), token, pageSize, currentPage);
    }

}
