package cn.cjsj.im.ui.widget.gridviewpager;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.cjsj.im.gty.home.base.ContextUtil;
import cn.cjsj.im.gty.home.entity.MenuItem;
import cn.cjsj.im.gty.tools.IOKit;

/**
 * Created by LuoYang on 2018/7/5.
 */

public class HomeGridPagerHelper {
    public static final String GROUP_FAVORITE = "home_item_group";
    public static final String GROUP_OTHER = "home_item_other";
    /*分组数据的缓存列表，初始化分组的时候用*/
    private List<MenuItem> favoriteList;
    private List<MenuItem> otherList;

    /**
     * 用于保存本地数据的文件名字
     */
    private static final String PREFERENCE_MENU_DATA_NAME = "home_menu_data";
    /**
     * 是否已经进行过初始化的字段名
     */
    private static final String PREFERENCE_HAS_EVER_INIT = "has_init190103";

    private int itemCounter = 0;//用于统计共有多少个子item,依次给每个item设置独立的id

    /**
     * 解析原始数据，用于模拟从服务器上获取到的JSON报文
     */
    private void parseJSONData() {
        if (getCustomCont() == 0) {
            String jsonStr = IOKit.getStringFromAssets(ContextUtil.getContext(), "home.json");//获取到assets目录下的报文
            JSONObject dataJson = JSON.parseObject(jsonStr);//将报文string转换为JSON
            favoriteList = parseJSONList(dataJson, GROUP_FAVORITE);
            otherList = parseJSONList(dataJson, GROUP_OTHER);

            savePreferFavoriteList(favoriteList);
            savePreferOtherList(otherList);
        }
    }

    private List<MenuItem> parseJSONList(JSONObject dataJSON, String group) {
        List<MenuItem> list = new ArrayList<>();
        JSONArray array = dataJSON.getJSONArray(group);
        int size = array.size();
        for (int i = 0; i < size; i++, itemCounter++) {
            JSONObject object = array.getJSONObject(i);
            //之所以没有在array层就进行JSON到java对象的转换，是为了进入内部遍历，产生id,并将id赋值给menuItem
            MenuItem item = JSON.toJavaObject(object, MenuItem.class);
            item.setItemId(itemCounter);
            list.add(item);
        }
        return list;
    }

    /**
     * 获取常用数量
     *
     * @return
     */
    public static int getCustomCont() {
        try {
            List<MenuItem> list = getPreferMenuListData(GROUP_FAVORITE);
            return list.size();
        } catch (NullPointerException nException) {
            return 0;
        }

    }

    /**
     * 从SharedPreference里面取出JsonString,再转换为List
     *
     * @param group
     * @return
     */
    private static List<MenuItem> getPreferMenuListData(String group) {
        String jsonStr = getMenuDataConfig().getString(group, "");
        JSONArray array = JSONArray.parseArray(jsonStr);
//        if(array != null){
        try {
            return array.toJavaList(MenuItem.class);
        } catch (NullPointerException nullException) {
            return null;
        }

//        }else {
//
//        }
    }

    /**
     * 获取本地数据的文件
     *
     * @return
     */
    public static SharedPreferences getMenuDataConfig() {
        return ContextUtil.getContext().getSharedPreferences(PREFERENCE_MENU_DATA_NAME, Context.MODE_PRIVATE);
    }

    /*----------------------------原始方法-----------------------------------*/

    /*----------------------------衍生方法-----------------------------------*/
    public static void savePreferFavoriteList(List<MenuItem> list) {
        savePreferMenuListData(GROUP_FAVORITE, list);
    }

    public static void savePreferOtherList(List<MenuItem> list) {
        savePreferMenuListData(GROUP_OTHER, list);
    }

    /**
     * 将List转换为JsonString保存进SharedPreference
     *
     * @param group
     * @param list
     */
    private static void savePreferMenuListData(String group, List<MenuItem> list) {
        SharedPreferences.Editor editor = getMenuDataConfig().edit();
        editor.putString(group, JSON.toJSONString(list));
        editor.commit();
    }

    public static List<MenuItem> getPreferFavoriteList() {
        return getPreferMenuListData(GROUP_FAVORITE);
    }

    public static List<MenuItem> getPreferOtherList() {
        return getPreferMenuListData(GROUP_OTHER);
    }

    public static boolean hasEverInit() {
        return getMenuDataConfig().getBoolean(PREFERENCE_HAS_EVER_INIT, false);
    }

    public static void setInit(boolean isInit) {
        getMenuDataConfig().edit().putBoolean(PREFERENCE_HAS_EVER_INIT, isInit);
    }


    /**
     * 初始化数据
     */
    public static void init() {
        HomeGridPagerHelper helper = new HomeGridPagerHelper();
        helper.parseJSONData();
        setInit(true);
    }


    /**
     * 清空本地数据文件里面的内容
     */
    public static void clearMenuDataConfig() {
        getMenuDataConfig().edit().clear().commit();
    }

}
