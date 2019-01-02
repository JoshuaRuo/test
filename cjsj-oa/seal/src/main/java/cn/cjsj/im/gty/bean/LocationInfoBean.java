package cn.cjsj.im.gty.bean;

/**
 * Created by LuoYang on 2017/8/1.
 */

public class LocationInfoBean {
    public String locTime; // 定位时间
    public int locType; // 定位类型 GPS or 网络
    public double locLatitude; // 维度
    public double loclongtitude;// 经度
    public String locAddr; // 当前地址
    public int locOperators;
    private static LocationInfoBean instance;

    public static LocationInfoBean getInstance(){
        if(instance == null){
            instance = new LocationInfoBean();
        }
        return instance;
    }

    public String getLocTime() {
        return locTime;
    }

    public void setLocTime(String locTime) {
        this.locTime = locTime;
    }

    public int getLocType() {
        return locType;
    }

    public void setLocType(int locType) {
        this.locType = locType;
    }

    public double getLocLatitude() {
        return locLatitude;
    }

    public void setLocLatitude(double locLatitude) {
        this.locLatitude = locLatitude;
    }

    public double getLoclongtitude() {
        return loclongtitude;
    }

    public void setLoclongtitude(double loclongtitude) {
        this.loclongtitude = loclongtitude;
    }

    public String getLocAddr() {
        return locAddr;
    }

    public void setLocAddr(String locAddr) {
        this.locAddr = locAddr;
    }

    public int getLocOperators() {
        return locOperators;
    }

    public void setLocOperators(int locOperators) {
        this.locOperators = locOperators;
    }
}

