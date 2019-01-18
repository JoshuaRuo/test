package cn.cjsj.im.ui.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.tools.NoUnderlineSpan;

/**
 * Created by LuoYang on 2018/3/29.
 * 关于我们
 */

public class AboutUsActivity extends BaseActivity{

    @Bind(R.id.mine_version_tv)
    TextView mVersion;

    @Bind(R.id.about_us_website_link_tv)
    TextView mLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
        setTitle("关于我们");
        mVersion.setText("V" + getVersion() + ".190118");


        NoUnderlineSpan mNoUnderlineSpan = new NoUnderlineSpan();
        if (mLink.getText() instanceof Spannable) {
            Spannable s = (Spannable) mLink.getText();
            s.setSpan(mNoUnderlineSpan, 0, s.length(), Spanned.SPAN_MARK_MARK);
        }
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            String version = info.versionName;
            return  version;
        } catch (Exception e) {
            e.printStackTrace();
            return "V1.3.23";
        }
    }
}
