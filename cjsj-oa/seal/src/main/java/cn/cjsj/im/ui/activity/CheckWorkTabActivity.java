package cn.cjsj.im.ui.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.cjsj.im.R;
import cn.cjsj.im.server.widget.LoadDialog;
import cn.cjsj.im.ui.fragment.CheckOnWorkFragment;
import cn.cjsj.im.ui.fragment.CheckOnWorkStatisticsFragment;
import rx.functions.Action1;

/**
 * Created by LuoYang on 2018/11/8 14:38
 * 考勤打卡
 */

@SuppressWarnings("deprecation")
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class CheckWorkTabActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    @Bind(R.id.check_tab_container)
    ViewPager mViewPager;
    @Bind(R.id.check_btn_left)
    Button mBack;
    @Bind(R.id.check_tv_title)
    TextView mTitle;
    private Typeface mTypeface;

    @Bind(R.id.check_work_bottom_button)
    RelativeLayout mCheckWorkBottom;

    @Bind(R.id.check_on_work_img)
    ImageView mCheckOnWorkImg;

    @Bind(R.id.check_on_work_title)
    TextView mCheckOnWorkTitle;

    @Bind(R.id.check_on_statistics_img)
    ImageView mCheckOnStatisticsImg;

    @Bind(R.id.check_on_statistics_title)
    TextView mCheckOnStatisticsTitle;


    @Bind(R.id.check_statistics_bottom_button)
    RelativeLayout mCheckStatisticsBottom;


    @Bind(R.id.check_tv_right)
    TextView mRightTv;

    private List<Fragment> mFragment = new ArrayList<>();

    private boolean mIsFromMine = false;
    private Intent mIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_work_tab);
        ButterKnife.bind(this);

        mIntent = getIntent();
        mIsFromMine = mIntent.getBooleanExtra("fromMine", false);

        mTypeface = Typeface.createFromAsset(getAssets(), "fonts/STSongti-SC-Bold-02.ttf");
        mTitle.setTypeface(mTypeface);
        mTitle.setText("考勤打卡");
        mCheckWorkBottom.setOnClickListener(this);
        mCheckStatisticsBottom.setOnClickListener(this);
        initViewPager();
        RxView.clicks(mBack)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });


        RxView.clicks(mRightTv)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(CheckWorkTabActivity.this, ReWorkAttHistoryActivity.class));
                    }
                });

        if (!mIsFromMine) {
            changeTextViewColor();
            changeSelectedTabState(0);
        } else {
            mIsFromMine = false;
            changeTextViewColor();
            changeSelectedTabState(1);
            mViewPager.setCurrentItem(1);
        }

    }


    private void initViewPager() {
        mFragment.add(new CheckOnWorkFragment());
        mFragment.add(new CheckOnWorkStatisticsFragment());

        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            @Override
            public int getCount() {
                return mFragment.size();
            }
        };

        mViewPager.setAdapter(fragmentPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setOnPageChangeListener(this);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        changeTextViewColor();
        changeSelectedTabState(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void changeTextViewColor() {
        mCheckOnWorkImg.setImageDrawable(getResources().getDrawable(R.drawable.clock_n));
        mCheckOnStatisticsImg.setImageDrawable(getResources().getDrawable(R.drawable.count_n));
        mCheckOnWorkTitle.setTextColor(ContextCompat.getColor(this, R.color.main_title_color_code));
        mCheckOnStatisticsTitle.setTextColor(ContextCompat.getColor(this, R.color.main_title_color_code));
    }

    private void changeSelectedTabState(int position) {
        switch (position) {
            case 0:
                mCheckOnWorkImg.setImageDrawable(getResources().getDrawable(R.drawable.clock_p));
                mCheckOnWorkTitle.setTextColor(ContextCompat.getColor(this, R.color.color_2293ff));
                mRightTv.setVisibility(View.GONE);
                break;
            case 1:
                mCheckOnStatisticsImg.setImageDrawable(getResources().getDrawable(R.drawable.count_s));
                mCheckOnStatisticsTitle.setTextColor(ContextCompat.getColor(this, R.color.color_2293ff));
                mRightTv.setVisibility(View.VISIBLE);

                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.check_work_bottom_button:
                mViewPager.setCurrentItem(0, false);
                mRightTv.setVisibility(View.GONE);
//                mCheckOnWorkImg.setImageDrawable(getResources().getDrawable(R.drawable.clock_p));
//                mCheckOnWorkTitle.setTextColor(ContextCompat.getColor(this, R.color.color_2293ff));
                break;
            case R.id.check_statistics_bottom_button:
                mViewPager.setCurrentItem(1, false);
                mRightTv.setVisibility(View.VISIBLE);
//                mCheckOnStatisticsImg.setImageDrawable(getResources().getDrawable(R.drawable.count_p));
//                mCheckOnStatisticsTitle.setTextColor(ContextCompat.getColor(this, R.color.color_2293ff));

                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra("systemconversation", false)) {
            mViewPager.setCurrentItem(0, false);
        }
    }
}
