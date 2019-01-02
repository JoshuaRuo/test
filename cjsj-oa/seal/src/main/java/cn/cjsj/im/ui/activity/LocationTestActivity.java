package cn.cjsj.im.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.LocationInfoBean;

/**
 * Created by LuoYang on 2018/6/15.
 * 定位测试
 */

public class LocationTestActivity extends BaseActivity {

    @Bind(R.id.lot_test)
    TextView mLotTv;
    @Bind(R.id.lat_test)
    TextView mLatTv;

    @Bind(R.id.address_test)
    TextView mAddressTv;
    @Bind(R.id.test_get_location)
    Button mBtn;

    private double mLot, mLat;
    private String mAddress;
    private final static int MESSAGE = 1001;
    private LocationInfoBean mlocationInfoBean;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE:
                    mlocationInfoBean = LocationInfoBean.getInstance();
                    mLot = mlocationInfoBean.getLoclongtitude();
                    mLat = mlocationInfoBean.getLocLatitude();
                    mAddress = mlocationInfoBean.getLocAddr();
                    mLotTv.setText("经度:" + mLot);
                    mLatTv.setText("纬度:" + mLat);
                    mAddressTv.setText("地址:" + mAddress);

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_test);
        ButterKnife.bind(this);
        setTitle("定位测试");

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.sendEmptyMessage(MESSAGE);
            }
        });
    }


}
