package cn.cjsj.im.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.ProjectChatBean;
import cn.cjsj.im.ui.viewholder.BaseRecyclerHolder;
import cn.cjsj.im.ui.viewholder.ProjectReceiveHolder;
import cn.cjsj.im.ui.viewholder.ProjectSendHolder;

/**
 * Created by LuoYang on 2018/8/8 16:19
 */
public class ProjectChatAdapter extends RecyclerView.Adapter<BaseRecyclerHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<ProjectChatBean> mList;

    public ProjectChatAdapter(Context context, List<ProjectChatBean> list) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mList = list;
    }

    @NonNull
    @Override
    public BaseRecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case ProjectChatBean.SEND_ITEM:
                view = mInflater.inflate(R.layout.project_chat_send_model, parent, false);
                return new ProjectSendHolder(view);
            case ProjectChatBean.RECEIVE_ITEM:
                view = mInflater.inflate(R.layout.project_chat_receive_model, parent, false);
                return new ProjectReceiveHolder(view);

            default:
                view = mInflater.inflate(R.layout.project_chat_receive_model, parent, false);
                return new ProjectReceiveHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ProjectChatBean.SEND_ITEM:
                ProjectSendHolder sendHolder = (ProjectSendHolder) holder;
                sendHolder.bindView(mList.get(position), position);
                break;

            case ProjectChatBean.RECEIVE_ITEM:
                ProjectReceiveHolder receiveHolder = (ProjectReceiveHolder) holder;
                receiveHolder.bindView(mList.get(position), position);
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

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getType();
    }
}
