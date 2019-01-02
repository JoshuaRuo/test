package cn.cjsj.im.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;

import java.util.List;

import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.ProjectDetailMemberResponse;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by LuoYang on 2018/8/6 08:53
 * 项目详情人员列表适配器
 */
public class ProjectDetailListAdapter extends RecyclerView.Adapter {

    private List<ProjectDetailMemberResponse> mList;
    private LayoutInflater mInflater;
    private ViewHolder mViewHolder;
    private Context mContext;

    public ProjectDetailListAdapter(Context context, List<ProjectDetailMemberResponse> list) {
        this.mList = list;
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.project_detail_list_model, parent, false);
        mViewHolder = new ViewHolder(view);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.title.setText(mList.get(position).getUserName());
            viewHolder.value.setText(mList.get(position).getUserName());

            if (mList.get(position).getHeadIcon() != null) {
                viewHolder.mImage.setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load(mList.get(position).getHeadIcon())
                        .into(viewHolder.mImage);
                viewHolder.title.setVisibility(View.GONE);
            } else {
                viewHolder.mImage.setVisibility(View.GONE);
                viewHolder.title.setVisibility(View.VISIBLE);
                if (mList.get(position).getUserName().length() > 2) {
                    String name = mList.get(position).getUserName().substring(1, 3);
                    viewHolder.title.setText(name);
                } else {
                    viewHolder.title.setText(mList.get(position).getUserName());
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mList == null) {
            return 0;
        } else {
            return mList.size();
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView value;
        private CircleImageView mImage;

        public ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.prodetail_list_name_title);
            value = itemView.findViewById(R.id.prodetail_list_name_value);
            mImage = itemView.findViewById(R.id.prodetail_list_name_img);
        }
    }
}
