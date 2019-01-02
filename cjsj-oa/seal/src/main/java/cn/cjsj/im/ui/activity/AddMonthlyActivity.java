package cn.cjsj.im.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.cjsj.im.App;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.WebViewInitSetting;
import cn.cjsj.im.gty.common.ConstantValue;

/**
 * @author LuoYang 2018.01.09
 * 月报
 */
public class AddMonthlyActivity extends BaseActivity implements View.OnClickListener{
    @Bind(R.id.add_monthly_webview)
    WebView mWebView;

    @Bind(R.id.add_monthly_web_progressbar)
    ProgressBar mProgressBar;

    private Button mBackBtn;

    private String mToken;

    private String BASE_URL = ConstantValue.WEB_BASE_URL + "html/monthly/add_monthly.html";

    public boolean isContact = false;

    private Intent mIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_monthly);
        ButterKnife.bind(this);
        setTitle("写月报");

        mIntent = getIntent();

        mBackBtn = getHeadLeftButton();
        mBackBtn.setOnClickListener(this);

        mToken = App.getInstance().getToken();

        WebSettings webSettings = mWebView.getSettings();
        WebViewInitSetting.getInstance(this).webViewSetting(webSettings);
        WebViewInitSetting.getInstance(this).initLoad(mWebView);
        mWebView.loadUrl(BASE_URL + "?token=" + mToken + "&actDefId=" + mIntent.getStringExtra("actDefId"));

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

                if(url.contains("protocol://android.contact")){
//                view.loadUrl("javascript:setImgUrl('"+testInt+"')");
//                view.loadUrl("javascript:callback()");
                    isContact = true;
                    return true;
                }else if(url.contains("protocol://android.finish")){
                    finish();
                    return true;
                }else {
                    return false;
                }

            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return super.shouldInterceptRequest(view, request);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_left:
                if(isContact){
                    mWebView.loadUrl("javascript:closedPopup()");
                    mWebView.loadUrl("javascript:backValue()");
                    isContact = false;
                }else{
                    if (mWebView.canGoBack()) {//当webview不是处于第一页面时，返回上一个页面
                        mWebView.goBack();
//                    mWebView.reload();
                    } else {//当webview处于第一页面时,直接退出程序
                        finish();
                    }
                }
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
                if (mWebView.canGoBack()) {//当webview不是处于第一页面时，返回上一个页面
                    mWebView.goBack();
                    return true;
                } else {//当webview处于第一页面时,直接退出程序
                    super.onBackPressed();
                }
            }

        }
        return false;
    }
}
