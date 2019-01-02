package cn.cjsj.im.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by LuoYang on 2017/9/27.
 */

public class ImageBannerLoader extends ImageLoader {
    private SimpleDraweeView mSimpleDraweeView;
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context).load(path).into(imageView);
    }

    @Override
    public ImageView createImageView(Context context) {
        try {
             mSimpleDraweeView = new SimpleDraweeView(context);
        }catch (NullPointerException n){

        }
        return mSimpleDraweeView;

    }
}
