package cn.cjsj.im.gty;

import android.content.Context;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by LuoYang on 2017/12/14.
 */

public class WebViewInitSetting {

    private static WebViewInitSetting mWebViewInitSetting;
    private static String mCachePath  = "";

    public synchronized static WebViewInitSetting getInstance(Context context){
        mCachePath = context.getCacheDir().getAbsolutePath() + "/webViewCache ";
        if (mWebViewInitSetting == null){
            mWebViewInitSetting = new WebViewInitSetting();
            return mWebViewInitSetting;
        }else {
            return mWebViewInitSetting;
        }
    }

    public void webViewSetting(WebSettings webSettings){
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setUseWideViewPort(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCachePath(mCachePath);
        webSettings.setDatabasePath(mCachePath);
        webSettings.setAppCacheEnabled(true);
//        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//读取缺省
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//读取新页面

//        webSettings.setLoadWithOverviewMode(true);
//        webSettings.setSavePassword(true);
//        webSettings.setSaveFormData(true);
//        webSettings.setJavaScriptEnabled(true);     // enable navigator.geolocation
//        webSettings.setGeolocationEnabled(true);
//        webSettings.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");

    }

    //快速加载
    public void initLoad(WebView webView) {
        if (Build.VERSION.SDK_INT >= 19) {
            webView.getSettings().setLoadsImagesAutomatically(true);
        } else {
            webView.getSettings().setLoadsImagesAutomatically(false);
        }
    }
    /**
     * 同步一下cookie
     */
    public void synCookies(Context context, String url,String cookies) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();//移除
        cookieManager.setCookie(url, cookies);//cookies是在HttpClient中获得的cookie
        CookieSyncManager.getInstance().sync();
    }
}
