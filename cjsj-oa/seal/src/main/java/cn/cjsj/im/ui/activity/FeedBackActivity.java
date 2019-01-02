package cn.cjsj.im.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.http.HttpMethods;
import cn.cjsj.im.gty.subscribers.ProgressSubscriber;
import cn.cjsj.im.gty.subscribers.SubscriberOnNextErrorListener;
import cn.cjsj.im.server.utils.NLog;
import cn.cjsj.im.server.widget.LoadDialog;
import cn.cjsj.im.utils.PhoneSystemInfo;
import rx.functions.Action1;

/**
 * Created by LuoYang on 2018/3/27.
 */

public class FeedBackActivity extends BaseActivity {

    @Bind(R.id.feedback_details)
    EditText mFeedBackDetailTv;
    @Bind(R.id.feedback_submit)
    Button mSubmitBtn;

    private String mToken;
    private String mAccount;
    private String mNikName;
    private Intent mIntent;

    private SubscriberOnNextErrorListener mSendFeedbackSubscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        setTitle("意见反馈");

        mIntent = getIntent();
        mToken = mIntent.getStringExtra("token");
        mAccount = mIntent.getStringExtra("account");
        mNikName = mIntent.getStringExtra("nikName");

        RxView.clicks(mSubmitBtn)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (!mFeedBackDetailTv.getText().toString().isEmpty()){
                            LoadDialog.show(mContext);
                            sendFeedback(mToken,mFeedBackDetailTv.getText().toString().trim() + "姓名:" + mNikName + "--账号:" + mAccount + "--手机型号及系统版本号：" + PhoneSystemInfo.getSystemModel() + "-/-"
                            + PhoneSystemInfo.getSystemVersion());

                            NLog.e("Ly__feedback:::" + mFeedBackDetailTv.getText().toString().trim() + "姓名:" + mNikName + "--账号:" + mAccount + "--手机型号及系统版本号：" + PhoneSystemInfo.getSystemModel() + "-/-"
                                    + PhoneSystemInfo.getSystemVersion());
                        }else {
                            Toast.makeText(FeedBackActivity.this,"请输入反馈内容再提交!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        mSendFeedbackSubscriber = new SubscriberOnNextErrorListener<String>() {
            @Override
            public void onNext(String o) {
                Toast.makeText(FeedBackActivity.this,"我们已收到您的问题,会尽快处理.",Toast.LENGTH_LONG).show();
                LoadDialog.dismiss(mContext);
                        finish();
            }

            @Override
            public void onError(String error) {
                LoadDialog.dismiss(mContext);
            }
        };
    }

    /**
     * 发送意见反馈
     * @param token
     * @param content
     */
    public void sendFeedback(String token, String content){
        HttpMethods.getInstance().sendFeedback(new ProgressSubscriber<String>(mSendFeedbackSubscriber,this,false),token,content);
    }

}
