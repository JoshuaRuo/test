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
 * Created by LuoYang on 2018/7/25 14:13
 * 投票
 */
public class VoteActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.vote_webview)
    WebView mWebView;
    @Bind(R.id.vote_web_progressbar)
    ProgressBar mProgressBar;

    private Button mBackBtn;

    private static final String TAG = "VoteActivity";

    private static final String BASE_URL = ConstantValue.WEB_BASE_URL + "html/vote/vote_list.html";

    private WebHistoryUtil mWebHistoryUtil; //历史工具类

    private int mHisIndex = 0; //历史角标


    private String mToken;

    public boolean isContact = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);
        ButterKnife.bind(this);
        setTitle("投票");


        mToken = App.getInstance().getToken();

        mBackBtn = getHeadLeftButton();
        mBackBtn.setOnClickListener(this);

        WebSettings webSettings = mWebView.getSettings();
        WebViewInitSetting.getInstance(this).webViewSetting(webSettings);
        WebViewInitSetting.getInstance(this).initLoad(mWebView);

        mWebHistoryUtil = new WebHistoryUtil();

            mWebView.loadUrl(BASE_URL + "?token=" + mToken);
            mWebHistoryUtil.setUrl(BASE_URL + "?token=" + mToken);

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
                if (url.contains("vote.html") || url.contains("vote_detail.html") || url.contains("vote_check.html")
                        || url.contains("option_voter.html") || url.contains("vote_new.html")) {
                    view.loadUrl(url);
                    mWebHistoryUtil.setUrl(url);
                    mHisIndex++;
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
    protected void onDestroy() {
        super.onDestroy();
        mWebHistoryUtil.clearHis();
        mHisIndex = 0;
    }

    @Override
    public void onClick(View v) {
        if (mHisIndex != 0) {//当webview不是处于第一页面时，返回上一个页面
            mWebView.loadUrl(mWebHistoryUtil.getUrl(mHisIndex));
            mWebHistoryUtil.remove(mHisIndex);
            mHisIndex--;
//                    mWebView.reload();
        } else {//当webview处于第一页面时,直接退出程序
            finish();
            mWebHistoryUtil.clearHis();
        }
        if (mWebView.getUrl().contains("vote_list.html")) {
            mHeadRightText.setVisibility(View.VISIBLE);
        }
        Log.d("SyncHttpClient", mWebView.getUrl());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mHisIndex != 0) {//当webview不是处于第一页面时，返回上一个页面
                mWebView.loadUrl(mWebHistoryUtil.getUrl(mHisIndex));
                mWebHistoryUtil.remove(mHisIndex);
                mHisIndex--;
                if (mWebView.getUrl().contains("vote_list.html")) {
                    mHeadRightText.setVisibility(View.VISIBLE);
                }
                return true;
            } else {//当webview处于第一页面时,直接退出程序
                mWebHistoryUtil.clearHis();
                super.onBackPressed();
            }

        }
        return false;
    }
}
