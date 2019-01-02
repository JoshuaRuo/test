package cn.cjsj.im.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;

import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.ProjectDetailMemberResponse;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by LuoYang on 2018/8/6 10:40
 * 项目成员适配器
 */
public class ProjectMemberAdapter extends BGARecyclerViewAdapter<ProjectDetailMemberResponse> {
    public ProjectMemberAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.project_member_model);
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, ProjectDetailMemberResponse model) {
        helper.setText(R.id.member_icon, model.getRoleName());
        helper.setText(R.id.member_name_tv, model.getUserName());
        CircleImageView img = helper.getView(R.id.member_model_head_img);
        setStatus(helper,model.getRoleName());

        if (model.getHeadIcon() != null) {
            img.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(model.getHeadIcon())
                    .into(img);
            helper.getTextView(R.id.member_model_name_title).setVisibility(View.GONE);
        } else {
            img.setVisibility(View.GONE);
            helper.getTextView(R.id.member_model_name_title).setVisibility(View.VISIBLE);
            if (model.getUserName().length() > 2) {
                String name = model.getUserName().substring(1, 3);
                helper.setText(R.id.member_model_name_title, name);
            } else {
                helper.setText(R.id.member_model_name_title, model.getUserName());
            }
        }
    }

    public void setStatus(BGAViewHolderHelper helper,String statusIcon) {
        switch (statusIcon) {
            case "项目负责人":
                helper.setVisibility(R.id.member_project_leader_icon, View.VISIBLE);
                helper.setVisibility(R.id.member_major_leader_icon, View.GONE);
                helper.setVisibility(R.id.member_icon, View.GONE);
                break;

            case "专业负责人":
                helper.setVisibility(R.id.member_project_leader_icon, View.GONE);
                helper.setVisibility(R.id.member_major_leader_icon, View.VISIBLE);
                helper.setVisibility(R.id.member_icon, View.GONE);
                break;
            case "成员":
                helper.setVisibility(R.id.member_project_leader_icon, View.GONE);
                helper.setVisibility(R.id.member_major_leader_icon, View.GONE);
                helper.setVisibility(R.id.member_icon, View.VISIBLE);
                break;
        }
    }
}
