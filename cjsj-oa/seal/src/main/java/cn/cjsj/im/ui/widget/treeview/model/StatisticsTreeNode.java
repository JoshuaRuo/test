package cn.cjsj.im.ui.widget.treeview.model;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import cn.cjsj.im.R;
import cn.cjsj.im.ui.widget.treeview.view.AndroidTreeView;
import cn.cjsj.im.ui.widget.treeview.view.StatisticsAndroidTreeView;
import cn.cjsj.im.ui.widget.treeview.view.TreeNodeWrapperView;

/**
 * Created by LuoYang on 2/10/16.
 */
public class StatisticsTreeNode {
    public static final String NODES_ID_SEPARATOR = ":";

    private int mId;
    private int mLastId;
    private StatisticsTreeNode mParent;
    private boolean mSelected;
    private boolean mSelectable = true;
    private final List<StatisticsTreeNode> children;
    private BaseNodeViewHolder mViewHolder;
    private TreeNodeClickListener mClickListener;
    private TreeNodeLongClickListener mLongClickListener;
    private Object mValue;
    private boolean mExpanded;

    public static StatisticsTreeNode root() {
        StatisticsTreeNode root = new StatisticsTreeNode(null);
        root.setSelectable(false);
        return root;
    }

    private int generateId() {
        return ++mLastId;
    }

    public StatisticsTreeNode(Object value) {
        children = new ArrayList<>();
        mValue = value;
    }

    public StatisticsTreeNode addChild(StatisticsTreeNode childNode) {
        childNode.mParent = this;
        childNode.mId = generateId();
        children.add(childNode);
        return this;
    }

    public StatisticsTreeNode addChildren(StatisticsTreeNode... nodes) {
        for (StatisticsTreeNode n : nodes) {
            addChild(n);
        }
        return this;
    }

    public StatisticsTreeNode addChildren(Collection<StatisticsTreeNode> nodes) {
        for (StatisticsTreeNode n : nodes) {
            addChild(n);
        }
        return this;
    }

    public int deleteChild(StatisticsTreeNode child) {
        for (int i = 0; i < children.size(); i++) {
            if (child.mId == children.get(i).mId) {
                children.remove(i);
                return i;
            }
        }
        return -1;
    }

    public List<StatisticsTreeNode> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public int size() {
        return children.size();
    }

    public StatisticsTreeNode getParent() {
        return mParent;
    }

    public int getId() {
        return mId;
    }

    public boolean isLeaf() {
        return size() == 0;
    }

    public Object getValue() {
        return mValue;
    }

    public boolean isExpanded() {
        return mExpanded;
    }

    public StatisticsTreeNode setExpanded(boolean expanded) {
        mExpanded = expanded;
        return this;
    }

    public void setSelected(boolean selected) {
        mSelected = selected;
    }

    public boolean isSelected() {
        return mSelectable && mSelected;
    }

    public void setSelectable(boolean selectable) {
        mSelectable = selectable;
    }

    public boolean isSelectable() {
        return mSelectable;
    }

    public String getPath() {
        final StringBuilder path = new StringBuilder();
        StatisticsTreeNode node = this;
        while (node.mParent != null) {
            path.append(node.getId());
            node = node.mParent;
            if (node.mParent != null) {
                path.append(NODES_ID_SEPARATOR);
            }
        }
        return path.toString();
    }


    public int getLevel() {
        int level = 0;
        StatisticsTreeNode root = this;
        while (root.mParent != null) {
            root = root.mParent;
            level++;
        }
        return level;
    }

    public boolean isLastChild() {
        if (!isRoot()) {
            int parentSize = mParent.children.size();
            if (parentSize > 0) {
                final List<StatisticsTreeNode> parentChildren = mParent.children;
                return parentChildren.get(parentSize - 1).mId == mId;
            }
        }
        return false;
    }

    public StatisticsTreeNode setViewHolder(BaseNodeViewHolder viewHolder) {
        mViewHolder = viewHolder;
        if (viewHolder != null) {
            viewHolder.mNode = this;
        }
        return this;
    }

    public StatisticsTreeNode setClickListener(TreeNodeClickListener listener) {
        mClickListener = listener;
        return this;
    }

    public TreeNodeClickListener getClickListener() {
        return this.mClickListener;
    }

    public StatisticsTreeNode setLongClickListener(TreeNodeLongClickListener listener) {
        mLongClickListener = listener;
        return this;
    }

    public TreeNodeLongClickListener getLongClickListener() {
        return mLongClickListener;
    }

    public BaseNodeViewHolder getViewHolder() {
        return mViewHolder;
    }

    public boolean isFirstChild() {
        if (!isRoot()) {
            List<StatisticsTreeNode> parentChildren = mParent.children;
            return parentChildren.get(0).mId == mId;
        }
        return false;
    }

    public boolean isRoot() {
        return mParent == null;
    }

    public StatisticsTreeNode getRoot() {
        StatisticsTreeNode root = this;
        while (root.mParent != null) {
            root = root.mParent;
        }
        return root;
    }

    public interface TreeNodeClickListener {
        void onClick(StatisticsTreeNode node, Object value);
    }

    public interface TreeNodeLongClickListener {
        boolean onLongClick(StatisticsTreeNode node, Object value);
    }

    public static abstract class BaseNodeViewHolder<E> {
        protected StatisticsAndroidTreeView tView;
        protected StatisticsTreeNode mNode;
        private View mView;
        protected int containerStyle;
        protected Context context;

        public BaseNodeViewHolder(Context context) {
            this.context = context;
        }

        public View getView() {
            if (mView != null) {
                return mView;
            }
            final View nodeView = getNodeView();
            final TreeNodeWrapperView nodeWrapperView = new TreeNodeWrapperView(nodeView.getContext(), getContainerStyle());
            nodeWrapperView.insertNodeView(nodeView);
            mView = nodeWrapperView;

            return mView;
        }

        public void setTreeViev(StatisticsAndroidTreeView treeViev) {
            this.tView = treeViev;
        }

        public StatisticsAndroidTreeView getTreeView() {
            return tView;
        }

        public void setContainerStyle(int style) {
            containerStyle = style;
        }

        public View getNodeView() {
            return createNodeView(mNode, (E) mNode.getValue());
        }

        public ViewGroup getNodeItemsView() {
            return (ViewGroup) getView().findViewById(R.id.node_items);
        }

        public boolean isInitialized() {
            return mView != null;
        }

        public int getContainerStyle() {
            return containerStyle;
        }


        public abstract View createNodeView(StatisticsTreeNode node, E value);

        public void toggle(boolean active) {
            // empty
        }

        public void toggleSelectionMode(boolean editModeEnabled) {
            // empty
        }
    }
}
