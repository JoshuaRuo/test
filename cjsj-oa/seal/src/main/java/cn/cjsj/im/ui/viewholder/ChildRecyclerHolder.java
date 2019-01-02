package cn.cjsj.im.ui.viewholder;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.ProspectDataBean;
import cn.cjsj.im.ui.activity.ProspectDetailActivity;
import rx.functions.Action1;

/**
 * Created by LuoYang on 2018/8/7 09:22
 * 项目策划勘察详情子布局Holder
 */
public class ChildRecyclerHolder extends BaseRecyclerHolder {
    private Context mContext;
    private View mView;
    private TextView mTitle,mTime,mStatus,mPrincipal;
    private RelativeLayout mItemLayout;

    public ChildRecyclerHolder(Context context, View itemView) {
        super(itemView);
        mView = itemView;
        this.mContext = context;
    }

    public void bingView(ProspectDataBean bean, int pos) {
        mTitle = mView.findViewById(R.id.product_item_title_tv);
        mTime = mView.findViewById(R.id.product_item_time);
        mPrincipal = mView.findViewById(R.id.product_item_principal_tv);
        mStatus = mView.findViewById(R.id.product_item_status);
        mItemLayout = mView.findViewById(R.id.product_recycler_layout);
        mTitle.setText(bean.getTitle());
        mTime.setText(bean.getTime());
        mPrincipal.setText(bean.getPrincipal());
        setStatus(bean.getStatus());

        RxView.clicks(mItemLayout)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                   mContext.startActivity(new Intent(mContext, ProspectDetailActivity.class));
                    }
                });

    }

    private void setStatus(String status) {

        switch (status) {
            case "1":
                mStatus.setText("进行中");
                mStatus.setTextColor(ContextCompat.getColor(mContext,R.color.color_2293ff));
                break;

            case "2":
                mStatus.setText("已延迟");
                mStatus.setTextColor(ContextCompat.getColor(mContext,R.color.color_fc472b));
                break;

            case "3":
                mStatus.setText("已完成");
                mStatus.setTextColor(ContextCompat.getColor(mContext,R.color.color_00cc05));
                break;


        }
    }
}
