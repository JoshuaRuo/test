package cn.cjsj.im.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by LuoYang on 2018/4/13.
 * 权限缓存
 */

public class LimitsLoadUtils {

    /**
     * 保存Preference的name
     */
    public static final String PREFERENCE_LOAD = "limits";
    private static SharedPreferences mSharedPreferences;
    private static LimitsLoadUtils mPreferenceManager;
    private static SharedPreferences.Editor editor;
    private String SHARED_LIMITS = "shared_limits";



    private LimitsLoadUtils(Context cxt) {
        mSharedPreferences = cxt.getSharedPreferences(PREFERENCE_LOAD, Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
    }

    public static synchronized void init(Context cxt) {
        if (mPreferenceManager == null) {
            mPreferenceManager = new LimitsLoadUtils(cxt);
        }
    }

    /**
     * 单例模式，获取instance实例
     *
     * @param
     * @return
     */
    public synchronized static LimitsLoadUtils getInstance() {
        if (mPreferenceManager == null) {
            throw new RuntimeException("please init first!");
        }
        return mPreferenceManager;
    }

    public void setLoadTag(int loadTag) {
        editor.putInt(SHARED_LIMITS, loadTag);
        editor.commit();
    }

    public int getLoadTag() {
        return mSharedPreferences.getInt(SHARED_LIMITS, 0);
    }

    public void removeLoadTag() {
        editor.remove(SHARED_LIMITS);
        editor.commit();
    }
}
