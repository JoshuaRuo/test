package cn.cjsj.im.ui.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.cjsj.im.R;
import cn.cjsj.im.ui.fragment.DepartmentPostFragment;
import cn.cjsj.im.ui.fragment.GroupPostFragment;
import cn.cjsj.im.ui.fragment.ProductDetailFragment;
import cn.cjsj.im.ui.fragment.ProspectDetailFragment;

/**
 * Created by LuoYang on 2018/8/6 14:56
 * 项目策划
 */
public class ProjectScheme extends BaseActivity {

    @Bind(R.id.scheme_container)
    ViewPager mViewPager;

    @Bind(R.id.scheme_tabs)
    TabLayout mTabLayout;

    @Bind(R.id.scheme_appbar)
    AppBarLayout mAppBarLayout;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_scheme);
        ButterKnife.bind(this);
        setTitle("项目策划");

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mAppBarLayout.setBackgroundColor(ContextCompat.getColor(this,R.color.white));
        getWindow().setFeatureInt(Window.FEATURE_ACTION_BAR, R.layout.layout_base);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.color_2293ff));
        mTabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.main_title_color_code), ContextCompat.getColor(this, R.color.color_2293ff));
        mTabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        mTabLayout.setupWithViewPager(mViewPager);
        setIndicator(mTabLayout,60,60);
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

            mFragments.add(new ProductDetailFragment());
            mFragments.add(new ProspectDetailFragment());

            mTitles.add("生产详情");
            mTitles.add("勘察详情");
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
     *
     * @param tabs
     * @param leftDip  设大值
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
