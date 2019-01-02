package cn.cjsj.im.ui.viewholder;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import cn.cjsj.im.R;
import cn.cjsj.im.ui.activity.DepartmentStaffActivity;
import cn.cjsj.im.ui.widget.treeview.model.TreeNode;

/**
 * Created by LuoYang on 2018/8/10 10:12
 * 通讯录holder
 */
public class TreeViewHolder extends TreeNode.BaseNodeViewHolder<TreeViewHolder.IconTreeItem> {
    private ImageView mRight;

    public TreeViewHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(TreeNode node, final IconTreeItem value) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.tree_view_holder_parent_model, null, false);
        TextView tvValue = view.findViewById(R.id.tree_name);
        tvValue.setText(value.department);

        RelativeLayout itemLayout = view.findViewById(R.id.tree_item_layout);

        TextView pepNum = view.findViewById(R.id.tree_pep_num);
        pepNum.setText(value.num);

        ImageView left = view.findViewById(R.id.tree_item_left);

        mRight = view.findViewById(R.id.tree_right_icon);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mRight.getLayoutParams();
        mRight.setLayoutParams(params);
        mRight.setRotation(0);


        TextPaint tp = tvValue.getPaint();
        if (node.getLevel() == 1){
            tp.setFakeBoldText(true);
        }else {
            tp.setFakeBoldText(false);
        }
        if (node.getLevel() == 2) {
            left.setImageResource(R.mipmap.contact_level2);
            /**临时结构**/
            itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!"(0人)".equals(value.num)) {
                        Intent intent = new Intent(context, DepartmentStaffActivity.class);
                        intent.putExtra("deName", value.department);
                        intent.putExtra("orgId", value.orgId);
                        context.startActivity(intent);
                    }
                }
            });
            mRight.clearAnimation();
        }
//        else if (node.getLevel() == 3) {
//            left.setVisibility(View.INVISIBLE);
//            itemLayout.setBackgroundColor(ContextCompat.getColor(context,R.color.gty_page_bgcolor));
//            itemLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(context,value.department,Toast.LENGTH_SHORT).show();
//                }
//            });
//            mRight.clearAnimation();
//        }
        return view;
    }

    public static class IconTreeItem {
        public String department;
        public String num;
        public long orgId;

        public IconTreeItem(String department, String num) {
            this.department = department;
            this.num = num;
        }

        public IconTreeItem(String department, String num,long orgId) {
            this.department = department;
            this.num = num;
            this.orgId = orgId;
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
