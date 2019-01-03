package cn.cjsj.im.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.home.entity.MenuItem;
import cn.cjsj.im.gty.tools.ImageKit;

/**
 * Created by LuoYang on 2019/1/3 15:36
 * 首页RV适配器
 */
public class Home203RvAdapter extends BGARecyclerViewAdapter<MenuItem> {
    ;


    public Home203RvAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.home_rv_203_model);
    }


    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, MenuItem model) {
        helper.setImageResource(R.id.home_rv_model_img, ImageKit.getMipMapImageSrcIdWithReflectByName(model.getIcon()));
        helper.setText(R.id.home_rv_model_name, model.getName());
    }
}
