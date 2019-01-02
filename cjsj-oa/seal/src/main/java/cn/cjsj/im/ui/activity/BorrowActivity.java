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
 * 借款
 * Created by LuoYang on 2018/1/31.
 */

public class BorrowActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.borrow_webview)
    WebView mWebView;

    @Bind(R.id.borrow_web_progressbar)
    ProgressBar mProgressBar;

    private Button mBackBtn;

    private static final String BASE_WEEKLY_URL = ConstantValue.WEB_BASE_URL + "html/loan/my_loan_list.html";

    private static final String TAG = "BorrowActivity";

    private String mToken;

    private WebHistoryUtil mWebHistoryUtil; //历史工具类

    private Intent mIntent;
    private String mActDefId;

    private int mHisIndex = 0;

    private boolean isContact = false;
    private boolean isReimburseDetail = false;
    private boolean isReimburseRelevance = false;
    private boolean isProductName = false;
    private boolean isVerify = false;
    private boolean isGatheringNo = false;
    private boolean isAddBankCard = false;
    private int mCloseBankCardIndex = 0;

    private boolean mIsApplyApproval = false;//审批进度
    private boolean mIsApplyHistory = false;//审批历史
    private boolean theSecondAdd = false;//添加银行卡控制添加成功后继续在银行卡列表Title继续显示添加按钮

    private String mBusinessKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_borrow);
        ButterKnife.bind(this);
        setTitle("工作请款");

        mBackBtn = getHeadLeftButton();
        mBackBtn.setOnClickListener(this);

        mIntent = getIntent();
        mActDefId = mIntent.getStringExtra("actDefId");

        mHeadRightText.setVisibility(View.VISIBLE);
        mHeadRightText.setText("新增");
        mHeadRightText.setClickable(true);
        mHeadRightText.setOnClickListener(this);

        mWebHistoryUtil = new WebHistoryUtil();

        mToken = App.getInstance().getToken();

        WebSettings webSettings = mWebView.getSettings();
        WebViewInitSetting.getInstance(this).webViewSetting(webSettings);
        WebViewInitSetting.getInstance(this).initLoad(mWebView);

        mWebView.loadUrl(BASE_WEEKLY_URL + "?token=" + mToken + "&actDefId=" + mActDefId);
        mWebHistoryUtil.setUrl(BASE_WEEKLY_URL + "?token=" + mToken + "&actDefId=" + mActDefId);

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
                    isReimburseDetail = false;
                    isReimburseRelevance = false;
                    isAddBankCard = false;
                    isContact = false;
                    mIsApplyApproval = false;//审批进度
                    mIsApplyHistory = false;//审批历史
                    mHisIndex = 0;
                    mCloseBankCardIndex = 0;
                    mWebHistoryUtil.clearHis();
                    mWebView.loadUrl(BASE_WEEKLY_URL + "?token=" + mToken + "&actDefId=" + mActDefId);
                    mWebHistoryUtil.setUrl(BASE_WEEKLY_URL + "?token=" + mToken + "&actDefId=" + mActDefId);
                    mHeadRightText.setText("新增");
                    mHeadRightText.setVisibility(View.VISIBLE);
                    return true;
                } else if (url.contains("protocol://android.contact")) {
                    isContact = true;
                    mHeadRightText.setVisibility(View.INVISIBLE);
                    return true;
                } else if (url.contains("loan_detail.html") || url.contains("pay_off_detail.html")) {
                    mWebHistoryUtil.setUrl(url);
                    mHisIndex++;
                    mHeadRightText.setText("审批进度");
                    int idUrl = url.indexOf("id=");
                    mBusinessKey = url.substring(idUrl + 3);
                    mIsApplyApproval = true;
                    return false;
                } else if (url.contains("protocol://android.reimburse.detail")) { //申请报销明细
                    isReimburseDetail = true;
                    return true;
                } else if (url.contains("protocol://android.reimburse.relevance")) {  //关联报销事项
                    isReimburseRelevance = true;
                    return true;
                } else if (url.contains("protocol://android.closewindow")) { //告知关闭窗口
                    isContact = false;
                    isReimburseDetail = false;
                    isReimburseRelevance = false;
                    isProductName = false;
                    isVerify = false;
                    isAddBankCard = true;
                    mCloseBankCardIndex = 0;
                    isGatheringNo = true;
                    if (theSecondAdd) {
                        mHeadRightText.setText("添加");
                        mHeadRightText.setVisibility(View.VISIBLE);
                    }
                    return true;
                } else if (url.contains("protocol://android.reimburse.save")) { //项目明细保存
                    mWebView.loadUrl(ConstantValue.WEB_BASE_URL + "html/reimbursement/apply_reimbursement.html" + "?token=" + mToken + "&actDefId=" + mActDefId);
                    return true;
                } else if (url.contains("protocol://android.reimburse.productname")) { //项目名称
                    isProductName = true;
                    return true;
                } else if (url.contains("protocol://android.reimburse.verify")) {//核销借款
                    isVerify = true;
                    return true;
                } else if (url.contains("protocol://android.borrow.gatheringNo")) {  //收款账号
                    isGatheringNo = true;
                    isAddBankCard = true;
                    mHeadRightText.setText("添加");
                    mHeadRightText.setVisibility(View.VISIBLE);
                    return true;
                } else if (url.contains("non_repayment_loan.html")) {
                    mHeadRightText.setVisibility(View.INVISIBLE);
                    mWebHistoryUtil.setUrl(url);
                    mHisIndex++;
                    return false;
                } else if (url.contains("repayment.html")) {
                    mWebView.loadUrl(url + "&token=" + mToken + "&actDefId=" + "zjhk:1:10000002380151");
                    mWebHistoryUtil.setUrl(url + "&token=" + mToken + "&actDefId=" + "zjhk:1:10000002380151");
                    mHisIndex++;
                    return true;
                } else {
                    mWebHistoryUtil.setUrl(url);
                    mHisIndex++;
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

                if (isGatheringNo) {
                    mWebView.loadUrl("javascript:closedPopupChooseBank()");
                    mWebView.loadUrl("javascript:backValue()");
                    isGatheringNo = false;
                    isAddBankCard = false;
                    mHeadRightText.setText("新增");
                    mHeadRightText.setVisibility(View.INVISIBLE);
                } else if (isAddBankCard && mCloseBankCardIndex == 1) {
                    mWebView.loadUrl("javascript:closedPopupBank()");
                    mWebView.loadUrl("javascript:backValueAddBank()");
                    isAddBankCard = true;
                    isGatheringNo = true;
                    mCloseBankCardIndex = 0;
                    mHeadRightText.setText("添加");
                    mHeadRightText.setVisibility(View.VISIBLE);
                } else if (isVerify) {
                    mWebView.loadUrl("javascript:closedPopup()");
                    mWebView.loadUrl("javascript:backValue()");
                    isVerify = false;
                } else if (isContact || isReimburseRelevance || isReimburseDetail || isProductName) {
                    mWebView.loadUrl("javascript:closedPopup()");
                    mWebView.loadUrl("javascript:backValue()");
                    isContact = false;
                    isReimburseDetail = false;
                    isReimburseRelevance = false;
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

                        } else if (mWebView.getUrl().contains("loan_detail.html") || mWebView.getUrl().contains("apply_loan.html") || mWebView.getUrl().contains("pay_off_detail.html")) {
                            mIsApplyApproval = true;
                            mIsApplyHistory = false;
                            mHeadRightText.setText("审批进度");
                            mHeadRightText.setVisibility(View.VISIBLE);
                        } else if (mWebView.getUrl().contains("my_loan_list.html")) {
                            mHeadRightText.setVisibility(View.VISIBLE);
                            mIsApplyApproval = false;
                            mIsApplyHistory = false;
                            mHeadRightText.setText("新增");

                        }
                    } else {
                        finish();
                        mWebHistoryUtil.clearHis();
                    }
                }
                break;

            case R.id.text_right:
                if (isAddBankCard) {
                    mWebView.loadUrl("javascript:appAddClick()");
                    mHeadRightText.setText("新增");
                    mHeadRightText.setVisibility(View.INVISIBLE);
                    isGatheringNo = false;
                    mCloseBankCardIndex = 1;
                    theSecondAdd = true;
                } else if (mIsApplyApproval) {
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
                    mWebView.loadUrl(ConstantValue.WEB_BASE_URL + "html/loan/apply_loan.html" + "?token=" + mToken + "&actDefId=" + mActDefId + "&businessKey=" + mBusinessKey);
                    mWebHistoryUtil.setUrl(ConstantValue.WEB_BASE_URL + "html/loan/apply_loan.html" + "?token=" + mToken + "&actDefId=" + mActDefId + "&businessKey=" + mBusinessKey);
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
            if (isGatheringNo) {
                mWebView.loadUrl("javascript:closedPopupChooseBank()");
                mWebView.loadUrl("javascript:backValue()");
                isGatheringNo = false;
                isAddBankCard = false;
                mHeadRightText.setText("新增");
                mHeadRightText.setVisibility(View.INVISIBLE);
            } else if (isAddBankCard && mCloseBankCardIndex == 1) {
                mWebView.loadUrl("javascript:closedPopupBank()");
                mWebView.loadUrl("javascript:backValueAddBank()");
                isAddBankCard = false;
                isGatheringNo = true;
            } else if (isVerify) {
                mWebView.loadUrl("javascript:closedPopup()");
                mWebView.loadUrl("javascript:backValue()");
                isVerify = false;
            } else if (isContact || isReimburseRelevance || isReimburseDetail || isProductName) {
                mWebView.loadUrl("javascript:closedPopup()");
                mWebView.loadUrl("javascript:backValue()");
                isContact = false;
                isReimburseDetail = false;
                isReimburseRelevance = false;
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

                    } else if (mWebView.getUrl().contains("loan_detail.html") || mWebView.getUrl().contains("apply_loan.html") || mWebView.getUrl().contains("pay_off_detail.html")) {
                        mIsApplyApproval = true;
                        mIsApplyHistory = false;
                        mHeadRightText.setText("审批进度");
                        mHeadRightText.setVisibility(View.VISIBLE);
                    } else if (mWebView.getUrl().contains("my_loan_list.html")) {
                        mHeadRightText.setVisibility(View.VISIBLE);
                        mIsApplyApproval = false;
                        mIsApplyHistory = false;
                        mHeadRightText.setText("新增");

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
