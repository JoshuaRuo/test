package cn.cjsj.im.ui.viewholder;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.cjsj.im.R;
import cn.cjsj.im.ui.widget.treeview.model.TreeNode;

/**
 * Created by LuoYang on 2018/8/10 17:10
 */
public class SecTreeViewHolder extends TreeNode.BaseNodeViewHolder<TreeViewHolder.IconTreeItem> {
    private ImageView mRight;
    public SecTreeViewHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(TreeNode node, TreeViewHolder.IconTreeItem value) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.tree_view_holder_parent_model, null, false);
        TextView tvValue = view.findViewById(R.id.tree_name);
//        tvValue.setText(value.text);

        mRight = view.findViewById(R.id.tree_right_icon);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mRight.getLayoutParams();
        mRight.setLayoutParams(params);
        mRight.setRotation(0);
        return view;
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
