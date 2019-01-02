package cn.cjsj.im.gty.bean;

/**
 * Created by LuoYang on 2018/3/14.
 * 用户信息
 */

public class OAUserBean {

    private CJSJUserParam cjsjUserParam;
    private SysUserBean sysUser;
    private int normalDaily;
    private int normalCheck;

    public CJSJUserParam getCjsjUserParam() {
        return cjsjUserParam;
    }

    public void setCjsjUserParam(CJSJUserParam cjsjUserParam) {
        this.cjsjUserParam = cjsjUserParam;
    }

    public SysUserBean getSysUser() {
        return sysUser;
    }

    public void setSysUser(SysUserBean sysUser) {
        this.sysUser = sysUser;
    }

    public int getNormalDaily() {
        return normalDaily;
    }

    public void setNormalDaily(int normalDaily) {
        this.normalDaily = normalDaily;
    }

    public int getNormalCheck() {
        return normalCheck;
    }

    public void setNormalCheck(int normalCheck) {
        this.normalCheck = normalCheck;
    }
}
