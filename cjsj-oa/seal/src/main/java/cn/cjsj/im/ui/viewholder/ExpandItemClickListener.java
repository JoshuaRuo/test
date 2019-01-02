package cn.cjsj.im.ui.viewholder;

import cn.cjsj.im.gty.bean.ProspectDataBean;

/**
 * Created by LuoYang on 2018/8/7 09:41
 * 父布局Item点击监听接口(项目策划)
 */
public interface ExpandItemClickListener {

    /**
     * 展开子Item
     * @param bean
     */
    void onExpandChildren(ProspectDataBean bean);

    /**
     * 隐藏子Item
     * @param bean
     */
    void onHideChildren(ProspectDataBean bean);
}
