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
 * Created by LuoYang on 2018/4/3.
 * 请假
 */

public class LeaveActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.leave_webview)
    WebView mWebView;
    @Bind(R.id.leave_web_progressbar)
    ProgressBar mProgressBar;


    private Intent mIntent;
    private Button mBackBtn;

    private static final String TAG = "LeaveActivity";

    private static final String BASE_URL = ConstantValue.WEB_BASE_URL + "html/leave/leave_list.html";
    private static final String BASE_apply_URL = ConstantValue.WEB_BASE_URL + "html/leave/apply_leave.html";

    private WebHistoryUtil mWebHistoryUtil; //历史工具类

    private int mHisIndex = 0; //历史角标

    private String mToken;
    private String mActDefId;

    public boolean isContact = false;

    private boolean mIsApplyApproval = false;//审批进度
    private boolean mIsApplyHistory = false;//审批历史
    private String mBusinessKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave);
        ButterKnife.bind(this);
        setTitle("请假");

        mIntent = getIntent();
        mActDefId = mIntent.getStringExtra("actDefId");

        mBackBtn = getHeadLeftButton();
        mBackBtn.setOnClickListener(this);

        WebSettings webSettings = mWebView.getSettings();
        WebViewInitSetting.getInstance(this).webViewSetting(webSettings);
        WebViewInitSetting.getInstance(this).initLoad(mWebView);

        mWebHistoryUtil = new WebHistoryUtil();


        mToken = App.getInstance().getToken();

        mWebView.loadUrl(BASE_URL + "?token=" + mToken + "&actDefId=" + mActDefId);
        mWebHistoryUtil.setUrl(BASE_URL + "?token=" + mToken + "&actDefId=" + mActDefId);

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
                if (url.contains("protocol://android.finish")) {
                    isContact = false;
                    mWebHistoryUtil.clearHis();
                    mHisIndex = 0;
                    mWebView.loadUrl(BASE_URL + "?token=" + mToken + "&actDefId=" + mActDefId);
                    mWebHistoryUtil.setUrl(BASE_URL + "?token=" + mToken + "&actDefId=" + mActDefId);
                    mIsApplyApproval = false;
                    mIsApplyHistory = false;
                    mHeadRightText.setText("新增");
                    mHeadRightText.setVisibility(View.VISIBLE);
                    return true;
                } else if (url.contains("leave_detail.html")) {
                    mHeadRightText.setVisibility(View.VISIBLE);
                    mWebHistoryUtil.setUrl(url);
                    mHisIndex++;
                    mIsApplyApproval = true;
                    mHeadRightText.setText("审批进度");
                    int idUrl = url.indexOf("id=");
                    mBusinessKey = url.substring(idUrl + 3);
                    return false;
                } else if (url.contains("protocol://android.contact")) {
                    mHeadRightText.setVisibility(View.INVISIBLE);
                    isContact = true;
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
        mHeadRightText.setText("新增");
        mHeadRightText.setClickable(true);
        mHeadRightText.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebHistoryUtil.clearHis();
        mWebView.clearHistory();
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

                        if (mWebView.getUrl().contains("leave_list.html")) {
                            mHeadRightText.setVisibility(View.VISIBLE);
                            mHeadRightText.setText("添加");
                            mIsApplyHistory = false;
                            mIsApplyApproval = false;
                        } else if (mWebView.getUrl().contains("approval_speed.html")) {
                            mIsApplyHistory = true;
                            mIsApplyApproval = false;
                            mHeadRightText.setText("审批历史");
                            mHeadRightText.setVisibility(View.VISIBLE);

                        } else if (mWebView.getUrl().contains("leave_detail.html")) {
                            mIsApplyApproval = true;
                            mIsApplyHistory = false;
                            mHeadRightText.setText("审批进度");
                            mHeadRightText.setVisibility(View.VISIBLE);
                        }
                    } else {//当webview处于第一页面时,直接退出程序
                        finish();
                        mWebHistoryUtil.clearHis();
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
                    mWebView.loadUrl(BASE_apply_URL + "?token=" + mToken + "&actDefId=" + mActDefId);
                    mWebHistoryUtil.setUrl(BASE_apply_URL + "?token=" + mToken + "&actDefId=" + mActDefId);
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
                    if (mWebView.getUrl().contains("leave_list.html")) {
                        mHeadRightText.setVisibility(View.VISIBLE);
                        mHeadRightText.setText("添加");
                        mIsApplyHistory = false;
                        mIsApplyApproval = false;
                    } else if (mWebView.getUrl().contains("approval_speed.html")) {
                        mIsApplyHistory = true;
                        mIsApplyApproval = false;

                        mHeadRightText.setVisibility(View.VISIBLE);mHeadRightText.setText("审批历史");

                    } else if (mWebView.getUrl().contains("leave_detail.html")) {
                        mIsApplyApproval = true;
                        mIsApplyHistory = false;
                        mHeadRightText.setText("审批进度");
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
}
