package cn.cjsj.im.gty.subscribers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import cn.cjsj.im.gty.progress.ProgressCancelListener;
import cn.cjsj.im.gty.progress.ProgressDialogHandler;
import cn.cjsj.im.server.utils.NLog;
import cn.cjsj.im.ui.activity.LoginActivity;
import rx.Subscriber;

/**
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 * Created by LuoYang on 17/2/23.
 */
public class ProgressSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {

    private SubscriberOnNextErrorListener mSubscriberOnNextErrorListener;
    private ProgressDialogHandler mProgressDialogHandler;

    private Context context;


    public ProgressSubscriber(SubscriberOnNextErrorListener subscriberOnNextErrorListener, Context context, boolean isProgress) {
        this.mSubscriberOnNextErrorListener = subscriberOnNextErrorListener;
        this.context = context;
        if (isProgress) {
            mProgressDialogHandler = new ProgressDialogHandler(context, this, true);
        }
    }

    private void showProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {
        showProgressDialog();
    }

    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onCompleted() {
        dismissProgressDialog();
//        Toast.makeText(context, "Get", Toast.LENGTH_SHORT).show();
    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        if (e instanceof SocketTimeoutException) {
            Toast.makeText(context, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else if (e instanceof ConnectException) {
            Toast.makeText(context, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else if (!"Invalid index 0, size is 0".equals(e.getMessage())) {
            NLog.d("LY_p_error", e.getMessage());
            if(mSubscriberOnNextErrorListener != null) {
                mSubscriberOnNextErrorListener.onError(e.getMessage());
                if(!"您没有加入考勤组！ is not a constant in cn.cjsj.im.gty.http.ErrorCode".equals(e.getMessage())
                        && !"No enum constant cn.cjsj.im.gty.http.ErrorCode.您没有加入考勤组！".equals(e.getMessage())
                       &&  !e.getMessage().contains("网络繁忙")) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                if ("用户未登陆".equals(e.getMessage())){
                    context.startActivity(new Intent(context, LoginActivity.class));
                }
            }

        }
        dismissProgressDialog();

    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T t) {
        if(mSubscriberOnNextErrorListener != null) {
            mSubscriberOnNextErrorListener.onNext(t);
        }
    }

    /**
     * 取消ProgressDialog的时候，取消对observable的订阅，同时也取消了http请求
     */
    @Override
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }
}