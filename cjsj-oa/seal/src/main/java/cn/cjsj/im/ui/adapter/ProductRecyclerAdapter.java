package cn.cjsj.im.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.cjsj.im.R;

/**
 * Created by LuoYang on 2018/8/6 15:47
 * 项目策划生产详情list适配器
 */
public class ProductRecyclerAdapter extends BGARecyclerViewAdapter<JSONObject> {

    private Context mContext;

    public ProductRecyclerAdapter(RecyclerView recyclerView, Context context) {
        super(recyclerView, R.layout.product_recycler_model);
        this.mContext = context;

    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, JSONObject model) {
        helper.setText(R.id.product_item_title_tv, model.getString("title"));
        helper.setText(R.id.product_item_principal_tv, model.getString("principal"));
        helper.setText(R.id.product_item_time, model.getString("time"));
        setStatus(helper,model.getString("status"));
    }

    private void setStatus(BGAViewHolderHelper helper, String status) {
        TextView textView = helper.getTextView(R.id.product_item_status);

        switch (status) {
            case "1":
                textView.setText("进行中");
                textView.setTextColor(ContextCompat.getColor(mContext,R.color.color_2293ff));
                break;

            case "2":
                textView.setText("已延迟");
                textView.setTextColor(ContextCompat.getColor(mContext,R.color.color_fc472b));
                break;

            case "3":
                textView.setText("已完成");
                textView.setTextColor(ContextCompat.getColor(mContext,R.color.color_00cc05));
                break;


        }
    }
}
