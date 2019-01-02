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
 * Created by LuoYang on 2017/10/18.
 * 市场信息列表
 */

public class MarketInfoListActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.market_webview)
    WebView mWebView;
    @Bind(R.id.market_web_progressbar)
    ProgressBar mProgressBar;


    private Intent mIntent;

    private Button mBackBtn;

    private static final String BASE_URL = ConstantValue.WEB_BASE_URL + "html/market/cjsj_marketInfo.html";
    private String mToken;
    private String mActDefId;

    public boolean isContact = false;

    public boolean isAddOwn = false;

    private WebHistoryUtil mWebHistoryUtil; //历史工具类
    private int mHisIndex = 0; //历史角标

    private int mLimits = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.market_info_list_activity);
        ButterKnife.bind(this);
        mIntent = getIntent();
        mActDefId = mIntent.getStringExtra("actDefId");
        setTitle(R.string.market_info_title);
        mHeadRightText.setVisibility(View.VISIBLE);
        mHeadRightText.setText("新增");
        mHeadRightText.setClickable(true);
        mHeadRightText.setOnClickListener(this);

        mLimits = LimitsLoadUtils.getInstance().getLoadTag();

        WebSettings webSettings = mWebView.getSettings();
        WebViewInitSetting.getInstance(this).webViewSetting(webSettings);
        WebViewInitSetting.getInstance(this).initLoad(mWebView);

        mWebHistoryUtil = new WebHistoryUtil();

        mToken = App.getInstance().getToken();

        mWebView.loadUrl(BASE_URL + "?token=" + mToken + "&actDefId=" + mActDefId + "&limits=" + mLimits);
        mWebHistoryUtil.setUrl(BASE_URL + "?token=" + mToken + "&actDefId=" + mActDefId + "&limits=" + mLimits);

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
//                    finish();
                    isContact = false;
                    mHisIndex = 0;
                    mWebHistoryUtil.clearHis();
                    mWebView.loadUrl(BASE_URL + "?token=" + mToken + "&actDefId=" + mActDefId + "&limits=" + mLimits);
                    mWebHistoryUtil.setUrl(BASE_URL + "?token=" + mToken + "&actDefId=" + mActDefId + "&limits=" + mLimits);
                    return true;
                }else if(url.contains("add_marketInfo")){
                    mHeadRightText.setVisibility(View.INVISIBLE);
                    mWebHistoryUtil.setUrl(url);
                    mHisIndex++;
                    return false;
                }else if(url.contains("marketInfoDetail.html")){
                    mWebHistoryUtil.setUrl(url);
                    mHisIndex++;
                    mHeadRightText.setVisibility(View.VISIBLE);
                    return false;
                }else if (url.contains("cjsj_marketInfo.html")){
                    mHeadRightText.setVisibility(View.VISIBLE);
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
                    mProgressBar.setVisibility(View.GONE);
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
                    if (mWebView.getUrl().contains("cjsj_marketInfo.html")) {
                        mHeadRightText.setVisibility(View.VISIBLE);
                    }
                    return true;
                } else {//当webview处于第一页面时,直接退出程序
                    mWebHistoryUtil.clearHis();
                   super.onBackPressed();
                }

            }
        }
        return false;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text_right:
                if (isAddOwn) {
                    mWebView.loadUrl(ConstantValue.WEB_BASE_URL + "html/market/addOwnerUnit.html" + "?token=" + mToken);
                    isAddOwn = false;
                    isContact = false;
                    mHeadRightText.setVisibility(View.INVISIBLE);
                    mWebHistoryUtil.setUrl(ConstantValue.WEB_BASE_URL + "html/market/addOwnerUnit.html");
                    mHisIndex++;
                }else {
                    mIntent = new Intent(this, AddMarketInfoActivity.class);
                    mIntent.putExtra("actDefId", mActDefId);
                    startActivity(mIntent);
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


                if (mWebView.getUrl().contains("cjsj_marketInfo.html")) {
                    mHeadRightText.setVisibility(View.VISIBLE);
                }

                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.loadUrl(BASE_URL + "?token=" + mToken + "&actDefId=" + mActDefId + "&limits=" + mLimits);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebHistoryUtil.clearHis();
    }
}
