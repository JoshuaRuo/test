package cn.cjsj.im.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;


import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.cjsj.im.App;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.WebHistoryUtil;
import cn.cjsj.im.gty.WebViewInitSetting;
import cn.cjsj.im.gty.common.ConstantValue;

/**
 * 待办详情
 * Created by LuoYang on 2018/1/3.
 */

public class AgendaDetailActivity extends BaseActivity implements View.OnClickListener {

    private static final String BASE_WEEKLY_URL = ConstantValue.WEB_BASE_URL + "html/mine/task_public.html";

    private static final String TAG = "AgendaDetailActivity";

    private Button mBackBtn;

    @Bind(R.id.agenda_detail_webview)
    WebView mWebView;
    @Bind(R.id.agenda_detail_web_progressbar)
    ProgressBar mProgressBar;

    private Intent mIntent;

    private String mActDefId;

    private String mBusinessKey;
    private String mToken;
    private String mRunId;
    private String mStatus;

    private boolean mIsApplyApproval = false;
    private boolean mIsApplyHistory = false;
    private boolean isContact = false;
    private boolean mIsIntegral = false;
    private boolean isSelectSeal = false;//添加印章

    private WebHistoryUtil mWebHistoryUtil; //历史工具类
    private int mHisIndex = 0;
    private boolean isReimburseDetail = false;
    private boolean isReimburseRelevance = false;
    private boolean isProductName = false;
    private boolean isVerify = false;
    private boolean isGatheringNo = false;
    private boolean isAddBankCard = false;
    private int mCloseBankCardIndex = 0;

    private UploadHandler mUploadHandler;
    private ValueCallback<Uri[]> mUploadMessageForAndroid5;
    public final static int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 2;

    private int myRequest = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_agenda_detail);
        ButterKnife.bind(this);


        mBackBtn = getHeadLeftButton();
        mBackBtn.setOnClickListener(this);

        mToken = App.getInstance().getToken();

        mWebHistoryUtil = new WebHistoryUtil();

        mHeadRightText.setVisibility(View.INVISIBLE);
        mHeadRightText.setClickable(true);
        mHeadRightText.setOnClickListener(this);

        WebSettings webSettings = mWebView.getSettings();
        WebViewInitSetting.getInstance(this).webViewSetting(webSettings);
        WebViewInitSetting.getInstance(this).initLoad(mWebView);

        mIntent = getIntent();
        mActDefId = mIntent.getStringExtra("actDefId");
        mBusinessKey = mIntent.getStringExtra("businessKey");
        mRunId = mIntent.getStringExtra("runId");
        mStatus = mIntent.getStringExtra("status");
        myRequest = mIntent.getIntExtra("MyRequest", 0);

        if (myRequest == 0) {
            setTitle("我的待办");
        } else {
            setTitle("我的申请");
        }

        mWebView.loadUrl(BASE_WEEKLY_URL + "?token=" + mToken + "&actDefId=" + mActDefId + "&businessKey=" + mBusinessKey + "&type=" + myRequest + "&runId=" + mRunId + "&status=" + mStatus );
        mWebHistoryUtil.setUrl(BASE_WEEKLY_URL + "?token=" + mToken + "&actDefId=" + mActDefId + "&businessKey=" + mBusinessKey + "&type=" + myRequest + "&runId=" + mRunId + "&status=" + mStatus);

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
                if (url.contains("reimbursement_detail.html") || url.contains("apply_reimbursement.html") || url.contains("loan_detail.html") || url.contains("pay_off_detail.html")
                        || url.contains("integral_detail.html") || url.contains("apply_integral.html")
                        || url.contains("matter_detail.html") || url.contains("add_matter.html") || url.contains("leave_detail.html")
                        || url.contains("trip_detail.html") || url.contains("supplement_detail.html") || url.contains("seal_detail.html")
                        || url.contains("add_outsourcing_contract.html") || url.contains("goods_detail.html") || url.contains("goodsPurchase.html")
                        || url.contains("vote_detail.html") || url.contains("view_detail.html") || url.contains("scan_details.html")
                        || url.contains("performance_plan.html") || url.contains("performance_default.html")) {
                    mIsApplyApproval = true;
                    mHeadRightText.setText("审批进度");
                    mHeadRightText.setVisibility(View.VISIBLE);
                    mWebHistoryUtil.setUrl(url);
                    mHisIndex++;
                    return false;
                } else if (url.contains("approval_history.html")) {
                    mIsApplyHistory = true;
                    mHeadRightText.setText("审批历史");
                    mHeadRightText.setVisibility(View.VISIBLE);
                    mWebHistoryUtil.setUrl(url);
                    mHisIndex++;
                    return false;
                } else if (url.contains("protocol://android.finish")) {
//                    finish();
                    mIsApplyApproval = false;
                    mIsApplyHistory = false;
                    mHisIndex = 0;
                    mWebHistoryUtil.clearHis();

                    isContact = false;
                    mIsIntegral = false;
                    isReimburseDetail = false;
                    isReimburseRelevance = false;
                    isProductName = false;
                    isVerify = false;
                    isGatheringNo = false;
                    isAddBankCard = false;
//                    mWebView.loadUrl(BASE_WEEKLY_URL + "?token=" + mToken + "&actDefId=" + mActDefId + "&businessKey=" + mBusinessKey + "&type=" + "0" + "&runId=" + mRunId + "&status=" + mStatus);
//                    mWebHistoryUtil.setUrl(BASE_WEEKLY_URL + "?token=" + mToken + "&actDefId=" + mActDefId + "&businessKey=" + mBusinessKey + "&type=" + "0" + "&runId=" + mRunId + "&status=" + mStatus);
                    mWebView.loadUrl(ConstantValue.WEB_BASE_URL + "html/mine/task_waitList.html" + "?token=" + mToken);
                    mWebHistoryUtil.setUrl(ConstantValue.WEB_BASE_URL + "html/mine/task_waitList.html" + "?token=" + mToken);
                    return true;
                } else if (url.contains("protocol://android.contact")) {
                    isContact = true;
                    mHeadRightText.setVisibility(View.INVISIBLE);
                    return true;
                } else if (url.contains("protocol://android.closewindow")) { //告知关闭窗口
                    isContact = false;
                    mIsIntegral = false;
                    isReimburseDetail = false;
                    isReimburseRelevance = false;
                    isProductName = false;
                    isVerify = false;
                    isGatheringNo = false;
                    isSelectSeal = false;
                    isAddBankCard = false;
                    mCloseBankCardIndex = 0;
                    return true;
                } else if (url.contains("protocol://android.integral")) {  //积分事项
//                    finish();
                    mIsIntegral = true;
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
                } else if (url.contains("protocol://android.reimburse.detail")) { //申请报销明细
                    isReimburseDetail = true;
                    return true;
                } else if (url.contains("protocol://android.reimburse.relevance")) {  //关联报销事项
                    isReimburseRelevance = true;
                    return true;
                } else if (url.contains("protocol://webapp.select.seal")) {
                    isSelectSeal = true;
                    return true;
                } else if (url.contains("vote.html") || url.contains("vote_check.html")) {
                    setTitle("投票");
                    return false;
                } else {
//                    mWebHistoryUtil.setUrl(url);
//                    mHisIndex++;
                    return false;
                }
            }

        });

        mWebView.setWebChromeClient(new MyChromeViewClient());
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
                } else if (isContact || isReimburseRelevance || isReimburseDetail || isProductName || mIsIntegral || isSelectSeal) {
                    mWebView.loadUrl("javascript:closedPopup()");
                    mWebView.loadUrl("javascript:backValue()");
                    isContact = false;
                    isReimburseDetail = false;
                    isReimburseRelevance = false;
                    isProductName = false;
                    mIsIntegral = false;
                } else {
                    if (mHisIndex > 1) {//当webview不是处于第一页面时，返回上一个页面
                        mWebView.loadUrl(mWebHistoryUtil.getUrl(mHisIndex));
                        mWebHistoryUtil.remove(mHisIndex);
                        mHisIndex--;

                        if (mWebView.getUrl().contains("approval_speed.html")) {
                            mIsApplyHistory = true;
                            mHeadRightText.setText("审批历史");
                            mHeadRightText.setVisibility(View.VISIBLE);

                        } else if (mWebView.getUrl().contains("reimbursement_detail.html") || mWebView.getUrl().contains("apply_reimbursement.html")
                                || mWebView.getUrl().contains("loan_detail.html") || mWebView.getUrl().contains("pay_off_detail.html")
                                || mWebView.getUrl().contains("integral_detail.html") || mWebView.getUrl().contains("apply_integral.html")
                                || mWebView.getUrl().contains("matter_detail.html") || mWebView.getUrl().contains("add_matter.html")) {
                            mIsApplyApproval = true;
                            mHeadRightText.setText("审批进度");
                            mHeadRightText.setVisibility(View.VISIBLE);
                        } else if (mWebView.getUrl().contains("task_public.html")) {
                            mHeadRightText.setVisibility(View.INVISIBLE);
                        }
                    } else {//当webview处于第一页面时,直接退出程序
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
                } else if (mIsApplyApproval) {
                    mWebView.loadUrl(ConstantValue.WEB_BASE_URL + "html/apply_approval/approval_speed.html" + "?token=" + mToken + "&actDefId=" + mActDefId + "&businessKey=" + mBusinessKey + "&type=" + myRequest + "&runId=" + mRunId + "&status=" + mStatus);
                    mWebHistoryUtil.setUrl(ConstantValue.WEB_BASE_URL + "html/apply_approval/approval_speed.html" + "?token=" + mToken + "&actDefId=" + mActDefId + "&businessKey=" + mBusinessKey + "&type=" + myRequest + "&runId=" + mRunId + "&status=" + mStatus);
                    mHisIndex++;
                    mHeadRightText.setText("审批历史");
                    mIsApplyApproval = false;
                    mIsApplyHistory = true;
                } else if (mIsApplyHistory) {
                    mWebView.loadUrl(ConstantValue.WEB_BASE_URL + "html/apply_approval/approval_history.html" + "?token=" + mToken + "&actDefId=" + mActDefId + "&businessKey=" + mBusinessKey + "&type=" + myRequest + "&runId=" + mRunId + "&status=" + mStatus);
                    mWebHistoryUtil.setUrl(ConstantValue.WEB_BASE_URL + "html/apply_approval/approval_history.html" + "?token=" + mToken + "&actDefId=" + mActDefId + "&businessKey=" + mBusinessKey + "&type=" + myRequest + "&runId=" + mRunId + "&status=" + mStatus);
                    mHisIndex++;
                    mHeadRightText.setVisibility(View.INVISIBLE);
                    mIsApplyHistory = false;
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
            } else if (isContact || isReimburseRelevance || isReimburseDetail || isProductName || mIsIntegral || isSelectSeal) {
                mWebView.loadUrl("javascript:closedPopup()");
                mWebView.loadUrl("javascript:backValue()");
                isContact = false;
                isReimburseDetail = false;
                isReimburseRelevance = false;
                isProductName = false;
                mIsIntegral = false;
            } else {
                if (mHisIndex > 1) {//当webview不是处于第一页面时，返回上一个页面
                    mWebView.loadUrl(mWebHistoryUtil.getUrl(mHisIndex));
                    mWebHistoryUtil.remove(mHisIndex);
                    mHisIndex--;
//                if (mWebView.canGoBack()) {//当webview不是处于第一页面时，返回上一个页面
//                    mWebView.goBack();

                    if (mWebView.getUrl().contains("approval_speed.html")) {
                        mIsApplyHistory = true;
                        mIsApplyApproval = false;
                        mHeadRightText.setText("审批历史");
                        mHeadRightText.setVisibility(View.VISIBLE);

                    } else if (mWebView.getUrl().contains("reimbursement_detail.html") || mWebView.getUrl().contains("apply_reimbursement.html")
                            || mWebView.getUrl().contains("loan_detail.html") || mWebView.getUrl().contains("pay_off_detail.html")
                            || mWebView.getUrl().contains("integral_detail.html") || mWebView.getUrl().contains("apply_integral.html")
                            || mWebView.getUrl().contains("matter_detail.html") || mWebView.getUrl().contains("add_matter.html")) {
                        mIsApplyApproval = true;
                        mIsApplyHistory = false;
                        mHeadRightText.setText("审批进度");
                        mHeadRightText.setVisibility(View.VISIBLE);
                    } else if (mWebView.getUrl().contains("task_public.html")) {
                        mHeadRightText.setVisibility(View.INVISIBLE);
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

    /**
     * 说明：
     * <p>
     * MyChromeViewClient
     * 继承WebChromeClient重写了几个关键方法。其中有三个重载方法openFileChooser，用来兼容不同的Andorid版本
     * ，以防出现NoSuchMethodError异常。
     * 另外一个类UploadHandler，起到一个解耦合作用，它相当于WebChromeClient和Web网页端的一个搬运工兼职翻译
     * ，解析网页端传递给WebChromeClient的动作
     * ，然后将onActivityResult接收用户选择的文件传递给司机ValueCallback
     * 。WebChromeClient提供了一个Web网页端和客户端交互的通道，而UploadHandler就是用来搬砖的~。
     * UploadHandler有个很重要的成员变量：ValueCallback<Uri>
     * mUploadMessage。ValueCallback是WebView留下来的一个回调
     * ，就像是WebView的司机一样，当WebChromeClient和UploadHandler合作将文件选择后
     * ，ValueCallback开始将文件给WebView，告诉WebView开始干活了，砖头已经运回来了，你可以盖房子了。
     */
    class MyChromeViewClient extends WebChromeClient {

        @Override
        public void onCloseWindow(WebView window) {
            AgendaDetailActivity.this.finish();
            super.onCloseWindow(window);
        }

        public void onProgressChanged(WebView view, final int progress) {
            if (progress == 100) {
                mProgressBar.setVisibility(View.GONE);
            } else {
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.setProgress(progress);
            }
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message,
                                 final JsResult result) {

            new AlertDialog.Builder(AgendaDetailActivity.this)
                    .setTitle("提示信息")
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    result.confirm();
                                }
                            }).setCancelable(false).create().show();
            return true;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message,
                                   final JsResult result) {

            new AlertDialog.Builder(AgendaDetailActivity.this)
                    .setTitle("提示信息")
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    result.confirm();
                                }
                            })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    result.cancel();
                                }
                            }).setCancelable(false).create().show();
            return true;

        }

        // Android 2.x
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooser(uploadMsg, "");
        }

        // Android 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                    String acceptType) {
            openFileChooser(uploadMsg, "", "filesystem");
        }

        // Android 4.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                    String acceptType, String capture) {

            mUploadHandler = new UploadHandler(new Controller());
            mUploadHandler.openFileChooser(uploadMsg, acceptType, capture);
        }

        // For Android 5.0+
        public boolean onShowFileChooser(WebView webView,
                                         ValueCallback<Uri[]> filePathCallback,
                                         WebChromeClient.FileChooserParams fileChooserParams) {

            openFileChooserImplForAndroid5(filePathCallback);
            return true;
        }

    }

    private void openFileChooserImplForAndroid5(ValueCallback<Uri[]> uploadMsg) {
        mUploadMessageForAndroid5 = uploadMsg;
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("image/*");

        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "选择图片");
        System.out.println("-----------调用");
        startActivityForResult(chooserIntent,
                FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
    }

    class MyWebViewClinet extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            return true;
        }

    }

    // copied from android-4.4.3_r1/src/com/android/browser/UploadHandler.java

    class UploadHandler {
        /*
         * The Object used to inform the WebView of the file to upload.
         */
        private ValueCallback<Uri> mUploadMessage;
        private String mCameraFilePath;
        private boolean mHandled;
        private boolean mCaughtActivityNotFoundException;
        private Controller mController;

        public UploadHandler(Controller controller) {
            mController = controller;
        }

        public String getFilePath() {
            return mCameraFilePath;
        }

        boolean handled() {
            return mHandled;
        }

        public void onResult(int resultCode, Intent intent) {
            if (resultCode == Activity.RESULT_CANCELED
                    && mCaughtActivityNotFoundException) {
                // Couldn't resolve an activity, we are going to try again so
                // skip
                // this result.
                mCaughtActivityNotFoundException = false;
                return;
            }
            Uri result = (intent == null || resultCode != Activity.RESULT_OK) ? null
                    : intent.getData();

            // As we ask the camera to save the result of the user taking
            // a picture, the camera application does not return anything other
            // than RESULT_OK. So we need to check whether the file we expected
            // was written to disk in the in the case that we
            // did not get an intent returned but did get a RESULT_OK. If it
            // was,
            // we assume that this result has came back from the camera.
            if (result == null && intent == null
                    && resultCode == Activity.RESULT_OK) {
                File cameraFile = new File(mCameraFilePath);
                if (cameraFile.exists()) {
                    result = Uri.fromFile(cameraFile);
                    // Broadcast to the media scanner that we have a new photo
                    // so it will be added into the gallery for the user.
                    mController.getActivity().sendBroadcast(
                            new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                    result));
                }
            }
            mUploadMessage.onReceiveValue(result);
            mHandled = true;
            mCaughtActivityNotFoundException = false;
        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                    String acceptType, String capture) {
            final String imageMimeType = "image/*";
            final String videoMimeType = "video/*";
            final String audioMimeType = "audio/*";
            final String mediaSourceKey = "capture";
            final String mediaSourceValueCamera = "camera";
            final String mediaSourceValueFileSystem = "filesystem";
            final String mediaSourceValueCamcorder = "camcorder";
            final String mediaSourceValueMicrophone = "microphone";
            // According to the spec, media source can be 'filesystem' or
            // 'camera' or 'camcorder'
            // or 'microphone' and the default value should be 'filesystem'.
            String mediaSource = mediaSourceValueFileSystem;
            if (mUploadMessage != null) {
                // Already a file picker operation in progress.
                return;
            }
            mUploadMessage = uploadMsg;
            // Parse the accept type.
            String params[] = acceptType.split(";");
            String mimeType = params[0];
            if (capture.length() > 0) {
                mediaSource = capture;
            }
            if (capture.equals(mediaSourceValueFileSystem)) {
                // To maintain backwards compatibility with the previous
                // implementation
                // of the media capture API, if the value of the 'capture'
                // attribute is
                // "filesystem", we should examine the accept-type for a MIME
                // type that
                // may specify a different capture value.
                for (String p : params) {
                    String[] keyValue = p.split("=");
                    if (keyValue.length == 2) {
                        // Process key=value parameters.
                        if (mediaSourceKey.equals(keyValue[0])) {
                            mediaSource = keyValue[1];
                        }
                    }
                }
            }
            // Ensure it is not still set from a previous upload.
            mCameraFilePath = null;
            if (mimeType.equals(imageMimeType)) {
                if (mediaSource.equals(mediaSourceValueCamera)) {
                    // Specified 'image/*' and requested the camera, so go ahead
                    // and launch the
                    // camera directly.
                    startActivity(createCameraIntent());
                    return;
                } else {
                    // Specified just 'image/*', capture=filesystem, or an
                    // invalid capture parameter.
                    // In all these cases we show a traditional picker filetered
                    // on accept type
                    // so launch an intent for both the Camera and image/*
                    // OPENABLE.
                    Intent chooser = createChooserIntent(createCameraIntent());
                    chooser.putExtra(Intent.EXTRA_INTENT,
                            createOpenableIntent(imageMimeType));
                    startActivity(chooser);
                    return;
                }
            } else if (mimeType.equals(videoMimeType)) {
                if (mediaSource.equals(mediaSourceValueCamcorder)) {
                    // Specified 'video/*' and requested the camcorder, so go
                    // ahead and launch the
                    // camcorder directly.
                    startActivity(createCamcorderIntent());
                    return;
                } else {
                    // Specified just 'video/*', capture=filesystem or an
                    // invalid capture parameter.
                    // In all these cases we show an intent for the traditional
                    // file picker, filtered
                    // on accept type so launch an intent for both camcorder and
                    // video/* OPENABLE.
                    Intent chooser = createChooserIntent(createCamcorderIntent());
                    chooser.putExtra(Intent.EXTRA_INTENT,
                            createOpenableIntent(videoMimeType));
                    startActivity(chooser);
                    return;
                }
            } else if (mimeType.equals(audioMimeType)) {
                if (mediaSource.equals(mediaSourceValueMicrophone)) {
                    // Specified 'audio/*' and requested microphone, so go ahead
                    // and launch the sound
                    // recorder.
                    startActivity(createSoundRecorderIntent());
                    return;
                } else {
                    // Specified just 'audio/*', capture=filesystem of an
                    // invalid capture parameter.
                    // In all these cases so go ahead and launch an intent for
                    // both the sound
                    // recorder and audio/* OPENABLE.
                    Intent chooser = createChooserIntent(createSoundRecorderIntent());
                    chooser.putExtra(Intent.EXTRA_INTENT,
                            createOpenableIntent(audioMimeType));
                    startActivity(chooser);
                    return;
                }
            }
            // No special handling based on the accept type was necessary, so
            // trigger the default
            // file upload chooser.
            startActivity(createDefaultOpenableIntent());
        }

        private void startActivity(Intent intent) {
            try {
                mController.getActivity().startActivityForResult(intent,
                        Controller.FILE_SELECTED);
            } catch (ActivityNotFoundException e) {
                // No installed app was able to handle the intent that
                // we sent, so fallback to the default file upload control.
                try {
                    mCaughtActivityNotFoundException = true;
                    mController.getActivity().startActivityForResult(
                            createDefaultOpenableIntent(),
                            Controller.FILE_SELECTED);
                } catch (ActivityNotFoundException e2) {
                    // Nothing can return us a file, so file upload is
                    // effectively disabled.
                    Toast.makeText(mController.getActivity(),
                            "File uploads are disabled.", Toast.LENGTH_LONG)
                            .show();
                }
            }
        }

        private Intent createDefaultOpenableIntent() {
            // Create and return a chooser with the default OPENABLE
            // actions including the camera, camcorder and sound
            // recorder where available.
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            Intent chooser = createChooserIntent(createCameraIntent(),
                    createCamcorderIntent(), createSoundRecorderIntent());
            chooser.putExtra(Intent.EXTRA_INTENT, i);
            return chooser;
        }

        private Intent createChooserIntent(Intent... intents) {
            Intent chooser = new Intent(Intent.ACTION_CHOOSER);
            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents);
            chooser.putExtra(Intent.EXTRA_TITLE, "Choose file for upload");
            return chooser;
        }

        private Intent createOpenableIntent(String type) {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType(type);
            return i;
        }

        private Intent createCameraIntent() {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File externalDataDir = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            File cameraDataDir = new File(externalDataDir.getAbsolutePath()
                    + File.separator + "browser-photos");
            cameraDataDir.mkdirs();
            mCameraFilePath = cameraDataDir.getAbsolutePath() + File.separator
                    + System.currentTimeMillis() + ".jpg";
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(mCameraFilePath)));
            return cameraIntent;
        }

        private Intent createCamcorderIntent() {
            return new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        }

        private Intent createSoundRecorderIntent() {
            return new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        }
    }


    class Controller {

        final static int FILE_SELECTED = 4;

        Activity getActivity() {
            return AgendaDetailActivity.this;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {

        if (requestCode == ApplyIntegralActivity.Controller.FILE_SELECTED) {
            // Chose a file from the file picker.
            if (mUploadHandler != null) {
                mUploadHandler.onResult(resultCode, intent);
            }
        } else if (requestCode == FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {
            if (null == mUploadMessageForAndroid5)
                return;
            Uri result = (intent == null || resultCode != Activity.RESULT_OK) ? null
                    : intent.getData();
            System.out.println("-----------界面执行了回调" + (result == null));
            if (result != null) {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
            } else {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
            }
            mUploadMessageForAndroid5 = null;
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }
}
