package cn.cjsj.im.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.cjsj.im.App;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.ProjectDetailMemberResponse;
import cn.cjsj.im.gty.bean.ProjectDetailResponse;
import cn.cjsj.im.gty.http.HttpMethods;
import cn.cjsj.im.gty.subscribers.ProgressSubscriber;
import cn.cjsj.im.gty.subscribers.SubscriberOnNextErrorListener;
import cn.cjsj.im.ui.adapter.ProjectDetailListAdapter;
import rx.functions.Action1;

/**
 * Created by LuoYang on 2018/8/3 10:51
 * 项目详情
 */
public class ProjectDetailActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.proj_detailbtn_left)
    Button mBack;

    @Bind(R.id.proj_detail_text_right)
    TextView mHeadRightText;

    @Bind(R.id.proj_detail_title)
    TextView mTitle;

    @Bind(R.id.project_detail_recylerview)
    RecyclerView mRV;

    @Bind(R.id.project_detail_tolist_bar)
    RelativeLayout mToListBar;

    @Bind(R.id.project_detail_chat_to)
    RelativeLayout mToChat;

    @Bind(R.id.project_detail_scheme)
    TextView mScheme;

    @Bind(R.id.project_detail_name)
    TextView mDetailName;

    @Bind(R.id.project_detail_time_value)
    TextView mTime;

    @Bind(R.id.project_detail_status)
    ImageView mStatusIcon;

    @Bind(R.id.project_detail_progress_value)
    TextView mProjectProgress;

    @Bind(R.id.project_detail_my_layout)
    RelativeLayout mProjectLayout;

    @Bind(R.id.project_detail_my_major_list)
    LinearLayout myMajorList;

    @Bind(R.id.project_detail_person_num)
    TextView mProjectPeopleCount;

    @Bind(R.id.project_detail_update_progress_btn)
    TextView mUpdateProgress;

    private Dialog mUpdateProgressDialog;


    private ProjectDetailListAdapter mAdapter;

    private List<ProjectDetailMemberResponse> mList;
    private String mToken;
    private long mId;
    private Intent mIntent;
    private ProjectDetailResponse mDetailModel;

    private static final int LIST_RESULT = 11;


    private SubscriberOnNextErrorListener mSubscriber;
    private SubscriberOnNextErrorListener mCloseSubscriber;
    private SubscriberOnNextErrorListener mUpdateProgressSubscriber;
    private static final int SET_DATA = 1001;
    private static final int UPDATE_PROGRESS = 1002;
    private LayoutInflater mInflater;
    private View myMajorItem;

    private Handler mHanlder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SET_DATA:
                    mDetailName.setText(mDetailModel.getProject().getXmmc());
                    mTime.setText(mDetailModel.getProject().getXmsj());
                    mProjectProgress.setText(mDetailModel.getProject().getXmjd() + "%");
                    if (mDetailModel.getProject().getXmzt() == 1) {
                        mStatusIcon.setImageResource(R.drawable.statusbar_to_do);
                    } else if (mDetailModel.getProject().getXmzt() == 2) {
                        mStatusIcon.setImageResource(R.drawable.statusbar_doing);
                    } else if (mDetailModel.getProject().getXmzt() == 3) {
                        mStatusIcon.setImageResource(R.drawable.statusbar_delay);
                    } else if (mDetailModel.getProject().getXmzt() == 10) {
                        mStatusIcon.setImageResource(R.drawable.statusbar_finish);
                    } else if (mDetailModel.getProject().getXmzt() == 11) {
                        mStatusIcon.setImageResource(R.drawable.statusbar_suspend);
                    } else if (mDetailModel.getProject().getXmzt() == 12) {
                        mStatusIcon.setImageResource(R.drawable.statusbar_abolish);
                    }
                    try {
                        if (mDetailModel.getMyTaskList().size() == 0) {
                            mProjectLayout.setVisibility(View.GONE);
                        } else {
                            mProjectLayout.setVisibility(View.VISIBLE);
                        }
                    } catch (NullPointerException nullException) {
                        mProjectLayout.setVisibility(View.GONE);
                    }
                    if (mDetailModel.getProject().getIsProLeader() == 1) {
                        mHeadRightText.setVisibility(View.VISIBLE);
                        mUpdateProgress.setVisibility(View.VISIBLE);
                    } else {
                        mHeadRightText.setVisibility(View.GONE);
                        mUpdateProgress.setVisibility(View.GONE);
                    }

                    try {
                        mProjectPeopleCount.setText(mDetailModel.getMemberList().size() + "人");
                    } catch (NullPointerException nullEx) {
                        mProjectPeopleCount.setText("0人");
                    }

                    GridLayoutManager gridLayoutManager = new GridLayoutManager(ProjectDetailActivity.this, 5);
                    gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
                    mAdapter = new ProjectDetailListAdapter(ProjectDetailActivity.this, mList);
                    mRV.setLayoutManager(gridLayoutManager);
                    mRV.setAdapter(mAdapter);

                    if (mDetailModel.getMyTaskList() != null) {
                        for (int i = 0; i < mDetailModel.getMyTaskList().size(); i++) {
                            myMajorItem = mInflater.inflate(R.layout.project_detail_my_major_model, null);
                            TextView majorValue = myMajorItem.findViewById(R.id.project_detail_major_value);
                            TextView time = myMajorItem.findViewById(R.id.project_detail_task_value);
//                            TextView progress = myMajorItem.findViewById(R.id.project_detail_task_progress_value);
                            TextView status = myMajorItem.findViewById(R.id.project_detail_task_status_value);

                            majorValue.setText(mDetailModel.getMyTaskList().get(i).getTaskName());
                            time.setText(mDetailModel.getMyTaskList().get(i).getStartDate() + "至" + mDetailModel.getMyTaskList().get(i).getEndDate());
                            if ("已延期".equals(mDetailModel.getMyTaskList().get(i).getStatus())) {
                                status.setTextColor(ContextCompat.getColor(ProjectDetailActivity.this, R.color.color_fc472b));
                            } else if ("已完成".equals(mDetailModel.getMyTaskList().get(i).getStatus())) {
                                status.setTextColor(ContextCompat.getColor(ProjectDetailActivity.this, R.color.color_00cc05));
                            } else if ("进行中".equals(mDetailModel.getMyTaskList().get(i).getStatus())) {
                                status.setTextColor(ContextCompat.getColor(ProjectDetailActivity.this, R.color.color_2293ff));
                            } else if ("未开始".equals(mDetailModel.getMyTaskList().get(i).getStatus())) {
                                status.setTextColor(ContextCompat.getColor(ProjectDetailActivity.this, R.color.color_999999));
                            } else {
                                status.setTextColor(ContextCompat.getColor(ProjectDetailActivity.this, R.color.color_999999));
                            }
                            status.setText(mDetailModel.getMyTaskList().get(i).getStatus());
                            myMajorList.addView(myMajorItem);
                        }
                    }

                    break;

                case UPDATE_PROGRESS:
                    double percent = msg.getData().getDouble("updatePercent");
                    Toast.makeText(ProjectDetailActivity.this, percent + "", Toast.LENGTH_SHORT).show();
//                    updateProgress(mToken, mId, percent);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_deatil);
        ButterKnife.bind(this);
        mTitle.setText("项目详情");
        mIntent = getIntent();
        mId = mIntent.getLongExtra("id", 0);
        mToken = App.getInstance().getToken();

        mHeadRightText.setText("关闭项目");
        mHeadRightText.setClickable(true);
        mHeadRightText.setOnClickListener(this);
        mToListBar.setOnClickListener(this);
        mInflater = LayoutInflater.from(this);
        RxView.clicks(mBack)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });


        RxView.clicks(mScheme)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(ProjectDetailActivity.this, ProjectScheme.class));
                    }
                });
        RxView.clicks(mToChat)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(new Intent(ProjectDetailActivity.this, ProjectChatActivity.class));
                    }
                });
        RxView.clicks(mHeadRightText)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        closeProject(mToken, mId);
                    }
                });

        RxView.clicks(mUpdateProgress)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showUpdateProgressDialog();
                    }
                });


        mSubscriber = new SubscriberOnNextErrorListener<ProjectDetailResponse>() {
            @Override
            public void onNext(ProjectDetailResponse model) {
                mDetailModel = model;
                mList = model.getMemberList();
                mHanlder.sendEmptyMessage(SET_DATA);
            }

            @Override
            public void onError(String error) {

            }
        };

        mCloseSubscriber = new SubscriberOnNextErrorListener() {
            @Override
            public void onNext(Object o) {
                setResult(LIST_RESULT);
                finish();
            }

            @Override
            public void onError(String error) {

            }
        };

        mUpdateProgressSubscriber = new SubscriberOnNextErrorListener() {
            @Override
            public void onNext(Object o) {
                getProjectDetail(mToken, mId);
            }

            @Override
            public void onError(String error) {

            }
        };

        getProjectDetail(mToken, mId);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.project_detail_tolist_bar:
                Intent intent = new Intent(ProjectDetailActivity.this, ProjectMemberActivity.class);
                intent.putExtra("id", mId);
                startActivity(intent);
                break;
        }
    }

    public void showUpdateProgressDialog() {
        mUpdateProgressDialog = new Dialog(this, R.style.Theme_AppCompat_Dialog_Alert);
        mUpdateProgressDialog.setCanceledOnTouchOutside(false);
        View view = getLayoutInflater().inflate(R.layout.project_detail_update_progress_dialog, null);
        TextView cancel = view.findViewById(R.id.update_progress_dialog_cancel);
        TextView ok = view.findViewById(R.id.update_progress_dialog_ok);
        final EditText et = view.findViewById(R.id.update_progress_dialog_cancel);

        RxView.clicks(cancel)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mUpdateProgressDialog.dismiss();
                    }
                });

        RxView.clicks(ok)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (et.getText().toString() != null) {
                            Bundle bundle = new Bundle();
                            bundle.putDouble("updatePercent", Double.parseDouble(et.getText().toString().trim()));
                        }
//                        mHanlder
                        mUpdateProgressDialog.dismiss();
                    }
                });

    }

    private void getProjectDetail(String token, long id) {
        HttpMethods.getInstance().getProjectDetail(new ProgressSubscriber<ProjectDetailResponse>(mSubscriber, this, false), token, id);
    }

    //关闭项目
    private void closeProject(String token, long id) {
        HttpMethods.getInstance().closeProject(new ProgressSubscriber<String>(mCloseSubscriber, this, false), token, id);
    }

    //修改进度
    private void updateProgress(String token, long id, double progressPercent) {
        HttpMethods.getInstance().updateProgress(new ProgressSubscriber<String>(mUpdateProgressSubscriber, this, false), token, id, progressPercent);
    }

}
