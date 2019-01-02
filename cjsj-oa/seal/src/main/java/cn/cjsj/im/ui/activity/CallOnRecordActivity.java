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
import cn.cjsj.im.server.utils.NLog;
import cn.cjsj.im.utils.LimitsLoadUtils;

/**
 * 拜访记录
 * Created by LuoYang on 2018/1/23.
 */

public class CallOnRecordActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.call_on_webview)
    WebView mWebView;

    @Bind(R.id.call_on_web_progressbar)
    ProgressBar mProgressBar;

    private static final String BASE_WEEKLY_URL = ConstantValue.WEB_BASE_URL + "html/visitRecord/visitRecord_list.html";

    private static final String TAG = "CallOnRecordActivity";

    private Button mBackBtn;

    private String mToken;

    private WebHistoryUtil mWebHistoryUtil; //历史工具类.
    private int mHisIndex = 0; //历史角标

    private Intent mIntent;
    private String mActDefId;

    private boolean isContact = false;
    private boolean isAddOwn = false;
    private boolean mClient = false;
    private String mOwnId;
    private String mUnitName;
    private boolean isAddClient = false;

    private int mLimits = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_call_on_his);
        ButterKnife.bind(this);
        setTitle("拜访记录");

        mIntent = getIntent();
        mActDefId = mIntent.getStringExtra("actDefId");
        mBackBtn = getHeadLeftButton();
        mBackBtn.setOnClickListener(this);

        mHeadRightText.setVisibility(View.VISIBLE);
        mHeadRightText.setText("新增");
        mHeadRightText.setClickable(true);
        mHeadRightText.setOnClickListener(this);

        mWebHistoryUtil = new WebHistoryUtil();

        mToken = App.getInstance().getToken();

        mLimits = LimitsLoadUtils.getInstance().getLoadTag();


        WebSettings webSettings = mWebView.getSettings();
        WebViewInitSetting.getInstance(this).webViewSetting(webSettings);
        WebViewInitSetting.getInstance(this).initLoad(mWebView);

        mWebView.loadUrl(BASE_WEEKLY_URL + "?token=" + mToken + "&actDefId=" + mActDefId + "&limits=" + mLimits);
        mWebHistoryUtil.setUrl(BASE_WEEKLY_URL + "?token=" + mToken + "&actDefId=" + mActDefId + "&limits=" + mLimits);
        NLog.d("LY__url" + BASE_WEEKLY_URL + "?token=" + mToken + "&actDefId=" + mActDefId + "&limits=" + mLimits);

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

                if (url.contains("visitRecord_detail.html")) {
                    mWebHistoryUtil.setUrl(url);
                    mHisIndex++;
                    return false;
                } else if (url.contains("addvisitRecord.html")) {
                    mWebHistoryUtil.setUrl(url);
                    mHisIndex++;
                    mHeadRightText.setVisibility(View.INVISIBLE);
                    return false;
                } else if (url.contains("protocol://android.contact")) {
                    isContact = true;
                    mHeadRightText.setVisibility(View.INVISIBLE);
                    return true;
                } else if (url.contains("protocol://android.addOwn")) {
                    isAddOwn = true;
                    mHeadRightText.setVisibility(View.VISIBLE);
                    return true;
                } else if (url.contains("protocol://android.client")) { //拜访客户
                    mClient = true;
                    int unitNameIndex = url.indexOf("&unitName=");
                    mUnitName = url.substring(unitNameIndex + 10);
                    int ownIdIndex = url.indexOf("&ownId=");
                    mOwnId = url.substring(ownIdIndex + 7, unitNameIndex);
                    isAddClient = true;
                    mHeadRightText.setVisibility(View.VISIBLE);
                    return true;
                } else if (url.contains("protocol://android.finish")) {
//                    finish();
                    isContact = false;
                    mWebHistoryUtil.clearHis();
                    mHisIndex = 0;
                    mWebView.loadUrl(BASE_WEEKLY_URL + "?token=" + mToken + "&actDefId=" + mActDefId + "&limits=" + mLimits);
                    mWebHistoryUtil.setUrl(BASE_WEEKLY_URL + "?token=" + mToken + "&actDefId=" + mActDefId + "&limits=" + mLimits);
                    return true;
                } else if (url.contains("protocol://android.closewindow")) {
                    isContact = false;
                    isAddOwn = false;
                    mClient = false;
                    isAddClient = false;
                    mHeadRightText.setVisibility(View.INVISIBLE);
                    return true;
                } else if (url.contains("protocol://android.closeownwindow")) {
                    isAddOwn = false;
                    mHisIndex = 1;
                    mWebHistoryUtil.clearHis();
                    mWebView.loadUrl(ConstantValue.WEB_BASE_URL + "html/visitRecord/addvisitRecord.html" + "?token=" + mToken + "&actDefId=" + mActDefId);
                    mWebHistoryUtil.setUrl(BASE_WEEKLY_URL + "?token=" + mToken + "&actDefId=" + mActDefId);
                    mWebHistoryUtil.setUrl(ConstantValue.WEB_BASE_URL + "html/visitRecord/addvisitRecord.html" + "?token=" + mToken + "&actDefId=" + mActDefId);
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                if (mClient) {
                    mWebView.loadUrl("javascript:ownerbackValue()"); //添加拜访客户
                    mHeadRightText.setVisibility(View.INVISIBLE);
                    mClient = false;
                } else if (isAddOwn) {
                    mWebView.loadUrl("javascript:unitbackValue()"); //添加拜访单位
                    mHeadRightText.setVisibility(View.INVISIBLE);
                    isAddOwn = false;
                } else if (isContact) {
                    mWebView.loadUrl("javascript:closedPopup()");
                    mWebView.loadUrl("javascript:backValue()");
                    isContact = false;
                } else {
                    if (mHisIndex != 0) {
                        mWebView.loadUrl(mWebHistoryUtil.getUrl(mHisIndex));
                        mWebHistoryUtil.remove(mHisIndex);
                        mHisIndex--;
                        if (mWebView.getUrl().contains("visitRecord_list.html")) {
                            mHeadRightText.setVisibility(View.VISIBLE);
                        }else if (isAddClient) {
                            mHeadRightText.setVisibility(View.INVISIBLE);
                            isAddClient = false;
                        }
                    } else {
                        finish();
                        mWebHistoryUtil.clearHis();
                        mWebView.loadUrl("javascript:delCookie()");
                    }
                }
                break;

            case R.id.text_right:
                if (isAddOwn) {
                    mWebView.loadUrl(ConstantValue.WEB_BASE_URL + "html/market/addOwnerUnit.html" + "?token=" + mToken);
                    isAddOwn = false;
                    isContact = false;
                    mHeadRightText.setVisibility(View.INVISIBLE);
                    mWebHistoryUtil.setUrl(ConstantValue.WEB_BASE_URL + "html/market/addOwnerUnit.html" + "?token=" + mToken);
                    mHisIndex++;
                } else if (isAddClient) {
                    mWebView.loadUrl(ConstantValue.WEB_BASE_URL + "html/market/addOwnerUnit.html" + "?token=" + mToken + "&ownId=" + mOwnId + "&unitName=" + mUnitName);
                    isAddOwn = false;
                    isContact = false;
                    mClient = false;
                    mHeadRightText.setVisibility(View.INVISIBLE);
                    mWebHistoryUtil.setUrl(ConstantValue.WEB_BASE_URL + "html/market/addOwnerUnit.html" + "?token=" + mToken + "&ownId=" + mOwnId + "&unitName=" + mUnitName);
                    mHisIndex++;
                } else {
                    mWebView.loadUrl(ConstantValue.WEB_BASE_URL + "html/visitRecord/addvisitRecord.html" + "?token=" + mToken + "&actDefId=" + mActDefId);
                    mWebHistoryUtil.setUrl(ConstantValue.WEB_BASE_URL + "html/visitRecord/addvisitRecord.html" + "?token=" + mToken + "&actDefId=" + mActDefId);
                    mHisIndex++;
                    mHeadRightText.setVisibility(View.INVISIBLE);
                }
                break;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO 自动生成的方法存根
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (mClient) {
                mWebView.loadUrl("javascript:ownerbackValue()"); //添加拜访客户
                mHeadRightText.setVisibility(View.INVISIBLE);
                mClient = false;
                return  true;
            } else if (isAddOwn) {
                mWebView.loadUrl("javascript:unitbackValue()"); //添加拜访单位
                mHeadRightText.setVisibility(View.INVISIBLE);
                isAddOwn = false;
                return  true;
            } else if (isContact) {
                mWebView.loadUrl("javascript:closedPopup()");
                mWebView.loadUrl("javascript:backValue()");
                isContact = false;
                return  true;
            } else {
                if (mHisIndex != 0) {//当webview不是处于第一页面时，返回上一个页面
                    mWebView.loadUrl(mWebHistoryUtil.getUrl(mHisIndex));
                    mWebHistoryUtil.remove(mHisIndex);
                    mHisIndex--;
                    if (mWebView.getUrl().contains("visitRecord_list.html")) {
                        mHeadRightText.setVisibility(View.VISIBLE);
                    } else if (isAddClient) {
                        mHeadRightText.setVisibility(View.INVISIBLE);
                        isAddClient = false;
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
