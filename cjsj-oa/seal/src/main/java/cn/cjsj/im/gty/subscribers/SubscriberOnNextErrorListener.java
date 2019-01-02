package cn.cjsj.im.gty.subscribers;

/**
 * Created by LuoYang on 2017/5/31.
 */

public interface SubscriberOnNextErrorListener <T>{

    void onNext(T t);
    void onError(String error);
}
