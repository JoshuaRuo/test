package cn.cjsj.im.utils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

import cn.cjsj.im.gty.bean.LocationInfoBean;

/**
 * Created by LuoYang on 2017/8/1.
 */

public class BaiduLocationUtil implements BDLocationListener {
    private LocationInfoBean locationInfo = LocationInfoBean.getInstance();
    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
            StringBuffer sb = new StringBuffer(256);
        sb.append("time : ");
        locationInfo.setLocTime(bdLocation.getTime());
        sb.append(locationInfo.getLocTime());
        sb.append("\nerror code : ");
        locationInfo.setLocType(bdLocation.getLocType());
        sb.append(locationInfo.getLocType());
        sb.append("\nlatitude : ");
        locationInfo.setLocLatitude(bdLocation.getLatitude());
        sb.append(locationInfo.getLocLatitude());
        sb.append("\nlontitude : ");
        locationInfo.setLoclongtitude(bdLocation.getLongitude());
        sb.append(locationInfo.getLoclongtitude());
        sb.append("\nradius : ");
        sb.append(bdLocation.getRadius());
        if(bdLocation.getLocType() == BDLocation.TypeGpsLocation){
            sb.append("\nspeed : ");
            sb.append(bdLocation.getSpeed());
            sb.append("\nsatellite : ");
            sb.append(bdLocation.getSatelliteNumber());
            sb.append("\ndirection : ");
            sb.append("\naddr : ");
            locationInfo.setLocAddr(bdLocation.getAddrStr());
            sb.append(locationInfo.getLocAddr());
            sb.append(bdLocation.getDirection());
        }else if(bdLocation.getLocType() == BDLocation.TypeNetWorkLocation){
            sb.append("\naddr : ");
            locationInfo.setLocAddr(bdLocation.getAddrStr());
            sb.append(locationInfo.getLocAddr());
            // 运营商信息
            sb.append("\noperationers : ");
            locationInfo.setLocOperators(bdLocation.getOperators());
            sb.append(locationInfo.getLocOperators());
        }

    }
}
