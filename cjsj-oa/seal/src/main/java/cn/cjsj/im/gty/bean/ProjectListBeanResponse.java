package cn.cjsj.im.gty.bean;

/**
 * Created by LuoYang on 2018/10/26 16:12
 * 项目列表数据模型
 */
public class ProjectListBeanResponse {
    private long id;

    //项目名称
    private String xmmc;

    //项目进度
    private double xmjd;

    //项目状态
    private int xmzt;

    //是否延迟 1是 0否
    private int expired;

    //是否关注(1-关注，0-未关注)
    private int attention;

    //是否项目负责人(1-是，0-不是)
    private  int isProLeader;

    //所属板块
    private String ssbk;

    //项目状态名称
    private String xmztName;

    //项目时间
    private String xmsj;

    //项目状态颜色
    private String xmztColor;


    public String getXmztColor() {
        return xmztColor;
    }

    public void setXmztColor(String xmztColor) {
        this.xmztColor = xmztColor;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getXmmc() {
        return xmmc;
    }

    public void setXmmc(String xmmc) {
        this.xmmc = xmmc;
    }

    public double getXmjd() {
        return xmjd;
    }

    public void setXmjd(double xmjd) {
        this.xmjd = xmjd;
    }

    public int getXmzt() {
        return xmzt;
    }

    public void setXmzt(int xmzt) {
        this.xmzt = xmzt;
    }

    public int getExpired() {
        return expired;
    }

    public void setExpired(int expired) {
        this.expired = expired;
    }

    public int getAttention() {
        return attention;
    }

    public void setAttention(int attention) {
        this.attention = attention;
    }

    public int getIsProLeader() {
        return isProLeader;
    }

    public void setIsProLeader(int isProLeader) {
        this.isProLeader = isProLeader;
    }

    public String getSsbk() {
        return ssbk;
    }

    public void setSsbk(String ssbk) {
        this.ssbk = ssbk;
    }

    public String getXmztName() {
        return xmztName;
    }

    public void setXmztName(String xmztName) {
        this.xmztName = xmztName;
    }

    public String getXmsj() {
        return xmsj;
    }

    public void setXmsj(String xmsj) {
        this.xmsj = xmsj;
    }

    @Override
    public String toString() {
        return "ProjectListBeanResponse{" +
                "id=" + id +
                ", xmmc='" + xmmc + '\'' +
                ", xmjd=" + xmjd +
                ", xmzt=" + xmzt +
                ", expired=" + expired +
                ", attention=" + attention +
                ", isProLeader=" + isProLeader +
                ", ssbk='" + ssbk + '\'' +
                ", xmztName='" + xmztName + '\'' +
                '}';
    }
}
