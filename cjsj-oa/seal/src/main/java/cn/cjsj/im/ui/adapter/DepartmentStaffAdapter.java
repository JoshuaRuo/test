package cn.cjsj.im.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.Glide;

import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.DepartmentStaffBean;
import cn.cjsj.im.ui.activity.StaffDetailActivity;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by LuoYang on 2018/11/5 15:37
 */
public class DepartmentStaffAdapter extends BGARecyclerViewAdapter<DepartmentStaffBean> {
    private Context mContext;
    public DepartmentStaffAdapter(RecyclerView recyclerView, Context context) {
        super(recyclerView, R.layout.de_staff_model);
        this.mContext = context;
    }


    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, DepartmentStaffBean model) {
        helper.setText(R.id.destaff_name, model.getFullName());
        helper.setText(R.id.destaff_pos_name, model.getPosName());
        CircleImageView img =  helper.getView(R.id.staff_model_detail_img);

        if (model.getHeadPortrait() != null){
            img.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(model.getHeadPortrait())
                    .into(img);
            helper.getTextView(R.id.destaff_model_headimg).setVisibility(View.GONE);
        }else {
            img.setVisibility(View.GONE);
            helper.getTextView(R.id.destaff_model_headimg).setVisibility(View.VISIBLE);
            if (model.getFullName().length() > 2) {
                String name = model.getFullName().substring(1, 3);
                helper.setText(R.id.destaff_model_headimg, name);
            } else {
                helper.setText(R.id.destaff_model_headimg, model.getFullName());
            }
        }




    }
}
