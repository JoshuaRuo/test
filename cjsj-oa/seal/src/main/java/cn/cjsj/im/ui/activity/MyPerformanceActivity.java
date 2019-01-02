package cn.cjsj.im.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
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

/**
 * Created by LuoYang on 2018/3/30.
 * 我的绩效
 */

public class MyPerformanceActivity extends BaseActivity implements View.OnClickListener {
    private static final String BASE_URL = ConstantValue.WEB_BASE_URL + "html/performance/myPerformance.html";


    @Bind(R.id.performance_webview)
    WebView mWebView;
    @Bind(R.id.performance_progressbar)
    ProgressBar mProgressBar;

    private Button mBackBtn;

    private static final String TAG = "MyPerformanceActivity";

    private WebHistoryUtil mWebHistoryUtil; //历史工具类

    private int mHisIndex = 0; //历史角标

    private String mToken;
    private static String mActDefId = "sqjjlc:1:10000002626802";

    public boolean isContact = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myperformance);
        ButterKnife.bind(this);
        setTitle("我的绩效");
        setTitleColor();
        mHeadLayout.setBackgroundResource(R.drawable.perf_titlebg);

        WebSettings webSettings = mWebView.getSettings();
        WebViewInitSetting.getInstance(this).webViewSetting(webSettings);
        WebViewInitSetting.getInstance(this).initLoad(mWebView);
        mWebHistoryUtil = new WebHistoryUtil();

        mToken = App.getInstance().getToken();

        mWebView.loadUrl(BASE_URL + "?token=" + mToken + "&actDefId=" + mActDefId);
        mWebHistoryUtil.setUrl(BASE_URL + "?token=" + mToken + "&actDefId=" + mActDefId);

        mBackBtn = getHeadLeftButton();
        mBackBtn.setOnClickListener(this);
        mBackBtn.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.back_white),null,null,null);

        mWebView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageFinished(WebView view, String url) {
                if (!mWebView.getSettings().getLoadsImagesAutomatically()) {
                    mWebView.getSettings().setLoadsImagesAutomatically(true);
                }
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mWebHistoryUtil.setUrl(url);
                mHisIndex++;
                return false;


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
        switch (v.getId()){
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

                    } else {//当webview处于第一页面时,直接退出程序
                        mWebHistoryUtil.clearHis();
                        super.onBackPressed();
                    }
                }
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
                    mWebHistoryUtil.remove(mHisIndex);
                    mHisIndex--;

                    return true;
                } else {//当webview处于第一页面时,直接退出程序
                    mWebHistoryUtil.clearHis();
                    super.onBackPressed();
                }
            }

        }
        return false;
    }
}
