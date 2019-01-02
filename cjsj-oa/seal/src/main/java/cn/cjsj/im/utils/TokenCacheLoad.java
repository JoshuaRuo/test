package cn.cjsj.im.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by LuoYang on 2018/12/7 14:05
 */
public class TokenCacheLoad {
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String TOKEN_CACHE_TAG = "config";
    private static final String TOKEN_CACHE = "oaLoginToken";
    private String mToken;
    private static TokenCacheLoad mTokenCache;

    private TokenCacheLoad(Context cxt) {
        mSharedPreferences = cxt.getSharedPreferences(TOKEN_CACHE_TAG, Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
    }

    public static synchronized void init(Context cxt) {
        if (mTokenCache == null) {
            mTokenCache = new TokenCacheLoad(cxt);
        }
    }

    /**
     * 单例模式，获取instance实例
     *
     * @param
     * @return
     */
    public synchronized static TokenCacheLoad getInstance() {
        if (mTokenCache == null) {
            throw new RuntimeException("please init first!");
        }
        return mTokenCache;
    }

    public void setToken(String token){
        editor.putString("TOKEN_CACHE",token);
        editor.apply();
    }

    public String getToken() {
        mToken = mSharedPreferences.getString("TOKEN_CACHE", "");
        return mToken;
    }
}
