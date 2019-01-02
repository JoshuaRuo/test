package cn.cjsj.im.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;


import butterknife.Bind;
import butterknife.ButterKnife;
import cn.cjsj.im.App;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.WebHistoryUtil;
import cn.cjsj.im.gty.WebViewInitSetting;
import cn.cjsj.im.gty.common.ConstantValue;
import cn.cjsj.im.utils.LimitsLoadUtils;

/**
 *
 * 周报
 * Created by LuoYang on 2017/12/12.
 */

public class WeeklyLogActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.weekly_webview)
    WebView mWebView;
    @Bind(R.id.weekly_web_progressbar)
    ProgressBar mProgressBar;

    private Intent mIntent;
    private Button mBackBtn;

    private static final String TAG = "WeeklyLogActivity";

    private static final String BASE_WEEKLY_URL = ConstantValue.WEB_BASE_URL + "html/weekly/weekly_list.html";

    private WebHistoryUtil mWebHistoryUtil; //历史工具类

    private int mHisIndex = 0; //历史角标

    private String mToken;
    private String mActDefId;

    public boolean isContact = false;

    private int mLimits = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.weekly_log_activity);
        ButterKnife.bind(this);
        setTitle("周报");
        mIntent = getIntent();
        mActDefId = mIntent.getStringExtra("actDefId");
        mLimits = LimitsLoadUtils.getInstance().getLoadTag();
        //获取缓存Token
//        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
//        mToken = sharedPreferences.getString("loginToken", "");
        WebSettings webSettings = mWebView.getSettings();
        WebViewInitSetting.getInstance(this).webViewSetting(webSettings);
        WebViewInitSetting.getInstance(this).initLoad(mWebView);
//        WebViewInitSetting.getInstance(this).synCookies(this,BASE_WEEKLY_URL,mToken);

        mWebHistoryUtil = new WebHistoryUtil();

        mToken = App.getInstance().getToken();

        mWebView.loadUrl(BASE_WEEKLY_URL + "?token=" + mToken + "&actDefId=" + mActDefId + "&limits=" + mLimits);
        mWebHistoryUtil.setUrl(BASE_WEEKLY_URL + "?token=" + mToken + "&actDefId=" + mActDefId + "&limits=" + mLimits);
        mWebView.loadUrl("javascript:clearTypeStor()");

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
                if (url.contains("weekly_detail.html")) {
                    mHeadRightText.setVisibility(View.INVISIBLE);
                    mWebHistoryUtil.setUrl(url);
                    mHisIndex++;
                    return false;
                } else if (url.contains("score_page.html")) {
                    view.loadUrl(url);
                    mWebHistoryUtil.setUrl(url);
                    mHisIndex++;
                    return true;
                } else if (url.contains("add_weekly.html")) {
                    mWebHistoryUtil.setUrl(url);
                    mHisIndex++;
                    mHeadRightText.setVisibility(View.INVISIBLE);
                    return false;
                } else if (url.contains("protocol://android.contact")) {
//                view.loadUrl("javascript:setImgUrl('"+testInt+"')");
//                view.loadUrl("javascript:callback()");
                    mHeadRightText.setVisibility(View.INVISIBLE);
                    isContact = true;
                    return true;
                } else if (url.contains("protocol://android.finish")) {
                    isContact = false;
                    mWebHistoryUtil.clearHis();
                    mHisIndex = 0;
                    mWebView.loadUrl(BASE_WEEKLY_URL + "?token=" + mToken + "&actDefId=" + mActDefId + "&limits=" + mLimits);
                    mWebHistoryUtil.setUrl(BASE_WEEKLY_URL + "?token=" + mToken + "&actDefId=" + mActDefId + "&limits=" + mLimits);
                    return true;
                } else {
                    return false;
                }


            }

        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        mHeadRightText.setVisibility(View.VISIBLE);
        mHeadRightText.setText("写周报");
        mHeadRightText.setClickable(true);
        mHeadRightText.setOnClickListener(this);

        mBackBtn = getHeadLeftButton();
        mBackBtn.setOnClickListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.loadUrl(BASE_WEEKLY_URL + "?token=" + mToken + "&actDefId=" + mActDefId + "&limits=" + mLimits);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_right:
                mIntent = new Intent(this, AddWeeklyLogActivity.class);
                mIntent.putExtra("actDefId", mActDefId);
                startActivity(mIntent);
                break;

            case R.id.btn_left:
                if (isContact) {
                    mWebView.loadUrl("javascript:closedPopup()");
                    mWebView.loadUrl("javascript:backValue()");
                    isContact = false;
                } else {
                    if (mHisIndex != 0) {//当webview不是处于第一页面时，返回上一个页面
                        mWebView.loadUrl(mWebHistoryUtil.getUrl(mHisIndex));
                        mHisIndex--;
//                    mWebView.reload();
                    } else {//当webview处于第一页面时,直接退出程序
                        finish();
                        mWebHistoryUtil.clearHis();
                        mWebView.loadUrl("javascript:clearTypeStor()");
                    }
                }
                if (mWebView.getUrl().contains("weekly_list.html")) {
                    mHeadRightText.setVisibility(View.VISIBLE);
                }
                Log.d("SyncHttpClient", mWebView.getUrl());

                break;

            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO 自动生成的方法存根
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isContact) {
                mWebView.loadUrl("javascript:closedPopup()");
                mWebView.loadUrl("javascript:backValue()");
                isContact = false;
            } else {
                if (mHisIndex != 0) {//当webview不是处于第一页面时，返回上一个页面
                    mWebView.loadUrl(mWebHistoryUtil.getUrl(mHisIndex));
                    mHisIndex--;
//                mWebView.reload();
                    if (mWebView.getUrl().contains("weekly_list.html")) {
                        mHeadRightText.setVisibility(View.VISIBLE);
                    }
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        mWebView.reload();
//                    }
//                });
                    return true;
                } else {//当webview处于第一页面时,直接退出程序
                    mWebHistoryUtil.clearHis();
                    super.onBackPressed();
                    mWebView.loadUrl("javascript:clearTypeStor()");
                }
            }

        }
        return false;
    }


}
