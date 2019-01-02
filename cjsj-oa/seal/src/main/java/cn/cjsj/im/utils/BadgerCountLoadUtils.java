package cn.cjsj.im.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by LuoYang on 2018/4/12.
 */

public class BadgerCountLoadUtils {


    /**
     * 保存Preference的name
     */
    public static final String PREFERENCE_LOAD = "badgerTag";
    private static SharedPreferences mSharedPreferences;
    private static BadgerCountLoadUtils mPreferenceManager;
    private static SharedPreferences.Editor editor;
    private String SHARED_BADGER = "shared_badger";



    private BadgerCountLoadUtils(Context cxt) {
        mSharedPreferences = cxt.getSharedPreferences(PREFERENCE_LOAD, Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
    }

    public static synchronized void init(Context cxt) {
        if (mPreferenceManager == null) {
            mPreferenceManager = new BadgerCountLoadUtils(cxt);
        }
    }

    /**
     * 单例模式，获取instance实例
     *
     * @param
     * @return
     */
    public synchronized static BadgerCountLoadUtils getInstance() {
        if (mPreferenceManager == null) {
            throw new RuntimeException("please init first!");
        }
        return mPreferenceManager;
    }

    public void setLoadTag(int loadTag) {
        editor.putInt(SHARED_BADGER, loadTag);
        editor.commit();
    }

    public int getLoadTag() {
        return mSharedPreferences.getInt(SHARED_BADGER, -1);
    }

    public void removeLoadTag() {
        editor.remove(SHARED_BADGER);
        editor.commit();
    }
}
