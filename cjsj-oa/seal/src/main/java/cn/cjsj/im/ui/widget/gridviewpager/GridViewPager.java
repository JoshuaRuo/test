package cn.cjsj.im.ui.widget.gridviewpager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liumeng on 10/30/15.
 */
public class GridViewPager extends ViewPager {
    private List<GridView> mLists = new ArrayList<>();
    private GridViewPagerAdapter adapter;
    private List listAll;
    private int rowInOnePage;
    private int columnInOnePage;
    private GridViewPagerDataAdapter gridViewPagerDataAdapter;

    public GridViewPager(Context context) {
        super(context);
    }

    private Bitmap bg;
    private Paint b = new Paint(Paint.ANTI_ALIAS_FLAG);

    public void setBackGround(Bitmap paramBitmap) {
        this.bg = paramBitmap;
        this.b.setFilterBitmap(true);
    }

    public GridViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewPagerDataAdapter getGridViewPagerDataAdapter() {
        return gridViewPagerDataAdapter;
    }

    public void setGridViewPagerDataAdapter(GridViewPagerDataAdapter gridViewPagerDataAdapter) {
        this.gridViewPagerDataAdapter = gridViewPagerDataAdapter;
        if (gridViewPagerDataAdapter.listAll == null || gridViewPagerDataAdapter.listAll.size() == 0) {
            return;
        }
        listAll = gridViewPagerDataAdapter.listAll;
        rowInOnePage = gridViewPagerDataAdapter.rowInOnePage;
        columnInOnePage = gridViewPagerDataAdapter.columnInOnePage;
        init();
    }

    public void init() {
        int sizeInOnePage = rowInOnePage * columnInOnePage;
        int pageCount = listAll.size() / sizeInOnePage;
        pageCount += listAll.size() % sizeInOnePage == 0 ? 0 : 1;
        if (mLists.size() > pageCount) {
            for (int i = mLists.size() - 1; i >= pageCount; i--) {
                mLists.remove(i);
            }
        }
        WrapContentGridView gv;
        int end;
        for (int i = 0; i < pageCount; i++) {
            final int pageIndex = i;
            if (i < mLists.size()) {
                gv = (WrapContentGridView) mLists.get(i);
            } else {
                gv = new WrapContentGridView(getContext());
                gv.setGravity(Gravity.CENTER);
                gv.setClickable(true);
                gv.setFocusable(true);
                mLists.add(gv);
            }
            gv.setNumColumns(columnInOnePage);
            end = Math.min((i + 1) * sizeInOnePage, listAll.size());
            gv.setAdapter(gridViewPagerDataAdapter.getGridViewAdapter(listAll.subList(i * sizeInOnePage, end), i));
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    gridViewPagerDataAdapter.onItemClick(arg0, arg1, arg2, arg3, pageIndex);
                }
            });
        }
        adapter = new GridViewPagerAdapter(getContext(), mLists);
        setAdapter(adapter);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        for (int i = 0; mLists != null && i < mLists.size(); i++) {
            View child = mLists.get(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height)
                height = h;
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height + getPaddingBottom() + getPaddingTop(), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (this.bg != null) {
            int width = this.bg.getWidth();
            int height = this.bg.getHeight();
            int count = getAdapter().getCount();
            int x = getScrollX();
            //子View中背景图片需要显示的宽度，放大背景图或缩小背景图。
            int n = height * getWidth() / getHeight();
            //(width - n) / (count - 1)表示除去显示第一个ViewPager页面用去的背景宽度，剩余的ViewPager需要显示的背景图片的宽度。
            //getWidth()等于ViewPager一个页面的宽度，即手机屏幕宽度。在该计算中可以理解为滑动一个ViewPager页面需要滑动的像素值。
            //((width - n) / (count - 1)) / getWidth()也就表示ViewPager滑动一个像素时，背景图片滑动的宽度。
            //x * ((width - n) / (count - 1)) / getWidth()也就表示ViewPager滑动x个像素时，背景图片滑动的宽度。
            //背景图片滑动的宽度的宽度可以理解为背景图片滑动到达的位置。
            int w = (x + getWidth()) * ((width - n) / (count - 1)) / getWidth();

            canvas.drawBitmap(this.bg, new Rect(w, 0, n + w, height), new Rect(x, 0, x + getWidth(), getHeight()), this.b);
        }
        super.dispatchDraw(canvas);
    }

}
