package cn.cjsj.im.ui.widget.gridviewpager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.cjsj.im.R;
import cn.cjsj.im.gty.home.entity.MenuItem;
import cn.cjsj.im.gty.tools.ImageKit;

public class HomeGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<MenuItem> mLists;

    public HomeGridViewAdapter(Context pContext, List<MenuItem> mLists) {
        this.mContext = pContext;
        this.mLists = mLists;

    }

    @Override
    public int getCount() {

        return mLists.size();
    }

    @Override
    public Object getItem(int position) {

        return mLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Holder holder = null;
        if (null == convertView) {
            holder = new Holder();
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.item_menu_grid, null);
            holder.img = convertView.findViewById(R.id.pic);
            holder.img.setFocusable(false);
            holder.name = convertView.findViewById(R.id.desc);
            holder.notifiIcon = convertView.findViewById(R.id.table_notification_icon_tv);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.img.setImageResource(ImageKit.getMipMapImageSrcIdWithReflectByName(mLists.get(position).getIcon()));
        holder.name.setText(mLists.get(position).getName());

        return convertView;
    }

    private static class Holder {
        ImageView img;
        TextView name;
        TextView notifiIcon;
    }

}
