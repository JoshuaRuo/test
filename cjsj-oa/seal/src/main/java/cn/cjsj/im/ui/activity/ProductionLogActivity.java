package cn.cjsj.im.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
 * 生产日志
 * Created by LuoYang on 2018/1/23.
 */

public class ProductionLogActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.production_webview)
    WebView mWebView;

    @Bind(R.id.production_web_progressbar)
    ProgressBar mProgressBar;

    private static final String BASE_WEEKLY_URL = ConstantValue.WEB_BASE_URL + "html/product_log/log_list.html";

    private static final String TAG = "ProductionLogActivity";

    private Button mBackBtn;

    private String mToken;

    private WebHistoryUtil mWebHistoryUtil; //历史工具类
    private int mHisIndex = 0; //历史角标

    private Intent mIntent;
    private String mActDefId;

    private boolean isContact = false;
    private boolean isProductName = false;

    private int mLimits = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_production_log);
        ButterKnife.bind(this);
        setTitle("生产日志");

        mBackBtn = getHeadLeftButton();
        mBackBtn.setOnClickListener(this);

        mIntent = getIntent();
        mActDefId = mIntent.getStringExtra("actDefId");

        mLimits = LimitsLoadUtils.getInstance().getLoadTag();

        mHeadRightText.setVisibility(View.VISIBLE);
        mHeadRightText.setText("新增");
        mHeadRightText.setClickable(true);
        mHeadRightText.setOnClickListener(this);

        mWebHistoryUtil = new WebHistoryUtil();

        mToken = App.getInstance().getToken();


        WebSettings webSettings = mWebView.getSettings();
        WebViewInitSetting.getInstance(this).webViewSetting(webSettings);
        WebViewInitSetting.getInstance(this).initLoad(mWebView);

        mWebView.loadUrl(BASE_WEEKLY_URL + "?token=" + mToken + "&actDefId=" + mActDefId + "&limits=" + mLimits);
        mWebHistoryUtil.setUrl(BASE_WEEKLY_URL + "?token=" + mToken + "&actDefId=" + mActDefId + "&limits=" + mLimits);

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

                if (url.contains("protocol://android.contact")) {
                    isContact = true;
                    return true;
                } else if (url.contains("protocol://android.finish")) {
//                    finish();
                    isContact = false;
                    mHisIndex = 0;
                    mWebHistoryUtil.clearHis();
                    mWebView.loadUrl(BASE_WEEKLY_URL + "?token=" + mToken + "&actDefId=" + mActDefId + "&limits=" + mLimits);
                    mWebHistoryUtil.setUrl(BASE_WEEKLY_URL + "?token=" + mToken + "&actDefId=" + mActDefId + "&limits=" + mLimits);
                    return true;
                } else if(url.contains("log_detail")){
                    mHeadRightText.setVisibility(View.INVISIBLE);
                    mWebHistoryUtil.setUrl(url);
                    mHisIndex++;
                    return false;
                } else if(url.contains("protocol://android.product")) {
                    isProductName = true;
                    return true;
                }else if(url.contains("add_log.html")){
                    mWebHistoryUtil.setUrl(url);
                    mHisIndex++;
                    mHeadRightText.setVisibility(View.INVISIBLE);
                    return false;
                }else if(url.contains("protocol://android.closewindow")){//点击ITEM选择后关闭窗口
                    isProductName = false;
                    isContact = false;
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                if (isProductName){
                    mWebView.loadUrl("javascript:eventTypeback()");
                    isProductName = false;
                }else if (isContact) {
                    mWebView.loadUrl("javascript:closedOwenPopUp()");
                    mWebView.loadUrl("javascript:backValue()");
                    isContact = false;
                } else {
                    if (mHisIndex != 0) {
                        mWebView.loadUrl(mWebHistoryUtil.getUrl(mHisIndex));
                        mWebHistoryUtil.remove(mHisIndex);
                        mHisIndex--;
                    } else {
                        finish();
                        mWebHistoryUtil.clearHis();
                        mWebView.loadUrl("javascript:delCookie()");
                    }
                }
                if (mWebView.getUrl().contains("log_list.html")) {
                    mHeadRightText.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.text_right:
                mWebView.loadUrl(ConstantValue.WEB_BASE_URL + "html/product_log/add_log.html" + "?token=" + mToken + "&actDefId=" + mActDefId);
                mWebHistoryUtil.setUrl(ConstantValue.WEB_BASE_URL + "html/product_log/add_log.html" + "?token=" + mToken + "&actDefId=" + mActDefId);
                mHisIndex++;
                mHeadRightText.setVisibility(View.INVISIBLE);
                break;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO 自动生成的方法存根
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (isProductName){
                mWebView.loadUrl("javascript:eventTypeback()");
                isProductName = false;
                return true;
            }else if (isContact) {
                mWebView.loadUrl("javascript:closedOwenPopUp()");
                mWebView.loadUrl("javascript:backValue()");
                isContact = false;
                return true;
            } else {
                if (mHisIndex != 0) {//当webview不是处于第一页面时，返回上一个页面
                    mWebView.loadUrl(mWebHistoryUtil.getUrl(mHisIndex));
                    mWebHistoryUtil.remove(mHisIndex);
                    mHisIndex--;
                    if (mWebView.getUrl().contains("log_list.html")) {
                        mHeadRightText.setVisibility(View.VISIBLE);
                    }
                    return true;
                } else {//当webview处于第一页面时,直接退出程序
                    super.onBackPressed();
                    mWebHistoryUtil.clearHis();
                    mWebView.loadUrl("javascript:delCookie()");
                }
            }

        }
        return false;
    }
}
