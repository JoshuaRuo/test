package cn.cjsj.im.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;


import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.DepartmentDispatchBean;
import cn.cjsj.im.server.utils.NLog;

/**
 * Created by LuoYang on 2018/7/18 09:30
 */
public class DepartmentDispatchAdapter extends BGARecyclerViewAdapter<DepartmentDispatchBean> {
    public DepartmentDispatchAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.dispatch_model);
    }
    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, DepartmentDispatchBean model) {
        helper.setText(R.id.dispatch_list_title_tv,model.getBt());
        helper.setText(R.id.dispatch_list_creatorname_tv,model.getDjrName());
        helper.setText(R.id.dispatch_list_time_tv,model.getDjrq());
        if (model.getIsRead() == 0) {
            helper.setVisibility(R.id.dispatch_list_mark_icon_tv, View.VISIBLE);
        } else {
            helper.setVisibility(R.id.dispatch_list_mark_icon_tv, View.INVISIBLE);
        }

    }
}
