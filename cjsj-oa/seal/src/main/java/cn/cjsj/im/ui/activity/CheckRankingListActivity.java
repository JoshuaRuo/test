package cn.cjsj.im.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.cjsj.im.R;
import cn.cjsj.im.ui.adapter.CheckRankViewPagerAdapter;
import cn.cjsj.im.ui.widget.ArrowTabWidget;
import rx.functions.Action1;

/**
 * Created by LuoYang on 2018/11/23 10:14
 * 考勤排行榜
 */
@SuppressWarnings("deprecation")
public class CheckRankingListActivity extends AppCompatActivity implements TabHost.TabContentFactory, TabHost.OnTabChangeListener {
    @Bind(R.id.check_rankbtn_left)
    Button mBack;

    @Bind(R.id.check_rank_host)
    TabHost mTabHost;

    @Bind(R.id.check_rank_viewpager)
    ViewPager mViewPager;

    @Bind(R.id.check_ranktv_title)
    TextView mTitle;


    private int mTabSelectedIndex = 0;// 当前时间段对应的tab(切换后所在位置)

    private CheckRankViewPagerAdapter mCheckRankViewPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_ranking);

        ButterKnife.bind(this);


        RxView.clicks(mBack)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });
//        setSupportActionBar(mToolbar);
        init();
    }

    private void init() {
        mTitle.setText("排行榜");
        mCheckRankViewPagerAdapter = new CheckRankViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mCheckRankViewPagerAdapter);
        mTabHost.setup();
        mTabHost.setOnTabChangedListener(this);

        final ArrowTabWidget widget = (ArrowTabWidget) mTabHost.getTabWidget();
                widget.setBackgroundColor(ContextCompat.getColor(this, R.color.color_1983e8));
        widget.setTabCount(mCheckRankViewPagerAdapter.getCount());

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    widget.updateArrow(position,positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                mTabSelectedIndex = position;
                mTabHost.setCurrentTab(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        createTabs();
        mViewPager.setCurrentItem(mTabSelectedIndex);

    }


    private void createTabs() {
        if (mTabHost.getTabWidget().getTabCount() > 0) {
            mTabHost.setCurrentTab(0);
            mTabHost.clearAllTabs();
        }

        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < mCheckRankViewPagerAdapter.mTileList.size(); i++) {

            TabHost.TabSpec spec = mTabHost.newTabSpec("tab" + i);
            spec.setContent(this);

            View tabLayout = inflater.inflate(R.layout.check_rank_tabhost_item, null);
            TextView title = tabLayout.findViewById(R.id.rank_tabhost_item_title);
            title.setText(mCheckRankViewPagerAdapter.mTileList.get(i));

            spec.setIndicator(tabLayout);
            mTabHost.addTab(spec);
        }
    }

    @Override
    public View createTabContent(String tag) {
        View view = new View(this);
        view.setMinimumHeight(0);
        view.setMinimumWidth(0);
        return view;
    }

    @Override
    public void onTabChanged(String tabId) {
        int newPosition = mTabHost.getCurrentTab();
        if (newPosition >= 0 && newPosition < mCheckRankViewPagerAdapter.getCount()) {
            mViewPager.setCurrentItem(newPosition, true);
        }
    }

}
