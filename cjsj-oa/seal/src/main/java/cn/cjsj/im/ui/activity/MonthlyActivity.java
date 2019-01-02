package cn.cjsj.im.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
 * @author LuoYang 2018.01.09
 * 月报
 */
public class MonthlyActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.monthly_webview)
    WebView mWebView;

    @Bind(R.id.monthly_web_progressbar)
    ProgressBar mProgressBar;

    private String mToken;

    private static final String TAG = "MonthlyActivity";

    private static final String BASE_WEEKLY_URL = ConstantValue.WEB_BASE_URL + "html/monthly/monthly_list.html";

    private WebHistoryUtil mWebHistoryUtil; //历史工具类

    private int mHisIndex = 0; //历史角标

    private Intent mIntent;

    private Button mBackBtn;

    private String mActDefId;

    public boolean isContact = false;

    private int mLimits = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly);

        ButterKnife.bind(this);
        setTitle("月报");
        mIntent = getIntent();
        mActDefId = mIntent.getStringExtra("actDefId");

        mToken = App.getInstance().getToken();

        mLimits = LimitsLoadUtils.getInstance().getLoadTag();

        WebSettings webSettings = mWebView.getSettings();
        WebViewInitSetting.getInstance(this).webViewSetting(webSettings);
        WebViewInitSetting.getInstance(this).initLoad(mWebView);

        mWebHistoryUtil = new WebHistoryUtil();

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
                if (url.contains("monthly_detail.html")) {
                    mHeadRightText.setVisibility(View.INVISIBLE);
                    mWebHistoryUtil.setUrl(url);
                    mHisIndex ++;
                    return false;
                }else if(url.contains("add_monthly.html")){
                    mWebHistoryUtil.setUrl(url);
                    mHisIndex++;
                    mHeadRightText.setVisibility(View.INVISIBLE);
                    return false  ;
                }else if (url.contains("protocol://android.contact")) {
//                view.loadUrl("javascript:setImgUrl('"+testInt+"')");
//                view.loadUrl("javascript:callback()");
                    mHeadRightText.setVisibility(View.INVISIBLE);
                    isContact = true;
                    return true;
                }else if(url.contains("protocol://android.finish")){
                    isContact = false;
                    mWebHistoryUtil.clearHis();
                    mHisIndex = 0;
                    mWebView.loadUrl(BASE_WEEKLY_URL + "?token=" + mToken + "&actDefId=" + mActDefId + "&limits=" + mLimits);
                    mWebHistoryUtil.setUrl(BASE_WEEKLY_URL + "?token=" + mToken + "&actDefId=" + mActDefId + "&limits=" + mLimits);
                    return true;
                }else {
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
        mHeadRightText.setText("写月报");
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
        switch (v.getId()){
            case R.id.text_right:
                mIntent = new Intent(this, AddMonthlyActivity.class);
                mIntent.putExtra("actDefId",mActDefId);
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
                if (mWebView.getUrl().contains("monthly_list.html")) {
                    mHeadRightText.setVisibility(View.VISIBLE);
                }
                Log.d("SyncHttpClient", mWebView.getUrl());
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
                    if (mWebView.getUrl().contains("monthly_list.html")) {
                        mHeadRightText.setVisibility(View.VISIBLE);
                    }
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
