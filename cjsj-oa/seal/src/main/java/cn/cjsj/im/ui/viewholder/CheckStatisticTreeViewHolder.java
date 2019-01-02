package cn.cjsj.im.ui.viewholder;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import cn.cjsj.im.R;
import cn.cjsj.im.ui.activity.ReWorkAttendanceCardActivity;
import cn.cjsj.im.ui.widget.treeview.model.StatisticsTreeNode;
import rx.functions.Action1;

/**
 * Created by LuoYang on 2018/11/21 09:17
 * 考勤统计伸展列表适配器
 */
public class CheckStatisticTreeViewHolder extends StatisticsTreeNode.BaseNodeViewHolder<CheckStatisticTreeViewHolder.StatisticsListHolder> {

    private ImageView mRight;

    public CheckStatisticTreeViewHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(StatisticsTreeNode node, final StatisticsListHolder value) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.statistics_list_holder, null, false);
        TextView leftTitle = view.findViewById(R.id.statistics_holder_left_title);
        TextView daysCount = view.findViewById(R.id.statistics_holder_days_count);
        RelativeLayout itemLayout = view.findViewById(R.id.statistics_holder_item_layout);
        mRight = view.findViewById(R.id.statistics_holder_right_icon);
        leftTitle.setText(value.leftTitle);
        if ("迟到".equals(value.leftTitle) || "早退".equals(value.leftTitle) || "缺卡".equals(value.leftTitle) || "旷工".equals(value.leftTitle)) {
            if (value.daysCount > 0) {
                daysCount.setTextColor(ContextCompat.getColor(context, R.color.color_fc472b));

            }
        }
        daysCount.setText(value.daysCount + value.timesName);

        if (node.getLevel() == 2) {
            mRight.setVisibility(View.GONE);
            daysCount.setVisibility(View.GONE);
            itemLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.gray_bg));
            leftTitle.setText(value.leftTitle);
        }

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mRight.getLayoutParams();
        mRight.setLayoutParams(params);
        mRight.setRotation(0);
//        RxView.clicks(itemLayout)
//                .throttleFirst(1, TimeUnit.SECONDS)
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//                        Toast.makeText(context,value.leftTitle,Toast.LENGTH_SHORT).show();
//                    }
//                });


        return view;
    }

    public static class StatisticsListHolder {

        public String leftTitle;
        public int daysCount;
        public String timesName;

        public StatisticsListHolder(String leftTitle, int daysCount, String timesName) {
            this.leftTitle = leftTitle;
            this.daysCount = daysCount;
            this.timesName = timesName;

        }
    }

    @Override
    public void toggle(boolean active) {
        super.toggle(active);
        if (active) {
            rotationExpandIcon(0, 90);
        } else {
            rotationExpandIcon(90, 0);
        }
    }

    private void rotationExpandIcon(float from, float to) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);//属性动画
            valueAnimator.setDuration(500);
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mRight.setRotation((Float) valueAnimator.getAnimatedValue());
                }
            });
            valueAnimator.start();
        }
    }

}
