package cn.cjsj.im.ui.fragment;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import cn.cjsj.im.App;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.CheckResponse;
import cn.cjsj.im.gty.bean.LocationInfoBean;
import cn.cjsj.im.gty.http.HttpMethods;
import cn.cjsj.im.gty.subscribers.ProgressSubscriber;
import cn.cjsj.im.gty.subscribers.SubscriberOnNextErrorListener;
import cn.cjsj.im.server.widget.LoadDialog;
import cn.cjsj.im.ui.adapter.CheckWorkAdapter;
import cn.cjsj.im.utils.DateUtils;
import cn.cjsj.im.utils.DialogUtils;
import rx.functions.Action1;

/**
 * Created by LuoYang on 2018/11/8 15:09
 * 打卡fragment
 */
public class CheckOnWorkFragment extends Fragment {
    private View rootView;
    private RecyclerView mRv;
    private CheckWorkAdapter mCheckWorkAdapter;
    private String mToken;
    private TextView mTopHint;

    //定位
    private LocationInfoBean mlocationInfoBean;
    private double mLot;
    private double mLat;
    private String mAddress;
    private boolean flag;

    private LinearLayout mDefaultLayout;
    private DialogUtils dialogUtils;

    private CheckResponse mCheckResponse;


    //检查考勤组
    private SubscriberOnNextErrorListener mSubscriberIsJoinWorking;
    private int mIsJoinWorkGroup = 0;
    private final static int IS_JOIN_WORK_GROUP = 102;
    private final static int SET_DATA = 103;

    //获取今天考勤情况
    private SubscriberOnNextErrorListener mGetCheckStatusSubscriber;

    //提交考勤
    private SubscriberOnNextErrorListener mPostCheckDataSubscriber;

    private Dialog mDialog;
    private Dialog mOutsideDialog;

    private String mType; //考勤类型(PUNCHING_TIME_CARD 考勤打卡 FIELD_CLOCK 外勤打卡)
    private int mOperation; //操作(0上班 1下班)

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case IS_JOIN_WORK_GROUP:
                    if (mIsJoinWorkGroup == 1) {
                        mRv.setVisibility(View.VISIBLE);
                        mDefaultLayout.setVisibility(View.GONE);
                        getLocation();
                    } else {
                        mRv.setVisibility(View.GONE);
                        mDefaultLayout.setVisibility(View.VISIBLE);
                    }
                    break;

                case SET_DATA:
                    mCheckWorkAdapter = new CheckWorkAdapter(getActivity(), mCheckResponse);
                    mCheckWorkAdapter.setPostCheckData(new CheckWorkAdapter.PostCheckData() {
                        @Override
                        public void postData(String type, int operation) {
                            mType = type;
                            mOperation = operation;
                            try {
                                if ("FIELD_CLOCK".equals(type)) {
                                    showOutsideDialog();
                                } else {
                                    if (operation == 1 && mCheckResponse.getNowTime()
                                            < DateUtils.dateToStamp(mCheckResponse.getCheckLogResult().getDateStr() + " " + mCheckResponse.getCheckClass().getString("endTimeStr") + ":00")) {
                                        showDialog();
                                    } else {
                                        LoadDialog.show(getActivity());
                                        postCheckData(mToken, mAddress, mLot, mLat, type, operation, "");
                                    }
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
//

                        }
                    });

                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    mRv.setLayoutManager(layoutManager);
                    mRv.setAdapter(mCheckWorkAdapter);
                    mCheckWorkAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            if (mlocationInfoBean.getLoclongtitude() > 0 && mlocationInfoBean.getLocLatitude() != 4.9E-324) {
                getCheckStatus(mToken, mLot, mLat);
            } else {
                mHandler.postDelayed(runnable, 2000);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.fragment_check_on_work, container, false);
        }

        mToken = App.getInstance().getToken();
        dialogUtils = new DialogUtils(getActivity());

        mRv = rootView.findViewById(R.id.check_work_recycler);
        mDefaultLayout = rootView.findViewById(R.id.check_default_layout_bg);
        mTopHint = rootView.findViewById(R.id.clock_on_top_hint);


        mSubscriberIsJoinWorking = new SubscriberOnNextErrorListener<Integer>() {
            @Override
            public void onNext(Integer o) {
                mIsJoinWorkGroup = o;
                mHandler.sendEmptyMessage(IS_JOIN_WORK_GROUP);
            }

            @Override
            public void onError(String error) {

            }
        };
        getIsJoinWorkGroup(mToken);
        checkLocationPermission();


        mGetCheckStatusSubscriber = new SubscriberOnNextErrorListener<CheckResponse>() {
            @Override
            public void onNext(CheckResponse checkResponse) {
                if (mRv != null && mCheckResponse != null) {
                    mRv.removeAllViews();
                    mCheckResponse = null;
                }
                LoadDialog.dismiss(getActivity());
                mCheckResponse = checkResponse;
                mHandler.sendEmptyMessage(SET_DATA);

                if (mCheckResponse.getCheckLogResult().getOnStatus() != null && !"缺卡".equals(mCheckResponse.getCheckLogResult().getOnStatus())
                        && !"旷工".equals(mCheckResponse.getCheckLogResult().getOnStatus())){
                    mTopHint.setVisibility(View.VISIBLE);
                }else {
                    mTopHint.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(String error) {

            }
        };


        mPostCheckDataSubscriber = new SubscriberOnNextErrorListener<String>() {
            @Override
            public void onNext(String arg) {
                getCheckStatus(mToken, mLot, mLat);
                LoadDialog.dismiss(getActivity());
                Toast.makeText(getActivity(),"打卡成功",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                LoadDialog.dismiss(getActivity());
            }
        };

        return rootView;
    }

    private void getDataInitView(CheckResponse checkResponse) {


    }

    private void getLocation() {
        App.getInstance().startLocation();

        final int i = -1;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mlocationInfoBean = LocationInfoBean.getInstance();

                if (mlocationInfoBean.getLocLatitude() > 0 && mlocationInfoBean.getLocLatitude() != 4.9E-324) {

                    mLot = mlocationInfoBean.getLoclongtitude();
                    mLat = mlocationInfoBean.getLocLatitude();
                    mAddress = mlocationInfoBean.getLocAddr();
//                    try {
//                        mAddress = URLEncoder.encode(mlocationInfoBean.getLocAddr(), "UTF-8");
//
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    } catch (NullPointerException nEx) {
//                        nEx.printStackTrace();
//                    }
                    Toast.makeText(getActivity(), "获取定位中.", Toast.LENGTH_SHORT).show();
                    LoadDialog.show(getActivity());
                    mHandler.postDelayed(runnable, 1000);

                } else {
                    Toast.makeText(getActivity(), "定位失败,请关闭页面重试,或请检查定位权限是否打开.", Toast.LENGTH_SHORT).show();
                    LoadDialog.dismiss(getActivity());
                }
            }
        });
    }


    private boolean initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //检查权限
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //请求权限
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                flag = true;
            }
        } else {
            flag = true;
        }
        return flag;
    }

    private void checkLocationPermission() {
        if (!initPermission() || !isLocationEnabled()) {
            /**百度**/
            dialogUtils.showUpgradeDialog("权限", "定位或权限未打开，去设置页面打开定位？", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent();
                    i.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(i);
                    dialogUtils.dismiss();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogUtils.dismiss();
                }
            });

        }
    }

    public boolean isLocationEnabled() {
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(getContext().getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            if (requestCode == 1) {
                flag = grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED;
            }
            if (flag) {
                mHandler.postDelayed(runnable, 1000);
            }
        } catch (ArrayIndexOutOfBoundsException arrayIndoutEx) {
            // do nothing
        }

    }

    /**
     * 获取是否加入考勤组
     *
     * @param token
     */
    public void getIsJoinWorkGroup(String token) {
        HttpMethods.getInstance().getIsJoinWorkGroup(new ProgressSubscriber<Integer>(mSubscriberIsJoinWorking, getActivity(), false), token);
    }

    /**
     * 获取今日考勤情况
     *
     * @param token
     * @param lot
     * @param lat
     */
    private void getCheckStatus(String token, double lot, double lat) {
        HttpMethods.getInstance().getCheck(new ProgressSubscriber<CheckResponse>(mGetCheckStatusSubscriber, getActivity(), false), token, lot, lat);
    }

    /**
     * 提交考勤
     *
     * @param token
     * @param address
     * @param lot
     * @param lat
     * @param type
     * @param operation
     */
    private void postCheckData(String token, String address, double lot, double lat, String type, int operation, String notes) {
        HttpMethods.getInstance().postCheckData(new ProgressSubscriber<String>(mPostCheckDataSubscriber, getActivity(), false), token, address, lot, lat, type, operation, notes);
    }

    private void showDialog() {

        mDialog = new Dialog(getActivity(), R.style.Theme_AppCompat_Dialog_Alert);
        mDialog.setCanceledOnTouchOutside(false);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_sure_early_check, null);
        TextView cancel = view.findViewById(R.id.check_early_cancel);
        TextView ok = view.findViewById(R.id.check_early_ok);
        TextView time = view.findViewById(R.id.check_dialog_time_value);
        try {
            time.setText(DateUtils.stampToTime(mCheckResponse.getNowTime()).split(" ")[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RxView.clicks(cancel)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mDialog.dismiss();
                        try {
                            Log.v("LY__stamp", DateUtils.dateToStamp(mCheckResponse.getCheckLogResult().getDateStr() + " " + mCheckResponse.getCheckClass().getString("endTimeStr") + ":00") + "");
//                            Log.v("LY__stamp", DateUtils.stampToTime(Long.parseLong(mCheckResponse.getNowTime())).split(" ")[1]);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });

        RxView.clicks(ok)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        LoadDialog.show(getActivity());
                        postCheckData(mToken, mAddress, mLot, mLat, mType, mOperation, "");
                        mDialog.dismiss();
                    }
                });

        mDialog.setContentView(view);
        mDialog.show();
    }

    private void showOutsideDialog() {
        mOutsideDialog = new Dialog(getActivity(), R.style.Theme_AppCompat_Dialog_Alert);
        mOutsideDialog.setCanceledOnTouchOutside(false);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_sure_outside_check, null);
        TextView cancel = view.findViewById(R.id.check_outside_cancel);
        TextView ok = view.findViewById(R.id.check_outside_ok);
        TextView time = view.findViewById(R.id.check_dialog_outside_time_value);
        EditText et = view.findViewById(R.id.check_dialog_reason_value);
        try {
            time.setText(DateUtils.stampToTime(mCheckResponse.getNowTime()).split(" ")[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RxView.clicks(cancel)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mOutsideDialog.dismiss();
                    }
                });

        RxView.clicks(ok)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        LoadDialog.show(getActivity());
                        postCheckData(mToken, mAddress, mLot, mLat, mType, mOperation, "");
                        mOutsideDialog.dismiss();
                    }
                });

        mOutsideDialog.setContentView(view);
        mOutsideDialog.show();
    }
}
