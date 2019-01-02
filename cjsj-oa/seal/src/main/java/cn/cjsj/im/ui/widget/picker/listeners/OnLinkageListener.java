package cn.cjsj.im.ui.widget.picker.listeners;

import cn.cjsj.im.ui.widget.picker.entity.City;
import cn.cjsj.im.ui.widget.picker.entity.County;
import cn.cjsj.im.ui.widget.picker.entity.Province;

/**
 * @author matt
 * blog: addapp.cn
 */

public interface OnLinkageListener {
    /**
     * 选择地址
     *
     * @param province the province
     * @param city    the city
     * @param county   the county ，if {@code hideCounty} is true，this is null
     */
    void onAddressPicked(Province province, City city, County county);
}
