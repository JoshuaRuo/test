package cn.cjsj.im.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;


import com.bumptech.glide.Glide;

import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildCheckedChangeListener;
import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.SysUserBean;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by LuoYang on 2018/11/27 15:15
 * 选择人适配器
 */
public class SelectPeopleAdapter extends BGARecyclerViewAdapter<SysUserBean> {
    private boolean[] flag;

    public SelectPeopleAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.select_people_model);

    }

    public SelectPeopleAdapter(RecyclerView recyclerView, boolean[] flag) {
        super(recyclerView, R.layout.select_people_model);
        this.flag = flag;
    }

    @Override
    protected void fillData(final BGAViewHolderHelper helper, final int position, SysUserBean model) {
        if (flag == null) {
            flag = new boolean[getItemCount()];
        }
        CircleImageView img = helper.getView(R.id.select_people_img);
//        final CheckedTextView box = helper.getView(R.id.select_model_checkbox);
        helper.setText(R.id.select_people_name, model.getFullname());
        helper.setText(R.id.select_people_org, model.getOrgName());
        helper.setItemChildCheckedChangeListener(R.id.select_model_checkbox);
        helper.setChecked(R.id.select_model_checkbox, flag[position]).setOnItemChildCheckedChangeListener(new BGAOnItemChildCheckedChangeListener() {
            @Override
            public void onItemChildCheckedChanged(ViewGroup parent, CompoundButton childView, int position, boolean isChecked) {
                flag[position] = isChecked;
                if (isChecked) {
                    mSelectPeopleData.setPosition(position, flag);
                } else {
                    mSelectPeopleData.removePosition(position, flag);
                }
            }
        });


//        box.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (box.isChecked()) {
//                    box.setBackgroundResource(R.drawable.check_default);
//                    box.setChecked(false);
//                    mSelectPeopleData.removePosition(position);
//                } else {
//                    box.setBackgroundResource(R.drawable.check_selected);
//                    box.setChecked(true);
//                    mSelectPeopleData.setPosition(position);
//                }
//            }
//        });

        if (model.getHeadPortrait() != null) {
            img.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(model.getHeadPortrait())
                    .into(img);
            helper.getTextView(R.id.select_people_headimg).setVisibility(View.GONE);
        } else {
            img.setVisibility(View.GONE);
            helper.getTextView(R.id.select_people_headimg).setVisibility(View.VISIBLE);
            if (model.getFullname().length() > 2) {
                String name = model.getFullname().substring(1, 3);
                helper.setText(R.id.select_people_headimg, name);
            } else {
                helper.setText(R.id.select_people_headimg, model.getFullname());
            }
        }


    }

    private SelectPeopleData mSelectPeopleData;

    public interface SelectPeopleData {
        void setPosition(int position, boolean[] flag);

        void removePosition(int position, boolean[] flag);
    }

    public void setSelectPeopleData(SelectPeopleData selectPeopleData) {
        this.mSelectPeopleData = selectPeopleData;
    }
}
