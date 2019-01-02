package cn.cjsj.im.ui.viewholder;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.ProspectDataBean;

/**
 * Created by LuoYang on 2018/8/6 16:46
 * 项目策划勘察详情父布局holder
 */
public class ParentRecyclerHolder extends BaseRecyclerHolder {
    private Context mContext;
    private View mView;
    private RelativeLayout mParent;
    private TextView mLeft;
    private ImageView mRight;
    private View mWhiteLine;

    public ParentRecyclerHolder(Context context, View itemView) {
        super(itemView);
        this.mView = itemView;
        mContext = context;
    }


    public void bindView(final ProspectDataBean bean, final int pos, final ExpandItemClickListener listener, final int itemSize) {
        mParent = mView.findViewById(R.id.parent_layout);
        mLeft = mView.findViewById(R.id.prospect_parent_left);
        mRight = mView.findViewById(R.id.prospect_parent_right);
        mWhiteLine = mView.findViewById(R.id.parent_dashed_view);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mRight.getLayoutParams();
        mRight.setLayoutParams(params);
        mLeft.setText(bean.getParentLeftTxt());

        if (pos == itemSize - 1) {
            mWhiteLine.setVisibility(View.GONE);
        }

        if (bean.isExpand()) {
            mRight.setRotation(90);
        } else {
            mRight.setRotation(0);
        }

        mParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    if (bean.isExpand()) {
                        listener.onHideChildren(bean);
                        bean.setExpand(false);
                        rotationExpandIcon(90, 0);
                        mWhiteLine.setVisibility(View.VISIBLE);
                        if (pos == itemSize - 1) {
                            mWhiteLine.setVisibility(View.GONE);
                        }
                    } else {
                        listener.onExpandChildren(bean);
                        bean.setExpand(true);
                        rotationExpandIcon(0, 90);
                        mWhiteLine.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
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
