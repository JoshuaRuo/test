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
 * 事项申请
 * Created by LuoYang on 2018/1/23.
 */

public class ItemsApplyActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.itemsapply_webview)
    WebView mWebView;

    @Bind(R.id.itemsapply_web_progressbar)
    ProgressBar mProgressBar;

    private static final String BASE_WEEKLY_URL = ConstantValue.WEB_BASE_URL + "html/reimbursement_matter/matter_list.html";

    private static final String TAG = "ItemsApplyActivity";

    private Button mBackBtn;

    private String mToken;

    private WebHistoryUtil mWebHistoryUtil; //历史工具类
    private int mHisIndex = 0;

    private Intent mIntent;
    private String mActDefId;

    private boolean isContact = false;
    private boolean isProductName = false;

    private boolean mIsApplyApproval = false;//审批进度
    private boolean mIsApplyHistory = false;//审批历史
    private String mBusinessKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_items_apply);
        ButterKnife.bind(this);
        setTitle("事项申请");

        mBackBtn = getHeadLeftButton();
        mBackBtn.setOnClickListener(this);

        mHeadRightText.setVisibility(View.VISIBLE);
        mHeadRightText.setText("新增");
        mHeadRightText.setClickable(true);
        mHeadRightText.setOnClickListener(this);

        mWebHistoryUtil = new WebHistoryUtil();

        mToken = App.getInstance().getToken();

        mIntent = getIntent();
        mActDefId = mIntent.getStringExtra("actDefId");


        WebSettings webSettings = mWebView.getSettings();
        WebViewInitSetting.getInstance(this).webViewSetting(webSettings);
        WebViewInitSetting.getInstance(this).initLoad(mWebView);

        mWebView.loadUrl(BASE_WEEKLY_URL + "?token=" + mToken);
        mWebHistoryUtil.setUrl(BASE_WEEKLY_URL + "?token=" + mToken);

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
//                    finish();
                    isContact = false;
                    isProductName = false;
                    mHisIndex = 0;
                    mWebHistoryUtil.clearHis();
                    mWebView.loadUrl(BASE_WEEKLY_URL + "?token=" + mToken + "&actDefId=" + mActDefId);
                    mWebHistoryUtil.setUrl(BASE_WEEKLY_URL + "?token=" + mToken + "&actDefId=" + mActDefId);
                    return true;
                } else if (url.contains("protocol://android.contact")) {
                    isContact = true;
                    mHeadRightText.setVisibility(View.INVISIBLE);
                    return true;
                } else if (url.contains("protocol://android.reimburse.productname")) { //项目名称
                    isProductName = true;
                    return true;
                } else  if(url.contains("protocol://android.closewindow")){ //告知关闭窗口
                    isContact = false;
                    isProductName =false;
                    return true;
                }else if(url.contains("matter_detail.html") || url.contains("add_matter.html")){
                    mWebHistoryUtil.setUrl(url);
                    mHisIndex++;

                    mHeadRightText.setText("审批进度");
                    int idUrl = url.indexOf("id=");
                    mBusinessKey = url.substring(idUrl + 3);
                    mIsApplyApproval = true;

                    return false;
                }else  if (url.contains("protocol://android.finish")) {
//                    finish();
                    mHisIndex = 0;
                    mWebHistoryUtil.clearHis();
                    mWebView.loadUrl(BASE_WEEKLY_URL + "?token=" + mToken + "&actDefId=" + mActDefId );
                    mWebHistoryUtil.setUrl(BASE_WEEKLY_URL + "?token=" + mToken + "&actDefId=" + mActDefId);
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
                if (isContact || isProductName) {
                    mWebView.loadUrl("javascript:closedPopup()");
                    mWebView.loadUrl("javascript:backValue()");
                    isContact = false;
                    isProductName = false;
                } else {
                    if (mHisIndex != 0) {//当webview不是处于第一页面时，返回上一个页面
                        mWebView.loadUrl(mWebHistoryUtil.getUrl(mHisIndex));
                        mWebHistoryUtil.remove(mHisIndex);
                        mHisIndex--;


                        if (mWebView.getUrl().contains("approval_speed.html")) {
                            mIsApplyHistory = true;
                            mIsApplyApproval = false;
                            mHeadRightText.setText("审批历史");
                            mHeadRightText.setVisibility(View.VISIBLE);

                        } else if (mWebView.getUrl().contains("integral_detail.html") || mWebView.getUrl().contains("apply_integral.html")) {
                            mIsApplyApproval = true;
                            mIsApplyHistory = false;
                            mHeadRightText.setText("审批进度");
                            mHeadRightText.setVisibility(View.VISIBLE);
                        }else  if (mWebView.getUrl().contains("matter_list.html")) {
                            mHeadRightText.setVisibility(View.VISIBLE);
                            mHeadRightText.setText("新增");
                            mIsApplyApproval = false;
                            mIsApplyHistory = false;
                        }
                    } else {
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
                }else {
                    mWebView.loadUrl(ConstantValue.WEB_BASE_URL + "html/reimbursement_matter/add_matter.html" + "?token=" + mToken + "&actDefId=" + mActDefId);
                    mWebHistoryUtil.setUrl(ConstantValue.WEB_BASE_URL + "html/reimbursement_matter/add_matter.html" + "?token=" + mToken + "&actDefId=" + mActDefId);
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
            if (isContact || isProductName) {
                mWebView.loadUrl("javascript:closedPopup()");
                mWebView.loadUrl("javascript:backValue()");
                isContact = false;
                isProductName = false;
            } else {
                if (mHisIndex != 0) {//当webview不是处于第一页面时，返回上一个页面
                    mWebView.loadUrl(mWebHistoryUtil.getUrl(mHisIndex));
                    mWebHistoryUtil.remove(mHisIndex);
                    mHisIndex--;

                    if (mWebView.getUrl().contains("approval_speed.html")) {
                        mIsApplyHistory = true;
                        mIsApplyApproval = false;
                        mHeadRightText.setText("审批历史");
                        mHeadRightText.setVisibility(View.VISIBLE);

                    } else if (mWebView.getUrl().contains("integral_detail.html") || mWebView.getUrl().contains("apply_integral.html")) {
                        mIsApplyApproval = true;
                        mIsApplyHistory = false;
                        mHeadRightText.setText("审批进度");
                        mHeadRightText.setVisibility(View.VISIBLE);
                    }else  if (mWebView.getUrl().contains("matter_list.html")) {
                        mHeadRightText.setVisibility(View.VISIBLE);
                        mHeadRightText.setText("新增");
                        mIsApplyApproval = false;
                        mIsApplyHistory = false;
                    }
                    return true;
                } else {//当webview处于第一页面时,直接退出程序
                    super.onBackPressed();
                    mWebHistoryUtil.clearHis();
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
