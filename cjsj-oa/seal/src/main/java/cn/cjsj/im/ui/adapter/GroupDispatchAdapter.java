package cn.cjsj.im.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.GroupDispatchBean;
import cn.cjsj.im.model.NoticeDetailBean;

/**
 * Created by LuoYang on 2018/7/17 14:48
 */
public class GroupDispatchAdapter extends BGARecyclerViewAdapter<GroupDispatchBean> {
    public GroupDispatchAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.dispatch_model);
    }


    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, GroupDispatchBean model) {
        helper.setText(R.id.dispatch_list_title_tv,model.getTitle());
        helper.setText(R.id.dispatch_list_creatorname_tv,model.getMakerName());
        helper.setText(R.id.dispatch_list_time_tv,model.getMakeTime());
        if (model.getIsRead() == 0) {
            helper.setVisibility(R.id.dispatch_list_mark_icon_tv, View.VISIBLE);
        } else {
            helper.setVisibility(R.id.dispatch_list_mark_icon_tv, View.INVISIBLE);
        }
    }
}
