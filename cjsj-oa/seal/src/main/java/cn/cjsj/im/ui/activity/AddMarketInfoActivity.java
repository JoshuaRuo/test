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

/**
 * Created by LuoYang on 2017/12/15.
 */

public class AddMarketInfoActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.add_market_webview)
    WebView mWebView;

    @Bind(R.id.add_market_web_progressbar)
    ProgressBar mProgressBar;

    private Button mBackBtn;

    private String mToken;

    public boolean isContact = false;

    public boolean isAddOwn = false;

    private String BaseUrl = ConstantValue.WEB_BASE_URL + "html/market/add_marketInfo.html";

    private WebHistoryUtil mWebHistoryUtil; //历史工具类
    private int mHisIndex = 0; //历史角标
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_market_info_activity);
        ButterKnife.bind(this);
        setTitle(R.string.add_market_info_title);
        mIntent = getIntent();
        mHeadRightText.setVisibility(View.INVISIBLE);
        mHeadRightText.setText("添加");
        mHeadRightText.setClickable(true);
        mHeadRightText.setOnClickListener(this);

        mWebHistoryUtil = new WebHistoryUtil();

        WebSettings webSettings = mWebView.getSettings();
        WebViewInitSetting.getInstance(this).webViewSetting(webSettings);
        WebViewInitSetting.getInstance(this).initLoad(mWebView);

        mToken = App.getInstance().getToken();

        mWebView.loadUrl(BaseUrl + "?token=" + mToken + "&actDefId=" + mIntent.getStringExtra("actDefId"));
        mWebHistoryUtil.setUrl(BaseUrl + "?token=" + mToken);

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

                if (url.contains("protocol://android.addOwn")) {
                    isAddOwn = true;
                    mHeadRightText.setVisibility(View.VISIBLE);
                    return true;
                } else if (url.contains("protocol://android.contact")) {
                    isContact = true;
                    mHeadRightText.setVisibility(View.INVISIBLE);
                    return true;
                } else if (url.contains("protocol://android.closeown")) {
                    mHeadRightText.setVisibility(View.INVISIBLE);
                    isAddOwn = false;
                    return true;
                }else if(url.contains("protocol://android.finish")){
                    finish();
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
                    mProgressBar.setVisibility(View.INVISIBLE);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        mBackBtn = getHeadLeftButton();
        mBackBtn.setOnClickListener(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO 自动生成的方法存根
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isAddOwn) {
                mWebView.loadUrl("javascript:closedOwenPopUp()");
                mHeadRightText.setVisibility(View.INVISIBLE);
                isAddOwn = false;
                return true;
            } else if (isContact) {
                mWebView.loadUrl("javascript:closedPopup()");
                mWebView.loadUrl("javascript:backValue()");
                isContact = false;
                return true;
            } else {
                if (mHisIndex != 0) {//当webview不是处于第一页面时，返回上一个页面
                    mWebView.loadUrl(mWebHistoryUtil.getUrl(mHisIndex));
                    mHisIndex--;
                    return true;
                } else {//当webview处于第一页面时,直接退出程序
                    mWebHistoryUtil.clearHis();
                    super.onBackPressed();
                }
            }

            if (mWebView.getUrl().contains("addOwnerUnit.html") || mWebView.getUrl().contains("protocol://android.addOwn")) {
                mHeadRightText.setVisibility(View.INVISIBLE);
            }

        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_right:
                if (isAddOwn) {
                    mWebView.loadUrl(ConstantValue.WEB_BASE_URL + "html/market/addOwnerUnit.html" + "?token=" + mToken);
                    isAddOwn = false;
                    isContact = false;
                    mHeadRightText.setVisibility(View.INVISIBLE);
                    mWebHistoryUtil.setUrl(ConstantValue.WEB_BASE_URL + "html/market/addOwnerUnit.html");
                    mHisIndex++;
                }
                break;

            case R.id.btn_left:
                if (isAddOwn) {
                    mWebView.loadUrl("javascript:closedOwenPopUp()");
                    mHeadRightText.setVisibility(View.INVISIBLE);
                    isAddOwn = false;
                } else if (isContact) {
                    mWebView.loadUrl("javascript:closedPopup()");
                    mWebView.loadUrl("javascript:backValue()");
                    isContact = false;
                } else {

                    if (mHisIndex != 0) {//当webview不是处于第一页面时，返回上一个页面
                        mWebView.loadUrl(mWebHistoryUtil.getUrl(mHisIndex));
                        mHisIndex--;
                    } else {//当webview处于第一页面时,直接退出程序
                        mWebHistoryUtil.clearHis();
                        finish();
                    }
                }

                if (mWebView.getUrl().contains("addOwnerUnit.html") || mWebView.getUrl().contains("protocol://android.addOwn")) {
                    mHeadRightText.setVisibility(View.INVISIBLE);
                }


                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebHistoryUtil.clearHis();
        mWebView.clearHistory();
    }
}
