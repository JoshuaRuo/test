package cn.cjsj.im.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.util.Random;

import cn.cjsj.im.App;
import cn.cjsj.im.R;
import cn.cjsj.im.SealConst;
import cn.cjsj.im.gty.bean.VersionBean;
import cn.cjsj.im.gty.common.ConstantValue;
import cn.cjsj.im.gty.http.HttpMethods;
import cn.cjsj.im.gty.subscribers.ProgressSubscriber;
import cn.cjsj.im.gty.subscribers.SubscriberOnNextErrorListener;
import cn.cjsj.im.server.UpdateService;
import cn.cjsj.im.server.utils.NLog;
import cn.cjsj.im.server.utils.NToast;
import cn.cjsj.im.server.widget.LoadDialog;
import cn.cjsj.im.utils.BadgerCountLoadUtils;
import cn.cjsj.im.utils.DialogUtils;
import cn.cjsj.im.utils.LimitsLoadUtils;
import cn.cjsj.im.utils.PermissionUtils;
import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by LuoYang on 16/8/5.
 */
public class SplashActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private Context context;
    private android.os.Handler handler = new android.os.Handler();
    private SubscriberOnNextErrorListener mSubscriber;
    private DialogUtils dialogUtils;
    private String mOaToken;
    private String mPhone, mPwd;
    private SubscriberOnNextErrorListener mLoginSubscriber;
    private SharedPreferences.Editor editor;
    private SharedPreferences sp;
    private long mId;
    private Dialog mDialog1;
    private ProgressBar mProgressBar;
    private TextView mPrecent;
    private DownloadManager mDownloadManager;
    private Typeface mTypeface;

    private TextView mHint1, mHint2, mHint3;

    private static final String[] HINT_TEXT = {"人们在一起可以做出单独一个人所不能做出的事业；智慧、双手、力量结合在一起，几乎是万能的。"
            , "真实是人生的命脉，是一切价值的根基。"
            , "人生的最大快乐，是自己的劳动得到了成果。"
            , "德行使心灵明晰，使人不仅更易了解德行，而且也更易了解科学的真理。"
            , "我们活着不能与草木同腐，不能醉生梦死，枉度人生，要有所做为。"
            , "书籍是思想的航船，在时代的波涛中破浪前进。它满载贵重的货物，运送给一代又一代。"
            , "伟大的工作，并不是用力量而是用耐心去完成的。"
            , "即使是世上最伟大、最壮丽的事业，兴许也常常需要瘦弱的手去扶掖。"
            , "没有引发任何行动的思想都不是思想，而是梦想。"
            , "在一个崇高的目标支持下，不停地工作，即使慢，也一定会获得成功。"
            , "现实是此岸，成功是彼岸，中间隔着湍急的河流，兴趣便是河上的桥，只要行动就可以通过。"
            , "知识是从劳动中得来的，任何成就都是刻苦劳动的结晶。"
            , "伟大的成绩和辛勤劳动是成正比例的，有一分劳动就有一分收获，日积月累，从少到多，奇迹就可以创造出来。"
            , "当劳动是种快乐时，生活是美的；当劳动是一种责任时，生活就是奴役。"
            , "只有美的交流，才能使社会团结，因为它关系到一切人都共同拥有的东西。"};
    private static final String [] HINT_TEXT_NAME = {"韦伯斯特","德莱塞","谢党哉","罗吉尔•培根","方志敏","培根","约翰逊","埃•斯宾塞"
    ,"马丁","爱因斯坦","克雷洛夫","宋庆龄","鲁迅","高尔基","席勒"};
    private String [] mHint;


    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case PermissionUtils.CODE_RECORD_AUDIO:
                    Toast.makeText(SplashActivity.this, "Result Permission Grant CODE_RECORD_AUDIO", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_GET_ACCOUNTS:
                    Toast.makeText(SplashActivity.this, "Result Permission Grant CODE_GET_ACCOUNTS", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_READ_PHONE_STATE:
                    Toast.makeText(SplashActivity.this, "Result Permission Grant CODE_READ_PHONE_STATE", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_CALL_PHONE:
                    Toast.makeText(SplashActivity.this, "Result Permission Grant CODE_CALL_PHONE", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_CAMERA:
                    Toast.makeText(SplashActivity.this, "Result Permission Grant CODE_CAMERA", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_ACCESS_FINE_LOCATION:
                    Toast.makeText(SplashActivity.this, "Result Permission Grant CODE_ACCESS_FINE_LOCATION", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_ACCESS_COARSE_LOCATION:
                    Toast.makeText(SplashActivity.this, "Result Permission Grant CODE_ACCESS_COARSE_LOCATION", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_READ_EXTERNAL_STORAGE:
                    Toast.makeText(SplashActivity.this, "Result Permission Grant CODE_READ_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE:
                    Toast.makeText(SplashActivity.this, "Result Permission Grant CODE_WRITE_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_MULTI_PERMISSION:
//                    Toast.makeText(SplashActivity.this, "Result All Permission Grant", Toast.LENGTH_SHORT).show();
                    getVersion();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        mHint = getString(HINT_TEXT,HINT_TEXT_NAME);
        mHint1 = findViewById(R.id.splash_text1);
        mHint2 = findViewById(R.id.splash_text2);
        mHint3 = findViewById(R.id.splash_logo_right_hint_tv);
        mHint1.setTypeface(mTypeface);
        mHint2.setTypeface(mTypeface);
        mHint3.setTypeface(mTypeface);
        context = this;
        dialogUtils = new DialogUtils(this);
        mTypeface = Typeface.createFromAsset(getAssets(), "fonts/STSongti-SC-Bold-02.ttf");
        sp = getSharedPreferences("config", MODE_PRIVATE);
        editor = sp.edit();
        mOaToken = App.getInstance().getToken();

        mHint1.setText(mHint[0]);
        mHint2.setText("———" + mHint[1]);
//        if (!TextUtils.isEmpty(mOaToken)) {
//            RongIM.connect(mOaToken, SealAppContext.getInstance().getConnectCallback());
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    goToMain();
//                }
//            }, 800);
//        } else {
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    goToLogin()
//                }
//            }, 800);
//        }

        mPhone = sp.getString(SealConst.SEALTALK_LOGING_PHONE, "");
        mPwd = sp.getString(SealConst.SEALTALK_LOGING_PASSWORD, "");

        mSubscriber = new SubscriberOnNextErrorListener<VersionBean>() {
            @Override
            public void onNext(final VersionBean versionBean) {

                if (versionBean != null) {
                    if (versionBean.getNumber() > packageCode(SplashActivity.this)) {
                        dialogUtils.showUpgradeDialog("有新版本V" + versionBean.getName(), versionBean.getExplain(), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogUtils.dismiss();
//                                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(versionBean.getUrl()));
//
//                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
//                                request.setTitle("下载");
//                                request.setDescription("成交设计升级包正在下载");
//                                request.setAllowedOverRoaming(false);
//                                //设置文件存放目录:Android/包名/download/...
//                                //request.setDestinationInExternalFilesDir(getActivity(), Environment.DIRECTORY_DOWNLOADS, "ryt-admin.apk");
//                                //设置文件存放目录: Download/xxx.apk
//
//                                //设置文件名
//                                String fileName = "cjsj_im" + ".apk";
//                                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
//
//                                DownloadManager downManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
//                                request.setMimeType("application/vnd.android.package-archive");
//                                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
//
//                                /********************test^^^^^^^^^^^^^^^^^^^^^^*/
//
                                UpdateService.Builder.create(versionBean.getUrl())
                                        .setStoreDir("update/flag")
                                        .setDownloadSuccessNotificationFlag(Notification.DEFAULT_ALL)
                                        .setDownloadErrorNotificationFlag(Notification.DEFAULT_ALL)
                                        .build(SplashActivity.this);

//                                /*********************************************/
                                goToLoginOrMain();
//                                try {
//                                    long id = downManager.enqueue(request);
//                                    App.getInstance().setLoadTag(id);
//
//                                } catch (SecurityException security) {
//                                    Intent intent = new Intent();
//                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                    Uri uri = Uri.fromParts("package", SplashActivity.this.getPackageName(), null);
//                                    intent.setData(uri);
//                                    startActivity(intent);
//                                    Toast.makeText(SplashActivity.this, "请打开存储权限,才能更新下载！", Toast.LENGTH_LONG).show();
//                                    return;
//                                }
//
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogUtils.dismiss();
                                goToLoginOrMain();
                            }
                        });


                        /*****新下载办呢*****/
//                        new AlertDialog.Builder(SplashActivity.this)
//                                .setTitle("版本更新")
//                                .setMessage("发现的App版本,请及时更新")
//                                .setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialogUtils.dismiss();
//                                        goToLoginOrMain();
////                                        goToLogin();
//                                    }
//                                })
//                                .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        //此处使用DownLoadManager开启下载任务
//                                        mDownloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
//
//                                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(versionBean.getUrl()));
//                                        // 下载过程和下载完成后通知栏有通知消息。
//                                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE | DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//                                        request.setTitle("下载");
//                                        request.setDescription("apk正在下载");
//                                        //设置保存目录  /storage/emulated/0/Android/包名/files/Download
//                                        request.setDestinationInExternalFilesDir(SplashActivity.this, Environment.DIRECTORY_DOWNLOADS, "cjsj-admin.apk");
////                                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);//隐藏notification
//                                        mId = mDownloadManager.enqueue(request);
//
//                                        //注册内容观察者，实时显示进度
//                                        MyContentObserver downloadChangeObserver = new MyContentObserver(null);
//                                        getContentResolver().registerContentObserver(Uri.parse("content://downloads/my_downloads"), true, downloadChangeObserver);
//
//                                        //广播监听下载完成
//                                        listener(mId);
//                                        //弹出进度条，先隐藏前一个dialog
//                                        dialog.dismiss();
//                                        //显示进度的对话框
//                                        mDialog1 = new Dialog(SplashActivity.this, R.style.Theme_AppCompat_Dialog_Alert);
//                                        mDialog1.setCanceledOnTouchOutside(false);
//                                        View view = SplashActivity.this.getLayoutInflater().inflate(R.layout.progress_dialog, null);
//                                        mProgressBar = view.findViewById(R.id.download_progressbar);
//                                        mPrecent = view.findViewById(R.id.download_percent);
//                                        mDialog1.setContentView(view);
//                                        mDialog1.show();
//
//                                    }
//                                }).create().show();
                    } else {
                        goToLoginOrMain();
//                        goToLogin();
                    }
                } else {
                    goToLoginOrMain();
//                    goToLogin();
                }
            }

            @Override
            public void onError(String error) {

            }
        };

        if (Build.VERSION.SDK_INT >= 23) {
            PermissionUtils.requestMultiPermissions(SplashActivity.this, mPermissionGrant);
        } else {
            getVersion();
        }

        mLoginSubscriber = new SubscriberOnNextErrorListener<JSONObject>() {
            @Override
            public void onNext(JSONObject jsonObject) {
                LimitsLoadUtils.getInstance().setLoadTag(jsonObject.getInteger("limits"));
                mOaToken = jsonObject.getString("token");
                goToMain();
            }

            @Override
            public void onError(String error) {
                LoadDialog.dismiss(SplashActivity.this);
                startActivity(new Intent(context, LoginActivity.class));
                finish();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        ShortcutBadger.removeCount(this);
        BadgerCountLoadUtils.getInstance().setLoadTag(0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        NLog.d("LY__permissions" + requestCode + "------" + permissions + " --------" + grantResults);
        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, mPermissionGrant);

    }

    private void goToMain() {
//        editor.putString("oaLoginToken", mOaToken);
//        editor.putString(SealConst.SEALTALK_LOGING_PHONE, mPhone);
//        editor.putString(SealConst.SEALTALK_LOGING_PASSWORD, mPwd);
//        editor.apply();
        LoadDialog.dismiss(this);
//        NToast.shortToast(this, R.string.login_success);
        startActivity(new Intent(context, MainActivity.class));
        finish();
    }

    /**
     * 自动登录
     */
    private void goToLogin() {
//        if (!TextUtils.isEmpty(mPhone) && !TextUtils.isEmpty(mPwd)) {
//            login(mPhone, mPwd, ConstantValue.JPUSH_REGISTER_ID);
//        } else {
        startActivity(new Intent(context, LoginActivity.class));
        finish();
//        }

    }

    /**
     * 通过本地token检验进入
     */
    private void goToLoginOrMain() {
        if (!TextUtils.isEmpty(App.getInstance().getToken())) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goToMain();
                }
            }, 800);
        } else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goToLogin();
                }
            }, 800);
        }
    }

    private boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    public void getVersion() {
        HttpMethods.getInstance().getVersion(new ProgressSubscriber<VersionBean>(mSubscriber, this, false));
    }

    public static int packageCode(Context context) {
        PackageManager manager = context.getPackageManager();
        int code = 0;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            code = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }

    public void login(String phone, String password, String jPushId) {
        HttpMethods.getInstance().login(new ProgressSubscriber<JSONObject>(mLoginSubscriber, this, false), phone, password, jPushId);
    }


    /***TEST DOWNLOAD***/


    private void listener(final long id) {
        //Toast.makeText(MainActivity.this,"XXXX",Toast.LENGTH_SHORT).show();
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long longExtra = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (id == longExtra) {
//                    Uri downloadUri = mDownloadManager.getUriForDownloadedFile(id);
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    File apkFile = getExternalFilesDir("DownLoad/cjsj-admin.apk");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        install.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        Uri uriForFile = FileProvider.getUriForFile(context, "cn.cjsj.im.FileProvider", apkFile);
                        install.setDataAndType(uriForFile, "application/vnd.android.package-archive");
                    } else {
                        install.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                    }

                    install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(install);
                    Toast.makeText(SplashActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
                }
            }

        };
        registerReceiver(broadcastReceiver, intentFilter);
    }


    class MyContentObserver extends ContentObserver {

        public MyContentObserver(Handler handler) {
            super(handler);
        }


        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public void onChange(boolean selfChange) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(mId);
            DownloadManager dManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            final Cursor cursor = dManager.query(query);
            if (cursor != null && cursor.moveToFirst()) {
                final int totalColumn = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
                final int currentColumn = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                int totalSize = cursor.getInt(totalColumn);
                int currentSize = cursor.getInt(currentColumn);
                float percent = (float) currentSize / (float) totalSize;
                float progress = (float) Math.floor(percent * 100);
                mPrecent.setText(progress + "%");
                mProgressBar.setProgress((int) progress, true);
                if (progress == 100)
                    mDialog1.dismiss();
            }
        }


    }

    private String[] getString(String []arg1,String [] arg2){
        int i =new Random().nextInt(15);
        String []arg3 = {arg1[i],arg2[i]};
        return arg3;
    }
}
