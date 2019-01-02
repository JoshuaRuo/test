package cn.cjsj.im.ui.adapter;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.alibaba.fastjson.JSONObject;

import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.ProjectListBeanResponse;

/**
 * Created by LuoYang on 2018/8/1 10:32
 * 项目管理ListModel
 */
public class ProjectAdapter extends BGARecyclerViewAdapter<ProjectListBeanResponse> {

    public ProjectAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.project_list_model);
    }


    @Override
    protected void fillData(final BGAViewHolderHelper helper, int position, final ProjectListBeanResponse model) {
        helper.setText(R.id.project_item_title_tv, model.getXmmc());
        helper.setText(R.id.project_item_time_tv, model.getXmsj());
        helper.setText(R.id.project_item_status, model.getXmztName());
        helper.setTextColor(R.id.project_item_status, Color.parseColor(model.getXmztColor()));
//        helper.setText(R.id.project_item_right_icon, "10");

        if (model.getAttention() == 1) {
            helper.setText(R.id.project_attention_tv, "已关注");
            helper.setTextColorRes(R.id.project_attention_tv, R.color.color_fc472b);
            helper.setImageResource(R.id.project_attention_img, R.drawable.project_focus_s);
        } else {
            helper.setText(R.id.project_attention_tv, "关注");
            helper.setTextColorRes(R.id.project_attention_tv, R.color.mine_head_color);
            helper.setImageResource(R.id.project_attention_img, R.drawable.project_focus_n);
        }

        helper.getView(R.id.project_item_left_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (model.getAttention()) {
                    case 1://关注
                        helper.setText(R.id.project_attention_tv, "关注");
                        helper.setTextColorRes(R.id.project_attention_tv, R.color.mine_head_color);
                        helper.setImageResource(R.id.project_attention_img, R.drawable.project_focus_n);
                        mProjectAttention.setAttention(model.getId(),"del");
                        break;
                    case 0://未关注
                        helper.setText(R.id.project_attention_tv, "已关注");
                        helper.setTextColorRes(R.id.project_attention_tv, R.color.color_fc472b);
                        helper.setImageResource(R.id.project_attention_img, R.drawable.project_focus_s);
                        mProjectAttention.setAttention(model.getId(),"add");
                        break;

                    default:
                        break;
                }
            }
        });
//            helper.setVisibility(R.id.project_item_left_layout, View.GONE);
//            helper.setVisibility(R.id.project_item_left_layout, View.VISIBLE);
    }


    private  ProjectAttention mProjectAttention;
    public interface ProjectAttention{
         void setAttention(long projectId,String action);
    }
    public void setProjectAtt(ProjectAttention projectAtt ){
        this.mProjectAttention =projectAtt;
    }

}
