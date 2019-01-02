package cn.cjsj.im.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.cjsj.im.R;
import rx.functions.Action1;

/**
 * Created by LuoYang on 2018/8/7 16:30
 * 勘察详情页面
 */
public class ProspectDetailActivity extends BaseActivity {
    @Bind(R.id.prospect_detail_status)
    TextView mStatusTV;

    @Bind(R.id.prospect_detail_title)
    TextView mTitleTv;

    @Bind(R.id.prospect_detail_principal)
    TextView mPrincipal;

    @Bind(R.id.prospect_detail_start_time_value)
    TextView mStartTime;

    @Bind(R.id.prospect_detail_end_time_value)
    TextView mEndTime;

    @Bind(R.id.prospect_detail_total_time_value)
    TextView mTotalTime;

    @Bind(R.id.prospect_detail_participant)
    TextView mParticipant;//参与人

    @Bind(R.id.prospect_detail_issubmitfile_value)
    TextView mIsSubmitFile;

    @Bind(R.id.prospect_detail_point_value)
    TextView mPointTV;

    @Bind(R.id.prospect_detail_submit_status_value)
    TextView mSubmitStatus;

    @Bind(R.id.prospect_detail_comments_value)
    TextView mComments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prospect_detail);
        ButterKnife.bind(this);
        setTitle("详情");

        RxView.clicks(mParticipant)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                    startActivity(new Intent(ProspectDetailActivity.this,ParticipantActivity.class));
                    }
                });

    }


    private void setStatus(String status) {

        switch (status) {
            case "1":
                mStatusTV.setText("进行中");
                mStatusTV.setTextColor(ContextCompat.getColor(mContext, R.color.color_2293ff));
                break;

            case "2":
                mStatusTV.setText("已延迟");
                mStatusTV.setTextColor(ContextCompat.getColor(mContext, R.color.color_fc472b));
                break;

            case "3":
                mStatusTV.setText("已完成");
                mStatusTV.setTextColor(ContextCompat.getColor(mContext, R.color.color_00cc05));
                break;


        }
    }

}
