package cn.cjsj.im.ui.widget.treeview.holder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import cn.cjsj.im.ui.widget.treeview.model.StatisticsTreeNode;
import cn.cjsj.im.ui.widget.treeview.model.TreeNode;

/**
 * Created by Bogdan Melnychuk on 2/11/15.
 */
public class StatisticsSimpleViewHolder extends StatisticsTreeNode.BaseNodeViewHolder<Object> {

    public StatisticsSimpleViewHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(StatisticsTreeNode node, Object value) {
        final TextView tv = new TextView(context);
        tv.setText(String.valueOf(value));
        return tv;
    }

    @Override
    public void toggle(boolean active) {

    }
}
