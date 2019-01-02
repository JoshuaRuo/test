package cn.cjsj.im.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.jakewharton.rxbinding.view.RxView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.cjsj.im.App;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.AgendaResponse;
import cn.cjsj.im.gty.bean.DailyPaperModel;
import cn.cjsj.im.gty.bean.NoticeAndIntegralBean;
import cn.cjsj.im.gty.home.base.Common;
import cn.cjsj.im.gty.home.entity.MenuItem;
import cn.cjsj.im.gty.http.HttpMethods;
import cn.cjsj.im.gty.subscribers.ProgressSubscriber;
import cn.cjsj.im.gty.subscribers.SubscriberOnNextErrorListener;
import cn.cjsj.im.gty.tools.ImageKit;
import cn.cjsj.im.model.NoticeDetailBean;
import cn.cjsj.im.server.utils.NLog;
import cn.cjsj.im.ui.activity.AgendaActivity;
import cn.cjsj.im.ui.activity.AgendaDetailActivity;
import cn.cjsj.im.ui.activity.CheckWorkTabActivity;
import cn.cjsj.im.ui.activity.DailyPaperActivity;
import cn.cjsj.im.ui.activity.NewsActivity;
import cn.cjsj.im.ui.activity.NoticeDetailActivity;
import cn.cjsj.im.ui.adapter.NoticesAdapter;
import cn.cjsj.im.ui.widget.CirclePageIndicator;
import cn.cjsj.im.ui.widget.gridviewpager.FixedSpeedScroller;
import cn.cjsj.im.ui.widget.gridviewpager.GridViewPager;
import cn.cjsj.im.ui.widget.gridviewpager.GridViewPagerDataAdapter;
import cn.cjsj.im.ui.widget.gridviewpager.HomeGridPagerHelper;
import cn.cjsj.im.ui.widget.gridviewpager.HomeGridViewAdapter;
import cn.cjsj.im.utils.ColorAnimator;
import cn.cjsj.im.utils.DateUtils;
import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.bean.air.now.AirNow;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.view.HeConfig;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;
import me.bakumon.library.view.BulletinView;
import rx.functions.Action1;

/**
 * Created by LuoYang on 2018/7/4.
 * 1.4Home 首页
 */

@SuppressWarnings("ResourceAsColor")
public class NewHomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, ViewPager.OnPageChangeListener
        , BGARefreshLayout.BGARefreshLayoutDelegate {

    @Bind(R.id.new_swipe_ly)
    SwipeRefreshLayout mSwipeLayout;

    @Bind(R.id.new_home_scroll)
    NestedScrollView mScroll;

    @Bind(R.id.home_t_num)
    TextView mTNum;

    @Bind(R.id.home_air_txt)
    TextView mHomeAirTxt;

    @Bind(R.id.home_new_title)
    TextView mTitle;

    @Bind(R.id.home_new_message)
    ImageView mNewsBtn;

    @Bind(R.id.home_notification_icon_tv)
    TextView mNotificationIcon;

    @Bind(R.id.home_new_pager)
    GridViewPager mViewPager;

    @Bind(R.id.home_new_indicator)
    CirclePageIndicator mIndicator;

    @Bind(R.id.bulletin_view_sale)
    BulletinView mBulletinView;


    @Bind(R.id.home_backlog_count_tv)
    TextView mBackLogCountTv;

    @Bind(R.id.home_go_agenda_more)
    RelativeLayout mGoAgenda;
    @Bind(R.id.home_go_myRequest_more)
    RelativeLayout mGoMyRequest;

    @Bind(R.id.home_backlog_layout)
    LinearLayout mLinearLayout;

    @Bind(R.id.home_myRequest_layout)
    LinearLayout mMyRequestLayout;


    @Bind(R.id.home_backlog_default_tv_bg)
    TextView mBackLogNothingDefaultTvBg;

    @Bind(R.id.home_myRequest_default_tv_bg)
    TextView mMyRequestNothingDefaultTvBg;

    @Bind(R.id.home_checkin_status)
    TextView mForenoon;

    @Bind(R.id.home_checkout_status)
    TextView mAfternoon;

    @Bind(R.id.home_dailypaper_status)
    TextView mDaily;

    @Bind(R.id.home_new_titlebar)
    RelativeLayout mTitleLayout;

    @Bind(R.id.backlog_title_tv)
    TextView mBackLogTitle;

    @Bind(R.id.myRequest_title_tv)
    TextView mRequestTitle;

    @Bind(R.id.home_todo_title)
    TextView mTodoTitle;

    @Bind(R.id.main_base_newline_view)
    View mLine;

//    @Bind(R.id.home_modulename_refresh)
//    BGARefreshLayout mRefresh;

    private Typeface mTypeface;

    private RecyclerUpdateReceiver mRecyclerUpdateReceiver;

    int lastColor = Color.TRANSPARENT;

    private List<MenuItem> mFavList;
    private AgendaResponse mAgendaResponse; //待办数据
    private AgendaResponse myRequestResponse; //待办数据

    private LayoutInflater mInflater;

    private List<NoticeDetailBean> mList;//公告

    private SubscriberOnNextErrorListener mCheckTodaySub;//检查考勤

    private static final int REFRESH_COMPLETE = 0X110;

    private static final int REFRESH_NEWS = 0X111;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_NEWS:
                    mNotificationIcon.setVisibility(View.VISIBLE);
                    break;
                case REFRESH_COMPLETE:
                    getnotice(mToken);
                    getBackLog(mToken);
                    getMyRequest(mToken);
                    getCheckToDay(mToken);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * GridPager Index
     */
    private final int INDEX_HOME = 1;

    private SubscriberOnNextErrorListener mNoticesSubscriber;//公告
    private SubscriberOnNextErrorListener mGetBackLogSubscriber; //待办
    private SubscriberOnNextErrorListener mGetMyRequestSubscriber; //我的请求

    private String mToken;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_home_fragment, container, false);
        ButterKnife.bind(this, view);

        mToken = App.getInstance().getToken();

        mTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/STSongti-SC-Bold-02.ttf");
        mBackLogTitle.setTypeface(mTypeface);
        mRequestTitle.setTypeface(mTypeface);
        mTitle.setTypeface(mTypeface);
        mTodoTitle.setTypeface(mTypeface);
        initDefaultVie();
        initEvents();


        RxView.clicks(mGoAgenda)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getActivity(), AgendaActivity.class));
                    }
                });
        RxView.clicks(mGoMyRequest)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getActivity(), AgendaActivity.class);
                        intent.putExtra("myRequest", 1);
                        startActivity(intent);
                    }
                });
        RxView.clicks(mNewsBtn)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mNotificationIcon.setVisibility(View.INVISIBLE);
                        startActivity(new Intent(getActivity(), NewsActivity.class));
                    }
                });

        return view;
    }

    /**
     * 设置ViewPager的滚动速度
     */
    private void initViewPagerSpeed() {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(mViewPager.getContext());
            mScroller.set(mViewPager, scroller);
        } catch (NoSuchFieldException e) {

        } catch (IllegalArgumentException e) {

        } catch (IllegalAccessException e) {

        }
    }

    //初始化模块信息
    private void initEvents() {

        //注册刷新数据的广播
        mRecyclerUpdateReceiver = new RecyclerUpdateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(1000);
        filter.addAction(Common.Notification.NOTIFY_REFRESH_MAIN_LIST_DATA);
        getActivity().registerReceiver(mRecyclerUpdateReceiver, filter);

        initData();
        initViewPagerSpeed();
        mViewPager.setBackGround(BitmapFactory.decodeResource(getResources(), R.drawable.home_pic_bg));
        mViewPager.setOffscreenPageLimit(4 - 1);
        mViewPager.setCurrentItem(INDEX_HOME);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setGridViewPagerDataAdapter(new GridViewPagerDataAdapter<MenuItem>(mFavList, 2, 4) {
            @Override
            public BaseAdapter getGridViewAdapter(List<MenuItem> currentList, int pageIndex) {
                return new HomeGridViewAdapter(getActivity(), currentList);
            }

            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id, int pageIndex) {
//                MenuItem menuItem = (MenuItem) view.getTag();
                Adapter adapter = parent.getAdapter();
                MenuItem menuItem = (MenuItem) adapter.getItem(position);
//                Toast.makeText(getContext(),pageIndex + menuItem.getName() ,Toast.LENGTH_SHORT).show();

                try {
                    Class cls = Class.forName(menuItem.getActivity());
                    Intent intent = new Intent(getActivity(), cls);
                    intent.putExtra("actDefId", menuItem.getDesc());
                    startActivity(intent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "开发中", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mIndicator.setVisibility(View.VISIBLE);
        mIndicator.setViewPager(mViewPager);
        final ColorAnimator mColorAnimator = new ColorAnimator(Color.TRANSPARENT, Color.WHITE);
        final ColorAnimator mColorAnimatorText = new ColorAnimator(Color.WHITE, Color.BLACK);
        final ColorAnimator mColorAnimatorLine = new ColorAnimator(Color.TRANSPARENT, ContextCompat.getColor(getActivity(), R.color.color_E5E8F3));

        mScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                float fraction = (float) 2 * scrollY / (mViewPager.getHeight());
                int color = mColorAnimator.getFractionColor(fraction);
                int colorText = mColorAnimatorText.getFractionColor(fraction);
                int colorLine = mColorAnimatorLine.getFractionColor(fraction);
                if (color != lastColor) {
                    lastColor = color;
                    mTitleLayout.setBackgroundColor(color);
//                    StatusBarUtil.addStatusBar(JianShuHeadActivity.this, color, 0);
                    mTitle.setTextColor(colorText);
                    mTNum.setTextColor(colorText);
                    mHomeAirTxt.setTextColor(colorText);
                    mNewsBtn.setImageResource(ImageKit.getDrawableImageSrcIdByName("news_icon"));
                    Log.d("LY_if", color + "");
                    mLine.setBackgroundColor(colorLine);
                }

                if (color == 0) {
                    mNewsBtn.setImageResource(ImageKit.getDrawableImageSrcIdByName("news_white_icon"));
                    Log.d("LY_else", color + "");
                }
            }
        });

    }

    //初始化数据列表
    private void initData() {
        if (mFavList != null) {
            mFavList.clear();
        } else {
            mFavList = new ArrayList<>();
        }

        mFavList.addAll(HomeGridPagerHelper.getPreferFavoriteList());
    }

    private void initDefaultVie() {

        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);


//        mRefresh.setDelegate(this);
//        BGANormalRefreshViewHolder mRefreshViewHolder = new BGANormalRefreshViewHolder(getContext(), true);
//        mRefresh.setRefreshViewHolder(mRefreshViewHolder);

        mInflater = LayoutInflater.from(getActivity().getApplicationContext());
        //公告
        mNoticesSubscriber = new SubscriberOnNextErrorListener<NoticeAndIntegralBean>() {
            @Override
            public void onNext(NoticeAndIntegralBean o) {
                mBulletinView.setAdapter(new NoticesAdapter(getActivity(), o.getNoticeList()));
                mList = o.getNoticeList();
                mBulletinView.setOnBulletinItemClickListener(new BulletinView.OnBulletinItemClickListener() {
                    @Override
                    public void onBulletinItemClick(int position) {
                        Intent intent = new Intent(getActivity(), NoticeDetailActivity.class);
                        intent.putExtra("id", mList.get(position).getId());
                        intent.putExtra("dispatchType", "notices");
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onError(String error) {

            }
        };
        getnotice(mToken);

        mGetBackLogSubscriber = new SubscriberOnNextErrorListener<AgendaResponse>() {
            @Override
            public void onNext(AgendaResponse o) {
                mAgendaResponse = o;
                NLog.d("LY_backlog", JSON.toJSON(o));
                if (mLinearLayout.getChildCount() > 0) {
                    mLinearLayout.removeAllViews();
                }
                try {
                    if (mAgendaResponse.getProcessFinishList().size() > 0) {
//                        mBackLogNothingDefaultBg.setVisibility(View.GONE);
                        mBackLogNothingDefaultTvBg.setVisibility(View.GONE);
                        mBackLogCountTv.setTextColor(getResources().getColor(R.color.white));
                        mBackLogCountTv.setText(o.getProcessFinishList().size() + "");
                        mBackLogCountTv.setVisibility(View.VISIBLE);
                    } else {
//                        mLinearLayout.addView(mBackLogNothingDefaultBg);
                        mLinearLayout.addView(mBackLogNothingDefaultTvBg);
//                        mBackLogNothingDefaultBg.setVisibility(View.VISIBLE);
                        mBackLogNothingDefaultTvBg.setVisibility(View.VISIBLE);
                        mBackLogCountTv.setVisibility(View.GONE);
                    }
                } catch (NullPointerException nullException) {
//                    mLinearLayout.addView(mBackLogNothingDefaultBg);
                    mLinearLayout.addView(mBackLogNothingDefaultTvBg);
//                    mBackLogNothingDefaultBg.setVisibility(View.VISIBLE);
                    mBackLogNothingDefaultTvBg.setVisibility(View.VISIBLE);
                }

                for (int i = 0; i < o.getProcessFinishList().size(); i++) {
                    if (i < 5) {
                        View mBackLogItemView = mInflater.inflate(R.layout.home_backlog_model, null);
                        TextView title = (TextView) mBackLogItemView.findViewById(R.id.home_model_backlog_subject);
                        TextView value = (TextView) mBackLogItemView.findViewById(R.id.home_model_backlog_detail);
                        TextView creator = (TextView) mBackLogItemView.findViewById(R.id.home_model_creator);
                        TextView type = (TextView) mBackLogItemView.findViewById(R.id.home_model_backlog_type);
                        TextView head = (TextView) mBackLogItemView.findViewById(R.id.home_model_headimg);
                        title.setText(o.getProcessFinishList().get(i).getSubject());
                        value.setText(o.getProcessFinishList().get(i).getMatterName());
                        creator.setText("发起人:" + o.getProcessFinishList().get(i).getCreator());
                        if (o.getProcessFinishList().get(i).getCreator().length() > 2) {
                            String name = o.getProcessFinishList().get(i).getCreator().substring(1, 3);
                            head.setText(name);
                        } else {
                            head.setText(o.getProcessFinishList().get(i).getCreator());
                        }

                        if (o.getProcessFinishList().get(i).getStatus() != 3) {
                            type.setTextColor(getResources().getColor(R.color.ac_filter_string_color));
                            type.setText("待审批");
                        } else {
                            type.setTextColor(getResources().getColor(R.color.color_F90F70));
                            type.setText("已驳回");
                        }

                        final int finalI = i;
                        mBackLogItemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), AgendaDetailActivity.class);
                                intent.putExtra("actDefId", mAgendaResponse.getProcessFinishList().get(finalI).getActDefId());
                                intent.putExtra("businessKey", mAgendaResponse.getProcessFinishList().get(finalI).getBusinessKey());
                                intent.putExtra("status", mAgendaResponse.getProcessFinishList().get(finalI).getStatus() + "");
                                intent.putExtra("runId", mAgendaResponse.getProcessFinishList().get(finalI).getBusinessKey() + "");

                                startActivity(intent);
                            }
                        });

                        mLinearLayout.addView(mBackLogItemView);
                    }

                }

//                    mRefresh.endRefreshing();
                if (mSwipeLayout.isRefreshing()) {
                    mSwipeLayout.setRefreshing(false);
                }

            }

            @Override
            public void onError(String error) {

            }
        };

        mGetMyRequestSubscriber = new SubscriberOnNextErrorListener<AgendaResponse>() {
            @Override
            public void onNext(AgendaResponse o) {
                myRequestResponse = o;
                NLog.d("LY_backlog", JSON.toJSON(o));
                if (mMyRequestLayout.getChildCount() > 0) {
                    mMyRequestLayout.removeAllViews();
                }

                try {
                    if (myRequestResponse.getProcessFinishList().size() > 0) {
//                        mMyRequestNothingDefaultImgBg.setVisibility(View.GONE);
                        mMyRequestNothingDefaultTvBg.setVisibility(View.GONE);
                    } else {
//                        mMyRequestLayout.addView(mMyRequestNothingDefaultImgBg);
                        mMyRequestLayout.addView(mMyRequestNothingDefaultTvBg);
//                        mMyRequestNothingDefaultImgBg.setVisibility(View.VISIBLE);
                        mMyRequestNothingDefaultTvBg.setVisibility(View.VISIBLE);
                    }
                } catch (NullPointerException nullException) {
//                    mMyRequestLayout.addView(mMyRequestNothingDefaultImgBg);
                    mMyRequestLayout.addView(mMyRequestNothingDefaultTvBg);
//                    mMyRequestNothingDefaultImgBg.setVisibility(View.VISIBLE);
                    mMyRequestNothingDefaultTvBg.setVisibility(View.VISIBLE);
                }

                for (int i = 0; i < o.getProcessFinishList().size(); i++) {
                    if (i < 5) {
                        View mBackLogItemView = mInflater.inflate(R.layout.home_backlog_model, null);
                        TextView title = mBackLogItemView.findViewById(R.id.home_model_backlog_subject);
                        TextView value = mBackLogItemView.findViewById(R.id.home_model_backlog_detail);
                        TextView creator = mBackLogItemView.findViewById(R.id.home_model_creator);
                        TextView type = mBackLogItemView.findViewById(R.id.home_model_backlog_type);
                        TextView head = mBackLogItemView.findViewById(R.id.home_model_headimg);
                        title.setText(o.getProcessFinishList().get(i).getSubject());
                        value.setText(o.getProcessFinishList().get(i).getMatterName());
                        creator.setText("发起人:" + o.getProcessFinishList().get(i).getCreator());
                        if (o.getProcessFinishList().get(i).getCreator().length() > 2) {
                            String name = o.getProcessFinishList().get(i).getCreator().substring(1, 3);
                            head.setText(name);
                        } else {
                            head.setText(o.getProcessFinishList().get(i).getCreator());
                        }

                        if (o.getProcessFinishList().get(i).getStatus() == 1) {
                            type.setTextColor(getResources().getColor(R.color.ac_filter_string_color));
                            type.setText("待审批");
                        } else if (o.getProcessFinishList().get(i).getStatus() == 3) {
                            type.setTextColor(getResources().getColor(R.color.color_F90F70));
                            type.setText("已驳回");
                        } else if (o.getProcessFinishList().get(i).getStatus() == 2) {
                            type.setTextColor(getResources().getColor(R.color.color_21cb26));
                            type.setText("已完成");
                        }

                        final int finalI = i;
                        mBackLogItemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), AgendaDetailActivity.class);
                                intent.putExtra("actDefId", myRequestResponse.getProcessFinishList().get(finalI).getActDefId());
                                intent.putExtra("businessKey", myRequestResponse.getProcessFinishList().get(finalI).getBusinessKey());
                                intent.putExtra("status", myRequestResponse.getProcessFinishList().get(finalI).getStatus() + "");
                                intent.putExtra("runId", myRequestResponse.getProcessFinishList().get(finalI).getBusinessKey() + "");
                                intent.putExtra("MyRequest", 1);
                                startActivity(intent);
                            }
                        });

                        mMyRequestLayout.addView(mBackLogItemView);
                    }

                }


            }

            @Override
            public void onError(String error) {

            }
        };

        final int color_2293ff = ContextCompat.getColor(getActivity(), R.color.color_2293ff);
        final int color_white = ContextCompat.getColor(getActivity(), R.color.white);
        mCheckTodaySub = new SubscriberOnNextErrorListener<JSONObject>() {
            @Override
            public void onNext(JSONObject jsonObject) {
                if (jsonObject != null) {
                    if (jsonObject.getInteger("checkForenoon") == 0) {
                        mForenoon.setText("去完成");

                        mForenoon.setTextColor(color_white);
                        mForenoon.setBackgroundResource(R.drawable.home_doto_done);
                        RxView.clicks(mForenoon)
                                .throttleFirst(1, TimeUnit.SECONDS)
                                .subscribe(new Action1<Void>() {
                                    @Override
                                    public void call(Void aVoid) {
                                        Intent intent = new Intent(getActivity(), CheckWorkTabActivity.class);
                                        intent.putExtra("actDefId", "bksq:1:10000002508861");
                                        startActivity(intent);
                                    }
                                });
                    } else {
                        mForenoon.setText("已完成");
                        mForenoon.setTextColor(color_2293ff);
                        mForenoon.setBackgroundResource(R.drawable.home_todo_none);
                    }

                    if (jsonObject.getInteger("checkAfternoon") == 0) {
                        mAfternoon.setText("去完成");
                        mAfternoon.setTextColor(color_white);
                        mAfternoon.setBackgroundResource(R.drawable.home_doto_done);
                        RxView.clicks(mAfternoon)
                                .throttleFirst(1, TimeUnit.SECONDS)
                                .subscribe(new Action1<Void>() {
                                    @Override
                                    public void call(Void aVoid) {
                                        Intent intent = new Intent(getActivity(), CheckWorkTabActivity.class);
                                        intent.putExtra("actDefId", "bksq:1:10000002508861");
                                        startActivity(intent);
                                    }
                                });
                    } else {
                        mAfternoon.setText("已完成");
                        mAfternoon.setTextColor(color_2293ff);
                        mAfternoon.setBackgroundResource(R.drawable.home_todo_none);
                    }


                    if (jsonObject.getInteger("havePaper") == 0) {
                        mDaily.setText("去完成");
                        mDaily.setTextColor(color_white);
                        mDaily.setBackgroundResource(R.drawable.home_doto_done);
                        RxView.clicks(mDaily)
                                .throttleFirst(1, TimeUnit.SECONDS)
                                .subscribe(new Action1<Void>() {
                                    @Override
                                    public void call(Void aVoid) {
                                        Intent intent = new Intent(getActivity(), DailyPaperActivity.class);
                                        intent.putExtra("dailyPaperType", 1);
                                        startActivity(intent);
                                    }
                                });
                    } else {
                        mDaily.setText("已完成");
                        mDaily.setTextColor(color_2293ff);
                        mDaily.setBackgroundResource(R.drawable.home_todo_none);
                    }
                }
            }

            @Override
            public void onError(String error) {

            }
        };


    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                HeWeather.getWeatherNow(getActivity(), new HeWeather.OnResultWeatherNowBeanListener() {
//                    @Override
//                    public void onError(Throwable throwable) {
//
//                    }
//
//                    @Override
//                    public void onSuccess(List<Now> list) {
//                            Log.d("LY_now",JSON.toJSONString(list).toString());
//                    }
//                });
                HeConfig.init("HE1807061425251413", "f1eb95e26b6042178a0bbf8e4d50ee0a");
                HeConfig.switchToFreeServerNode();
                HeWeather.getWeatherNow(getContext(), Lang.CHINESE_SIMPLIFIED, Unit.METRIC,
                        new HeWeather.OnResultWeatherNowBeanListener() {
                            @Override
                            public void onError(Throwable e) {
                                Log.i("Log", "onError: ", e);
                            }

                            @Override
                            public void onSuccess(List<Now> list) {
                                Log.i("Log", "onSuccess: " + new Gson().toJson(list));

                                mTNum.setText(list.get(0).getNow().getFl() + "°");
                                mHomeAirTxt.setText(list.get(0).getNow().getCond_txt() + "\n" + list.get(0).getNow().getWind_dir());
                            }
                        });
//                HeWeather.getAirNow(getContext(), Lang.CHINESE_SIMPLIFIED, Unit.METRIC,
//                        new HeWeather.OnResultAirNowBeansListener() {
//                            @Override
//                            public void onError(Throwable throwable) {
//                                Log.i("Log", "onError: ", throwable);
//                            }
//
//                            @Override
//                            public void onSuccess(List<AirNow> list) {
//                                Log.i("Log", "onSuccess: " + new Gson().toJson(list));
//                                mHomeAirNum.setText("PM2.5:" + list.get(0).getAir_now_city().getPm25());
//                            }
//                        });
            }
        });
        mToken = App.getInstance().getToken();
        mHandler.sendEmptyMessage(REFRESH_COMPLETE);
    }

    @Override
    public void onRefresh() {
        mHandler.sendEmptyMessage(REFRESH_COMPLETE);
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 获取公告
     *
     * @param token
     */
    public void getnotice(String token) {
        HttpMethods.getInstance().getNotice(new ProgressSubscriber<NoticeAndIntegralBean>(mNoticesSubscriber, getActivity(), false), token, 0, 100, 1);
    }


    /**
     * 获取待办列表
     *
     * @param token
     */
    public void getBackLog(String token) {
        HttpMethods.getInstance().getBackLog(new ProgressSubscriber<AgendaResponse>(mGetBackLogSubscriber, getActivity(), false), token);
    }

    /**
     * 获取我的请求列表
     *
     * @param token
     */
    public void getMyRequest(String token) {
        HttpMethods.getInstance().getMyRequest(new ProgressSubscriber<AgendaResponse>(mGetMyRequestSubscriber, getActivity(), false), token);
    }

    /**
     * 检查考勤
     *
     * @param token
     */
    public void getCheckToDay(String token) {
        HttpMethods.getInstance().getCheckToday(new ProgressSubscriber<JSONObject>(mCheckTodaySub, getActivity(), false), token);

    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mHandler.sendEmptyMessage(REFRESH_COMPLETE);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }


    /**
     * 用于执行刷新数据的广播接收器
     */
    private class RecyclerUpdateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
//            initData();
//            mAdapter.notifyDataSetChanged();
            mHandler.sendEmptyMessage(REFRESH_NEWS);
        }
    }
}
