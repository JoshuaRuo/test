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

/**
 * 任务交办
 * Created by LuoYang on 2018/1/29.
 */

public class TaskActivity extends BaseActivity implements View.OnClickListener{
    @Bind(R.id.task_webview)
    WebView mWebView;

    @Bind(R.id.task_web_progressbar)
    ProgressBar mProgressBar;

    private static final String BASE_WEEKLY_URL = ConstantValue.WEB_BASE_URL + "html/assign/assignList.html";
    private static final String Task_DETAIL_URL = ConstantValue.WEB_BASE_URL + "html/assign/receiveDetail.html";

    private static final String TAG = "TaskActivity";

    private Button mBackBtn;

    private String mToken;

    private WebHistoryUtil mWebHistoryUtil; //历史工具类
    private int mHisIndex = 0; //历史角标

    private Intent mIntent;
    private String mActDefId;
    private String mRunId;
    private boolean mIsFromPush = false;

    private boolean isContact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        ButterKnife.bind(this);
        setTitle("任务交办");

        mBackBtn = getHeadLeftButton();
        mBackBtn.setOnClickListener(this);

        mIntent = getIntent();
        try {
            mActDefId = mIntent.getStringExtra("actDefId");
            mRunId = mIntent.getStringExtra("runId");
            mIsFromPush = mIntent.getBooleanExtra("itsFromPush", false);
        }catch (NullPointerException nullPointer){
            NLog.e("The PushReceiver data is null");
        }
        mHeadRightText.setVisibility(View.VISIBLE);
        mHeadRightText.setText("新增");
        mHeadRightText.setClickable(true);
        mHeadRightText.setOnClickListener(this);

        mWebHistoryUtil = new WebHistoryUtil();
        mWebHistoryUtil.clearHis();

        mToken = App.getInstance().getToken();


        WebSettings webSettings = mWebView.getSettings();
        WebViewInitSetting.getInstance(this).webViewSetting(webSettings);
        WebViewInitSetting.getInstance(this).initLoad(mWebView);

        if(!mIsFromPush) {      //推送过来的就加载详情
            mWebView.loadUrl(BASE_WEEKLY_URL + "?token=" + mToken + "&actDefId=" + mActDefId);
            mWebHistoryUtil.setUrl(BASE_WEEKLY_URL + "?token=" + mToken + "&actDefId=" + mActDefId);
        }else {
            mWebView.loadUrl(Task_DETAIL_URL + "?token=" + mToken + "&runId=" + mRunId);
            mWebHistoryUtil.setUrl(Task_DETAIL_URL + "?token=" + mToken + "&runId=" + mRunId);
        }

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
                    mWebView.loadUrl(BASE_WEEKLY_URL + "?token=" + mToken + "&actDefId=" + mActDefId);
                    mWebHistoryUtil.setUrl(BASE_WEEKLY_URL + "?token=" + mToken + "&actDefId=" + mActDefId);
                    mHeadRightText.setVisibility(View.VISIBLE);
                    return true;
                }else if(url.contains("receiveDetail.html")){
                    mWebHistoryUtil.setUrl(url);
                    mHisIndex++;
                    mHeadRightText.setVisibility(View.INVISIBLE);
                    return false;
                }else if(url.contains("addAssign.html")){
                    mWebHistoryUtil.setUrl(url);
                    mHisIndex++;
                    mHeadRightText.setVisibility(View.INVISIBLE);
                    return false;
                }else if(url.contains("sendDetail.html")){
                    mWebHistoryUtil.setUrl(url);
                    mHisIndex++;
                    mHeadRightText.setVisibility(View.INVISIBLE);
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
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
              if (isContact) {
                    mWebView.loadUrl("javascript:closedPopup()");
//                    mWebView.loadUrl("javascript:backValue()");
                    isContact = false;
                } else {
                    if (mHisIndex != 0) {
                        mWebView.loadUrl(mWebHistoryUtil.getUrl(mHisIndex));
                        mWebHistoryUtil.remove(mHisIndex);
                        mHisIndex--;
                    } else {
                        if(!mIsFromPush){
                            finish();
                            mWebHistoryUtil.clearHis();
                            mWebView.loadUrl("javascript:delCookie()");
                        }else {
                            mWebView.loadUrl(BASE_WEEKLY_URL + "?token=" + mToken + "&actDefId=" + mActDefId);
                            mWebHistoryUtil.setUrl(BASE_WEEKLY_URL + "?token=" + mToken + "&actDefId=" + mActDefId);
                            mIsFromPush = false;
                        }
                        //如果推送过来进的详情点返回就跳到列表
                    }
                }
                if (mWebView.getUrl().contains("assignList.html")) {
                    mHeadRightText.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.text_right:
                mWebView.loadUrl(ConstantValue.WEB_BASE_URL + "html/assign/addAssign.html" + "?token=" + mToken + "&actDefId=" + mActDefId);
                mWebHistoryUtil.setUrl(ConstantValue.WEB_BASE_URL + "html/assign/addAssign.html" + "?token=" + mToken + "&actDefId=" + mActDefId);
                mHisIndex++;
                mHeadRightText.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO 自动生成的方法存根
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isContact) {
                mWebView.loadUrl("javascript:closedPopup()");
//                mWebView.loadUrl("javascript:backValue()");
                isContact = false;
            } else {
                if (mHisIndex != 0) {//当webview不是处于第一页面时，返回上一个页面
                    mWebView.loadUrl(mWebHistoryUtil.getUrl(mHisIndex));
                    mWebHistoryUtil.remove(mHisIndex);
                    mHisIndex--;
                    if (mWebView.getUrl().contains("assignList.html")) {
                        mHeadRightText.setVisibility(View.VISIBLE);
                    }
                    return true;
                } else {//当webview处于第一页面时,直接退出程序
                    if(!mIsFromPush){
                        super.onBackPressed();
                        mWebHistoryUtil.clearHis();
                        mWebView.loadUrl("javascript:delCookie()");
                    }else {
                        mWebView.loadUrl(BASE_WEEKLY_URL + "?token=" + mToken + "&actDefId=" + mActDefId);
                        mWebHistoryUtil.setUrl(BASE_WEEKLY_URL + "?token=" + mToken + "&actDefId=" + mActDefId);
                        mIsFromPush = false;
                    }
                    //如果推送过来进的详情点返回就跳到列表

                }
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebHistoryUtil.clearHis();
        mWebView.clearHistory();
    }
}
