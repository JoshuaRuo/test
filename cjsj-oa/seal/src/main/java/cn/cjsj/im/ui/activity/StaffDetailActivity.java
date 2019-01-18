package cn.cjsj.im.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.cjsj.im.App;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.SysUserBean;
import cn.cjsj.im.gty.http.HttpMethods;
import cn.cjsj.im.gty.subscribers.ProgressSubscriber;
import cn.cjsj.im.gty.subscribers.SubscriberOnNextErrorListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by LuoYang on 2018/11/6 15:56
 * 员工详情
 */
public class StaffDetailActivity extends BaseActivity {

    private String mToken;
    private SubscriberOnNextErrorListener mDetailSub;
    private long mUserId;
    private String mDeName;
    private Intent mIntent;

    @Bind(R.id.staff_detail_headimg)
    TextView mHeadName;

    @Bind(R.id.staff_name)
    TextView mNameTv;

    @Bind(R.id.staff_head_department_name)
    TextView mHeadDepartment;

    @Bind(R.id.staff_department_name)
    TextView mDepartmentName;

    @Bind(R.id.staff_job_num)
    TextView mJobNumTv;

    @Bind(R.id.staff_phone_num)
    TextView mPhoneTv;

    @Bind(R.id.staff_work_years)
    TextView mWorkYears;

    @Bind(R.id.staff_company_years)
    TextView mCompanyYears;

    @Bind(R.id.staff_politics_status)
    TextView mPoliticsStatus;

    @Bind(R.id.staff_bottom_layout)
    LinearLayout mCallLayout;

    @Bind(R.id.staff_detail_company_info)
    TextView mCompanyInfo;

    @Bind(R.id.staff_sex_iv)
    ImageView mSexIv;


    @Bind(R.id.staff_detail_img)
    CircleImageView mHeadImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_detail);
        ButterKnife.bind(this);

        mIntent = getIntent();
        mUserId = mIntent.getLongExtra("userId", 0);
        mDeName = mIntent.getStringExtra("deName");
        mToken = App.getInstance().getToken();
        mBaseLineView.setVisibility(View.GONE);


        mDetailSub = new SubscriberOnNextErrorListener<SysUserBean>() {
            @Override
            public void onNext(final SysUserBean sysUserBean) {

                if (sysUserBean.getHeadPortrait() != null) {
                    mHeadImg.setVisibility(View.VISIBLE);
                    Glide.with(StaffDetailActivity.this)
                            .load(Uri.parse(sysUserBean.getHeadPortrait()))
                            .into(mHeadImg);
                    mHeadName.setVisibility(View.GONE);
                } else {
                    mHeadName.setVisibility(View.VISIBLE);
                    mHeadImg.setVisibility(View.GONE);
                }

                if (sysUserBean.getFullname().length() > 2) {
                    String name = sysUserBean.getFullname().substring(1, 3);
                    mHeadName.setText(name);
                } else {
                    mHeadName.setText(sysUserBean.getFullname());
                }
                mNameTv.setText(sysUserBean.getFullname());
                mHeadDepartment.setText(mDeName);
                mDepartmentName.setText(mDeName);
                mJobNumTv.setText(sysUserBean.getStaffCode());
                mPhoneTv.setText(sysUserBean.getMobile());
                mWorkYears.setText(sysUserBean.getStanding());
                mCompanyYears.setText(sysUserBean.getSchoolAge());
                mPoliticsStatus.setText(sysUserBean.getPoliticalOutlook());
//                mNationTv.setText(sysUserBean.getNational());
//                majorTv.setText(sysUserBean.getMajor());
//                mUniversityTv.setText(sysUserBean.getSchool());

                if ("0".equals(sysUserBean.getSex())){
                    mSexIv.setImageResource(R.mipmap.staff_female_icon);
                }else {
                    mSexIv.setImageResource(R.mipmap.staff_male_icon);
                }
                mCallLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse("tel:" + sysUserBean.getMobile());

                        Intent it = new Intent(Intent.ACTION_DIAL, uri);

                        startActivity(it);
                    }
                });
            }

            @Override
            public void onError(String error) {

            }
        };
        getStaffDetail(mToken, mUserId);

    }

    private void getStaffDetail(String token, long userId) {
        HttpMethods.getInstance().getStaffDetail(new ProgressSubscriber<SysUserBean>(mDetailSub, this, false), token, userId);

    }

}
