package cn.cjsj.im.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.cjsj.im.R;

/**
 * Created by LuoYang on 2017/9/30.
 * 我的审批
 */

public class ApprovalActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.approval_webview)
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.approval_activity);

        ButterKnife.bind(this);

        setTitle("我的审批");
    }


    @Override
    public void onClick(View v) {

    }
}
