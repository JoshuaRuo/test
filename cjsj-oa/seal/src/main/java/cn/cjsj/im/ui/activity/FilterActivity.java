package cn.cjsj.im.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.cjsj.im.R;
import cn.cjsj.im.ui.widget.FlowLayout;
import cn.cjsj.im.ui.widget.picker.DatePicker;
import rx.functions.Action1;

/**
 * Created by LuoYang on 2018/8/1 16:36
 * 筛选
 */
public class FilterActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.filter_time_all)
    CheckedTextView mTimeAll;

    @Bind(R.id.filter_time_start)
    CheckedTextView mTimeStart;

    @Bind(R.id.filter_time_end)
    CheckedTextView mTimeEnd;

    @Bind(R.id.filter_remake)
    Button mRemake;

    @Bind(R.id.filter_submit)
    Button mSubmit;

    @Bind(R.id.filter_department_recyclerview)
    FlowLayout mDepartmentF;

    @Bind(R.id.filter_status_recyclerview)
    FlowLayout mProjectStatusF;

    private Button mBackBtn;

    private static final int SELECT_START_TIME = 12;
    private static final int SELECT_END_TIME = 13;

    private String mStartTime;
    private String mEndTime;

    private int mThisYear = 0;
    private int mThisMonth = 0;
    private int mToday = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case SELECT_START_TIME:
                    if (mStartTime.length() == 0) {
                        mTimeStart.setText("选择时间");
                    } else {
                        mTimeStart.setText(mStartTime);
                    }
                    break;

                case SELECT_END_TIME:
                    if (mEndTime.length() == 0) {
                        mTimeEnd.setText("选择时间");
                    } else {
                        mTimeEnd.setText(mEndTime);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        setTitle("筛选");
        ButterKnife.bind(this);

        mTimeAll.setOnClickListener(this);
        mTimeStart.setOnClickListener(this);
        mTimeEnd.setOnClickListener(this);

        mBackBtn = getHeadLeftButton();
        mBackBtn.setOnClickListener(this);


        RxView.clicks(mRemake)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mTimeAll.setChecked(false);
                        mTimeStart.setChecked(false);
                        mTimeEnd.setChecked(false);
                        mStartTime = "";
                        mEndTime = "";
                        mTimeStart.setText("选择时间");
                        mTimeEnd.setText("选择时间");
                    }
                });

        RxView.clicks(mSubmit)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                    }
                });
        getToday();

        final String[] datas = getResources().getStringArray(R.array.flows_test);
        for (int mI = 0; mI < datas.length; mI++) {
            final CheckedTextView mTextView = (CheckedTextView) LayoutInflater
                    .from(mContext).inflate(R.layout.flow_item_layout, mDepartmentF, false);
            mTextView.setText(datas[mI]);
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, mTextView.getText().toString(),Toast.LENGTH_SHORT).show();
                    if (mTextView.isChecked()){
                        mTextView.setChecked(false);
                    }else {
                        mTextView.setChecked(true);
                    }
                }
            });
            mDepartmentF.addView(mTextView);
        }


        for (int mI = 0; mI < datas.length; mI++) {
            final CheckedTextView mTextView = (CheckedTextView) LayoutInflater
                    .from(mContext).inflate(R.layout.flow_item_layout, mProjectStatusF, false);
            mTextView.setText(datas[mI]);
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, mTextView.getText().toString(),Toast.LENGTH_SHORT).show();
                    if (mTextView.isChecked()){
                        mTextView.setChecked(false);
                    }else {
                        mTextView.setChecked(true);
                    }
                }
            });
            mProjectStatusF.addView(mTextView);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.filter_time_all:

                if (mTimeAll.isChecked()) {
                    mTimeAll.setChecked(false);
                } else {
                    mTimeAll.setChecked(true);
                    mTimeStart.setChecked(false);
                    mTimeEnd.setChecked(false);
                    mTimeStart.setText("选择时间");
                    mTimeEnd.setText("选择时间");
                    mStartTime = "";
                    mEndTime = "";
                }
                break;

            case R.id.filter_time_start:
                if (mTimeStart.isChecked()) {
                    mTimeStart.setChecked(false);
                } else {
                    onYearMonthDayPicker(1);
                    mTimeStart.setChecked(true);
                }
                break;

            case R.id.filter_time_end:
                if (mTimeEnd.isChecked()) {
                    mTimeEnd.setChecked(false);
                } else {
                    mTimeEnd.setChecked(true);
                    onYearMonthDayPicker(2);
                }
                break;

            case R.id.btn_left:
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("test", "1");
                intent.putExtra("bundle", bundle);
                setResult(10, intent);
                finish();
                break;


        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO 自动生成的方法存根
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("test", "1");
            intent.putExtra("bundle", bundle);
            setResult(10, intent);
            finish();
            super.onBackPressed();
        }
        return false;
    }


    public void onYearMonthDayPicker(final int type) {
        final DatePicker picker = new DatePicker(this);
        picker.setCanLoop(true);
        picker.setWheelModeEnable(true);
        picker.setTopPadding(15);
        picker.setRangeStart(1990, 1, 1);
        picker.setRangeEnd(2050, 1, 1);
        picker.setSelectedItem(mThisYear, mThisMonth, mToday);
        picker.setWeightEnable(true);
        picker.setLineColor(Color.BLACK);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                Toast.makeText(FilterActivity.this, year + "-" + month + "-" + day, Toast.LENGTH_SHORT).show();
                if (type == 1) {
                    mStartTime = year + "." + month + "." + day;
                    mHandler.sendEmptyMessage(SELECT_START_TIME);
                }else if (type == 2){
                    mEndTime = year + "." + month + "." + day;
                    mHandler.sendEmptyMessage(SELECT_END_TIME);
                }
            }
        });
        picker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
            }

            @Override
            public void onDayWheeled(int index, String day) {
                picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
            }
        });
        picker.show();
    }

    public void getToday(){
        Calendar now = Calendar.getInstance();
        mThisYear = now.get(Calendar.YEAR);
        mThisMonth = now.get(Calendar.MONTH) + 1;
        mToday = now.get(Calendar.DAY_OF_MONTH);
    }

}
