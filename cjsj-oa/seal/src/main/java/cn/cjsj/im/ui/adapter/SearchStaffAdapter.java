package cn.cjsj.im.ui.adapter;

import android.support.v7.widget.RecyclerView;

import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.DepartmentStaffBean;
import cn.cjsj.im.gty.bean.SysUserBean;

/**
 * Created by LuoYang on 2018/11/5 15:37
 */
public class SearchStaffAdapter extends BGARecyclerViewAdapter<DepartmentStaffBean> {
    public SearchStaffAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.de_staff_model);
    }


    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, DepartmentStaffBean model) {
        helper.setText(R.id.destaff_name, model.getFullName());
        helper.setText(R.id.destaff_pos_name, model.getPosName());

        if (model.getFullName().length() > 2) {
            String name = model.getFullName().substring(1, 3);
            helper.setText(R.id.destaff_model_headimg, name);
        } else {
            helper.setText(R.id.destaff_model_headimg, model.getFullName());
        }


    }
}
