package cn.cjsj.im.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


import com.bumptech.glide.Glide;

import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.SysUserBean;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by LuoYang on 2018/11/27 14:50
 * 发送人适配器
 */
public class ReWorkAttendanceAdapter extends BGARecyclerViewAdapter<SysUserBean> {
    public ReWorkAttendanceAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.re_attendance_adapter);
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, final SysUserBean model) {
        CircleImageView img = helper.getView(R.id.re_attendance_adapter_img);
        helper.setText(R.id.re_attendance_adapter_name, model.getFullname());
        helper.setItemChildClickListener(R.id.attendance_adapter_layout);
        helper.setOnItemChildClickListener(new BGAOnItemChildClickListener() {
            @Override
            public void onItemChildClick(ViewGroup parent, View childView, int position) {
                mAttendanceData.setData(model.getFullname(),position);
            }
        });
        if (model.getHeadPortrait() != null) {
            if ("select".equals(model.getHeadPortrait())) {
                img.setImageResource(R.mipmap.add_t_icon);
                img.setVisibility(View.VISIBLE);
                helper.getTextView(R.id.re_attendance_adapter_headimg).setVisibility(View.GONE);
            } else {
                img.setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load(model.getHeadPortrait())
                        .into(img);
                helper.getTextView(R.id.re_attendance_adapter_headimg).setVisibility(View.GONE);
            }

        } else {
            img.setVisibility(View.GONE);
            helper.getTextView(R.id.re_attendance_adapter_headimg).setVisibility(View.VISIBLE);
            if (model.getFullname().length() > 2) {
                String name = model.getFullname().substring(1, 3);
                helper.setText(R.id.re_attendance_adapter_headimg, name);
            } else {
                helper.setText(R.id.re_attendance_adapter_headimg, model.getFullname());
            }
        }
    }

    private AttendanceData mAttendanceData;
    public interface AttendanceData{
        void setData(String name,int position);
    }
    public void setAttendanceData(AttendanceData attendanceData){
        this.mAttendanceData = attendanceData;
    }
}
