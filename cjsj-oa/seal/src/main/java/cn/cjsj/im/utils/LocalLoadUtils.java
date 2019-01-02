package cn.cjsj.im.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author wangxy
 * @version 1.0.1
 * @date 2017/7/27
 */

public class LocalLoadUtils {

    /**
     * 保存Preference的name
     */
    public static final String PREFERENCE_LOAD = "loadTag";
    private static SharedPreferences mSharedPreferences;
    private static LocalLoadUtils mPreferenceManager;
    private static SharedPreferences.Editor editor;
    private String SHARED_LOCAL_LOAD = "shared_local_load";
    private String OFF_DUTY_TIME = "off_duty_time";
    private String COMPANY_ID = "company_id";

    private LocalLoadUtils(Context cxt) {
        mSharedPreferences = cxt.getSharedPreferences(PREFERENCE_LOAD, Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
    }

    public static synchronized void init(Context cxt) {
        if (mPreferenceManager == null) {
            mPreferenceManager = new LocalLoadUtils(cxt);
        }
    }

    /**
     * 单例模式，获取instance实例
     *
     * @param
     * @return
     */
    public synchronized static LocalLoadUtils getInstance() {
        if (mPreferenceManager == null) {
            throw new RuntimeException("please init first!");
        }
        return mPreferenceManager;
    }

    public void setLoadTag(long loadTag) {
        editor.putLong(SHARED_LOCAL_LOAD, loadTag);
        editor.commit();
    }

    public long getLoadTag() {
        return mSharedPreferences.getLong(SHARED_LOCAL_LOAD, -1);
    }

    public void removeLoadTag() {
        editor.remove(SHARED_LOCAL_LOAD);
        editor.commit();
    }

    /**
     * 一键离场显示时间
     * @param dutyTime
     */
    public void setDutyTime (long dutyTime){
        editor.putLong(OFF_DUTY_TIME,dutyTime);
        editor.commit();
    }

    /**
     * 一键离场显示时间
     * @return
     */
    public long getDutyTIme(){

        return mSharedPreferences.getLong(OFF_DUTY_TIME,15);
    }

    /**
     * 一键离场显示时间
     */
    public void removeDutyTime(){
        editor.remove(OFF_DUTY_TIME);
        editor.commit();
    }

    /**
     * 公司ID
     * @param companyId
     */
    public void setCompanyId(int companyId){
        editor.putInt(COMPANY_ID,companyId);
        editor.commit();
    }

    public int getCompanyId(){
        return mSharedPreferences.getInt(COMPANY_ID,0);
    }

    public void removeCompanyId(){
        editor.remove(COMPANY_ID);
        editor.commit();
    }


}
