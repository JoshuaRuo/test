package cn.cjsj.im.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.ProspectDataBean;
import cn.cjsj.im.ui.viewholder.BaseRecyclerHolder;
import cn.cjsj.im.ui.viewholder.ChildRecyclerHolder;
import cn.cjsj.im.ui.viewholder.ExpandItemClickListener;
import cn.cjsj.im.ui.viewholder.ParentRecyclerHolder;

/**
 * Created by LuoYang on 2018/8/6 16:20
 * 项目策划勘察详情适配器
 */
public class ProspectRecyclerAdapter extends RecyclerView.Adapter<BaseRecyclerHolder> {
    private Context mContext;
    private List<ProspectDataBean> mList;
    private OnScrollListener mListener;
    private LayoutInflater mInflater;

    private int mChildLength = 0;
    private List<ProspectDataBean> mChildList;


    public ProspectRecyclerAdapter(Context context, List<ProspectDataBean> list,List<ProspectDataBean> childList) {
        this.mList = list;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mChildList = childList;
        if (childList != null) {
            mChildLength = mChildList.size();
        }
    }

    @NonNull
    @Override
    public BaseRecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case ProspectDataBean.PARENT_ITEM:
                view = mInflater.inflate(R.layout.prospect_parent_model, parent, false);
                return new ParentRecyclerHolder(mContext, view);

            case ProspectDataBean.CHILD_ITEM:
                view = mInflater.inflate(R.layout.product_recycler_model, parent, false);
                return new ChildRecyclerHolder(mContext, view);

            default:
                view = mInflater.inflate(R.layout.prospect_parent_model, parent, false);
                return new ParentRecyclerHolder(mContext, view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ProspectDataBean.PARENT_ITEM:
                ParentRecyclerHolder parentViewHolder = (ParentRecyclerHolder) holder;
                parentViewHolder.bindView(mList.get(position), position, itemClickListener,mList.size());
                break;
            case ProspectDataBean.CHILD_ITEM:
                ChildRecyclerHolder childViewHolder = (ChildRecyclerHolder) holder;
                childViewHolder.bingView(mList.get(position), position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        } else {
            return 0;
        }
    }

    private ExpandItemClickListener itemClickListener = new ExpandItemClickListener() {
        @Override
        public void onExpandChildren(ProspectDataBean bean) {
            int position = getCurrentPosition(bean.getID()); //确定当前点击的Item位置

            if (mChildList == null) {
                return;
            }
            if (mChildLength > 0){
                for (int i = 0; i < mChildList.size(); i ++){
                    add(mChildList.get(i), position + 1);
                }
            }
            if (position == mList.size() - 2 && mListener != null) { //如果点击的item为最后一个
                mListener.scrollTo(position + 1);//向下滚动，使子布局能够完全展示
            }
        }

        @Override
        public void onHideChildren(ProspectDataBean bean) {
            int position = getCurrentPosition(bean.getID());//确定当前点击的item位置
            ProspectDataBean children = bean.getChildBean();//获取子布局对象
            if (children == null) {

                return;
            }
            if (mChildLength > 0){
                for (int i = 0; i < mChildList.size(); i ++) {
                    remove(position + 1);//删除
                }
            }else {

            }
            if (mListener != null) {
                mListener.scrollTo(position);
            }
        }
    };

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getType();
    }

    public interface OnScrollListener {
        void scrollTo(int pos);
    }

    public void setOnScrollListener(OnScrollListener listener) {
        this.mListener = listener;
    }

    /**
     * 确定当前点击的item位置并返回
     *
     * @param uuid
     * @return
     */
    protected int getCurrentPosition(String uuid) {
        for (int i = 0; i < mList.size(); i++) {
            if (uuid.equalsIgnoreCase(mList.get(i).getID())) {
                return i;
            }
        }
        return -1;
    }



    /**
     * 在父布局下方插入一条数据
     *
     * @param bean
     * @param position
     */
    public void add(ProspectDataBean bean, int position) {
        mList.add(position, bean);
        notifyItemInserted(position);
    }

    /**
     * 移除子布局数据
     *
     * @param position
     */
    protected void remove(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }
}
