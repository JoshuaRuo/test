package cn.cjsj.im.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.jakewharton.rxbinding.view.RxView;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.cjsj.im.App;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.CheckStatisticsResponse;
import cn.cjsj.im.gty.http.HttpMethods;
import cn.cjsj.im.gty.subscribers.ProgressSubscriber;
import cn.cjsj.im.gty.subscribers.SubscriberOnNextErrorListener;
import cn.cjsj.im.server.widget.LoadDialog;
import cn.cjsj.im.ui.activity.CheckRankingListActivity;
import cn.cjsj.im.ui.activity.FilterActivity;
import cn.cjsj.im.ui.activity.ReWorkAttendanceCardActivity;
import cn.cjsj.im.ui.viewholder.CheckStatisticTreeViewHolder;
import cn.cjsj.im.ui.widget.picker.DatePicker;
import cn.cjsj.im.ui.widget.treeview.model.StatisticsTreeNode;
import cn.cjsj.im.ui.widget.treeview.view.AndroidTreeView;
import cn.cjsj.im.ui.widget.treeview.view.StatisticsAndroidTreeView;
import rx.functions.Action1;

/**
 * Created by LuoYang on 2018/11/8 15:10
 * 打卡统计
 */
public class CheckOnWorkStatisticsFragment extends Fragment {

    private View rootView;
    private SubscriberOnNextErrorListener mStatisticsSubscriber;

    private String mToken;

    @Bind(R.id.check_to_rank_btn)
    RelativeLayout mToRankBtn;

    @Bind(R.id.check_select_month_btn)
    TextView mSelectMonthBtn;

    @Bind(R.id.check_month_value_tv)
    TextView mCheckMonthValue;

    @Bind(R.id.check_work_statistics_scrollview)
    ScrollView mScrollView;

    private StatisticsAndroidTreeView mTreeView;
    private ViewGroup mContainerView;
    private StatisticsTreeNode mRootNode;

    private int mThisYear = 0;
    private int mThisMonth = 0;
    private int mToday = 0;
    private String mSelectTimeValue;

    private CheckStatisticsResponse mCheckStatisticsResponse;

    private static final int SELECT_VALUE_TIME = 2001;

    private Intent mIntent;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case SELECT_VALUE_TIME:

                    mSelectMonthBtn.setText(mSelectTimeValue);
                    getCheckStatistics(mToken, mSelectTimeValue);
                    break;

                default:
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.fragment_check_work_statistics, container, false);
        }
        ButterKnife.bind(this, rootView);
        mToken = App.getInstance().getToken();
        getToday();


        mStatisticsSubscriber = new SubscriberOnNextErrorListener<CheckStatisticsResponse>() {
            @Override
            public void onNext(CheckStatisticsResponse checkStatisticsResponse) {
                mCheckStatisticsResponse = checkStatisticsResponse;
                mSelectTimeValue = checkStatisticsResponse.getCurrentCheckDate();
                mThisMonth = Integer.parseInt(checkStatisticsResponse.getCurrentCheckDate().split("-")[1]);
                mSelectMonthBtn.setText(mSelectTimeValue);
                if (mThisMonth == 1) {
                    mCheckMonthValue.setText("(" + "12" + "/21~" + mThisMonth + "/20)");
                } else {
                    mCheckMonthValue.setText("(" + (mThisMonth - 1) + "/21~" + mThisMonth + "/20)");
                }

                mContainerView = rootView.findViewById(R.id.statistics_relativelayout);
                if (mRootNode != null && mTreeView != null) {
                    mRootNode = null;
                    mTreeView = null;
                    mContainerView.removeAllViews();
                }
                mRootNode = StatisticsTreeNode.root();

                //出勤天数
                StatisticsTreeNode checkInDay = new StatisticsTreeNode(new CheckStatisticTreeViewHolder.StatisticsListHolder("出勤", mCheckStatisticsResponse.getCheckInDaysList().size(), "天")).setViewHolder(new CheckStatisticTreeViewHolder(getActivity()));
                StatisticsTreeNode checkInDayChild;
                for (int i = 0; i < mCheckStatisticsResponse.getCheckInDaysList().size(); i++) {
                    checkInDayChild = new StatisticsTreeNode(new CheckStatisticTreeViewHolder.StatisticsListHolder(mCheckStatisticsResponse.getCheckInDaysList().get(i), 0, "")).setViewHolder(new CheckStatisticTreeViewHolder(getActivity()));
                    checkInDay.addChildren(checkInDayChild);
                }

                //休息天数
                StatisticsTreeNode restDays = new StatisticsTreeNode(new CheckStatisticTreeViewHolder.StatisticsListHolder("休息", mCheckStatisticsResponse.getRestDaysList().size(), "天")).setViewHolder(new CheckStatisticTreeViewHolder(getActivity()));
                StatisticsTreeNode restDaysChild;

                for (int restDayInt = 0; restDayInt < mCheckStatisticsResponse.getRestDaysList().size(); restDayInt++) {
                    restDaysChild = new StatisticsTreeNode(new CheckStatisticTreeViewHolder.StatisticsListHolder(mCheckStatisticsResponse.getRestDaysList().get(restDayInt), 0, "")).setViewHolder(new CheckStatisticTreeViewHolder(getActivity()));
                    restDays.addChildren(restDaysChild);
                }


                //出差天数
                StatisticsTreeNode businessTrip = new StatisticsTreeNode(new CheckStatisticTreeViewHolder.StatisticsListHolder("出差", mCheckStatisticsResponse.getBusinessTripDaysList().size(), "天")).setViewHolder(new CheckStatisticTreeViewHolder(getActivity()));
                StatisticsTreeNode businessTripChild;
                for (int busInt = 0; busInt < mCheckStatisticsResponse.getBusinessTripDaysList().size(); busInt++) {
                    businessTripChild = new StatisticsTreeNode(new CheckStatisticTreeViewHolder.StatisticsListHolder(mCheckStatisticsResponse.getBusinessTripDaysList().get(busInt), 0, "")).setViewHolder(new CheckStatisticTreeViewHolder(getActivity()));
                    businessTrip.addChildren(businessTripChild);
                }


                //旷工天数
                StatisticsTreeNode absenteeismDays = new StatisticsTreeNode(new CheckStatisticTreeViewHolder.StatisticsListHolder("旷工", mCheckStatisticsResponse.getAbsenteeismDaysList().size(), "天")).setViewHolder(new CheckStatisticTreeViewHolder(getActivity()));
                StatisticsTreeNode absenteeismDaysChild;
                for (int absenteeismDaysInt = 0; absenteeismDaysInt < mCheckStatisticsResponse.getAbsenteeismDaysList().size(); absenteeismDaysInt++) {
                    absenteeismDaysChild = new StatisticsTreeNode(new CheckStatisticTreeViewHolder.StatisticsListHolder(mCheckStatisticsResponse.getAbsenteeismDaysList().get(absenteeismDaysInt), 0, "")).setViewHolder(new CheckStatisticTreeViewHolder(getActivity()));

                    //跳旷工补卡页面
                    if (mCheckStatisticsResponse.getAbsenteeismDaysList().size() != 0) {
                        final int finalAbsenteeismDaysInt = absenteeismDaysInt;
                        absenteeismDaysChild.setClickListener(new StatisticsTreeNode.TreeNodeClickListener() {
                            @Override
                            public void onClick(StatisticsTreeNode node, Object value) {
                                mIntent = new Intent(getActivity(), ReWorkAttendanceCardActivity.class);
                                mIntent.putExtra("reTypeName", "旷工补卡");
                                mIntent.putExtra("type", 2);
                                mIntent.putExtra("time", mCheckStatisticsResponse.getAbsenteeismDaysList().get(finalAbsenteeismDaysInt));
                                startActivity(mIntent);

                            }
                        });
                    }
                    absenteeismDays.addChildren(absenteeismDaysChild);
                }


                //迟到天数
                StatisticsTreeNode lateDays = new StatisticsTreeNode(new CheckStatisticTreeViewHolder.StatisticsListHolder("迟到", mCheckStatisticsResponse.getLateDaysList().size(), "次")).setViewHolder(new CheckStatisticTreeViewHolder(getActivity()));
                StatisticsTreeNode lateDaysChild;
                for (int lateDaysInt = 0; lateDaysInt < mCheckStatisticsResponse.getLateDaysList().size(); lateDaysInt++) {
                    lateDaysChild = new StatisticsTreeNode(new CheckStatisticTreeViewHolder.StatisticsListHolder(mCheckStatisticsResponse.getLateDaysList().get(lateDaysInt), 0, "")).setViewHolder(new CheckStatisticTreeViewHolder(getActivity()));


                    //跳迟到补卡页面
                    if (mCheckStatisticsResponse.getLateDaysList().size() != 0) {
                        final int finalLateDaysInt = lateDaysInt;
                        lateDaysChild.setClickListener(new StatisticsTreeNode.TreeNodeClickListener() {
                            @Override
                            public void onClick(StatisticsTreeNode node, Object value) {
                                mIntent = new Intent(getActivity(), ReWorkAttendanceCardActivity.class);
                                mIntent.putExtra("reTypeName", "迟到补卡");
                                mIntent.putExtra("type", 3);
                                mIntent.putExtra("time", mCheckStatisticsResponse.getLateDaysList().get(finalLateDaysInt));
                                startActivity(mIntent);

                            }
                        });
                    }

                    lateDays.addChildren(lateDaysChild);
                }

                //早退次数
                StatisticsTreeNode earlyDays = new StatisticsTreeNode(new CheckStatisticTreeViewHolder.StatisticsListHolder("早退", mCheckStatisticsResponse.getLeaveEarlyDaysList().size(), "次")).setViewHolder(new CheckStatisticTreeViewHolder(getActivity()));
                StatisticsTreeNode earlyDaysChild;
                for (int earlyDaysInt = 0; earlyDaysInt < mCheckStatisticsResponse.getLeaveEarlyDaysList().size(); earlyDaysInt++) {
                    earlyDaysChild = new StatisticsTreeNode(new CheckStatisticTreeViewHolder.StatisticsListHolder(mCheckStatisticsResponse.getLeaveEarlyDaysList().get(earlyDaysInt), 0, "")).setViewHolder(new CheckStatisticTreeViewHolder(getActivity()));

                    //跳早退补卡页面
                    if (mCheckStatisticsResponse.getLeaveEarlyDaysList().size() != 0) {
                        final int finalEarlyDaysInt = earlyDaysInt;
                        earlyDaysChild.setClickListener(new StatisticsTreeNode.TreeNodeClickListener() {
                            @Override
                            public void onClick(StatisticsTreeNode node, Object value) {
                                mIntent = new Intent(getActivity(), ReWorkAttendanceCardActivity.class);
                                mIntent.putExtra("reTypeName", "早退补卡");
                                mIntent.putExtra("type", 4);
                                mIntent.putExtra("time", mCheckStatisticsResponse.getLeaveEarlyDaysList().get(finalEarlyDaysInt));
                                startActivity(mIntent);

                            }
                        });
                    }

                    earlyDays.addChildren(earlyDaysChild);
                }


                //缺卡次数
                final StatisticsTreeNode lackCardTimes = new StatisticsTreeNode(new CheckStatisticTreeViewHolder.StatisticsListHolder("缺卡", mCheckStatisticsResponse.getLackCardDaysList().size(), "次")).setViewHolder(new CheckStatisticTreeViewHolder(getActivity()));
                StatisticsTreeNode lackCardTimesChild;
                for (int lackCardInt = 0; lackCardInt < mCheckStatisticsResponse.getLackCardDaysList().size(); lackCardInt++) {
                    lackCardTimesChild = new StatisticsTreeNode(new CheckStatisticTreeViewHolder.StatisticsListHolder(mCheckStatisticsResponse.getLackCardDaysList().get(lackCardInt).get("lackCardDay"), 0, "")).setViewHolder(new CheckStatisticTreeViewHolder(getActivity()));

                    //跳缺卡补卡页面
                    if (mCheckStatisticsResponse.getLackCardDaysList().size() != 0) {
                        final int finalLackCardInt = lackCardInt;
                        lackCardTimesChild.setClickListener(new StatisticsTreeNode.TreeNodeClickListener() {
                            @Override
                            public void onClick(StatisticsTreeNode node, Object value) {
                                mIntent = new Intent(getActivity(), ReWorkAttendanceCardActivity.class);
                                mIntent.putExtra("reTypeName", "缺卡补卡");
                                if ("0".equals(mCheckStatisticsResponse.getLackCardDaysList().get(finalLackCardInt).get("lackCardType"))){
                                    mIntent.putExtra("type", 0);
                                }else {
                                    mIntent.putExtra("type", 1);
                                }
                                mIntent.putExtra("time", mCheckStatisticsResponse.getLackCardDaysList().get(finalLackCardInt).get("lackCardDay"));
                                startActivity(mIntent);

                            }
                        });
                    }
                    lackCardTimes.addChildren(lackCardTimesChild);
                }
                    lackCardTimes.setClickListener(new StatisticsTreeNode.TreeNodeClickListener() {
                        @Override
                        public void onClick(StatisticsTreeNode node, Object value) {
                            if (!lackCardTimes.isExpanded() && mCheckStatisticsResponse.getLackCardDaysList().size() != 0){
                                mScrollView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                                    }
                                });
                            }
                        }
                    });




                //请假次数
                StatisticsTreeNode leaveTimes = new StatisticsTreeNode(new CheckStatisticTreeViewHolder.StatisticsListHolder("请假", mCheckStatisticsResponse.getAskLeaveDaysList().size(), "次")).setViewHolder(new CheckStatisticTreeViewHolder(getActivity()));
                StatisticsTreeNode leaveTimesChild;
                for (int leaveInt = 0; leaveInt < mCheckStatisticsResponse.getAskLeaveDaysList().size(); leaveInt++) {
                    leaveTimesChild = new StatisticsTreeNode(new CheckStatisticTreeViewHolder.StatisticsListHolder(mCheckStatisticsResponse.getAskLeaveDaysList().get(leaveInt), 0, "")).setViewHolder(new CheckStatisticTreeViewHolder(getActivity()));
                    leaveTimes.addChildren(leaveTimesChild);
                }

                //外请打卡
                StatisticsTreeNode fieldClockDays = new StatisticsTreeNode(new CheckStatisticTreeViewHolder.StatisticsListHolder("外勤", mCheckStatisticsResponse.getFieldClockDaysList().size(), "次")).setViewHolder(new CheckStatisticTreeViewHolder(getActivity()));
                StatisticsTreeNode fieldClockDaysChild;
                for (int fieldInt = 0; fieldInt < mCheckStatisticsResponse.getFieldClockDaysList().size(); fieldInt++) {
                    fieldClockDaysChild = new StatisticsTreeNode(new CheckStatisticTreeViewHolder.StatisticsListHolder(mCheckStatisticsResponse.getFieldClockDaysList().get(fieldInt), 0, "")).setViewHolder(new CheckStatisticTreeViewHolder(getActivity()));
                    fieldClockDays.addChildren(fieldClockDaysChild);
                }


                mRootNode.addChildren(checkInDay, restDays, businessTrip,absenteeismDays,leaveTimes,fieldClockDays,lateDays, earlyDays, lackCardTimes);

                mTreeView = new StatisticsAndroidTreeView(getActivity(), mRootNode);
                mTreeView.setDefaultAnimation(true);
                mTreeView.setDefaultContainerStyle(R.style.TreeNodeStyleDivided, true);
                mContainerView.addView(mTreeView.getView());
            }

            @Override
            public void onError(String error) {

            }
        };
//        if (savedInstanceState != null) {
//            String state = savedInstanceState.getString("statistic");
//            if (!TextUtils.isEmpty(state)) {
//                mTreeView.restoreState(state);
//            }
//        }




        RxView.clicks(mToRankBtn)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(getActivity(), CheckRankingListActivity.class));
                    }
                });
        RxView.clicks(mSelectMonthBtn)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        onYearMonthDayPicker();
                    }
                });

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            getCheckStatistics(mToken, "");
        }
    }

    public void getCheckStatistics(String token, String yearMonth) {
        HttpMethods.getInstance().getCheckStatistics(new ProgressSubscriber<CheckStatisticsResponse>(mStatisticsSubscriber, getActivity(), false), token, yearMonth);
    }

    public void onYearMonthDayPicker() {
        final DatePicker picker = new DatePicker(getActivity(), DatePicker.YEAR_MONTH);
        picker.setOffset(2);
        picker.setCanLoop(true);
        picker.setWheelModeEnable(false);
        picker.setTopPadding(25);
        picker.setRangeStart(1990, 1);
        picker.setRangeEnd(2050, 1);
        picker.setSelectedItem(mThisYear, mThisMonth);
        picker.setWeightEnable(true);
        picker.setLineColor(ContextCompat.getColor(getActivity(),R.color.color_858585));
        picker.setOnDatePickListener(new DatePicker.OnYearMonthPickListener() {
            @Override
            public void onDatePicked(String year, String month) {
                Toast.makeText(getActivity(), year + "-" + month, Toast.LENGTH_SHORT).show();
                mSelectTimeValue = year + "-" + month;
                mHandler.sendEmptyMessage(SELECT_VALUE_TIME);
            }
        });
        picker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                mThisYear = Integer.parseInt(year);
                picker.setTitleText(year + "-" + picker.getSelectedMonth());
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                mThisMonth = Integer.parseInt(month);
                picker.setTitleText(picker.getSelectedYear() + "-" + month);

            }

            @Override
            public void onDayWheeled(int index, String day) {
//                picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
            }
        });
        picker.show();
    }

//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//            outState.putString("statistic", mTreeView.getSaveState());
//
//    }


    @Override
    public void onResume() {
        super.onResume();
        mHandler.sendEmptyMessage(SELECT_VALUE_TIME);
    }

    public void getToday() {
        Calendar now = Calendar.getInstance();
        mThisYear = now.get(Calendar.YEAR);
        mThisMonth = now.get(Calendar.MONTH) + 1;
        mToday = now.get(Calendar.DAY_OF_MONTH);
    }
}
