package cn.cjsj.im;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.StrictMode;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.dumpapp.DumperPlugin;
import com.facebook.stetho.inspector.database.DefaultDatabaseConnectionProvider;
import com.facebook.stetho.inspector.protocol.ChromeDevtoolsDomain;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import cn.cjsj.im.gty.LogUtils;
import cn.cjsj.im.gty.common.ConstantValue;
import cn.cjsj.im.gty.home.base.ContextUtil;
import cn.cjsj.im.stetho.RongDatabaseDriver;
import cn.cjsj.im.stetho.RongDatabaseFilesProvider;
import cn.cjsj.im.stetho.RongDbFilesDumperPlugin;
import cn.cjsj.im.ui.widget.gridviewpager.HomeGridPagerHelper;
import cn.cjsj.im.utils.BadgerCountLoadUtils;
import cn.cjsj.im.utils.BaiduLocationUtil;
import cn.cjsj.im.utils.LimitsLoadUtils;
import cn.cjsj.im.utils.LocalLoadUtils;
import cn.cjsj.im.utils.TokenCacheLoad;
import cn.jpush.android.api.JPushInterface;

/**
 * @author LuoYang 2017.12.15
 */
public class App extends MultiDexApplication {

    private static final String TAG = "ALI_PUSH";
    private long loadTag = -1l;
    private static App instance;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = null;
    private String phone = null;
    private String nickname = null;
    private String mToken;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    //hotfix init need attr
    public interface MsgDisplayListener {
        void handle(String msg);
    }

    public static MsgDisplayListener msgDisplayListener = null;
    public static StringBuilder cacheMsg = new StringBuilder();

    public static synchronized App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        instance = this;
        LocalLoadUtils.init(instance);
        BadgerCountLoadUtils.init(this);
        LimitsLoadUtils.init(this);
        TokenCacheLoad.init(this);

        //模块初始化
        ContextUtil.init(getApplicationContext());
//        if(!MenuHelper.hasEverInit()){
//            MenuHelper.clearMenuDataConfig();
//            MenuHelper.init();
//        }
        if (!HomeGridPagerHelper.hasEverInit()) {
            HomeGridPagerHelper.clearMenuDataConfig();
            HomeGridPagerHelper.init();
        }

        Stetho.initialize(new Stetho.Initializer(this) {
            @Override
            protected Iterable<DumperPlugin> getDumperPlugins() {
                return new Stetho.DefaultDumperPluginsBuilder(App.this)
                        .provide(new RongDbFilesDumperPlugin(App.this, new RongDatabaseFilesProvider(App.this)))
                        .finish();
            }

            @Override
            protected Iterable<ChromeDevtoolsDomain> getInspectorModules() {
                Stetho.DefaultInspectorModulesBuilder defaultInspectorModulesBuilder = new Stetho.DefaultInspectorModulesBuilder(App.this);
                defaultInspectorModulesBuilder.provideDatabaseDriver(new RongDatabaseDriver(App.this, new RongDatabaseFilesProvider(App.this), new DefaultDatabaseConnectionProvider()));
                return defaultInspectorModulesBuilder.finish();
            }
        });


        //JPUSH初始化
        JPushInterface.setDebugMode(true);// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);// 初始化 JPush

        ConstantValue.JPUSH_REGISTER_ID = JPushInterface.getRegistrationID(getApplicationContext());
        Set<String> tags = new HashSet<>();
        tags.add(ConstantValue.JPUSH_RYT_CUSTOMER_TAG);
        JPushInterface.setTags(getApplicationContext(), tags, null);
        LogUtils.debug("The JPUSH registerId is :" + ConstantValue.JPUSH_REGISTER_ID);


        //百度
        mLocationClient = new LocationClient(App.getInstance().getApplicationContext());
        myListener = new BaiduLocationUtil();
        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setOpenGps(true);
        option.setAddrType("all");
        option.setCoorType("bd09ll");
//        option.setCoorType("bd0911");
        option.setScanSpan(5000);
        option.disableCache(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();


//        try {
        sp = getSharedPreferences("config", MODE_PRIVATE);
        phone = sp.getString(SealConst.SEALTALK_LOGING_PHONE, "");
        nickname = sp.getString(SealConst.SEALTALK_LOGIN_NAME, "");
//            //报告Bugly
//            CrashReport.initCrashReport(getApplicationContext(), "1a1320adef", true);
//            CrashReport.setUserId(nickName);
//        } catch (NullPointerException nullException) {
//            NLog.d("Application Bugly exception::" + nullException);
//        }


        //Android 7.0 FileUriExposedException
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();


        //aliPush
//        initManService();
//        initPushFeedbackService();
//        initHttpDnsService();
//        initHotfix();
//        initPushService(this);
    }


    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    public void setLoadTag(long loadTag) {
        this.loadTag = loadTag;
        LocalLoadUtils.getInstance().setLoadTag(loadTag);
        LogUtils.debug("The setLoadTag loadTag is :" + loadTag);
    }

    public long getLoadTag() {
        if (this.loadTag > 0) {
        } else {
            this.loadTag = LocalLoadUtils.getInstance().getLoadTag();
        }
        LogUtils.debug("The getLoadTag loadTag is :" + loadTag);
        return this.loadTag;
    }

    public void clearLoadTag() {
        this.loadTag = -1l;
        LocalLoadUtils.getInstance().removeLoadTag();
    }


    public void startLocation() {
        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        }
    }

    public void stopLocation() {
        mLocationClient.stop();
    }

    //获取缓存token
    public String getToken() {
        if (mToken != null) {
            return mToken;
        } else {
            return TokenCacheLoad.getInstance().getToken();
        }
    }

    //缓存token
    public void setToken(String token) {
        this.mToken = token;
        TokenCacheLoad.getInstance().setToken(token);
    }


//    private void initManService() {
//        MANService manService = MANServiceProvider.getService();
//        //打开日志调试
//        manService.getMANAnalytics().turnOnDebug();
//        manService.getMANAnalytics().setAppVersion("3.0");
//
//        //MAN初始化方法之一,通过插件介入后直接在下发json中获取appKey和appSecret初始化
//        manService.getMANAnalytics().init(this, getApplicationContext());
//        // 通过此接口关闭页面自动打点功能，详见文档4.2
//        manService.getMANAnalytics().turnOffAutoPageTrack();
//    }

//    private void initPushFeedbackService() {
//        /**
//         * 添加自定义的error handler
//         */
//        FeedbackAPI.addErrorCallback(new FeedbackErrorCallback() {
//            @Override
//            public void onError(Context context, String s, ErrorCode errorCode) {
//                Toast.makeText(context, "ErrMsg is: " + s, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        FeedbackAPI.addLeaveCallback(new Callable() {
//            @Override
//            public Object call() throws Exception {
//                Log.d("DemoApplication", "custom leave callback");
//                return null;
//            }
//        });
//
//        //初始化
//        FeedbackAPI.init(this);
//
//        //自定义参数
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("phone", phone);
//            jsonObject.put("nickname", nickname);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        FeedbackAPI.setAppExtInfo(jsonObject);
//
//        /**
//         * 以下是设置UI
//         */
//        //设置默认联系方式
//        FeedbackAPI.setDefaultUserContactInfo(phone);
//        //沉浸式任务栏，控制台设置为true之后此方法才能生效
//        FeedbackAPI.setTranslucent(true);
//        //设置返回按钮图标
//        //FeedbackAPI.setBackIcon(R.drawable.ali_feedback_common_back_btn_bg);
//        //设置标题栏"历史反馈"的字号，需要将控制台中此字号设置为0
//        FeedbackAPI.setHistoryTextSize(20);
//        //设置标题栏高度，单位为像素
//        FeedbackAPI.setTitleBarHeight(100);
//
//
//    }
//
//    private void initHttpDnsService() {
//        HttpDnsService httpDns = HttpDns.getService(getApplicationContext());
//        // 允许过期IP以实现懒加载策略
////        httpDns.setExpiredIPEnabled(true);
//    }

//    private void initHotfix() {
//        String appVersion = null;
//        try {
//            appVersion = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
//        } catch (PackageManager.NameNotFoundException e) {
//            appVersion = "1.0.0";
//        }
//        SophixManager.getInstance().setContext(this)
//                .setAppVersion(appVersion)
//                .setAesKey(null)
//                .setEnableDebug(true)
//                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
//                    @Override
//                    public void onLoad(int i, int i1, String s, int i2) {
//                        String msg = new StringBuilder("").append("Mode:").append(i)
//                                .append(" Code:").append(i1)
//                                .append(" Info:").append(s)
//                                .append(" HandlePatchVersion:").append(i2).toString();
//                        if (msgDisplayListener != null) {
//                            msgDisplayListener.handle(msg);
//                        } else {
//                            cacheMsg.append("\n").append(msg);
//                        }
//                    }
//                }).initialize();
//    }

//    private void initPushService(Context applicationContext) {
//        PushServiceFactory.init(applicationContext);
//        final CloudPushService pushService = PushServiceFactory.getCloudPushService();
//        pushService.register(applicationContext, new CommonCallback() {
//            @Override
//            public void onSuccess(String s) {
//                ConstantValue.JPUSH_REGISTER_ID =  pushService.getDeviceId();
//                Log.i("ALI_DeviceId", pushService.getDeviceId());
//                Log.i(TAG, "init cloudchannel success");
//            }
//
//            @Override
//            public void onFailed(String s, String s1) {
//
//                Log.e(TAG, "init cloudchannel failed -- errorcode:" + s + " -- errorMessage:" + s1);
//            }
//        });
//    }
}
