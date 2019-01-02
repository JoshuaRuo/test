package cn.cjsj.im.ui.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.NoticeAndIntegralBean;
import cn.cjsj.im.gty.tools.ImageKit;
import cn.cjsj.im.model.NoticeDetailBean;
import me.bakumon.library.adapter.BulletinAdapter;

/**
 * Created by LuoYang on 2018/7/5.
 * 轮播公告适配器
 */

public class NoticesAdapter extends BulletinAdapter<NoticeDetailBean> {
    public NoticesAdapter(Context context, List<NoticeDetailBean> list) {
        super(context, list);
    }

    @Override
    public View getView(int position) {
        View view = getRootView(R.layout.home_notices_model);
        TextView value = view.findViewById(R.id.notices_value_tv);
        TextView time = view.findViewById(R.id.notices_time_tv);
        ImageView img = view.findViewById(R.id.home_news_img);
        value.setEms(15);
        NoticeDetailBean noticeDetailBean = mData.get(position);
        value.setText(noticeDetailBean.getTitle());
        time.setText(noticeDetailBean.getTime());
            if (noticeDetailBean.getType() == 1){
                img.setImageResource(ImageKit.getMipMapImageSrcIdWithReflectByName("home_news_icon"));
            }else if (noticeDetailBean.getType() == 3){
                img.setImageResource(ImageKit.getMipMapImageSrcIdWithReflectByName("home_group_icon"));
            }else if (noticeDetailBean.getType() == 4){
                img.setImageResource(ImageKit.getMipMapImageSrcIdWithReflectByName("home_department_icon"));
            }
        return view;
    }
}
