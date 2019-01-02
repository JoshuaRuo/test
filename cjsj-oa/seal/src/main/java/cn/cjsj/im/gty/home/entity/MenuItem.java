package cn.cjsj.im.gty.home.entity;

import java.io.Serializable;



/**
 * 描述:列表子item实体类
 * <p>
 * 作者:LuoYang
 * 创建时间:2017年11月03日 11:51
 * 邮箱:chenjunsen@outlook.com
 *
 * @version 1.0
 */
public class MenuItem implements Serializable {

    /**
     * name : 匕首
     * icon : ic_cold_1
     * desc : 传说的匕首,杀人于无形
     * group : cold_weapon
     */

    private String name;
    private String icon;
    private String desc;
    private String group;
    private boolean notification;
    private String activity;

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public boolean isNotification() {
        if ("报销".equals(getName()) || "借款".equals(getName()) || "报销事项申请".equals(getName()) || "积分申请".equals(getName())){
            return false;
        }else {
            return notification;
        }
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    private int viewType;
    private int itemId;


    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }


    @Override
    public String toString() {
        return "MenuItem{" +
                "name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", desc='" + desc + '\'' +
                ", group='" + group + '\'' +
                ", notification=" + notification +
                ", activity='" + activity + '\'' +
                ", viewType=" + viewType +
                ", itemId=" + itemId +
                '}';
    }
}
