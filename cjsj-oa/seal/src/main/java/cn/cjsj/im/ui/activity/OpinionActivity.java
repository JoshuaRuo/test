package cn.cjsj.im.ui.activity;

import android.app.DownloadManager;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
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
import cn.cjsj.im.server.UpdateService;
import cn.cjsj.im.utils.WebViewCacheClearUtil;

/**
 * Created by LuoYang on 2018/8/29 15:45
 * 意见稿
 */
public class OpinionActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.opinion_web_progressbar)
    ProgressBar mProgressBar;

    @Bind(R.id.opinion_webview)
    WebView mWebView;

    private Button mBackBtn;

    private static final String BASE_WEEKLY_URL = ConstantValue.WEB_BASE_URL + "html/view_draft/view_list.html";

    private static final String TAG = "OpinionActivity";

    private String mToken;

    private WebHistoryUtil mWebHistoryUtil; //历史工具类


    private int mHisIndex = 0;

    private boolean isOpinionFilter = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_opinnion);
        ButterKnife.bind(this);
        setTitle("意见稿");

        mBackBtn = getHeadLeftButton();
        mBackBtn.setOnClickListener(this);

        mWebHistoryUtil = new WebHistoryUtil();

        mToken = App.getInstance().getToken();

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
                if (url.contains("protocol://opinion.idea")) {
                    isOpinionFilter = true;
                    return true;
                } else if (url.contains(".jpg") || url.contains(".png") || url.contains(".pdf") || url.contains(".doc") || url.contains(".docx")) {
                    downloadByBrowser(url);
                    return true;
                } else if (url.contains("protocol://android.closewindow")) {
                    mHisIndex = 0;
                    isOpinionFilter = false;
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
//        mWebView.setDownloadListener(new DownloadListener() {
//            @Override
//            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
//                // TODO: 2017-5-6 处理下载事件
////                downloadBySystem(url,contentDisposition,mimeType);
//
//                downloadByBrowser(url);
//            }
//        });

    }

    private void downloadByBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private void downloadBySystem(String url, String contentDisposition, String mimeType) {
        // 指定下载地址
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        // 允许媒体扫描，根据下载的文件类型被加入相册、音乐等媒体库
        request.allowScanningByMediaScanner();
        // 设置通知的显示类型，下载进行时和完成后显示通知
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        // 设置通知栏的标题，如果不设置，默认使用文件名
//        request.setTitle("This is title");
        // 设置通知栏的描述
//        request.setDescription("This is description");
        // 允许在计费流量下下载
        request.setAllowedOverMetered(false);
        // 允许该记录在下载管理界面可见
        request.setVisibleInDownloadsUi(false);
        // 允许漫游时下载
        request.setAllowedOverRoaming(true);
        // 允许下载的网路类型
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        // 设置下载文件保存的路径和文件名
        String fileName = URLUtil.guessFileName(url, contentDisposition, mimeType);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
//        另外可选一下方法，自定义下载路径
//        request.setDestinationUri()
//        request.setDestinationInExternalFilesDir()
        final DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        // 添加一个下载任务
        long downloadId = downloadManager.enqueue(request);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_left:
                if (isOpinionFilter) {
                    mWebView.loadUrl("javascript:closedPopup()");
                    mWebView.loadUrl("javascript:backValue()");
                    isOpinionFilter = false;
                } else {
                    if (mHisIndex != 0) {//当webview不是处于第一页面时，返回上一个页面
                        mWebView.loadUrl(mWebHistoryUtil.getUrl(mHisIndex));
                        mWebHistoryUtil.remove(mHisIndex);
                        mHisIndex--;

                    } else {//当webview处于第一页面时,直接退出程序
                        finish();
                        mWebHistoryUtil.clearHis();
                    }

                }
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebHistoryUtil.clearHis();
        mWebView.clearHistory();
        WebViewCacheClearUtil.clearCache(mWebView, this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isOpinionFilter) {
                mWebView.loadUrl("javascript:closedPopup()");
                mWebView.loadUrl("javascript:backValue()");
                isOpinionFilter = false;
            } else {
                if (mHisIndex != 0) {//当webview不是处于第一页面时，返回上一个页面
                    mWebView.loadUrl(mWebHistoryUtil.getUrl(mHisIndex));
                    mWebHistoryUtil.remove(mHisIndex);
                    mHisIndex--;
                    return true;
                } else {//当webview处于第一页面时,直接退出程序
                    super.onBackPressed();
                    mWebHistoryUtil.clearHis();
                }
            }
        }
        return false;
    }
}
