package cn.cjsj.im.ui.activity;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.cjsj.im.R;
import cn.cjsj.im.ui.fragment.DepartmentPostFragment;
import cn.cjsj.im.ui.fragment.GroupPostFragment;
import rx.functions.Action1;

/**
 * Created by LuoYang on 2018/7/17 09:22
 * 发文
 */
public class GroupPostActivity extends AppCompatActivity {
    @Bind(R.id.group_container)
    ViewPager mViewPager;

    @Bind(R.id.group_post_tabs)
    TabLayout mTabLayout;

    @Bind(R.id.group_post_appbar)
    AppBarLayout mAppBarLayout;

    @Bind(R.id.btn_left)
    Button mBack;

    @Bind(R.id.tv_title)
    TextView mTitle;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private Typeface mTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_post);
        ButterKnife.bind(this);
        mTypeface = Typeface.createFromAsset(getAssets(),"fonts/STSongti-SC-Bold-02.ttf");
        mTitle.setTypeface(mTypeface);
        mTitle.setText("发文");

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mAppBarLayout.setBackgroundColor(ContextCompat.getColor(this,R.color.white));
        getWindow().setFeatureInt(Window.FEATURE_ACTION_BAR, R.layout.layout_base);
        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);


        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.color_2293ff));
        mTabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.main_title_color_code), ContextCompat.getColor(this, R.color.color_2293ff));
        mTabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        mTabLayout.setupWithViewPager(mViewPager);
        setIndicator(mTabLayout,60,60);

        RxView.clicks(mBack)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragments = new ArrayList<>();
        private List<String> mTitles = new ArrayList<>();


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);

            mFragments.add(new GroupPostFragment());
            mFragments.add(new DepartmentPostFragment());

            mTitles.add("集团发文");
            mTitles.add("部门发文");
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
//            return PlaceholderFragment.newInstance(position + 1);

            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }
    }

    /**
     * 设置下划线的宽度
     * @param tabs
     * @param leftDip 设大值
     * @param rightDip 设大值
     */
    private static void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }
}
