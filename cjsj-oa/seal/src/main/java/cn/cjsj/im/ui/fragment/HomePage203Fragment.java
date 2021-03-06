package cn.cjsj.im.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Adapter;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.jakewharton.rxbinding.view.RxView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.cjsj.im.App;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.AgendaResponse;
import cn.cjsj.im.gty.bean.CheckStatisticsResponse;
import cn.cjsj.im.gty.bean.NoticeAndIntegralBean;
import cn.cjsj.im.gty.bean.OAUserBean;
import cn.cjsj.im.gty.home.entity.MenuItem;
import cn.cjsj.im.gty.http.HttpMethods;
import cn.cjsj.im.gty.subscribers.ProgressSubscriber;
import cn.cjsj.im.gty.subscribers.SubscriberOnNextErrorListener;
import cn.cjsj.im.model.NoticeDetailBean;
import cn.cjsj.im.ui.activity.AgendaActivity;
import cn.cjsj.im.ui.activity.CalendarActivity;
import cn.cjsj.im.ui.activity.CheckWorkTabActivity;
import cn.cjsj.im.ui.activity.DailyPaperActivity;
import cn.cjsj.im.ui.activity.NoticeDetailActivity;
import cn.cjsj.im.ui.adapter.Home203RvAdapter;
import cn.cjsj.im.ui.adapter.ImageBannerLoader;
import cn.cjsj.im.ui.adapter.NoticesAdapter;
import cn.cjsj.im.ui.widget.gridviewpager.HomeGridPagerHelper;
import cn.cjsj.im.utils.ColorAnimator;
import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.view.HeConfig;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;
import me.bakumon.library.view.BulletinView;
import rx.functions.Action1;

/**
 * Created by LuoYang on 2018/12/27 15:40
 */
public class HomePage203Fragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private Typeface mTypeface;
    @Bind(R.id.home_t_num)
    TextView mTNum;

    @Bind(R.id.home_air_txt)
    TextView mHomeAirTxt;

    @Bind(R.id.home_203_banner)
    Banner mBanner;

    @Bind(R.id.often_use_rv)
    RecyclerView mOftenRv;

    @Bind(R.id.other_use_rv)
    RecyclerView mOtherRv;

    @Bind(R.id.often_use_layout)
    RelativeLayout mOftenUseLayout;

    @Bind(R.id.other_use_layout)
    RelativeLayout mOtherLayout;

    @Bind(R.id.often_status_title)
    TextView mOftenStatusTitle;

    @Bind(R.id.other_use_status_title)
    TextView mOtherUseStatusTitle;

    @Bind(R.id.home_check_status_time_tv)
    TextView mCheckStatus;

    @Bind(R.id.home_dailypaper_status_time_tv)
    TextView mDailyPaperStatus;

    @Bind(R.id.home_to_write_dailypaper)
    RelativeLayout mDaily;

    @Bind(R.id.home_to_check)
    RelativeLayout mToCheck;

    @Bind(R.id.home_wait_list_tv)
    TextView mGoAgenda;

    @Bind(R.id.home_wait_list_icon_tv)
    TextView mAgendaCount;

    @Bind(R.id.home_wait_list_rectangle_tv)
    TextView mAgendaRectCount;

    @Bind(R.id.home_my_send_list_tv)
    TextView mGoMyRequest;


    @Bind(R.id.home_my_send_icon_tv)
    TextView myRequestCount;

    @Bind(R.id.home_my_send_rectangle_tv)
    TextView myRequestRectCount;

    @Bind(R.id.home_my_attendance_icon_tv)
    TextView mCheckCount;

    @Bind(R.id.home_my_attendance_rectangle_tv)
    TextView mCheckRectCount;

    @Bind(R.id.home_my_dailypager_tv)
    TextView myLog;

    @Bind(R.id.new_home_swipe_ly)
    SwipeRefreshLayout mSwipeLayout;

    @Bind(R.id.home_203_scrollview)
    NestedScrollView mScrollView;

    @Bind(R.id.main_base_newline_view)
    View mLine;
    private int lastColor = Color.TRANSPARENT;
    private float mViewHeight = 390;//组件高度
    private float mDensity;
    private boolean isFold = false;//是否是收起状态
    boolean isAnimating = false;//是否正在执行动画

    @Bind(R.id.bulletin_view_sale)
    BulletinView mBulletinView;

    @Bind(R.id.home_my_attendance_tv)
    TextView myCheckTotal;

    @Bind(R.id.home_my_dailypager_icon_tv)
    TextView mDailyNormalCountTv;

    @Bind(R.id.home_my_dailypager_rectangle_tv)
    TextView mDailyNormalRectCountTv;

    @Bind(R.id.home_banner_frame)
    FrameLayout mBannerLayout;

    private Home203RvAdapter mMainAdapter;
    private Home203RvAdapter mOtherAdapter;

    private SubscriberOnNextErrorListener mNoticesSubscriber;//公告
    private SubscriberOnNextErrorListener mGetBackLogSubscriber; //待办
    private SubscriberOnNextErrorListener mGetMyRequestSubscriber; //我的请求
    private SubscriberOnNextErrorListener mCheckTodaySub;//检查考勤
    private SubscriberOnNextErrorListener mStatisticsSubscriber; //出勤天数
    private SubscriberOnNextErrorListener mGetUserInfoSubscriber;//用户信息

    //data
    private List<NoticeDetailBean> mList;//公告
    private String mToken;
    private static final int REFRESH_COMPLETE = 0X110;//刷新
    private List<MenuItem> mFavList;
    private List<MenuItem> mOtherList;
    private int mThisYear;
    private int mThisMonth;
    private int mToday;
    private String mDailyDate;
    private AgendaResponse mAgendaResponse; //待办数据
    private AgendaResponse myRequestResponse; //我的请求数据
    private CheckStatisticsResponse mCheckStatisticsResponse; //出勤天数数据

    private static int pageIndex = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_COMPLETE:
                    getnotice(mToken);
                    getBackLog(mToken);
                    getMyRequest(mToken);
                    getCheckToDay(mToken);
                    getCheckStatistics(mToken, mThisYear + "-" + mThisMonth);
                    getUserInfo(mToken);
                    break;
                default:
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page_203, container, false);
        ButterKnife.bind(this, view);
        mTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/STSongti-SC-Bold-02.ttf");
        mToken = App.getInstance().getToken();
        //获取像素密度
        mDensity = getResources().getDisplayMetrics().density;
        //获取布局的高度
        initData();
        initDefaultView();
        return view;
    }

    private void initData() {
        mViewHeight = getActivity().getResources().getDimension(R.dimen.margin_dp_195);//组件高度
        if (mFavList != null) {
            mFavList.clear();
        } else {
            mFavList = new ArrayList<>();
        }

        if (mOtherList != null) {
            mOtherList.clear();
        } else {
            mOtherList = new ArrayList<>();
        }

        mFavList.addAll(HomeGridPagerHelper.getPreferFavoriteList());
        mOtherList.addAll(HomeGridPagerHelper.getPreferOtherList());
        getToday();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initDefaultView() {
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        animateOpen(mOftenRv);//默认打开常用
        /***banner*/
        mBanner.setIndicatorGravity(BannerConfig.RIGHT);
        List<Integer> list = new ArrayList<>();
        list.add(R.drawable.home_banner);
        list.add(R.drawable.home_banner1);
        list.add(R.drawable.home_banner2);
        mBanner.setDelayTime(4000);
        mBanner.setIndicatorGravity(20);
        mBanner.setImages(list).setImageLoader(new ImageBannerLoader())
                .start();


        /****模块****/


        /**ScrollView**/
        final ColorAnimator mColorAnimator = new ColorAnimator(Color.TRANSPARENT, Color.WHITE);
        final ColorAnimator mColorAnimatorLine = new ColorAnimator(Color.TRANSPARENT, ContextCompat.getColor(getActivity(), R.color.color_E5E8F3));
        mScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                float fraction = (float) 2 * scrollY / (mBannerLayout.getHeight());
                int color = mColorAnimator.getFractionColor(fraction);
                int colorLine = mColorAnimatorLine.getFractionColor(fraction);
                if (color != lastColor) {
                    lastColor = color;
                    mLine.setBackgroundColor(colorLine);
                }
            }
        });

        /****/
        //常用
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);

        mMainAdapter = new Home203RvAdapter(mOftenRv);
        mOftenRv.setLayoutManager(gridLayoutManager);
        mOftenRv.setNestedScrollingEnabled(false);
        mOftenRv.setAdapter(mMainAdapter.getHeaderAndFooterAdapter());
        mMainAdapter.setData(mFavList);
        mMainAdapter.notifyDataSetChanged();
        mMainAdapter.setOnRVItemClickListener(new BGAOnRVItemClickListener() {
            @Override
            public void onRVItemClick(ViewGroup parent, View itemView, int position) {
                try {
                    Class cls = Class.forName(mFavList.get(position).getActivity());
                    Intent intent = new Intent(getActivity(), cls);
                    intent.putExtra("actDefId", mFavList.get(position).getDesc());
                    startActivity(intent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "开发中", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //其它
        GridLayoutManager otherGridLayoutManager = new GridLayoutManager(getActivity(), 4);
        otherGridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mOtherAdapter = new Home203RvAdapter(mOtherRv);
        mOtherRv.setLayoutManager(otherGridLayoutManager);
        mOtherRv.setNestedScrollingEnabled(false);
        mOtherRv.setAdapter(mOtherAdapter.getHeaderAndFooterAdapter());
        mOtherAdapter.setData(mOtherList);
        mOtherAdapter.notifyDataSetChanged();
        mOtherAdapter.setOnRVItemClickListener(new BGAOnRVItemClickListener() {
            @Override
            public void onRVItemClick(ViewGroup parent, View itemView, int position) {
                try {
                    Class cls = Class.forName(mOtherList.get(position).getActivity());
                    Intent intent = new Intent(getActivity(), cls);
                    intent.putExtra("actDefId", mOtherList.get(position).getDesc());
                    startActivity(intent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "开发中", Toast.LENGTH_SHORT).show();
                }
            }
        });


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
        //获取用户信息(考勤月日志数量getNormalDaily)
        mGetUserInfoSubscriber = new SubscriberOnNextErrorListener<OAUserBean>() {
            @Override
            public void onNext(OAUserBean oaUserBean) {
                if (oaUserBean.getNormalDaily() != 0) {
                    if (oaUserBean.getNormalDaily() >= 100){
                        mDailyNormalCountTv.setVisibility(View.GONE);
                        mDailyNormalRectCountTv.setVisibility(View.VISIBLE);
                        mDailyNormalRectCountTv.setText(oaUserBean.getNormalDaily() + "");
                    }else {
                        mDailyNormalCountTv.setVisibility(View.VISIBLE);
                        mDailyNormalCountTv.setText(oaUserBean.getNormalDaily() + "");
                        mDailyNormalRectCountTv.setVisibility(View.GONE);
                    }
                } else {
                    mDailyNormalCountTv.setVisibility(View.GONE);
                    mDailyNormalRectCountTv.setVisibility(View.GONE);
                }

                if (mSwipeLayout.isRefreshing()) {
                    mSwipeLayout.setRefreshing(false);
                }
            }

            @Override
            public void onError(String error) {

            }
        };
        //检查考勤
        mCheckTodaySub = new SubscriberOnNextErrorListener<JSONObject>() {
            @Override
            public void onNext(JSONObject jsonObject) {
                if (jsonObject != null) {
                    if (jsonObject.getInteger("checkForenoon") == 0) {
                        mCheckStatus.setText("08:30 未打卡");
                        RxView.clicks(mToCheck)
                                .throttleFirst(1, TimeUnit.SECONDS)
                                .subscribe(new Action1<Void>() {
                                    @Override
                                    public void call(Void aVoid) {
                                        Intent intent = new Intent(getActivity(), CheckWorkTabActivity.class);
                                        intent.putExtra("actDefId", "bksq:1:10000002508861");
                                        startActivity(intent);
                                    }
                                });
                    } else if (jsonObject.getInteger("checkForenoon") == 1 && jsonObject.getInteger("checkAfternoon") == 0) {
                        mCheckStatus.setText("17:30 未打卡");
                        RxView.clicks(mToCheck)
                                .throttleFirst(1, TimeUnit.SECONDS)
                                .subscribe(new Action1<Void>() {
                                    @Override
                                    public void call(Void aVoid) {
                                        Intent intent = new Intent(getActivity(), CheckWorkTabActivity.class);
                                        intent.putExtra("actDefId", "bksq:1:10000002508861");
                                        startActivity(intent);
                                    }
                                });
                    } else if (jsonObject.getInteger("checkForenoon") == 1 && jsonObject.getInteger("checkAfternoon") == 1) {
                        mCheckStatus.setText("今日已完成");
                    }

                    if (jsonObject.getInteger("havePaper") == 0) {
                        mDailyPaperStatus.setText(mThisMonth + "月" + mToday + "日日志");
                        RxView.clicks(mDaily)
                                .throttleFirst(1, TimeUnit.SECONDS)
                                .subscribe(new Action1<Void>() {
                                    @Override
                                    public void call(Void aVoid) {
                                        Intent intent = new Intent(getActivity(), DailyPaperActivity.class);
                                        intent.putExtra("dailyPaperType", 1);
                                        intent.putExtra("date",mDailyDate);
                                        startActivity(intent);
                                    }
                                });
                    } else {
                        mDailyPaperStatus.setText("今日已完成");
                    }
                }
            }

            @Override
            public void onError(String error) {

            }
        };
        getnotice(mToken);

        //待我审批
        mGetBackLogSubscriber = new SubscriberOnNextErrorListener<AgendaResponse>() {
            @Override
            public void onNext(AgendaResponse model) {
                mAgendaResponse = model;
                if (mAgendaResponse.getProcessFinishList().size() > 0) {
                    if (mAgendaResponse.getProcessFinishList().size() >= 100) {
                        mAgendaCount.setVisibility(View.GONE);
                        mAgendaRectCount.setVisibility(View.VISIBLE);
                        mAgendaRectCount.setText(mAgendaResponse.getProcessFinishList().size() + "");
                    } else {
                        mAgendaCount.setText(mAgendaResponse.getProcessFinishList().size() + "");
                        mAgendaCount.setVisibility(View.VISIBLE);
                        mAgendaRectCount.setVisibility(View.GONE);
                    }

                } else {
                    mAgendaCount.setVisibility(View.GONE);
                    mAgendaRectCount.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(String error) {

            }
        };

        //我的请求
        mGetMyRequestSubscriber = new SubscriberOnNextErrorListener<AgendaResponse>() {
            @Override
            public void onNext(AgendaResponse model) {
                myRequestResponse = model;
                int count = 0;
                for (int i = 0; i < model.getProcessFinishList().size(); i++) {
                    if (model.getProcessFinishList().get(i).getStatus() != 2) {
                        count++;
                    }
                }
                if (count > 0) {
                    if (count >= 100) {
                        myRequestCount.setVisibility(View.GONE);
                        myRequestRectCount.setVisibility(View.VISIBLE);
                        myRequestRectCount.setText(count + "");
                    } else {
                        myRequestCount.setText(count + "");
                        myRequestCount.setVisibility(View.VISIBLE);
                        myRequestRectCount.setVisibility(View.GONE);
                    }
                } else {
                    myRequestCount.setVisibility(View.GONE);
                    myRequestRectCount.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(String error) {

            }
        };
        //出勤天数
        mStatisticsSubscriber = new SubscriberOnNextErrorListener<CheckStatisticsResponse>() {
            @Override
            public void onNext(CheckStatisticsResponse model) {
                mCheckStatisticsResponse = model;
                if (mCheckStatisticsResponse.getCheckInDaysList().size() != 0) {
                    if (mCheckStatisticsResponse.getCheckInDaysList().size() >= 100) {
                        mCheckCount.setVisibility(View.GONE);
                        mCheckRectCount.setVisibility(View.VISIBLE);
                        mCheckRectCount.setText(mCheckStatisticsResponse.getCheckInDaysList().size() + "");
                    } else {
                        mCheckCount.setText(mCheckStatisticsResponse.getCheckInDaysList().size() + "");
                        mCheckCount.setVisibility(View.VISIBLE);
                        mCheckRectCount.setVisibility(View.GONE);
                    }
                } else {
                    mCheckCount.setVisibility(View.GONE);
                    mCheckRectCount.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(String error) {

            }
        };

        RxView.clicks(mOftenUseLayout)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
//                        if (isAnimating) return;
//                        //如果动画没在执行,走到这一步就将isAnimating制为true , 防止这次动画还没有执行完毕的
//                        //情况下,又要执行一次动画,当动画执行完毕后会将isAnimating制为false,这样下次动画又能执行
//                        isAnimating = true;
                        if (mOftenRv.getVisibility() == View.GONE) {
                            animateOpen(mOftenRv);
                            mOftenStatusTitle.setText("收起");
                        } else {
                            mOftenStatusTitle.setText("展开");
                            animateClose(mOftenRv);
                        }
                    }
                });

        RxView.clicks(mOtherLayout)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (mOtherRv.getVisibility() == View.GONE) {
                            animateOpen(mOtherRv);
                            mOtherUseStatusTitle.setText("收起");
                        } else {
                            mOtherUseStatusTitle.setText("展开");
                            animateClose(mOtherRv);
                        }
                    }
                });

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
        RxView.clicks(myCheckTotal)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getActivity(), CheckWorkTabActivity.class);
                        intent.putExtra("actDefId", "bksq:1:10000002508861");
                        intent.putExtra("fromMine", true);
                        startActivity(intent);
                    }
                });

        RxView.clicks(myLog)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getActivity(), CalendarActivity.class));
                    }
                });


    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                HeConfig.init("HE1807061425251413", "f1eb95e26b6042178a0bbf8e4d50ee0a");
                HeConfig.switchToFreeServerNode();
                HeWeather.getWeatherNow(getActivity(), Lang.CHINESE_SIMPLIFIED, Unit.METRIC,
                        new HeWeather.OnResultWeatherNowBeanListener() {
                            @Override
                            public void onError(Throwable e) {
                                Log.i("Log", "onError: ", e);
                                mTNum.setVisibility(View.GONE);
                                mHomeAirTxt.setVisibility(View.GONE);
                            }

                            @Override
                            public void onSuccess(List<Now> list) {
                                Log.i("Log", "onSuccess: " + new Gson().toJson(list));
                                mTNum.setVisibility(View.VISIBLE);
                                mHomeAirTxt.setVisibility(View.VISIBLE);
                                mTNum.setText(list.get(0).getNow().getFl() + "°");
                                mHomeAirTxt.setText(list.get(0).getNow().getCond_txt() + "\n" + list.get(0).getNow().getWind_dir());
                            }
                        });
            }
        });
        mHandler.sendEmptyMessage(REFRESH_COMPLETE);
//        if (pageIndex ==0) {
//            pageIndex =1;
//            mScrollView.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mScrollView.fullScroll(View.FOCUS_UP);
//                }
//            }, 2000);
//        }

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.v("LY__hidden", hidden + "");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.v("LY__hidden", "onAttach");
    }

    private void animateOpen(RecyclerView view) {
        view.setVisibility(View.VISIBLE);
        ValueAnimator animator = createDropAnimator(view, 0, (int)mViewHeight);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimating = false;
            }
        });
        animator.start();
    }

    private void animateClose(final RecyclerView view) {
        int origHeight = view.getHeight();
        ValueAnimator animator = createDropAnimator(view, origHeight, 0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
                isAnimating = false;
            }
        });
        animator.start();
    }

    private ValueAnimator createDropAnimator(final View view, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = value;
                view.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }


    /**
     * 提示框的动画
     *
     * @param showOrHinde 显示与隐藏
     */
    public void showOrHindeAnimation(RecyclerView view, boolean showOrHinde) {
        if (showOrHinde) {
            if (view.getVisibility() == View.VISIBLE) {
                return;
            } else {
                view.setVisibility(View.VISIBLE);
            }
        } else {
            if (view.getVisibility() != View.VISIBLE) {
                return;
            }
        }
        TranslateAnimation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, showOrHinde ? 0.0f : 1.0f, Animation.RELATIVE_TO_SELF, showOrHinde ? 1.0f : 0.0f
        );
        animation.setDuration(500);
        animation.setFillAfter(true);
        view.startAnimation(animation);

        if (!showOrHinde) {
            view.setVisibility(View.GONE);
        }
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

    /**
     * 获取用户信息
     *
     * @param token
     */
    public void getUserInfo(String token) {
        HttpMethods.getInstance().getUserInfo(new ProgressSubscriber<OAUserBean>(mGetUserInfoSubscriber, getActivity(), false), token);
    }

    /**
     * 获取出勤天数
     *
     * @param token
     * @param yearMonth
     */
    public void getCheckStatistics(String token, String yearMonth) {
        HttpMethods.getInstance().getCheckStatistics(new ProgressSubscriber<CheckStatisticsResponse>(mStatisticsSubscriber, getActivity(), false), token, yearMonth);
    }

    public void getToday() {
        Calendar now = Calendar.getInstance();
        mThisYear = now.get(Calendar.YEAR);
        mThisMonth = now.get(Calendar.MONTH) + 1;
        mToday = now.get(Calendar.DAY_OF_MONTH);
        String argMonth = null;
        String argToday = null;
        if (mThisMonth < 10){
            argMonth = "0" + mThisMonth;
        }else {
            argMonth = mThisMonth + "";
        }
        if (mToday < 10){
            argToday = "0" + mToday;
        }else {
            argToday = mToday +"";
        }
        mDailyDate = mThisYear + "-" + argMonth + "-" + argToday;
    }

    @Override
    public void onRefresh() {
        mHandler.sendEmptyMessage(REFRESH_COMPLETE);
    }
}
