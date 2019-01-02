package cn.cjsj.im.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.cjsj.im.R;
import cn.cjsj.im.model.NoticeDetailBean;

/**
 * Created by LuoYang on 2018/5/18.
 */

public class NoticeRecyclerViewAdapter extends RecyclerView.Adapter {
    private List<NoticeDetailBean> mList;
    private View.OnClickListener mOnClickListener;
    private LayoutInflater mInflater;
    private ViewHolder mViewHolder;


    public NoticeRecyclerViewAdapter(Context context, List<NoticeDetailBean> list, View.OnClickListener onClickListener) {
        this.mList = list;
        this.mOnClickListener = onClickListener;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.notice_list_model, parent, false);
        mViewHolder = new ViewHolder(view,mOnClickListener);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            NoticeDetailBean noticeDetailBean = mList.get(position);
            viewHolder.titleTV.setText(noticeDetailBean.getTitle());
            viewHolder.timeTV.setText(noticeDetailBean.getTime());
            viewHolder.creatorName.setText(noticeDetailBean.getCreatorName());
            viewHolder.noticeLayout.setTag(noticeDetailBean);
            if (noticeDetailBean.getIsRead() == 0) {
                viewHolder.iconTV.setVisibility(View.VISIBLE);
            } else {
                viewHolder.iconTV.setVisibility(View.INVISIBLE);
            }

        }
    }

    @Override
    public int getItemCount() {
        if (mList != null){
            return mList.size();
        }else {
            return 0;
        }

    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTV;
        private TextView timeTV;
        private TextView creatorName;
        private TextView iconTV;
        private RelativeLayout noticeLayout;

        public ViewHolder(View itemView, View.OnClickListener onClickListener) {
            super(itemView);
            titleTV = (TextView) itemView.findViewById(R.id.notice_list_title_tv);
            timeTV = (TextView) itemView.findViewById(R.id.notice_list_time_tv);
            creatorName = (TextView) itemView.findViewById(R.id.notice_list_creatorname_tv);
            iconTV = (TextView) itemView.findViewById(R.id.notice_list_mark_icon_tv);
            noticeLayout = (RelativeLayout) itemView.findViewById(R.id.notice_layout);
            noticeLayout.setOnClickListener(onClickListener);
        }
    }
}
