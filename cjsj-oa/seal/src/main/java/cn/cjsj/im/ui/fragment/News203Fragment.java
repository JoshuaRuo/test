package cn.cjsj.im.ui.fragment;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.jakewharton.rxbinding.view.RxView;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.cjsj.im.App;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.NewsStatisticsResponse;
import cn.cjsj.im.gty.http.HttpMethods;
import cn.cjsj.im.gty.subscribers.ProgressSubscriber;
import cn.cjsj.im.gty.subscribers.SubscriberOnNextErrorListener;
import cn.cjsj.im.ui.activity.NewsList203Activity;
import rx.functions.Action1;

/**
 * Created by LuoYang on 2019/1/8 16:07
 * 消息通知
 */
public class News203Fragment extends Fragment {
    private Typeface mTypeface;
    private String mToken;
    private SubscriberOnNextErrorListener mSubscriber;

    @Bind(R.id.news_pending_layout)
    RelativeLayout mPendingLayout; //type 1 审批通知

    @Bind(R.id.notice_list_layout)
    RelativeLayout mNoticeLayout; //type 2 新闻公告

    @Bind(R.id.vote_list_layout)
    RelativeLayout mVoteLayout; // type 3 投票

    @Bind(R.id.work_log_list_layout)
    RelativeLayout mWorkLayout; // type 4 工作日志

    @Bind(R.id.attendance_list_layout)
    RelativeLayout mAttendanceLayout; //type 5考勤打卡


    @Bind(R.id.news_pending_value_tv)
    TextView mPendingValueTv;

    @Bind(R.id.notice_list_value_tv)
    TextView mNoticeValueTv;

    @Bind(R.id.vote_list_value_tv)
    TextView mVoteValueTv;

    @Bind(R.id.word_log_list_value_tv)
    TextView mWorkValueTv;

    @Bind(R.id.attendance_list_value_tv)
    TextView mAttendanceValueTv;

    @Bind(R.id.news_pending_time_tv)
    TextView mPendingTimeTv;

    @Bind(R.id.notice_list_iv_time_tv)
    TextView mNoticeTimeTv;

    @Bind(R.id.vote_list_time_tv)
    TextView mVoteTimeTv;

    @Bind(R.id.work_log_list_time_tv)
    TextView mWorkLogTimeTv;

    @Bind(R.id.attendance_list_time_tv)
    TextView mAttendanceTimeTv;

    @Bind(R.id.news_pending_count_tv)
    TextView mPendingCountTv;

    @Bind(R.id.notices_list_count_tv)
    TextView mNoticesCountTv;

    @Bind(R.id.vote_list_count_tv)
    TextView mVoteCountTv;

    @Bind(R.id.work_log_list_count_tv)
    TextView mWorkLogCountTv;

    @Bind(R.id.attendance_list_count_tv)
    TextView mAttendanceCountTv;

    private int mThisYear;
    private int mThisMonth;
    private int mToday;

    private List<NewsStatisticsResponse> mList;
    private static final int UPLOAD_UI = 301;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPLOAD_UI:
                    for (int i = 0; i < mList.size(); i++) {
                        if (mList.get(i).getType() == 1) {//审批通知
                            mPendingValueTv.setText(mList.get(i).getContent());
                            if (mList.get(i).getCreateTime() != null) {
                                if (mThisYear == mList.get(i).getCreateTime().getInteger("year")
                                        && mThisMonth == mList.get(i).getCreateTime().getInteger("monthValue")
                                        && mToday == mList.get(i).getCreateTime().getInteger("dayOfMonth")) {
                                    mPendingTimeTv.setText(mList.get(i).getCreateTime().getInteger("hour") + ":" + mList.get(i).getCreateTime().getInteger("minute"));
                                } else if (mThisYear == mList.get(i).getCreateTime().getInteger("year")
                                        && mThisMonth == mList.get(i).getCreateTime().getInteger("monthValue")
                                        && (mToday - mList.get(i).getCreateTime().getInteger("dayOfMonth") == 1)) {
                                    mPendingTimeTv.setText("昨天");
                                } else {
                                    mPendingTimeTv.setText(mList.get(i).getCreateTime().getInteger("monthValue") + "月" + mList.get(i).getCreateTime().getInteger("dayOfMonth") + "日");
                                }
                            }

                            if (mList.get(i).getUnread() > 0) {
                                mPendingCountTv.setVisibility(View.VISIBLE);
                                mPendingCountTv.setText(mList.get(i).getUnread() + "");
                            } else {
                                mPendingCountTv.setVisibility(View.GONE);
                            }
                        } else if (mList.get(i).getType() == 2) { //新闻公告
                            mNoticeValueTv.setText(mList.get(i).getContent());
                            if (mList.get(i).getCreateTime() != null) {
                                if (mThisYear == mList.get(i).getCreateTime().getInteger("year")
                                        && mThisMonth == mList.get(i).getCreateTime().getInteger("monthValue")
                                        && mToday == mList.get(i).getCreateTime().getInteger("dayOfMonth")) {
                                    mNoticeTimeTv.setText(mList.get(i).getCreateTime().getInteger("hour") + ":" + mList.get(i).getCreateTime().getInteger("minute"));
                                } else if (mThisYear == mList.get(i).getCreateTime().getInteger("year")
                                        && mThisMonth == mList.get(i).getCreateTime().getInteger("monthValue")
                                        && (mToday - mList.get(i).getCreateTime().getInteger("dayOfMonth") == 1)) {
                                    mNoticeTimeTv.setText("昨天");
                                } else {
                                    mNoticeTimeTv.setText(mList.get(i).getCreateTime().getInteger("monthValue") + "月" + mList.get(i).getCreateTime().getInteger("dayOfMonth") + "日");
                                }
                            }

                            if (mList.get(i).getUnread() > 0) {
                                mNoticesCountTv.setVisibility(View.VISIBLE);
                                mNoticesCountTv.setText(mList.get(i).getUnread() + "");
                            } else {
                                mNoticesCountTv.setVisibility(View.GONE);
                            }
                        } else if (mList.get(i).getType() == 3) { //投票

                            mVoteValueTv.setText(mList.get(i).getContent());
                            if (mList.get(i).getCreateTime() != null) {
                                if (mThisYear == mList.get(i).getCreateTime().getInteger("year")
                                        && mThisMonth == mList.get(i).getCreateTime().getInteger("monthValue")
                                        && mToday == mList.get(i).getCreateTime().getInteger("dayOfMonth")) {
                                    mVoteTimeTv.setText(mList.get(i).getCreateTime().getInteger("hour") + ":" + mList.get(i).getCreateTime().getInteger("minute"));
                                } else if (mThisYear == mList.get(i).getCreateTime().getInteger("year")
                                        && mThisMonth == mList.get(i).getCreateTime().getInteger("monthValue")
                                        && (mToday - mList.get(i).getCreateTime().getInteger("dayOfMonth") == 1)) {
                                    mVoteTimeTv.setText("昨天");
                                } else {
                                    mVoteTimeTv.setText(mList.get(i).getCreateTime().getInteger("monthValue") + "月" + mList.get(i).getCreateTime().getInteger("dayOfMonth") + "日");
                                }
                            }

                            if (mList.get(i).getUnread() > 0) {
                                mVoteCountTv.setVisibility(View.VISIBLE);
                                mVoteCountTv.setText(mList.get(i).getUnread() + "");
                            } else {
                                mVoteCountTv.setVisibility(View.GONE);
                            }

                        } else if (mList.get(i).getType() == 4) {//工作日志评价

                            mWorkValueTv.setText(mList.get(i).getContent());
                            if (mList.get(i).getCreateTime() != null) {
                                if (mThisYear == mList.get(i).getCreateTime().getInteger("year")
                                        && mThisMonth == mList.get(i).getCreateTime().getInteger("monthValue")
                                        && mToday == mList.get(i).getCreateTime().getInteger("dayOfMonth")) {
                                    mWorkLogTimeTv.setText(mList.get(i).getCreateTime().getInteger("hour") + ":" + mList.get(i).getCreateTime().getInteger("minute"));
                                } else if (mThisYear == mList.get(i).getCreateTime().getInteger("year")
                                        && mThisMonth == mList.get(i).getCreateTime().getInteger("monthValue")
                                        && (mToday - mList.get(i).getCreateTime().getInteger("dayOfMonth") == 1)) {
                                    mWorkLogTimeTv.setText("昨天");
                                } else {
                                    mWorkLogTimeTv.setText(mList.get(i).getCreateTime().getInteger("monthValue") + "月" + mList.get(i).getCreateTime().getInteger("dayOfMonth") + "日");
                                }
                            }

                            if (mList.get(i).getUnread() > 0) {
                                mWorkLogCountTv.setVisibility(View.VISIBLE);
                                mWorkLogCountTv.setText(mList.get(i).getUnread() + "");
                            } else {
                                mWorkLogCountTv.setVisibility(View.GONE);
                            }

                        } else if (mList.get(i).getType() == 5) {//考勤打卡

                            mAttendanceValueTv.setText(mList.get(i).getContent());
                            if (mList.get(i).getCreateTime() != null) {
                                if (mThisYear == mList.get(i).getCreateTime().getInteger("year")
                                        && mThisMonth == mList.get(i).getCreateTime().getInteger("monthValue")
                                        && mToday == mList.get(i).getCreateTime().getInteger("dayOfMonth")) {
                                    mAttendanceTimeTv.setText(mList.get(i).getCreateTime().getInteger("hour") + ":" + mList.get(i).getCreateTime().getInteger("minute"));
                                } else if (mThisYear == mList.get(i).getCreateTime().getInteger("year")
                                        && mThisMonth == mList.get(i).getCreateTime().getInteger("monthValue")
                                        && (mToday - mList.get(i).getCreateTime().getInteger("dayOfMonth") == 1)) {
                                    mAttendanceTimeTv.setText("昨天");
                                } else {
                                    mAttendanceTimeTv.setText(mList.get(i).getCreateTime().getInteger("monthValue") + "月" + mList.get(i).getCreateTime().getInteger("dayOfMonth") + "日");
                                }
                            }

                            if (mList.get(i).getUnread() > 0) {
                                mAttendanceCountTv.setVisibility(View.VISIBLE);
                                mAttendanceCountTv.setText(mList.get(i).getUnread() + "");
                            } else {
                                mAttendanceCountTv.setVisibility(View.GONE);
                            }

                        }
                    }
                    break;
            }

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_203_news, container, false);
        ButterKnife.bind(this, view);
        mTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/STSongti-SC-Bold-02.ttf");
        mToken = App.getInstance().getToken();
        getToday();
        ;
        initData();
        initDefaultView();
        return view;
    }

    private void initData() {
        mSubscriber = new SubscriberOnNextErrorListener<List<NewsStatisticsResponse>>() {
            @Override
            public void onNext(List<NewsStatisticsResponse> list) {
                mList = list;
                mHandler.sendEmptyMessage(UPLOAD_UI);
            }

            @Override
            public void onError(String error) {

            }
        };
//        getNewsOutValue(mToken);

    }


    private void initDefaultView() {
        RxView.clicks(mPendingLayout)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getActivity(), NewsList203Activity.class);
                        intent.putExtra("type", 1);
                        intent.putExtra("titleName", "审批通知");
                        startActivity(intent);
                    }
                });

        RxView.clicks(mNoticeLayout)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getActivity(), NewsList203Activity.class);
                        intent.putExtra("type", 2);
                        intent.putExtra("titleName", "新闻公告");
                        startActivity(intent);
                    }
                });

        RxView.clicks(mVoteLayout)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getActivity(), NewsList203Activity.class);
                        intent.putExtra("type", 3);
                        intent.putExtra("titleName", "投票");
                        startActivity(intent);
                    }
                });

        RxView.clicks(mWorkLayout)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getActivity(), NewsList203Activity.class);
                        intent.putExtra("type", 4);
                        intent.putExtra("titleName", "工作日志");
                        startActivity(intent);
                    }
                });

        RxView.clicks(mAttendanceLayout)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getActivity(), NewsList203Activity.class);
                        intent.putExtra("type", 5);
                        intent.putExtra("titleName", "考勤打卡");
                        startActivity(intent);
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        getNewsOutValue(App.getInstance().getToken());
    }

    private void getNews203List(String token) {
//        HttpMethods.getInstance().getNews203List(new ProgressSubscriber<JSONObject>(mSubscriber,getActivity(),false),token,)
    }

    private void getNewsOutValue(String token) {
        HttpMethods.getInstance().getNewsOutValue(new ProgressSubscriber<List<NewsStatisticsResponse>>(mSubscriber, getActivity(), false), token);
    }

    public void getToday() {
        Calendar now = Calendar.getInstance();
        mThisYear = now.get(Calendar.YEAR);
        mThisMonth = now.get(Calendar.MONTH) + 1;
        mToday = now.get(Calendar.DAY_OF_MONTH);
    }
}
