package cn.cjsj.im.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TabWidget;


import cn.cjsj.im.R;
import cn.cjsj.im.gty.tools.ImageKit;
import cn.cjsj.im.utils.Path9DotUtils;

/**
 * 带箭头指示选中位置，底部箭头跟随滑动
 * <p>
 * 说明：
 * 支持任意个数，但由于无法左右滑动，太多了会导致宽度不够.
 *
 * @author LuoYang
 */
public class ArrowTabWidget extends TabWidget {

    private final int SCREEN;
    private final int BG_WIDTH;

    private int mCount;
    private float mItemWidth;
    private float mOffset;
    private float mInitPos;

    private Bitmap mBitmap;

    public ArrowTabWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        SCREEN = Path9DotUtils.getWidth(context);
        BG_WIDTH = SCREEN * 2;
        init(context);
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        Paint paint = new Paint();
        paint.setAlpha(0x40);
        if (mCount > 0) {
            float leftPos = mInitPos;
            leftPos += mOffset;
            canvas.drawBitmap(mBitmap, leftPos, 65, null);
        }
    }

    /**
     * 9patch图在nexus 5x(Android 6.0)上没有对齐，解决办法如下.
     * <p>
     * 原先放在在drawable-xxhdpi目录，对比发现
     * 1、放在drawable目录，对齐正常，但图片模糊；
     * 2、放在drawable-nodpi目录，对齐正常，显示正常.
     */
    private void init(Context context) {
        int itemHeight = getResources().getDimensionPixelSize(R.dimen.tab_icon_height);
        mBitmap = Path9DotUtils.getNinePatchBitmap(R.drawable.check_rank_tab_icon, BG_WIDTH, itemHeight, context);
    }

    /**
     * pager数目
     *
     * @param count tab count
     */
    public void setTabCount(int count) {
        if (count > 0) {
            mCount = count;
            mItemWidth = (float) SCREEN / mCount;
            mInitPos = -SCREEN + mItemWidth / 2;
        } else {
            System.out.println("ArrowTabWidget: Tab count can't be 0");
        }
    }

    public void updateArrow(int position, int offsetPx) {
        if (mCount > 0) {
            float o = offsetPx / (float) mCount;
            mOffset = position * mItemWidth + o;
            invalidate();
        }
    }

    /**
     * 适配各android版本 Drawable资源转Bitmap
     * @param context
     * @param vectorDrawableId
     * @return
     */
    private static Bitmap getBitmap(Context context,int vectorDrawableId) {
        Bitmap bitmap=null;
        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
            Drawable vectorDrawable = context.getDrawable(vectorDrawableId);
            bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                    vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            vectorDrawable.draw(canvas);
        }else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), vectorDrawableId);
        }
        return bitmap;
    }
}
