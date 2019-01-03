package cn.cjsj.im.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Adapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.jakewharton.rxbinding.view.RxView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.cjsj.im.App;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.AgendaResponse;
import cn.cjsj.im.gty.bean.NoticeAndIntegralBean;
import cn.cjsj.im.gty.home.entity.MenuItem;
import cn.cjsj.im.gty.http.HttpMethods;
import cn.cjsj.im.gty.subscribers.ProgressSubscriber;
import cn.cjsj.im.gty.subscribers.SubscriberOnNextErrorListener;
import cn.cjsj.im.model.NoticeDetailBean;
import cn.cjsj.im.ui.activity.NoticeDetailActivity;
import cn.cjsj.im.ui.adapter.Home203RvAdapter;
import cn.cjsj.im.ui.adapter.ImageBannerLoader;
import cn.cjsj.im.ui.adapter.NoticesAdapter;
import cn.cjsj.im.ui.widget.gridviewpager.HomeGridPagerHelper;
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
public class HomePage203Fragment extends Fragment {

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

    private int mViewHeight = 343;//组件高度
    private float mDensity;
    private boolean isFold = false;//是否是收起状态
    boolean isAnimating = false;//是否正在执行动画

    @Bind(R.id.bulletin_view_sale)
    BulletinView mBulletinView;

    private Home203RvAdapter mMainAdapter;
    private Home203RvAdapter mOtherAdapter;

    private SubscriberOnNextErrorListener mNoticesSubscriber;//公告
    private SubscriberOnNextErrorListener mGetBackLogSubscriber; //待办
    private SubscriberOnNextErrorListener mGetMyRequestSubscriber; //我的请求
    private SubscriberOnNextErrorListener mCheckTodaySub;//检查考勤

    //data
    private List<NoticeDetailBean> mList;//公告
    private String mToken;
    private static final int REFRESH_COMPLETE = 0X110;//刷新
    private List<MenuItem> mFavList;
    private List<MenuItem> mOtherList;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_COMPLETE:
                    getnotice(mToken);
//                    getBackLog(mToken);
//                    getMyRequest(mToken);
//                    getCheckToDay(mToken);
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
//        int w = View.MeasureSpec.makeMeasureSpec(0,
//                View.MeasureSpec.UNSPECIFIED);
//        int h = View.MeasureSpec.makeMeasureSpec(0,
//                View.MeasureSpec.UNSPECIFIED);
//        mOftenRv.measure(w, h);
//        int height = mOftenRv.getMeasuredHeight();
//        mViewHeight = (int) (mDensity * height + 0.5);
        initData();
        initDefaultView();
        return view;
    }

    private void initData() {
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
    }

    private void initDefaultView() {

        /***banner*/
        mBanner.setIndicatorGravity(BannerConfig.RIGHT);
        List<Integer> list = new ArrayList<>();
        list.add(R.drawable.home_banner);
        list.add(R.drawable.home_banner1);
        mBanner.setDelayTime(4000);
        mBanner.setIndicatorGravity(20);
        mBanner.setImages(list).setImageLoader(new ImageBannerLoader())
                .start();


        /****模块****/
        //常用
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);

        mMainAdapter = new Home203RvAdapter(mOftenRv);
        mOftenRv.setLayoutManager(gridLayoutManager);
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
        getnotice(mToken);

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
                            }

                            @Override
                            public void onSuccess(List<Now> list) {
                                Log.i("Log", "onSuccess: " + new Gson().toJson(list));

                                mTNum.setText(list.get(0).getNow().getFl() + "°");
                                mHomeAirTxt.setText(list.get(0).getNow().getCond_txt() + "\n" + list.get(0).getNow().getWind_dir());
                            }
                        });
            }
        });
    }


    private void animateOpen(RecyclerView view) {
        view.setVisibility(View.VISIBLE);
        ValueAnimator animator = createDropAnimator(view, 0, mViewHeight);
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

}
