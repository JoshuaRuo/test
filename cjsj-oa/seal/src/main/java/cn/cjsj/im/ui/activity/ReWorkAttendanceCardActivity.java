package cn.cjsj.im.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jakewharton.rxbinding.view.RxView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.cjsj.im.App;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.SysUserBean;
import cn.cjsj.im.gty.http.HttpMethods;
import cn.cjsj.im.gty.subscribers.ProgressSubscriber;
import cn.cjsj.im.gty.subscribers.SubscriberOnNextErrorListener;
import cn.cjsj.im.ui.adapter.ReWorkAttendanceAdapter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.functions.Action1;

/**
 * Created by LuoYang on 2018/11/27 10:29
 * 补卡
 */
public class ReWorkAttendanceCardActivity extends BaseActivity {

    @Bind(R.id.re_attendance_examine_rv)
    RecyclerView mExamineRV;

    @Bind(R.id.re_attendance_cc_rv)
    RecyclerView mCcRV;

    @Bind(R.id.re_attendance_button)
    Button mSubmit;

    @Bind(R.id.re_attendance_reeason_et)
    EditText mEt;

    @Bind(R.id.re_work_att_type_tv)
    TextView mTypeTv;

    @Bind(R.id.re_work_att_time_tv)
    TextView mTimeTv;

    private SubscriberOnNextErrorListener mSubscriber;


    private ReWorkAttendanceAdapter mAdapter;
    private static final int RESULT_INT = 901;
    //请求参数
    private int mType = 7;
    private String mCardDate;
    private String mReason;
    private String lastDestTaskIdsRun;
    private String lastDestTaskUidsRun;
    private StringBuilder copyPersonID;
    private StringBuilder copyPerson;
    private GridLayoutManager mFlowGridLayoutManager;
    private GridLayoutManager mCcGridLayoutManager;
    private static final int FLOW = 1005;
    private static final int CC = 1006;

    private List<SysUserBean> mDataFlowList;//选择人
    private List<SysUserBean> mDataCcList;//选择人
    private List<SysUserBean> mParentFlowList;//所有人
    private List<SysUserBean> mParentCcList;//所有人
    private boolean[] mFlag;
    private boolean[] mCc;
    private String mToken;
    private Intent mIntent;

    private String mTypeName;
    private String mTimeValue;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case FLOW:
                    mExamineRV.setLayoutManager(mFlowGridLayoutManager);
                    mAdapter = new ReWorkAttendanceAdapter(mExamineRV);
                    mAdapter.setAttendanceData(new ReWorkAttendanceAdapter.AttendanceData() {
                        @Override
                        public void setData(String name, int position) {
                            if ("请选择".equals(name)) {//跳转选择人页面
                                Intent intent = new Intent(ReWorkAttendanceCardActivity.this, SelectPeopleActivity.class);
                                intent.putExtra("arrayFlag", mFlag);
                                intent.putExtra("selectType", "flow");
                                if (mDataFlowList != null) {
                                    intent.putExtra("selectedList", (Serializable) mDataFlowList);
                                }
                                startActivityForResult(intent, RESULT_INT);
                            } else {
                                /****test****/

                                for (int i = 0; i < mParentFlowList.size(); i++) {
                                    if (mDataFlowList.get(position).getUserId().equals(mParentFlowList.get(i).getUserId())) {
                                        mFlag[i] = false;
                                    }
                                }
                                /**********/

                                mDataFlowList.remove(position);
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                    mExamineRV.setAdapter(mAdapter);
                    mAdapter.setData(mDataFlowList);
                    mAdapter.notifyDataSetChanged();
                    break;

                case CC:
                    mCcRV.setLayoutManager(mCcGridLayoutManager);
                    mAdapter = new ReWorkAttendanceAdapter(mCcRV);
                    mAdapter.setAttendanceData(new ReWorkAttendanceAdapter.AttendanceData() {
                        @Override
                        public void setData(String name, int position) {
                            if ("请选择".equals(name)) {
                                Intent intent = new Intent(ReWorkAttendanceCardActivity.this, SelectPeopleActivity.class);
                                intent.putExtra("arrayFlag", mCc);
                                intent.putExtra("selectType", "cc");
                                if (mDataCcList != null){
                                    intent.putExtra("selectedList", (Serializable) mDataCcList);
                                }
                                startActivityForResult(intent, RESULT_INT);
                            } else {
                                /****test****/

                                for (int i = 0; i < mParentCcList.size(); i++) {
                                    if (mDataCcList.get(position).getUserId().equals(mParentCcList.get(i).getUserId())) {
                                        mCc[i] = false;
                                    }
                                }
                                /**********/


                                mDataCcList.remove(position);
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                    mCcRV.setAdapter(mAdapter);
                    mAdapter.setData(mDataCcList);
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_work_attendance);
        ButterKnife.bind(this);
        setTitle("考勤补卡");
        mDataFlowList = new ArrayList<>();
        mDataCcList = new ArrayList<>();
        copyPersonID = new StringBuilder();
        copyPerson = new StringBuilder();
        mToken = App.getInstance().getToken();
        mIntent = getIntent();
        mType = mIntent.getIntExtra("type", 7);
        mCardDate = mIntent.getStringExtra("cardDate");
        mTypeName = mIntent.getStringExtra("reTypeName");
        mTimeValue = mIntent.getStringExtra("time");

        if (mTimeValue != null) {
            mCardDate = mTimeValue.substring(0, 10);
        }
        mTypeTv.setText(mTypeName);
        mTimeTv.setText(mTimeValue);

        SysUserBean sysUserBean = new SysUserBean();
        sysUserBean.setFullname("请选择");
        sysUserBean.setHeadPortrait("select");
        mDataFlowList.add(sysUserBean);
        mDataCcList.add(sysUserBean);
        mHandler.sendEmptyMessage(FLOW);
        mHandler.sendEmptyMessage(CC);
        mFlowGridLayoutManager = new GridLayoutManager(this, 5);
        mCcGridLayoutManager = new GridLayoutManager(this, 5);

        RxView.clicks(mSubmit)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        String pId;
                        String pName;
                        try {
                            if (mReason != null && mType != 7 && lastDestTaskIdsRun != null && lastDestTaskUidsRun != null) {
                                if (copyPersonID == null) {
                                    pId = "";
                                } else {
                                    pId = copyPersonID.toString();
                                }
                                if (copyPerson == null) {
                                    pName = "";
                                } else {
                                    pName = copyPerson.toString();
                                }

                                postReAtt(mToken, mType, mCardDate, mReason, lastDestTaskIdsRun, lastDestTaskUidsRun, pId, pName);
                            } else {
                                Toast.makeText(ReWorkAttendanceCardActivity.this, "请完善信息再提交", Toast.LENGTH_SHORT).show();
                            }
                        } catch (NullPointerException exception) {

                        }
                    }
                });

        mSubscriber = new SubscriberOnNextErrorListener<String>() {
            @Override
            public void onNext(String arg) {
                Toast.makeText(ReWorkAttendanceCardActivity.this, "补卡成功", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(ReWorkAttendanceCardActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        };

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().trim().isEmpty() && s.toString().trim() != null) {
                    mReason = s.toString().trim();
                }
            }
        };
        mEt.addTextChangedListener(textWatcher);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_INT && data != null) {
            String type = data.getStringExtra("type");
            if ("flow".equals(type)) {
                lastDestTaskIdsRun = data.getStringExtra("lastDestTaskIdsRun");
                lastDestTaskUidsRun = data.getStringExtra("lastDestTaskUidsRun");
                mFlag = data.getBooleanArrayExtra("arrayFlag");
                SysUserBean sysUserBean = new SysUserBean();
                sysUserBean.setFullname("请选择");
                sysUserBean.setHeadPortrait("select");
                mDataFlowList.remove(mDataFlowList.size() - 1);
                mDataFlowList = (List<SysUserBean>) data.getSerializableExtra("userList");
                mParentFlowList = (List<SysUserBean>) data.getSerializableExtra("parentList");

                mDataFlowList.add(sysUserBean);
//                Log.v("LY_CC_test", JSON.toJSONString(mDataFlowList));
                mHandler.sendEmptyMessage(FLOW);
            } else if ("cc".equals(type)) {
//                copyPersonID = data.getStringExtra("copyPersonID");
//                copyPerson = data.getStringExtra("copyPerson");
                mCc = data.getBooleanArrayExtra("arrayFlag");

                SysUserBean sysUserBean = new SysUserBean();
                sysUserBean.setFullname("请选择");
                sysUserBean.setHeadPortrait("select");
                mDataCcList.remove(mDataCcList.size() - 1);
                mFlag = data.getBooleanArrayExtra("arrayFlag");
                mDataCcList = (List<SysUserBean>) data.getSerializableExtra("userList");
//                mDataCcList.addAll((Collection<? extends SysUserBean>) data.getSerializableExtra("userList"));
                mParentCcList = (List<SysUserBean>) data.getSerializableExtra("parentList");
                mDataCcList.add(sysUserBean);
//                Log.v("LY_CC_test", JSON.toJSONString(mDataCcList));
                mHandler.sendEmptyMessage(CC);


                if (mDataCcList.size() != 0) {
                    copyPersonID.delete(0,copyPersonID.length());
                    copyPerson.delete(0,copyPerson.length());
                    for (int i = 0; i < mDataCcList.size(); i++) {
                        if (i < mDataCcList.size() - 1 && !"请选择".equals(mDataCcList.get(i).getFullname())) {
                            copyPersonID.append(mDataCcList.get(i).getUserId());
                            if (mDataCcList.size() - 1 - i > 1) {
                                copyPersonID.append(",");
                            }
                        }
                        if (i < mDataCcList.size() - 1 && !"请选择".equals(mDataCcList.get(i).getFullname())) {
                            copyPerson.append(mDataCcList.get(i).getFullname());
                            if (mDataCcList.size() - 1 - i > 1) {
                                copyPerson.append(",");
                            }
                        }
                    }
                    Log.v("LY_CC_test", copyPersonID.toString());
                    Log.v("LY_CC_test", copyPerson.toString());
                }
            }

        }
    }


    /**
     * 提交补卡申请
     *
     * @param token
     * @param type                0-补上班 1-补下班 2-补旷工 3-补迟到 4-补早退 5-外勤上班卡 6-外勤下班卡
     * @param cardDate            补卡日期
     * @param reason
     * @param lastDestTaskIdsRun  下个节点id
     * @param lastDestTaskUidsRun 下个流程执行人ID
     * @param copyPersonID        抄送人id
     * @param copyPerson          抄送人名称
     */
    private void postReAtt(String token, int type, String cardDate, String reason, String lastDestTaskIdsRun, String lastDestTaskUidsRun, String copyPersonID, String copyPerson) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", type);
        jsonObject.put("cardDate", cardDate);
        jsonObject.put("reason", reason);
        jsonObject.put("lastDestTaskIdsRun", lastDestTaskIdsRun);
        jsonObject.put("lastDestTaskUidsRun", lastDestTaskUidsRun);
        jsonObject.put("copyPersonID", copyPersonID);
        jsonObject.put("copyPerson", copyPerson);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toJSONString());
//        HttpMethods.getInstance().postReAttendance(new ProgressSubscriber<JSONObject>(mSubscriber, this, false), token, type, cardDate, reason, lastDestTaskIdsRun, lastDestTaskUidsRun, copyPersonID, copyPerson);
        HttpMethods.getInstance().postReAttendance(new ProgressSubscriber<String>(mSubscriber, this, false), token, body);
    }
}
