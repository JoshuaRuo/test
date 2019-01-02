package cn.cjsj.im.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.cjsj.im.R;
import cn.cjsj.im.SealConst;
import cn.cjsj.im.gty.http.HttpMethods;
import cn.cjsj.im.gty.subscribers.ProgressSubscriber;
import cn.cjsj.im.gty.subscribers.SubscriberOnNextErrorListener;
import cn.cjsj.im.server.network.http.HttpException;
import cn.cjsj.im.server.response.ChangePasswordResponse;
import cn.cjsj.im.server.utils.NToast;
import cn.cjsj.im.server.widget.LoadDialog;

/**
 * Created by AMing on 16/6/23.
 * Company RongCloud
 */
public class UpdatePasswordActivity extends BaseActivity implements View.OnClickListener {

    private static final int UPDATE_PASSWORD = 15;

    private EditText oldPasswordEdit, newPasswordEdit, newPassword2Edit;
    private String mOldPassword, mNewPassword;
    private Button mConfirm;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String phoneString;

    private SubscriberOnNextErrorListener mUpdatePsdSubscriber;  //重置OA密码请求接口观察者


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pswd);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        editor = sp.edit();
        phoneString = sp.getString(SealConst.SEALTALK_LOGING_PHONE, "");
        setTitle(R.string.change_password);
        initViews();

        mUpdatePsdSubscriber = new SubscriberOnNextErrorListener() {
            @Override
            public void onNext(Object o) {
                LoadDialog.dismiss(mContext);
                Toast.makeText(UpdatePasswordActivity.this,"修改密码成功",Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(String error) {
                LoadDialog.dismiss(mContext);
                Toast.makeText(UpdatePasswordActivity.this,error,Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void initViews() {
        oldPasswordEdit = (EditText) findViewById(R.id.old_password);
        newPasswordEdit = (EditText) findViewById(R.id.new_password);
        newPassword2Edit = (EditText) findViewById(R.id.new_password2);
        mConfirm = (Button) findViewById(R.id.update_pswd_confirm);
        mConfirm.setOnClickListener(this);
        mConfirm.setEnabled(false);
        oldPasswordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setConformButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        newPasswordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setConformButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        newPassword2Edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setConformButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setConformButtonState() {
        if (TextUtils.isEmpty(oldPasswordEdit.getText().toString().trim())
                && TextUtils.isEmpty(newPasswordEdit.getText().toString().trim())
                && TextUtils.isEmpty(oldPasswordEdit.getText().toString().trim())) {
            mConfirm.setEnabled(false);
        } else {
            mConfirm.setEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        String old = oldPasswordEdit.getText().toString().trim();
        String new1 = newPasswordEdit.getText().toString().trim();
        String new2 = newPassword2Edit.getText().toString().trim();
        String cachePassword = sp.getString(SealConst.SEALTALK_LOGING_PASSWORD, "");
        if (TextUtils.isEmpty(old)) {
            NToast.shortToast(mContext, R.string.original_password);
            return;
        }
        if (TextUtils.isEmpty(new1)) {
            NToast.shortToast(mContext, R.string.new_password_not_null);
            return;
        }

        if (new1.length() < 6 || new1.length() > 16) {
            NToast.shortToast(mContext, R.string.passwords_invalid);
            return;
        }

        if (TextUtils.isEmpty(new2)) {
            NToast.shortToast(
                mContext, R.string.confirm_password_not_null);
            return;
        }
        if (!cachePassword.equals(old)) {
            NToast.shortToast(mContext, R.string.original_password_mistake);
            return;
        }
        if (!new1.equals(new2)) {
            NToast.shortToast(mContext, R.string.passwords_do_not_match);
            return;
        }

        if (new1.equals(old)) {
            NToast.shortToast(mContext, R.string.new_and_old_password);
            return;
        }

        mOldPassword = old;
        mNewPassword = new1;
        LoadDialog.show(mContext);
//        request(UPDATE_PASSWORD, true);
        registerOaPsd(phoneString, mNewPassword);

    }


    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        return action.changePassword(mOldPassword, mNewPassword);
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        ChangePasswordResponse cpRes = (ChangePasswordResponse) result;
        if (cpRes.getCode() == 200) {
            editor.putString(SealConst.SEALTALK_LOGING_PASSWORD, newPasswordEdit.getText().toString().trim());
            editor.apply();
            NToast.shortToast(mContext, getString(R.string.update_success));
            LoadDialog.dismiss(mContext);
            if(phoneString.length() == 11) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        registerOaPsd(phoneString, mNewPassword);
                    }
                });
            }
            finish();
        } else if (cpRes.getCode() == 1000) {
            NToast.shortToast(mContext, getString(R.string.original_password_mistake));
            LoadDialog.dismiss(mContext);
        } else {
            NToast.shortToast(mContext, "修改密码失败:" + cpRes.getCode());
            LoadDialog.dismiss(mContext);
        }
    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        LoadDialog.dismiss(mContext);
        NToast.shortToast(mContext, "修改密码请求失败");
    }

    //重置OA密码
    public void registerOaPsd(String phone,String password){
        HttpMethods.getInstance().updatePassword(new ProgressSubscriber<String>(mUpdatePsdSubscriber,this,false),phone,password);
    }
}
