package cn.cjsj.im.ui.activity;

import android.app.DownloadManager;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;

import cn.cjsj.im.App;
import cn.cjsj.im.R;
import cn.cjsj.im.SealConst;
import cn.cjsj.im.gty.bean.VersionBean;
import cn.cjsj.im.gty.http.HttpMethods;
import cn.cjsj.im.gty.subscribers.ProgressSubscriber;
import cn.cjsj.im.gty.subscribers.SubscriberOnNextErrorListener;
import cn.cjsj.im.server.UpdateService;
import cn.cjsj.im.server.broadcast.BroadcastManager;
import cn.cjsj.im.server.utils.NToast;
import cn.cjsj.im.server.widget.DialogWithYesOrNoUtils;
import cn.cjsj.im.ui.widget.switchbutton.SwitchButton;
import cn.cjsj.im.utils.CleanCache;
import cn.cjsj.im.utils.DialogUtils;


/**
 * Created by AMing on 16/6/23.
 * Company RongCloud
 */
public class AccountSettingActivity extends BaseActivity implements View.OnClickListener {

    private boolean isDebug;
    private final static String TAG = "AccountSettingActivity";

    private String mToken;

    private SubscriberOnNextErrorListener mLogOutSubscriber;

    private SubscriberOnNextErrorListener mSubscriber;

    private DialogUtils dialogUtils;

    private VersionBean mVersionBean;
    private TextView mVersionCode, mClearCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_set);
        isDebug = getSharedPreferences("config", MODE_PRIVATE).getBoolean("isDebug", false);
        mToken = App.getInstance().getToken();
        setTitle(R.string.account_setting);
        initViews();

        mLogOutSubscriber = new SubscriberOnNextErrorListener<Object>() {
            @Override
            public void onNext(Object o) {
                Toast.makeText(AccountSettingActivity.this, "退出成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AccountSettingActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String error) {

            }
        };
    }

    private void initViews() {
        RelativeLayout mPassword = (RelativeLayout) findViewById(R.id.ac_set_change_pswd);
        RelativeLayout mClean = (RelativeLayout) findViewById(R.id.ac_set_clean);
        RelativeLayout mExit = (RelativeLayout) findViewById(R.id.ac_set_exit);
        RelativeLayout mUpdateV = (RelativeLayout) findViewById(R.id.update_layout);
        LinearLayout layout_push = (LinearLayout) findViewById(R.id.layout_push_setting);
        mVersionCode = (TextView) findViewById(R.id.version_name_code);
        mClearCache = (TextView) findViewById(R.id.setting_clear_cache);
        dialogUtils = new DialogUtils(this);

        if (isDebug) {
            layout_push.setVisibility(View.VISIBLE);
        } else {
            layout_push.setVisibility(View.GONE);
        }

        final SwitchButton mSwitchDetail = (SwitchButton) findViewById(R.id.switch_push_detail);


        mPassword.setOnClickListener(this);
        mClean.setOnClickListener(this);
        mExit.setOnClickListener(this);
        mUpdateV.setOnClickListener(this);

        try {
            mClearCache.setText(CleanCache.getTotalCacheSize(AccountSettingActivity.this));
        } catch (Exception e) {
            e.printStackTrace();
        }
        mSubscriber = new SubscriberOnNextErrorListener<VersionBean>() {
            @Override
            public void onNext(final VersionBean versionBean) {

                if (versionBean != null) {
                    mVersionBean = versionBean;
                    if (mVersionBean.getNumber() > packageCode(AccountSettingActivity.this)) {

                        mVersionCode.setText("v" + mVersionBean.getName());
                    }
                } else {

                }
            }

            @Override
            public void onError(String error) {

            }
        };
        getVersion();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ac_set_change_pswd:
                startActivity(new Intent(this, UpdatePasswordActivity.class));
                break;
            case R.id.ac_set_clean:
                DialogWithYesOrNoUtils.getInstance().showDialog(mContext, "是否清除缓存?", new DialogWithYesOrNoUtils.DialogCallBack() {
                    @Override
                    public void executeEvent() {
                        File file = new File(Environment.getExternalStorageDirectory().getPath() + getPackageName());
                        deleteFile(file);
                        NToast.shortToast(mContext, "清除成功");
                        try {
                            mClearCache.setText("0M");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void executeEditEvent(String editText) {

                    }

                    @Override
                    public void updatePassword(String oldPassword, String newPassword) {

                    }
                });
                break;
            case R.id.ac_set_exit:
                DialogWithYesOrNoUtils.getInstance().showDialog(mContext, "是否退出登录?", new DialogWithYesOrNoUtils.DialogCallBack() {
                    @Override
                    public void executeEvent() {
//                        BroadcastManager.getInstance(mContext).sendBroadcast(SealConst.EXIT);
                        logOut();
                    }

                    @Override
                    public void executeEditEvent(String editText) {

                    }

                    @Override
                    public void updatePassword(String oldPassword, String newPassword) {

                    }
                });
                break;
            case R.id.update_layout:
                if (mVersionBean != null) {
                    if (mVersionBean.getNumber() > packageCode(AccountSettingActivity.this)) {
                        dialogUtils.showUpgradeDialog("有新版本V" + mVersionBean.getName(), mVersionBean.getExplain(), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                /**********************正式*********/
                                dialogUtils.dismiss();
//                                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(mVersionBean.getUrl()));
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
//                                try {
//                                    long id = downManager.enqueue(request);
//                                    App.getInstance().setLoadTag(id);
//                                } catch (SecurityException security) {
//                                    Intent intent = new Intent();
//                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                    Uri uri = Uri.fromParts("package", AccountSettingActivity.this.getPackageName(), null);
//                                    intent.setData(uri);
//                                    startActivity(intent);
//                                    Toast.makeText(AccountSettingActivity.this, "请打开存储权限,才能更新下载！", Toast.LENGTH_LONG).show();
//                                    return;
//                                }
                                /*************************************************************/
                                UpdateService.Builder.create(mVersionBean.getUrl())
                                        .setStoreDir("update/flag")
                                        .setDownloadSuccessNotificationFlag(Notification.DEFAULT_ALL)
                                        .setDownloadErrorNotificationFlag(Notification.DEFAULT_ALL)
                                        .build(mContext);

                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogUtils.dismiss();


                            }
                        });
                    } else {
                        Toast.makeText(AccountSettingActivity.this, "已是最新版本", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }


    public void deleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                deleteFile(f);
            }
            file.delete();
        }
    }

    /**
     * 退出OA
     */
    public void logOut() {
        HttpMethods.getInstance().logOut(new ProgressSubscriber<Object>(mLogOutSubscriber, this, false), mToken);
    }

    public void getVersion() {
        HttpMethods.getInstance().getVersion(new ProgressSubscriber<VersionBean>(mSubscriber, this, false));
    }

    public int packageCode(Context context) {
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
}
