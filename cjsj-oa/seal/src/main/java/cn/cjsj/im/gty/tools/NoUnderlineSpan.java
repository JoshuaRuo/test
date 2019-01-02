package cn.cjsj.im.gty.tools;

import android.text.TextPaint;
import android.text.style.UnderlineSpan;

/**
 * Created by LuoYang on 2018/3/29.
 */

public class NoUnderlineSpan extends UnderlineSpan {
    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(ds.linkColor);
        ds.setUnderlineText(false);
    }
}
