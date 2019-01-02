package cn.cjsj.im.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.cjsj.im.App;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.DepartmentDispatchBean;
import cn.cjsj.im.gty.bean.DepartmentDispatchDetailBean;
import cn.cjsj.im.gty.bean.GroupDispatchBean;
import cn.cjsj.im.gty.bean.GroupDispatchDetailBean;
import cn.cjsj.im.gty.bean.NoticeDispatchDetail;
import cn.cjsj.im.gty.bean.NoticeDispatchSupport;
import cn.cjsj.im.gty.http.HttpMethods;
import cn.cjsj.im.gty.subscribers.ProgressSubscriber;
import cn.cjsj.im.gty.subscribers.SubscriberOnNextErrorListener;

/**
 * Created by LuoYang on 2017/9/29.
 * 公告详情
 */

public class NoticeDetailActivity extends BaseActivity {


    @Bind(R.id.notice_detail_title_tv)
    TextView mTitleTv;

    @Bind(R.id.notice_detail_time_tv)
    TextView mTimeTv;

    @Bind(R.id.notice_detail_text_tv)
    TextView mDetailTv;
    @Bind(R.id.notice_detail_creator_tv)
    TextView mCreatorName;

    private long id;

    private Intent mIntent;

    private SubscriberOnNextErrorListener mNoticesSubscriber;
    private SubscriberOnNextErrorListener mDepartmentSubscriber;
    private SubscriberOnNextErrorListener mGroupSubscriber;

    private String mToken;

    private NoticeDispatchDetail mNoticeDispatchDetail;
    private GroupDispatchBean groupDispatchBean;
    private DepartmentDispatchBean departmentDispatchBean;

    private String mDispatchType;

    private int FOR_DETAIL_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_detail_activity);
        ButterKnife.bind(this);

        setTitle(R.string.notice_detail_title);
        mToken = App.getInstance().getToken();

        mIntent = getIntent();
        id = mIntent.getLongExtra("id", 0);
        mDispatchType = mIntent.getStringExtra("dispatchType");

//        mTitleTv.setText(mTitle);
//        mCreatorName.setText(mCreatorNameString);
//        mTimeTv.setText(mTime);
//        mDetailTv.setText(mDetail);
        mNoticesSubscriber = new SubscriberOnNextErrorListener<NoticeDispatchSupport>() {
            @Override
            public void onNext(NoticeDispatchSupport noticeDispatchDetail) {
                mNoticeDispatchDetail = noticeDispatchDetail.getNotice();
                mTitleTv.setText(mNoticeDispatchDetail.getTitle());
                mCreatorName.setText(mNoticeDispatchDetail.getCreatorName());
                mTimeTv.setText(mNoticeDispatchDetail.getTime());
                mDetailTv.setText(mNoticeDispatchDetail.getContext());
            }

            @Override
            public void onError(String error) {

            }
        };
        mDepartmentSubscriber = new SubscriberOnNextErrorListener<DepartmentDispatchDetailBean>() {
            @Override
            public void onNext(DepartmentDispatchDetailBean departmentDispatchDetailBean) {
                departmentDispatchBean = departmentDispatchDetailBean.getDeptnotice();
                mTitleTv.setText(departmentDispatchBean.getBt());
                mCreatorName.setText(departmentDispatchBean.getDjrName());
                mTimeTv.setText(departmentDispatchBean.getDjrq());
                mDetailTv.setText(departmentDispatchBean.getZy());
            }

            @Override
            public void onError(String error) {

            }
        };
        mGroupSubscriber = new SubscriberOnNextErrorListener<GroupDispatchDetailBean>() {
            @Override
            public void onNext(GroupDispatchDetailBean groupDispatchDetailBean) {
                groupDispatchBean = groupDispatchDetailBean.getGroupNotice();
                mTitleTv.setText(groupDispatchBean.getTitle());
                mCreatorName.setText(groupDispatchBean.getMakerName());
                mTimeTv.setText(groupDispatchBean.getMakeTime());
                mDetailTv.setText(groupDispatchBean.getSummary());
            }

            @Override
            public void onError(String error) {

            }
        };

        if (mDispatchType.equals("notices")) {
            getNoticeDetail(id);
            setTitle(R.string.notice_detail_title);
        } else if (mDispatchType.equals("group")) {
            getGroupDetail(id);
            setTitle("集团发文详情");
        } else if (mDispatchType.equals("department")) {
            getDepartmentDetail(id);
            setTitle("部门发文详情");
        }


    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        setResult(FOR_DETAIL_REQUEST_CODE);
    }

    //公告
    public void getNoticeDetail(long id) {
        HttpMethods.getInstance().getNoticesDetail(new ProgressSubscriber<NoticeDispatchSupport>(mNoticesSubscriber, this, false), mToken, id);
    }

    //集团
    public void getGroupDetail(long id) {
        HttpMethods.getInstance().getGroupDispatchDetail(new ProgressSubscriber<GroupDispatchDetailBean>(mGroupSubscriber, this, false), mToken, id);
    }

    //部门
    public void getDepartmentDetail(long id) {
        HttpMethods.getInstance().getDepartmentDispatchDetail(new ProgressSubscriber<DepartmentDispatchDetailBean>(mDepartmentSubscriber, this, false), mToken, id);
    }


}
