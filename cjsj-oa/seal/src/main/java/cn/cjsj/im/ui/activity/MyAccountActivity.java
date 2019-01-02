package cn.cjsj.im.ui.activity;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qiniu.android.storage.UploadManager;


import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.cjsj.im.App;
import cn.cjsj.im.R;
import cn.cjsj.im.SealConst;
import cn.cjsj.im.gty.bean.OAUserBean;
import cn.cjsj.im.gty.http.HttpMethods;
import cn.cjsj.im.gty.subscribers.ProgressSubscriber;
import cn.cjsj.im.gty.subscribers.SubscriberOnNextErrorListener;
import cn.cjsj.im.server.broadcast.BroadcastManager;
import cn.cjsj.im.server.network.http.HttpException;
import cn.cjsj.im.server.response.QiNiuTokenResponse;
import cn.cjsj.im.server.response.SetPortraitResponse;
import cn.cjsj.im.server.utils.NToast;
import cn.cjsj.im.server.utils.photo.PhotoUtils;
import cn.cjsj.im.server.widget.BottomMenuDialog;
import cn.cjsj.im.server.widget.LoadDialog;
import de.hdodenhof.circleimageview.CircleImageView;


public class MyAccountActivity extends BaseActivity implements View.OnClickListener {

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private CircleImageView mImageView;
    private TextView mName;
    private PhotoUtils photoUtils;
    private BottomMenuDialog dialog;
    private UploadManager uploadManager;
    private String imageUrl;
    private Uri selectUri;
    private SubscriberOnNextErrorListener mGetToPicUpdata;
    private SubscriberOnNextErrorListener mGetUserInfoSubscriber;
    private String mToken;
    private static  final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 4001;
    @Bind(R.id.job_num_tv)
    TextView mJobNum;
    @Bind(R.id.department_tv)
    TextView mDepartment;
    @Bind(R.id.position_tv)
    TextView mPosition;//职称
    @Bind(R.id.hire_relation_tv)
    TextView mHireRelation;//雇佣关系
    @Bind(R.id.be_employed_years_tv)
    TextView mEmployedYears; //院龄

    private OAUserBean mOAUserBean;

    private static final int UPDATE_USER_INFO = 1001;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UPDATE_USER_INFO:
                    mName.setText(mOAUserBean.getSysUser().getFullname());

                    mJobNum.setText(mOAUserBean.getSysUser().getStaffCode());


                    mDepartment.setText(mOAUserBean.getSysUser().getOrgName());


                    mPosition.setText(mOAUserBean.getSysUser().getPosName());


                    mHireRelation.setText(mOAUserBean.getSysUser().getUserStatus());//雇佣关系


                    if(!"".equals(mOAUserBean.getCjsjUserParam().getSchoolAge()) || mOAUserBean.getCjsjUserParam().getSchoolAge() != null)
                    {
                        mEmployedYears.setText(mOAUserBean.getCjsjUserParam().getSchoolAge());
                    }else {
                        mEmployedYears.setText("");
                    }


                    break;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaccount);
        ButterKnife.bind(this);
        setTitle(R.string.de_actionbar_myacc);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        editor = sp.edit();
        mToken = App.getInstance().getToken();
        initView();


        mGetToPicUpdata = new SubscriberOnNextErrorListener<String>() {
            @Override
            public void onNext(String o) {
                imageUrl = o;
                LoadDialog.dismiss(mContext);
                editor.putString(SealConst.SEALTALK_LOGING_PORTRAIT, imageUrl);
                editor.commit();
                Glide.with(MyAccountActivity.this)
                        .load(imageUrl)
                        .into(mImageView);
//                ImageLoader.getInstance().displayImage(imageUrl, mImageView, App.getOptions());
//                if (RongIM.getInstance() != null) {
//                    RongIM.getInstance().setCurrentUserInfo(new UserInfo(sp.getString(SealConst.SEALTALK_LOGIN_ID, ""), sp.getString(SealConst.SEALTALK_LOGIN_NAME, ""), Uri.parse(imageUrl)));
//                }
                BroadcastManager.getInstance(mContext).sendBroadcast(SealConst.CHANGEINFO);
                NToast.shortToast(mContext, getString(R.string.portrait_update_success));
            }

            @Override
            public void onError(String error) {
                NToast.shortToast(mContext, getString(R.string.upload_portrait_failed));
                LoadDialog.dismiss(mContext);
            }
        };
        mGetUserInfoSubscriber = new SubscriberOnNextErrorListener<OAUserBean>() {
            @Override
            public void onNext(OAUserBean oaUserBean) {
                mOAUserBean = oaUserBean;
                mHandler.sendEmptyMessage(UPDATE_USER_INFO);
            }

            @Override
            public void onError(String error) {

            }
        };
        getUserInfo(mToken);
    }

    private void initView() {
        TextView mPhone = (TextView) findViewById(R.id.tv_my_phone);
        RelativeLayout portraitItem = (RelativeLayout) findViewById(R.id.rl_my_portrait);
        RelativeLayout nameItem = (RelativeLayout) findViewById(R.id.rl_my_username);
        mImageView = (CircleImageView) findViewById(R.id.img_my_portrait);
        mName = (TextView) findViewById(R.id.tv_my_username);
        portraitItem.setOnClickListener(this);
        nameItem.setOnClickListener(this);
        String cacheName = sp.getString(SealConst.SEALTALK_LOGIN_NAME, "");
        String cachePortrait = sp.getString(SealConst.SEALTALK_LOGING_PORTRAIT, "");
        String cachePhone = sp.getString(SealConst.SEALTALK_LOGING_PHONE, "");
        if (!TextUtils.isEmpty(cachePhone)) {
            mPhone.setText("+86 " + cachePhone);
        }
        if (!TextUtils.isEmpty(cacheName)) {
            mName.setText(cacheName);
            String cacheId = sp.getString(SealConst.SEALTALK_LOGIN_ID, "a");
//            String portraitUri = SealUserInfoManager.getInstance().getPortraitUri(new UserInfo(
//                    cacheId, cacheName, Uri.parse(cachePortrait)));
//            ImageLoader.getInstance().displayImage(portraitUri, mImageView, App.getOptions());
            Glide.with(this)
                    .load(Uri.parse(cachePortrait))
                    .into(mImageView);
        }
        setPortraitChangeListener();
        BroadcastManager.getInstance(mContext).addAction(SealConst.CHANGEINFO, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mName.setText(sp.getString(SealConst.SEALTALK_LOGIN_NAME, ""));
            }
        });
    }

    private void setPortraitChangeListener() {
        photoUtils = new PhotoUtils(new PhotoUtils.OnPhotoResultListener() {
            @Override
            public void onPhotoResult(Uri uri) {
                if (uri != null && !TextUtils.isEmpty(uri.getPath())) {
                    selectUri = uri;
                    LoadDialog.show(mContext);
                    uploadImage(selectUri);
                }
            }

            @Override
            public void onPhotoCancel() {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_my_portrait:

                if(Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        //申请WRITE_EXTERNAL_STORAGE权限
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                    }else {
                        showPhotoDialog();
                    }
                }else {
                    showPhotoDialog();
                }
                break;
            case R.id.rl_my_username:
//                startActivity(new Intent(this, UpdateNameActivity.class));
                break;
        }
    }


    static public final int REQUEST_CODE_ASK_PERMISSIONS = 101;

    /**
     * 弹出底部框
     */
    @TargetApi(23)
    private void showPhotoDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        dialog = new BottomMenuDialog(mContext);
        dialog.setConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (Build.VERSION.SDK_INT >= 23) {
                    int checkPermission = checkSelfPermission(Manifest.permission.CAMERA);
                    if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_PERMISSIONS);
                        } else {
                            new AlertDialog.Builder(mContext)
                                    .setMessage("您需要在设置里打开相机权限。")
                                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_PERMISSIONS);
                                        }
                                    })
                                    .setNegativeButton("取消", null)
                                    .create().show();
                        }
                        return;
                    }
                }
                photoUtils.takePicture(MyAccountActivity.this);
            }
        });
        dialog.setMiddleListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                photoUtils.selectPicture(MyAccountActivity.this);
            }
        });
        dialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PhotoUtils.INTENT_CROP:
            case PhotoUtils.INTENT_TAKE:
            case PhotoUtils.INTENT_SELECT:
                photoUtils.onActivityResult(MyAccountActivity.this, requestCode, resultCode, data);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode,grantResults);
    }

    //final String domain, String imageToken,
    public void uploadImage( Uri imagePath) {
//        if (TextUtils.isEmpty(domain) && TextUtils.isEmpty(imageToken) && TextUtils.isEmpty(imagePath.toString())) {
//            throw new RuntimeException("upload parameter is null!");
//        }
        File imageFile = new File(imagePath.getPath());
        getToPicUpdata(imageFile);

//        if (this.uploadManager == null) {
//            this.uploadManager = new UploadManager();
//        }
//        this.uploadManager.put(imageFile, null, imageToken, new UpCompletionHandler() {
//
//            @Override
//            public void complete(String s, ResponseInfo responseInfo, JSONObject jsonObject) {
//                if (responseInfo.isOK()) {
//                    try {
//                        String key = (String) jsonObject.get("key");
//                        imageUrl = "http://" + domain + "/" + key;
//                        Log.e("uploadImage", imageUrl);
//                        if (!TextUtils.isEmpty(imageUrl)) {
//                            request(UP_LOAD_PORTRAIT);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    NToast.shortToast(mContext, getString(R.string.upload_portrait_failed));
//                    LoadDialog.dismiss(mContext);
//                }
//            }
//        }, null);
    }

    public void getToPicUpdata(File file) {
        //代理模式生成对应server的实例化对象
        HttpMethods.getInstance().getToPicUpdata(new ProgressSubscriber<String>(mGetToPicUpdata, this, false), file,mToken);
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                showPhotoDialog();
            } else {
                // Permission Denied
            }
        }
    }

    /**
     * 获取用户信息
     * @param token
     */
    public void getUserInfo(String token){
        HttpMethods.getInstance().getUserInfo(new ProgressSubscriber<OAUserBean>(mGetUserInfoSubscriber,this,false),token);
    }

}
