package cn.cjsj.im.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;



import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.cjsj.im.App;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.WebHistoryUtil;
import cn.cjsj.im.gty.WebViewInitSetting;
import cn.cjsj.im.gty.bean.LocationInfoBean;
import cn.cjsj.im.gty.common.ConstantValue;
import cn.cjsj.im.gty.http.HttpMethods;
import cn.cjsj.im.gty.subscribers.ProgressSubscriber;
import cn.cjsj.im.gty.subscribers.SubscriberOnNextErrorListener;
import cn.cjsj.im.server.utils.NLog;
import cn.cjsj.im.server.widget.LoadDialog;
import cn.cjsj.im.utils.DialogUtils;

/**
 * Created by LuoYang on 2018/3/7.
 * 考勤打卡
 */

public class CheckOnWorkActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.checking_webview)
    WebView mWebView;

    @Bind(R.id.checking_web_progressbar)
    ProgressBar mProgressBar;

    @Bind(R.id.checking_default_layout_bg)
    LinearLayout mDefaultLayout;

    private Button mBackBtn;

    private WebHistoryUtil mWebHistoryUtil;

    private int mHisIndex = 0;

    private boolean mIsApplyApproval = false;//审批进度
    private boolean mIsApplyHistory = false;//审批历史
    private String mBusinessKey;

    private boolean flag;
    private final static int RELOAD_URL = 101;
    private final static int IS_JOIN_WORK_GROUP = 102;
    private DialogUtils dialogUtils;

    private int mIsJoinWorkGroup = 0;

    private SubscriberOnNextErrorListener mSubscriberIsJoinWorking;
    private Intent mIntent;

    public boolean isContact = false;

    private String mToken;

    private String mActDefId;

    private LocationInfoBean mlocationInfoBean;
    private boolean mIsFromMine = false;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case RELOAD_URL:

                    mWebView.loadUrl(BASE_WEEKLY_URL + "?token=" + mToken + "&lot=" + mLot + "&lat=" + mLat + "&address=" + mAddress + "&actDefId=" + mActDefId);
                    break;

                case IS_JOIN_WORK_GROUP:
                    if (mIsJoinWorkGroup == 1) {
                        mWebView.setVisibility(View.VISIBLE);
                        mDefaultLayout.setVisibility(View.GONE);
                    } else {
                        mWebView.setVisibility(View.GONE);
                        mDefaultLayout.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    };

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            if (mlocationInfoBean.getLoclongtitude() > 0  && mlocationInfoBean.getLocLatitude() != 4.9E-324) {
                mWebView.loadUrl(BASE_WEEKLY_URL + "?token=" + mToken + "&lot=" + mLot + "&lat=" + mLat + "&address=" + mAddress + "&actDefId=" + mActDefId);
                Log.e("LY__url", BASE_WEEKLY_URL + "?token=" + mToken + "&lot=" + mLot + "&lat=" + mLat + "&address=" + mAddress + "&actDefId=" + mActDefId);
                mWebHistoryUtil.setUrl(BASE_WEEKLY_URL + "?token=" + mToken + "&lot=" + mLot + "&lat=" + mLat + "&address=" + mAddress + "&actDefId=" + mActDefId);

            } else {
                mHandler.postDelayed(runnable, 2000);
            }
        }
    };

    private static final String BASE_WEEKLY_URL = ConstantValue.WEB_BASE_URL + "html/attendance_card/punch_clock.html";
    private static final String BASE_STATISTICS = ConstantValue.WEB_BASE_URL + "html/attendance_card/statistics.html";
    private static final String BASE_HIS = ConstantValue.WEB_BASE_URL + "html/supplement_card/supplement_list.html";

    private double mLot;
    private double mLat;
    private String mAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_on_work);
        ButterKnife.bind(this);


        mIntent = getIntent();
        mActDefId = mIntent.getStringExtra("actDefId");

        setTitle("考勤打卡");
        dialogUtils = new DialogUtils(this);
        mBackBtn = getHeadLeftButton();
        mBackBtn.setOnClickListener(this);
        mIntent = getIntent();
        mIsFromMine = mIntent.getBooleanExtra("fromMine", false);


        mHeadRightText.setVisibility(View.INVISIBLE);
        mHeadRightText.setText("补卡历史");
        mHeadRightText.setClickable(true);
        mHeadRightText.setOnClickListener(this);


        mWebHistoryUtil = new WebHistoryUtil();

        mToken = App.getInstance().getToken();

        WebSettings webSettings = mWebView.getSettings();
        WebViewInitSetting.getInstance(this).webViewSetting(webSettings);
        WebViewInitSetting.getInstance(this).initLoad(mWebView);


        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (!mWebView.getSettings().getLoadsImagesAutomatically()) {
                    mWebView.getSettings().setLoadsImagesAutomatically(true);
                }
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("punch_clock")) {
                    mHeadRightText.setVisibility(View.INVISIBLE);
                    mHandler.postDelayed(runnable, 500);
                    mWebHistoryUtil.clearHis();
                    mWebHistoryUtil.setUrl(url);
                    return true;
                } else if (url.contains("statistics")) {
                    mHeadRightText.setText("补卡历史");
                    mHeadRightText.setVisibility(View.VISIBLE);
                    mWebHistoryUtil.clearHis();
                    mWebHistoryUtil.setUrl(url);
                    return false;
                } else if (url.contains("protocol://android.contact")) {
//                view.loadUrl("javascript:setImgUrl('"+testInt+"')");
//                view.loadUrl("javascript:callback()");
                    isContact = true;
                    return true;
                } else if (url.contains("supplement_detail.html") || url.contains("apply_reimbursement.html")) {
                    mWebHistoryUtil.setUrl(url);
                    mHisIndex++;
                    mHeadRightText.setVisibility(View.VISIBLE);
                    mHeadRightText.setText("审批进度");
                    int idUrl = url.indexOf("id=");
                    mBusinessKey = url.substring(idUrl + 3);
                    mIsApplyApproval = true;
                    return false;
                } else if (url.contains("protocol://android.finish")) {
                    isContact = false;
                    mWebHistoryUtil.clearHis();
                    mHisIndex = 0;
                    mWebView.loadUrl(BASE_STATISTICS + "?token=" + mToken + "&actDefId=" + mActDefId);
                    mWebHistoryUtil.setUrl(BASE_STATISTICS + "?token=" + mToken + "&actDefId=" + mActDefId);
                    mHeadRightText.setVisibility(View.VISIBLE);
                    mIsApplyApproval = false;
                    mIsApplyHistory = false;
                    return true;
                } else if (url.contains("apply_supplement.html")) {
                    String ur1 = url + "&actDefId=" + mActDefId;
                    view.loadUrl(ur1);
                    mWebHistoryUtil.setUrl(ur1);
                    mHisIndex++;
                    mHeadRightText.setVisibility(View.INVISIBLE);
                    return true;
                } else if (url.contains("supplement_list.html")) {
                    mWebHistoryUtil.setUrl(url);
                    mHisIndex++;
                    mHeadRightText.setVisibility(View.INVISIBLE);
                    return false;
                } else if (url.contains("attendance_rank.html")){
                    mHeadRightText.setVisibility(View.INVISIBLE);
                    mWebHistoryUtil.setUrl(url);
                    mHisIndex++;
                    return false;
                }else {

                    return false;
                }
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                if (newProgress == 100) {
                    LoadDialog.dismiss(mContext);
//                    mProgressBar.setVisibility(View.GONE);
                } else {
//                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        if (!initPermission() || !isLocationEnabled()) {
            /**百度**/
            dialogUtils.showUpgradeDialog("权限", "定位或权限未打开，去设置页面打开定位？", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent();
                    i.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(i);
                    dialogUtils.dismiss();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogUtils.dismiss();
                }
            });

        }

        mSubscriberIsJoinWorking = new SubscriberOnNextErrorListener<Integer>() {
            @Override
            public void onNext(Integer o) {
                mIsJoinWorkGroup = o;
                mHandler.sendEmptyMessage(IS_JOIN_WORK_GROUP);
            }

            @Override
            public void onError(String error) {

            }
        };

        getIsJoinWorkGroup(mToken);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                if (isContact) {
                    mWebView.loadUrl("javascript:closedPopup()");
                    mWebView.loadUrl("javascript:backValue()");
                    isContact = false;
                } else {
                    if (mHisIndex != 0) {//当webview不是处于第一页面时，返回上一个页面
                        mWebView.loadUrl(mWebHistoryUtil.getUrl(mHisIndex));
                        mWebHistoryUtil.remove(mHisIndex);
                        mHisIndex--;
//                    mWebView.reload();
                    } else {//当webview处于第一页面时,直接退出程序
                        finish();
                        mWebHistoryUtil.clearHis();
                    }
                    try {
                        if (mWebView.getUrl().contains("approval_speed.html")) {
                            mIsApplyHistory = true;
                            mIsApplyApproval = false;
                            mHeadRightText.setText("审批历史");
                            mHeadRightText.setVisibility(View.VISIBLE);

                        } else if (mWebView.getUrl().contains("supplement_detail.html") || mWebView.getUrl().contains("apply_reimbursement.html")) {
                            mIsApplyApproval = true;
                            mIsApplyHistory = false;
                            mHeadRightText.setText("审批进度");
                            mHeadRightText.setVisibility(View.VISIBLE);
                        }  else if (mWebView.getUrl().contains("supplement_list.html")) {
                            mHeadRightText.setVisibility(View.INVISIBLE);
                            mIsApplyApproval = false;
                            mIsApplyHistory = false;
                        }else if (mWebView.getUrl().contains("statistics.html")){
                            mHeadRightText.setText("补卡历史");
                            mHeadRightText.setVisibility(View.VISIBLE);
                        }
                    } catch (NullPointerException nEx) {
                        finish();
                    }
                }
                break;
            case R.id.text_right:

                if (mIsApplyApproval) {
                    mWebView.loadUrl(ConstantValue.WEB_BASE_URL + "html/apply_approval/approval_speed.html" + "?token=" + mToken + "&actDefId=" + mActDefId + "&businessKey=" + mBusinessKey);
                    mWebHistoryUtil.setUrl(ConstantValue.WEB_BASE_URL + "html/apply_approval/approval_speed.html" + "?token=" + mToken + "&actDefId=" + mActDefId + "&businessKey=" + mBusinessKey);
                    mHisIndex++;
                    mHeadRightText.setText("审批历史");
                    mIsApplyApproval = false;
                    mIsApplyHistory = true;
                } else if (mIsApplyHistory) {
                    mWebView.loadUrl(ConstantValue.WEB_BASE_URL + "html/apply_approval/approval_history.html" + "?token=" + mToken + "&actDefId=" + mActDefId + "&businessKey=" + mBusinessKey);
                    mWebHistoryUtil.setUrl(ConstantValue.WEB_BASE_URL + "html/apply_approval/approval_history.html" + "?token=" + mToken + "&actDefId=" + mActDefId + "&businessKey=" + mBusinessKey);
                    mHisIndex++;
                    mHeadRightText.setVisibility(View.INVISIBLE);
                    mIsApplyHistory = false;
                } else {
                    mWebView.loadUrl(BASE_HIS + "?token=" + mToken + "&actDefId=" + mActDefId);
                    mWebHistoryUtil.setUrl(BASE_HIS + "?token=" + mToken + "&actDefId=" + mActDefId);
                    mHisIndex++;
                    mHeadRightText.setVisibility(View.INVISIBLE);
                }
                break;


        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isContact) {
                mWebView.loadUrl("javascript:closedPopup()");
                mWebView.loadUrl("javascript:backValue()");
                isContact = false;
            } else {
                if (mHisIndex != 0) {//当webview不是处于第一页面时，返回上一个页面
                    mWebView.loadUrl(mWebHistoryUtil.getUrl(mHisIndex));
                    mWebHistoryUtil.remove(mHisIndex);
                    mHisIndex--;
//                    mWebView.reload();

                } else {//当webview处于第一页面时,直接退出程序
                    mWebHistoryUtil.clearHis();
                    super.onBackPressed();
                }
                try {
                    if (mWebView.getUrl().contains("approval_speed.html")) {
                        mIsApplyHistory = true;
                        mIsApplyApproval = false;
                        mHeadRightText.setText("审批历史");
                        mHeadRightText.setVisibility(View.VISIBLE);

                    } else if (mWebView.getUrl().contains("supplement_detail.html") || mWebView.getUrl().contains("apply_reimbursement.html")) {
                        mIsApplyApproval = true;
                        mIsApplyHistory = false;
                        mHeadRightText.setText("审批进度");
                        mHeadRightText.setVisibility(View.VISIBLE);
                    } else if (mWebView.getUrl().contains("supplement_list.html")
                            ) {
                        mHeadRightText.setVisibility(View.INVISIBLE);
                        mIsApplyApproval = false;
                        mIsApplyHistory = false;
                    }else if (mWebView.getUrl().contains("statistics.html")){
                        mHeadRightText.setText("补卡历史");
                        mHeadRightText.setVisibility(View.VISIBLE);
                    }
                    return true;
                } catch (NullPointerException nEx) {
                    finish();
                }
            }
        }
        return false;
    }

    private boolean initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //检查权限
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //请求权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                flag = true;
            }
        } else {
            flag = true;
        }
        return flag;
    }

    public boolean isLocationEnabled() {
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            if (requestCode == 1) {
                flag = grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED;
            }
            if (flag) {
                mHandler.postDelayed(runnable, 2000);
            }
        } catch (ArrayIndexOutOfBoundsException arrayIndoutEx) {
            // do nothing
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
//        initPermission();
        App.getInstance().startLocation();

        final int i = -1;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mlocationInfoBean = LocationInfoBean.getInstance();

                if (mlocationInfoBean.getLocLatitude() > 0 &&  mlocationInfoBean.getLocLatitude() != 4.9E-324) {

                    mLot = mlocationInfoBean.getLoclongtitude();
                    mLat = mlocationInfoBean.getLocLatitude();
                    try {
                        mAddress = URLEncoder.encode(mlocationInfoBean.getLocAddr(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (NullPointerException nEx){
                        nEx.printStackTrace();
                    }

                    if (!mIsFromMine) {
                        Toast.makeText(CheckOnWorkActivity.this, "获取定位中.", Toast.LENGTH_SHORT).show();
                        LoadDialog.show(mContext);
                        mHandler.postDelayed(runnable, 2000);
                    } else {
                        mIsFromMine = false;
                        mWebView.loadUrl(BASE_STATISTICS + "?token=" + mToken);
                        mWebHistoryUtil.setUrl(BASE_STATISTICS + "?token=" + mToken);
                        Log.e("LY__url", BASE_STATISTICS + "?token=" + mToken);
                    }

//                    ToastUtil.showToast(CheckOnWorkActivity.this, mLot + "==" + mLat + "==" + mAddress);

                }else {
                    Toast.makeText(CheckOnWorkActivity.this,"定位失败,请关闭页面重试,或请检查定位权限是否打开.",Toast.LENGTH_SHORT).show();
                    LoadDialog.dismiss(mContext);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mlocationInfoBean.setLocAddr("");
        App.getInstance().stopLocation();
        mWebHistoryUtil.clearHis();
    }

    public void openGPS() {
        Intent i = new Intent();
        i.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(i);
    }

    /**
     * 中文处理
     *
     * @param imageUrl
     * @return
     */
    public String transition(String imageUrl) {

        File f = new File(imageUrl);
        if (f.exists()) {
            //正常逻辑代码
        } else {
            //处理中文路径
            /*try {
                    imageUrl = URLEncoder.encode(imageUrl,UTF-8);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }*/
            imageUrl = Uri.encode(imageUrl);
        }
//        imageUrl = imageUrl.replace(%3A, :);
//        imageUrl = imageUrl.replace(%2F, /);
        return imageUrl;
    }


    /**
     * 获取是否加入考勤组
     *
     * @param token
     */
    public void getIsJoinWorkGroup(String token) {
        HttpMethods.getInstance().getIsJoinWorkGroup(new ProgressSubscriber<Integer>(mSubscriberIsJoinWorking, this, false), token);
    }
}
