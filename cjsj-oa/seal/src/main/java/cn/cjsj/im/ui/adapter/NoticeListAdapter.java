package cn.cjsj.im.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.cjsj.im.R;
import cn.cjsj.im.model.NoticeDetailBean;

/**
 * Created by LuoYang on 2017/9/30.
 * 公告列表适配器
 */

public class NoticeListAdapter extends BaseAdapter {
    private List<NoticeDetailBean> mListNewInfo;
    private Context mContext;
    private LayoutInflater mInflater;

    public NoticeListAdapter(Context context, List<NoticeDetailBean> listNewInfo) {
        this.mListNewInfo = listNewInfo;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (mListNewInfo != null) {
            return mListNewInfo.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        convertView = mInflater.inflate(R.layout.notice_list_model, null);
        viewHolder.titleTV = (TextView) convertView.findViewById(R.id.notice_list_title_tv);
        viewHolder.timeTV = (TextView) convertView.findViewById(R.id.notice_list_time_tv);
        viewHolder.creatorName = (TextView) convertView.findViewById(R.id.notice_list_creatorname_tv);
        viewHolder.iconTV = (TextView) convertView.findViewById(R.id.notice_list_mark_icon_tv);
        convertView.setTag(viewHolder);

        viewHolder.titleTV.setText(mListNewInfo.get(position).getTitle());
        viewHolder.timeTV.setText(mListNewInfo.get(position).getTime());
        viewHolder.creatorName.setText("发布人:" + mListNewInfo.get(position).getCreatorName());
        if (mListNewInfo.get(position).getIsRead() == 0) {
            viewHolder.iconTV.setVisibility(View.VISIBLE);
        } else {
            viewHolder.iconTV.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    private class ViewHolder {
        private TextView titleTV;
        private TextView timeTV;
        private TextView creatorName;
        private TextView iconTV;
    }
}
