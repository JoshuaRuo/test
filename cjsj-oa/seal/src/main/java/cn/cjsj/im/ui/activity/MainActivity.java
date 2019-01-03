package cn.cjsj.im.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.cjsj.im.R;
import cn.cjsj.im.gty.LogUtils;
import cn.cjsj.im.ui.fragment.ContactAddressFragment;
import cn.cjsj.im.ui.fragment.HomePage203Fragment;
import cn.cjsj.im.ui.fragment.MineFragment;
import cn.cjsj.im.ui.fragment.NewHomeFragment;
import cn.cjsj.im.ui.fragment.ProjectFragment;

@SuppressWarnings("deprecation")
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class MainActivity extends FragmentActivity implements
        ViewPager.OnPageChangeListener,
        View.OnClickListener{

    public static ViewPager mViewPager;
    private List<Fragment> mFragment = new ArrayList<>();
    private ImageView  mImageChats, mImageContact, mImageFind, mImageMe, mMineRed;
    private TextView mTextChats, mTextContact, mTextFind, mTextMe,mFilter;
    private ImageView mSearchImageView;
    private ImageView mNews;
    private TextView mTitle;
    private RelativeLayout mTitleLayout;
    private LinearLayout mTopLayout;
    private View mBottomLine;
    private Typeface mTypeface;
    /**
     * 会话列表的fragment
     */
    private boolean isDebug;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        isDebug = getSharedPreferences("config", MODE_PRIVATE).getBoolean("isDebug", false);
        mTypeface = Typeface.createFromAsset(getAssets(),"fonts/STSongti-SC-Bold-02.ttf");
        initViews();
        changeTextViewColor();
        changeSelectedTabState(0);
        initMainViewPager();
//        registerHomeKeyReceiver(this);
    }


    private void initViews() {
        RelativeLayout chatRLayout = (RelativeLayout) findViewById(R.id.seal_chat);
        RelativeLayout contactRLayout = (RelativeLayout) findViewById(R.id.seal_contact_list);
        RelativeLayout foundRLayout = (RelativeLayout) findViewById(R.id.seal_find);
        RelativeLayout mineRLayout = (RelativeLayout) findViewById(R.id.seal_me);
        mImageChats = (ImageView) findViewById(R.id.tab_img_chats);
        mImageContact = (ImageView) findViewById(R.id.tab_img_contact);
        mImageFind = (ImageView) findViewById(R.id.tab_img_find);
        mImageMe = (ImageView) findViewById(R.id.tab_img_me);
        mTextChats = (TextView) findViewById(R.id.tab_text_chats);
        mTextContact = (TextView) findViewById(R.id.tab_text_contact);
        mTextFind = (TextView) findViewById(R.id.tab_text_find);
        mTextMe = (TextView) findViewById(R.id.tab_text_me);
        mMineRed = (ImageView) findViewById(R.id.mine_red);
        mSearchImageView = (ImageView) findViewById(R.id.ac_iv_search);
        mNews = (ImageView) findViewById(R.id.home_message);
        mTitle = (TextView) findViewById(R.id.main_title_tv);
        mTitleLayout = (RelativeLayout) findViewById(R.id.main_title_layout);
        mBottomLine = findViewById(R.id.main_base_line_view);
        mTopLayout = findViewById(R.id.main_top_layout);
        mFilter = findViewById(R.id.home_filtrate);

        chatRLayout.setOnClickListener(this);
        contactRLayout.setOnClickListener(this);
        foundRLayout.setOnClickListener(this);
        mineRLayout.setOnClickListener(this);
        mSearchImageView.setOnClickListener(this);
        mFilter.setOnClickListener(this);
        mNews.setOnClickListener(this);
//        BroadcastManager.getInstance(mContext).addAction(MineFragment.SHOW_RED, new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                mMineRed.setVisibility(View.VISIBLE);
//            }
//        });
        mTitle.setTypeface(mTypeface);
    }


    private void initMainViewPager() {
        mViewPager = findViewById(R.id.main_viewpager);


//        mFragment.add(new NewHomeFragment());
        mFragment.add(new HomePage203Fragment());
        mFragment.add(new ProjectFragment());
        mFragment.add(new ContactAddressFragment());
//        mFragment.add(new DiscoverFragment());
        mFragment.add(new MineFragment());
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
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setOnPageChangeListener(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.debug("LY__onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.debug("LY__onStop");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.debug("LY__onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.debug("LY__onResume");
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        changeTextViewColor();
        changeSelectedTabState(position);
    }

    private void changeTextViewColor() {
        mImageChats.setBackgroundDrawable(getResources().getDrawable(R.drawable.home_icon));
        mImageContact.setBackgroundDrawable(getResources().getDrawable(R.drawable.message_icon));
        mImageFind.setBackgroundDrawable(getResources().getDrawable(R.drawable.linkman_icon));
        mImageMe.setBackgroundDrawable(getResources().getDrawable(R.drawable.mine_icon));
        mTextChats.setTextColor(Color.parseColor("#666666"));
        mTextContact.setTextColor(Color.parseColor("#666666"));
        mTextFind.setTextColor(Color.parseColor("#666666"));
        mTextMe.setTextColor(Color.parseColor("#666666"));
    }


    private void changeSelectedTabState(int position) {
        switch (position) {
            case 0:
                mTextChats.setTextColor(Color.parseColor("#0099ff"));
                mImageChats.setBackgroundDrawable(getResources().getDrawable(R.drawable.home_press_icon));
                mNews.setImageDrawable(getResources().getDrawable(R.drawable.news_icon));
                mTitle.setTextColor(Color.parseColor("#333333"));
                mTitle.setText("成交设计");
                mTitleLayout.setBackgroundColor(getResources().getColor(R.color.white));
                mBottomLine.setVisibility(View.VISIBLE);
                mTopLayout.setVisibility(View.GONE);
                mFilter.setVisibility(View.GONE);
                mNews.setVisibility(View.VISIBLE);
                break;
            case 1:
                mTextContact.setTextColor(Color.parseColor("#0099ff"));
                mImageContact.setBackgroundDrawable(getResources().getDrawable(R.drawable.message_press_icon));
                mNews.setImageDrawable(getResources().getDrawable(R.drawable.news_icon));
                mTitle.setTextColor(Color.parseColor("#333333"));
                mTitle.setText("项目");
                mTitleLayout.setBackgroundColor(getResources().getColor(R.color.white));
                mBottomLine.setVisibility(View.VISIBLE);
                mTopLayout.setVisibility(View.VISIBLE);
                mFilter.setVisibility(View.GONE);
                mNews.setVisibility(View.GONE);
                break;
            case 2:
                mTextFind.setTextColor(Color.parseColor("#0099ff"));
                mImageFind.setBackgroundDrawable(getResources().getDrawable(R.drawable.linkman_press_icon));
                mNews.setImageDrawable(getResources().getDrawable(R.drawable.news_icon));
                mTitle.setTextColor(Color.parseColor("#333333"));
                mTitle.setText("通讯录");
                mTitleLayout.setBackgroundColor(getResources().getColor(R.color.white));
                mBottomLine.setVisibility(View.VISIBLE);
                mTopLayout.setVisibility(View.VISIBLE);
                mFilter.setVisibility(View.GONE);
                mNews.setVisibility(View.GONE);
                break;
            case 3:
                mTextMe.setTextColor(Color.parseColor("#0099ff"));
                mImageMe.setBackgroundDrawable(getResources().getDrawable(R.drawable.mine_press_icon));
                mNews.setImageDrawable(getResources().getDrawable(R.drawable.news_white_icon));
                mTitle.setText("个人中心");
                mTitle.setTextColor(Color.WHITE);
                mTitleLayout.setBackground(getResources().getDrawable(R.drawable.bg_nav));
                mBottomLine.setVisibility(View.GONE);
                mTopLayout.setVisibility(View.VISIBLE);
                mFilter.setVisibility(View.GONE);
                mNews.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    long firstClick = 0;
    long secondClick = 0;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.seal_chat:
                mViewPager.setCurrentItem(0, false);
                SpringAnimation  springAnimationHome = new SpringAnimation(mImageChats, DynamicAnimation.Y);
                springAnimationHome.setStartValue(15f);
                SpringForce mSpringForce = new SpringForce();
                mSpringForce.setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
                mSpringForce.setStiffness(SpringForce.STIFFNESS_LOW);
                mSpringForce.setFinalPosition(mImageChats.getY());
                springAnimationHome.setSpring(mSpringForce);
                springAnimationHome.start();
                break;
            case R.id.seal_contact_list:
                mViewPager.setCurrentItem(1, false);
                SpringAnimation springAnimationChats = new SpringAnimation(mImageContact, DynamicAnimation.Y);
                springAnimationChats.setStartValue(15f);
                SpringForce mSpringForceChats = new SpringForce();
                mSpringForceChats.setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
                mSpringForceChats.setStiffness(SpringForce.STIFFNESS_LOW);
                mSpringForceChats.setFinalPosition(mImageContact.getY());
                springAnimationChats.setSpring(mSpringForceChats);
                springAnimationChats.start();
                break;
            case R.id.seal_find:
                mViewPager.setCurrentItem(2, false);
                SpringAnimation  springAnimationContact = new SpringAnimation(mImageFind, DynamicAnimation.Y);
                springAnimationContact.setStartValue(15f);
                SpringForce mSpringForceContact = new SpringForce();
                mSpringForceContact.setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
                mSpringForceContact.setStiffness(SpringForce.STIFFNESS_LOW);
                mSpringForceContact.setFinalPosition(mImageFind.getY());
                springAnimationContact.setSpring(mSpringForceContact);
                springAnimationContact.start();
                break;
            case R.id.seal_me:
                mViewPager.setCurrentItem(3, false);
                mMineRed.setVisibility(View.GONE);
                SpringAnimation  springAnimationMe = new SpringAnimation(mImageMe, DynamicAnimation.Y);
                springAnimationMe.setStartValue(15f);
                SpringForce mSpringForceMe  = new SpringForce();
                mSpringForceMe.setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
                mSpringForceMe.setStiffness(SpringForce.STIFFNESS_LOW);
                mSpringForceMe.setFinalPosition(mImageMe.getY());
                springAnimationMe.setSpring(mSpringForceMe);
                springAnimationMe.start();
                break;
            case R.id.ac_iv_search:
                break;
            case R.id.home_message:
                startActivity(new Intent(MainActivity.this,NewsActivity.class));
                break;

            case R.id.home_filtrate:

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




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus() && event.getAction() == MotionEvent.ACTION_UP) {
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }




}
