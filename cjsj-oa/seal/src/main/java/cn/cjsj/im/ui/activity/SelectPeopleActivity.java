package cn.cjsj.im.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.jakewharton.rxbinding.view.RxView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.cjsj.im.App;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.SelectPeopleBean;
import cn.cjsj.im.gty.bean.SelectPeopleCcResponse;
import cn.cjsj.im.gty.bean.SysUserBean;
import cn.cjsj.im.gty.http.HttpMethods;
import cn.cjsj.im.gty.subscribers.ProgressSubscriber;
import cn.cjsj.im.gty.subscribers.SubscriberOnNextErrorListener;
import cn.cjsj.im.server.widget.ClearWriteEditText;
import cn.cjsj.im.server.widget.LoadDialog;
import cn.cjsj.im.ui.adapter.SelectPeopleAdapter;
import rx.functions.Action1;

/**
 * Created by LuoYang on 2018/11/27 14:59
 * 选择发送人
 */
public class SelectPeopleActivity extends BaseActivity {
    @Bind(R.id.select_people_rv)
    RecyclerView mRv;

    @Bind(R.id.select_people_count)
    TextView mCount;

    @Bind(R.id.select_people_ok)
    Button mOk;

    @Bind(R.id.staff_search_et)
    ClearWriteEditText et;

    @Bind(R.id.staff_search_et_layout)
    LinearLayout mSearchLayout;

    private TextWatcher mTextWatcher;

    private SubscriberOnNextErrorListener mSubscriber;
    private SubscriberOnNextErrorListener mCCSubscriber;

    private SelectPeopleAdapter mAdapter;
    private String mToken;
    private static final String ACTDEFID = "bksq:1:10000002508861";
    private static final int PEOPLE_LIST = 1021;
    private List<SysUserBean> mList; //所有人列表
    private List<SysUserBean> mDataList;//选中的人
    private static final int RESULT_INT = 901;
    private SelectPeopleBean mSelectPeopleBean;
    private Intent mIntent;
    private String type;
    private SelectPeopleCcResponse mSelectPeopleCcResponse;
    private boolean[] mFlag;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PEOPLE_LIST:
                    mAdapter.setData(mList);
                    mAdapter.notifyDataSetChanged();
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_people);
        ButterKnife.bind(this);
        setTitle("人员选择");
        mToken = App.getInstance().getToken();
        mIntent = getIntent();
        type = mIntent.getStringExtra("selectType");
        mFlag = mIntent.getBooleanArrayExtra("arrayFlag");
        initView();
        try {
            mDataList = (List<SysUserBean>) mIntent.getSerializableExtra("selectedList");
            for (int i = 0; i < mDataList.size(); i++) {
                if ("请选择".equals(mDataList.get(i).getFullname())){
                    mDataList.remove(i);
                }
            }
        } catch (NullPointerException exception) {
            mDataList = new ArrayList<>();
        }
        mSubscriber = new SubscriberOnNextErrorListener<SelectPeopleBean>() {
            @Override
            public void onNext(SelectPeopleBean selectPeopleBean) {
                mSelectPeopleBean = selectPeopleBean;
                mList = selectPeopleBean.getSysUserList();
                mHandler.sendEmptyMessage(PEOPLE_LIST);
                LoadDialog.dismiss(SelectPeopleActivity.this);

            }

            @Override
            public void onError(String error) {

            }
        };

        mCCSubscriber = new SubscriberOnNextErrorListener<SelectPeopleCcResponse>() {
            @Override
            public void onNext(SelectPeopleCcResponse selectPeopleCcResponse) {
                if (mList != null) {
                    mList.clear();
                }
                mSelectPeopleCcResponse = selectPeopleCcResponse;
                mList = mSelectPeopleCcResponse.getSysUserList();
                mHandler.sendEmptyMessage(PEOPLE_LIST);
                LoadDialog.dismiss(SelectPeopleActivity.this);
            }

            @Override
            public void onError(String error) {

            }
        };

        RxView.clicks(mOk)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (mDataList.size() != 0) {
                            if ("flow".equals(type)) {
                                Intent intent = new Intent();
                                intent.putExtra("lastDestTaskIdsRun", mSelectPeopleBean.getNodeId());
                                intent.putExtra("lastDestTaskUidsRun", mSelectPeopleBean.getUserType() + "^" + mDataList.get(0).getUserId() + "^" + mDataList.get(0).getAccount());
                                intent.putExtra("userList", (Serializable) mDataList);
                                intent.putExtra("parentList", (Serializable) mList);
                                intent.putExtra("arrayFlag", mFlag);
                                intent.putExtra("type", type);
                                setResult(RESULT_INT, intent);
                                finish();
                            } else {
                                Intent intent = new Intent();
                                intent.putExtra("type", type);
//                                intent.putExtra("copyPersonID", mIds.toString());//抄送人ID
//                                intent.putExtra("copyPerson", mNames.toString());//抄送人名称
                                intent.putExtra("userList", (Serializable) mDataList);
                                intent.putExtra("parentList", (Serializable) mList);
                                intent.putExtra("arrayFlag", mFlag);
                                setResult(RESULT_INT, intent);
                                finish();
                            }

                        } else {
                            Toast.makeText(SelectPeopleActivity.this, "请选择发送人", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        switch (type) {
            case "flow":
                LoadDialog.show(this);
                et.setVisibility(View.GONE);
                mSearchLayout.setVisibility(View.GONE);
                getFlowPeopleList(mToken, ACTDEFID);
                break;

            case "cc":
                LoadDialog.show(this);
                getCcPeopleList(mToken, "", 300, 1);
                break;
        }

        mTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getCcPeopleList(mToken, s.toString(), 200, 1);
            }
        };
        et.addTextChangedListener(mTextWatcher);
    }


    private void initView() {
        mRv.setLayoutManager(new LinearLayoutManager(this));
        if (mFlag != null) {
            mAdapter = new SelectPeopleAdapter(mRv, mFlag);
        } else {
            mAdapter = new SelectPeopleAdapter(mRv);

        }
        mAdapter.setSelectPeopleData(new SelectPeopleAdapter.SelectPeopleData() {
            @Override
            public void setPosition(int position, boolean[] flag) {
                mFlag = flag;
                mDataList.add(mList.get(position));
                mCount.setText("已选中：" + mDataList.size() + "人");

            }

            @Override
            public void removePosition(int position, boolean[] flag) {
                mFlag = flag;

                for (int i = 0;i < mDataList.size(); i ++){
                    if (mList.get(position).getUserId().equals(mDataList.get(i).getUserId())){
                        mDataList.remove(i);
                    }
                }
                mCount.setText("已选中：" + mDataList.size() + "人");



            }
        });
        mRv.setAdapter(mAdapter.getHeaderAndFooterAdapter());


    }

    private void getFlowPeopleList(String token, String actDe) {
        HttpMethods.getInstance().getFlowPeopleList(new ProgressSubscriber<SelectPeopleBean>(mSubscriber, this, false), token, actDe, "");
    }

    private void getCcPeopleList(String token, String name, int pageSize, int currentPage) {
        HttpMethods.getInstance().getCCPeopleList(new ProgressSubscriber<SelectPeopleCcResponse>(mCCSubscriber, this, false), token, name, pageSize, currentPage);
    }

    @Override
    protected void onDestroy() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }

        super.onDestroy();

    }
}
