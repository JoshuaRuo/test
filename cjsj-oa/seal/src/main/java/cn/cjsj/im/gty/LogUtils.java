package cn.cjsj.im.gty;

import android.util.Log;

/**
 * 日志工具类
 *
 * @author wangxy
 * @version 1.0.1
 * @date 2016/10/9
 */
public class LogUtils {

    private final static String tag="CJSJ_OA_LOG";

    /**
     * 不可实例化
     */
    private LogUtils() {

    }

    /**
     * 日志INFO
     * @param object
     */
    public static void info (Object object) {
        Log.i(tag, object.toString());
    }

    /**
     * 日志DEBUG
     * @param object
     */
    public static void debug (Object object) {
        Log.d(tag, object.toString());
    }

    /**
     * 日志ERROR
     * @param object
     */
    public static void error (Object object) {
        Log.e(tag, object.toString());
    }

    /**
     * 日志ERROR
     * @param object
     * @param throwable
     */
    public static void error (Object object, Throwable throwable) {
        Log.e(tag, object.toString(), throwable);
    }
}
