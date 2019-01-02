package cn.cjsj.im.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import cn.cjsj.im.App;
import cn.cjsj.im.R;
import cn.cjsj.im.SealConst;
import cn.cjsj.im.gty.bean.IntegralBean;
import cn.cjsj.im.gty.bean.OAUserBean;
import cn.cjsj.im.gty.bean.PerformanceBean;
import cn.cjsj.im.gty.http.HttpMethods;
import cn.cjsj.im.gty.subscribers.ProgressSubscriber;
import cn.cjsj.im.gty.subscribers.SubscriberOnNextErrorListener;
import cn.cjsj.im.server.SealAction;
import cn.cjsj.im.server.broadcast.BroadcastManager;
import cn.cjsj.im.server.network.async.AsyncTaskManager;
import cn.cjsj.im.server.network.async.OnDataListener;
import cn.cjsj.im.server.network.http.HttpException;
import cn.cjsj.im.server.response.VersionResponse;
import cn.cjsj.im.server.widget.SelectableRoundedImageView;
import cn.cjsj.im.ui.activity.AboutUsActivity;
import cn.cjsj.im.ui.activity.AccountSettingActivity;
import cn.cjsj.im.ui.activity.ApplyIntegralActivity;
import cn.cjsj.im.ui.activity.CheckOnWorkActivity;
import cn.cjsj.im.ui.activity.CheckWorkTabActivity;
import cn.cjsj.im.ui.activity.DailyPaperActivity;
import cn.cjsj.im.ui.activity.FeedBackActivity;
import cn.cjsj.im.ui.activity.LocationTestActivity;
import cn.cjsj.im.ui.activity.MyAccountActivity;
import cn.cjsj.im.ui.activity.MyPerformanceActivity;
import cn.cjsj.im.utils.CleanCache;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by AMing on 16/6/21.
 * Company RongCloud
 */
public class MineFragment extends Fragment implements View.OnClickListener {
    private static final int COMPARE_VERSION = 54;
    public static final String SHOW_RED = "SHOW_RED";
    private SelectableRoundedImageView imageView;
    private TextView mName;
    private ImageView mNewVersionView;
    private boolean isHasNewVersion;
    private String url;
    private boolean isDebug;
    private TextView mCleanCacheBtn;
    private CircleImageView mHeadImg;
    private SubscriberOnNextErrorListener mGetUserInfoSubscriber;
    private SubscriberOnNextErrorListener mGetIntegralSubscriber;
    private SubscriberOnNextErrorListener mGetPerformanceSubscriber;
    private TextView mUserName, mDepartment, mIntegralTv, mPerformanceTv, mDailyPaperTv, mCheckTv;
    private LinearLayout mPerformanceLayout;
    private OAUserBean mOAUserBean;
    private IntegralBean mIntegralBean;
    private static final int UPDATE_USER_INFO = 1003;
    private static final int UPDATE_INTEGRAL = 1002;

    private SharedPreferences sp;
    private String mToken;
    private LinearLayout mIntegralLayout, mProblems, mSuggest, mDailyPaperLayout, mCheckLayout, mLocationTest, mLocationLine;
    private Intent mIntent;
    private SharedPreferences.Editor editor;
    private PerformanceBean mPerformanceBean;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_USER_INFO:
                    try {

                        mUserName.setText(mOAUserBean.getSysUser().getFullname());
                        mDepartment.setText(mOAUserBean.getSysUser().getOrgName());
                        mDailyPaperTv.setText(mOAUserBean.getNormalDaily() + "");
                        mCheckTv.setText(mOAUserBean.getNormalCheck() + "");


                        if ("陈冬全".equals(mOAUserBean.getSysUser().getFullname())) {
                            mLocationTest.setVisibility(View.VISIBLE);
                            mLocationLine.setVisibility(View.VISIBLE);
                        }
                    } catch (NullPointerException nullException) {

                    }

                    //make cache
                    editor.putString(SealConst.SEALTALK_LOGIN_NAME, mOAUserBean.getSysUser().getFullname());
                    editor.apply();
                    break;

                case UPDATE_INTEGRAL:
                    try {
                        mIntegralTv.setText(String.valueOf(mIntegralBean.getRank().getFz()));
                    } catch (NullPointerException nullException) {

                    }
                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.seal_mine_fragment, container, false);
        isDebug = getContext().getSharedPreferences("config", getContext().MODE_PRIVATE).getBoolean("isDebug", false);
        sp = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        editor = sp.edit();
        mToken = App.getInstance().getToken();
        initViews(mView);
        initData();
        BroadcastManager.getInstance(getActivity()).addAction(SealConst.CHANGEINFO, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateUserInfo();
            }
        });
//        compareVersion();
        return mView;
    }

    private void compareVersion() {
        AsyncTaskManager.getInstance(getActivity()).request(COMPARE_VERSION, new OnDataListener() {
            @Override
            public Object doInBackground(int requestCode, String parameter) throws HttpException {
                return new SealAction(getActivity()).getSealTalkVersion();
            }

            @Override
            public void onSuccess(int requestCode, Object result) {
                if (result != null) {
                    VersionResponse response = (VersionResponse) result;
                    String[] s = response.getAndroid().getVersion().split("\\.");
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < s.length; i++) {
                        sb.append(s[i]);
                    }

                    String[] s2 = getVersionInfo()[1].split("\\.");
                    StringBuilder sb2 = new StringBuilder();
                    for (int i = 0; i < s2.length; i++) {
                        sb2.append(s2[i]);
                    }
                    if (Integer.parseInt(sb.toString()) > Integer.parseInt(sb2.toString())) {
                        mNewVersionView.setVisibility(View.VISIBLE);
                        url = response.getAndroid().getUrl();
                        isHasNewVersion = true;
                        BroadcastManager.getInstance(getActivity()).sendBroadcast(SHOW_RED);
                    }
                }
            }

            @Override
            public void onFailure(int requestCode, int state, Object result) {

            }
        });
    }

    private void initData() {
        sp = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        updateUserInfo();
    }

    private void initViews(View mView) {
        imageView = (SelectableRoundedImageView) mView.findViewById(R.id.mine_header);
        mName = (TextView) mView.findViewById(R.id.mine_name);
        LinearLayout mUserProfile = (LinearLayout) mView.findViewById(R.id.new_start_user_profile);
        LinearLayout mMineSetting = (LinearLayout) mView.findViewById(R.id.mine_setting);
        LinearLayout mCleanCache = (LinearLayout) mView.findViewById(R.id.clean_cache_button);
        LinearLayout mMineService = (LinearLayout) mView.findViewById(R.id.mine_service);
        LinearLayout mMineXN = (LinearLayout) mView.findViewById(R.id.mine_xiaoneng);
        LinearLayout mAboutUs = (LinearLayout) mView.findViewById(R.id.gty_about_us_layout);
        mCleanCacheBtn = (TextView) mView.findViewById(R.id.clean_cache_size_tv);
        mHeadImg = (CircleImageView) mView.findViewById(R.id.mine_simpleDraweeView);
        mUserName = (TextView) mView.findViewById(R.id.mine_user_name_tv);
        mDepartment = (TextView) mView.findViewById(R.id.mine_department_name_tv);
        mIntegralTv = (TextView) mView.findViewById(R.id.mine_integral_tv);
        mPerformanceTv = (TextView) mView.findViewById(R.id.mine_performance_total_tv);
        mIntegralLayout = (LinearLayout) mView.findViewById(R.id.mine_integral_layout);
        mPerformanceLayout = (LinearLayout) mView.findViewById(R.id.performance_layout);
        mProblems = (LinearLayout) mView.findViewById(R.id.problems_layout);
        mSuggest = (LinearLayout) mView.findViewById(R.id.suggest_layout);
        mDailyPaperLayout = (LinearLayout) mView.findViewById(R.id.mine_dailyPaper_layout);
        mCheckLayout = (LinearLayout) mView.findViewById(R.id.mine_check_layout);
        mLocationTest = (LinearLayout) mView.findViewById(R.id.location_test);
        mLocationLine = (LinearLayout) mView.findViewById(R.id.location_line);
        mDailyPaperTv = (TextView) mView.findViewById(R.id.mine_dailyPaper);
        mCheckTv = (TextView) mView.findViewById(R.id.mine_check);
        if (isDebug) {
            mMineXN.setVisibility(View.VISIBLE);
        } else {
            mMineXN.setVisibility(View.GONE);
        }
        mUserProfile.setOnClickListener(this);
        mMineSetting.setOnClickListener(this);
        mMineService.setOnClickListener(this);
        mCleanCache.setOnClickListener(this);
        mAboutUs.setOnClickListener(this);
        mIntegralLayout.setOnClickListener(this);
        mPerformanceLayout.setOnClickListener(this);
        mProblems.setOnClickListener(this);
        mSuggest.setOnClickListener(this);
        mMineXN.setOnClickListener(this);
        mDailyPaperLayout.setOnClickListener(this);
        mLocationTest.setOnClickListener(this);
        mCheckLayout.setOnClickListener(this);
        try {
            mCleanCacheBtn.setText("清除缓存 " + CleanCache.getTotalCacheSize(getActivity()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        mGetUserInfoSubscriber = new SubscriberOnNextErrorListener<OAUserBean>() {
            @Override
            public void onNext(OAUserBean oaUserBean) {
                mOAUserBean = oaUserBean;
                mHandler.sendEmptyMessage(UPDATE_USER_INFO);
            }

            @Override
            public void onError(String error) {

            }
        };

        mGetIntegralSubscriber = new SubscriberOnNextErrorListener<IntegralBean>() {
            @Override
            public void onNext(IntegralBean integralBean) {
                mIntegralBean = integralBean;
                mHandler.sendEmptyMessage(UPDATE_INTEGRAL);
            }

            @Override
            public void onError(String error) {

            }
        };
        mGetPerformanceSubscriber = new SubscriberOnNextErrorListener<PerformanceBean>() {
            @Override
            public void onNext(PerformanceBean o) {
                mPerformanceBean = o;
                if (mPerformanceBean != null) {
                    mPerformanceTv.setText(mPerformanceBean.getCount().getMoney() + "");
                }
            }

            @Override
            public void onError(String error) {

            }
        };
        getUserInfo(mToken);
        getIntegral(mToken);
        getPerformance(mToken);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_start_user_profile:
                startActivity(new Intent(getActivity(), MyAccountActivity.class));
                break;
            case R.id.mine_setting:
                startActivity(new Intent(getActivity(), AccountSettingActivity.class));
                break;
            case R.id.clean_cache_button:
                if (CleanCache.clearAllCache(getActivity())) {
                    try {
                        mCleanCacheBtn.setText("清除缓存 " + CleanCache.getTotalCacheSize(getActivity()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.mine_integral_layout:
                Intent mIntent = new Intent(getActivity(), ApplyIntegralActivity.class);
                mIntent.putExtra("actDefId", "jfsqlc:1:10000002581337");
                startActivity(mIntent);
                break;
            case R.id.problems_layout:

                break;
            case R.id.suggest_layout:
                Intent toFeedbackIntent = new Intent(getActivity(), FeedBackActivity.class);
                toFeedbackIntent.putExtra("token", mToken);
                toFeedbackIntent.putExtra("nikName", mOAUserBean.getSysUser().getFullname());
                toFeedbackIntent.putExtra("account", mOAUserBean.getSysUser().getAccount());
                startActivity(toFeedbackIntent);
                break;

            case R.id.gty_about_us_layout:
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
                break;

            case R.id.performance_layout:
                startActivity(new Intent(getActivity(), MyPerformanceActivity.class));
//                ToastUtil.showToast(getActivity(),"开发中");
                break;

            case R.id.mine_dailyPaper_layout:
                startActivity(new Intent(getActivity(), DailyPaperActivity.class));
                break;
            case R.id.mine_check_layout:
                mIntent = new Intent(getActivity(), CheckWorkTabActivity.class);
                mIntent.putExtra("actDefId", "bksq:1:10000002508861");
                mIntent.putExtra("fromMine", true);
                startActivity(mIntent);
                break;

            case R.id.location_test:
                mIntent = new Intent(getActivity(), LocationTestActivity.class);
                startActivity(mIntent);
                break;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            try {
                mCleanCacheBtn.setText("清除缓存 " + CleanCache.getTotalCacheSize(getActivity()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void updateUserInfo() {
        String userId = sp.getString(SealConst.SEALTALK_LOGIN_ID, "");
        String username = sp.getString(SealConst.SEALTALK_LOGIN_NAME, "");
        String userPortrait = sp.getString(SealConst.SEALTALK_LOGING_PORTRAIT, "");
        mName.setText(username);
        if (!TextUtils.isEmpty(username)) {
//            String portraitUri = SealUserInfoManager.getInstance().getPortraitUri
//                    (new UserInfo(userId, username, Uri.parse(userPortrait)));
////            ImageLoader.getInstance().displayImage(portraitUri, imageView, App.getOptions());
//            ImageLoader.getInstance().displayImage(portraitUri, mHeadImg, App.getOptions());
            if (userPortrait == null || "".equals(userPortrait)){
                mHeadImg.setImageResource(R.drawable.mine_head_default_img);
            }else {
                Glide.with(this)
                        .load(Uri.parse(userPortrait))
                        .into(mHeadImg);
            }
        }
    }

    private String[] getVersionInfo() {
        String[] version = new String[2];

        PackageManager packageManager = getActivity().getPackageManager();

        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getActivity().getPackageName(), 0);
            version[0] = String.valueOf(packageInfo.versionCode);
            version[1] = packageInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
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
     * 获取积分
     *
     * @param token
     */
    public void getIntegral(String token) {
        HttpMethods.getInstance().getIntegral(new ProgressSubscriber<IntegralBean>(mGetIntegralSubscriber, getActivity(), false), token);
    }


    /**
     * 获取绩效
     *
     * @param token
     */
    public void getPerformance(String token) {
        HttpMethods.getInstance().getPerformance(new ProgressSubscriber<PerformanceBean>(mGetPerformanceSubscriber, getActivity(), false), token);
    }
}
