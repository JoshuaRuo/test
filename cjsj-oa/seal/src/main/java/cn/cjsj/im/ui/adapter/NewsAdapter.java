package cn.cjsj.im.ui.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.NewsResponse;
import cn.cjsj.im.ui.viewholder.NewsFootHolder;

/**
 * 消息列表适配器
 * Created by LuoYang on 2018/1/4.
 */

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<NewsResponse> mNewsList;
    private View.OnClickListener mClickListener;
    private LayoutInflater mInflater;
    public NewsFootHolder mNewsFootHolder;

    private final int NORMALLAYOUT = 0;
    private final int FOOTERLAYOUT = 1;

    public NewsAdapter(Context context, List<NewsResponse> newsList, View.OnClickListener clickListener) {
        this.mClickListener = clickListener;
        this.mNewsList = newsList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = mInflater.inflate(R.layout.news_model, parent, false);
            return new ViewHolder(view, mClickListener);
        } else {
            view = mInflater.inflate(R.layout.news_foot_holder, parent, false);
            mNewsFootHolder = new NewsFootHolder(view);
        }
        return mNewsFootHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            NewsResponse newsResponse = mNewsList.get(position);
            viewHolder.title.setText(newsResponse.getContext());
            viewHolder.time.setText(newsResponse.getTime());
            viewHolder.newslayout.setTag(newsResponse);

            if (newsResponse.getJson() != null && !"".equals(newsResponse.getJson())) {

                HashMap<String, Object> hashMap = JSONObject.parseObject(newsResponse.getJson(), HashMap.class);
                StringBuilder builder = new StringBuilder();
                int i = 0;
                for (Map.Entry<String, Object> entry : hashMap.entrySet()) {
                    i++;
                    if (i < hashMap.size()) {
                        builder.append(entry.getKey() + entry.getValue() + "\n");
                    } else {
                        builder.append(entry.getKey() + entry.getValue());
                    }
                }
                viewHolder.value.setText(builder);
            }
        }


    }

    @Override
    public int getItemCount() {
        if (mNewsList != null) {
            return mNewsList.size();
        } else {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mNewsList.size()) {
            return FOOTERLAYOUT;
        } else {
            return NORMALLAYOUT;
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        public TextView time;
        public TextView title;
        public TextView value;
        public RelativeLayout newslayout;

        public ViewHolder(View itemView, View.OnClickListener clickListener) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.news_time);
            title = (TextView) itemView.findViewById(R.id.news_title);
            value = (TextView) itemView.findViewById(R.id.news_value);
            newslayout = (RelativeLayout) itemView.findViewById(R.id.news_layout);
            newslayout.setOnClickListener(clickListener);
        }
    }
}
