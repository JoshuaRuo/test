package cn.cjsj.im.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.jakewharton.rxbinding.view.RxView;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.cjsj.im.App;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.EarlyArrvalRank;
import cn.cjsj.im.gty.bean.EarlyArrvalRankListResponse;
import cn.cjsj.im.gty.http.HttpMethods;
import cn.cjsj.im.gty.subscribers.ProgressSubscriber;
import cn.cjsj.im.gty.subscribers.SubscriberOnNextErrorListener;
import cn.cjsj.im.ui.adapter.EarlyArrvalRankAdapter;
import cn.cjsj.im.ui.widget.ScrollLinearLayoutManager;
import cn.cjsj.im.ui.widget.picker.DatePicker;
import retrofit2.http.Body;
import rx.functions.Action1;

/**
 * Created by LuoYang on 2018/11/23 10:57
 * 早到榜
 */
public class EarlyArrvalRankFragment extends Fragment {

    private View mRootView;

    @Bind(R.id.early_arrval_rank_recyclerview)
    RecyclerView mRv;

    @Bind(R.id.early_arrval_headimg)
    TextView mNameHead;

    @Bind(R.id.early_arrval_name)
    TextView mName;

    @Bind(R.id.early_arrval_org_name)
    TextView mOrgName;

    @Bind(R.id.early_arrval_rank_time)
    TextView mRankTime;

    @Bind(R.id.early_arrval_rank_no_tv)
    TextView mRankNum;

    @Bind(R.id.early_arrval_rank_no_img)
    ImageView mRankImg;

    @Bind(R.id.check_early_rank_scrollview)
    ScrollView mScrollView;

    @Bind(R.id.check_early_nothing_default)
    TextView mDefaultNothing;

    @Bind(R.id.early_rank_select_time_btn)
    TextView mSelectTimeBtn;

    private String mToken;

    private SubscriberOnNextErrorListener subscriber;

    private int mThisYear, mThisMonth, mToday;
    private String mYearMonthDay;

    private List<EarlyArrvalRank> mList;

    private EarlyArrvalRankAdapter mAdapter;
    private EarlyArrvalRank mEarlyArrvalRank;
    private int mUserRankNum;
    private static final int INIT_VIEW = 1005;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INIT_VIEW:
                    try {
                        mScrollView.setVisibility(View.VISIBLE);
                        mDefaultNothing.setVisibility(View.GONE);
                        mAdapter.setData(mList);
                        mAdapter.notifyDataSetChanged();
                        if (mUserRankNum == 1) {
                            mRankImg.setVisibility(View.VISIBLE);
                            mRankImg.setImageResource(R.mipmap.early_rank_no1);
                            mRankNum.setVisibility(View.GONE);
                        } else if (mUserRankNum == 2) {
                            mRankImg.setVisibility(View.VISIBLE);
                            mRankImg.setImageResource(R.mipmap.early_rank_no2);
                            mRankNum.setVisibility(View.GONE);
                        } else if (mUserRankNum == 3) {
                            mRankImg.setVisibility(View.VISIBLE);
                            mRankImg.setImageResource(R.mipmap.early_rank_no3);
                            mRankNum.setVisibility(View.GONE);
                        } else {
                            mRankImg.setVisibility(View.GONE);
                            mRankNum.setVisibility(View.VISIBLE);
                            mRankNum.setText(mUserRankNum + "");
                        }
                        if (mEarlyArrvalRank.getName().length() > 2) {
                            String name = mEarlyArrvalRank.getName().substring(1, 3);
                            mNameHead.setText(name);
                        } else {
                            mNameHead.setText(mEarlyArrvalRank.getName());
                        }
                        mRankTime.setText(mEarlyArrvalRank.getTime());
                        mOrgName.setText(mEarlyArrvalRank.getDepartment());
                        mName.setText(mEarlyArrvalRank.getName());
                        mSelectTimeBtn.setText(mYearMonthDay);
                    } catch (NullPointerException exception) {
                        mScrollView.setVisibility(View.GONE);
                        mDefaultNothing.setVisibility(View.VISIBLE);
                        exception.printStackTrace();
                    }

                    break;
                default:
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (null != mRootView) {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (null != parent) {
                parent.removeView(mRootView);
            }
        } else {
            mRootView = inflater.inflate(R.layout.fragment_check_early_arrval_rank, container, false);
        }

        ButterKnife.bind(this, mRootView);

        mToken = App.getInstance().getToken();
        getToday();

        subscriber = new SubscriberOnNextErrorListener<EarlyArrvalRankListResponse>() {
            @Override
            public void onNext(EarlyArrvalRankListResponse earlyArrvalRankListResponse) {
                mList = earlyArrvalRankListResponse.getCheckEarlyRankList();
                mEarlyArrvalRank = earlyArrvalRankListResponse.getUserRank();
                mUserRankNum = earlyArrvalRankListResponse.getUserRankNum();
                initView();
                mHandler.sendEmptyMessage(INIT_VIEW);
            }

            @Override
            public void onError(String error) {

            }
        };
        getEarlyArrvalList(mToken, mYearMonthDay, 100, 1);

        RxView.clicks(mSelectTimeBtn)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        onYearMonthDayPicker();
                    }
                });

        return mRootView;
    }


    private void initView() {
        mRv.setLayoutManager(new ScrollLinearLayoutManager(getActivity()));
        mAdapter = new EarlyArrvalRankAdapter(mRv);
        mRv.setAdapter(mAdapter.getHeaderAndFooterAdapter());

    }

    public void getEarlyArrvalList(String token, String yearMonthDay, int pageSize, int currentPage) {
        HttpMethods.getInstance().getEarlyArrvalList(new ProgressSubscriber<EarlyArrvalRankListResponse>(subscriber, getActivity(), false), token, yearMonthDay, pageSize, currentPage);
    }

    public void getToday() {
        Calendar now = Calendar.getInstance();
        mThisYear = now.get(Calendar.YEAR);
        mThisMonth = now.get(Calendar.MONTH) + 1;
        mToday = now.get(Calendar.DAY_OF_MONTH);

        mYearMonthDay = mThisYear + "-" + mThisMonth + "-" + mToday;
    }

    private void onYearMonthDayPicker() {
        final DatePicker picker = new DatePicker(getActivity());
        picker.setOffset(2);
        picker.setCanLoop(true);
        picker.setWheelModeEnable(true);
        picker.setTopPadding(25);
        picker.setRangeStart(1990, 1, 1);
        picker.setRangeEnd(2050, 1, 1);
        picker.setSelectedItem(mThisYear, mThisMonth, mToday);
        picker.setWeightEnable(true);
        picker.setLineColor(ContextCompat.getColor(getActivity(), R.color.color_858585));
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                Toast.makeText(getActivity(), year + "-" + month + "-" + day, Toast.LENGTH_SHORT).show();
                mYearMonthDay = year + "-" + month + "-" + day;
                getEarlyArrvalList(mToken, mYearMonthDay, 100, 1);
            }
        });
        picker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                mThisYear = Integer.parseInt(year);
                picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                mThisMonth = Integer.parseInt(month);
                picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());

            }

            @Override
            public void onDayWheeled(int index, String day) {
                mToday = Integer.parseInt(day);
                picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
            }
        });
        picker.show();
    }


}
