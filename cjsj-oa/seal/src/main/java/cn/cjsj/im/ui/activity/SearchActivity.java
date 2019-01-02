package cn.cjsj.im.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.cjsj.im.R;
import cn.cjsj.im.server.widget.ClearWriteEditText;

/**
 * Created by LuoYang on 2018/8/3 09:38
 * 搜索
 */
public class SearchActivity extends BaseActivity {
    @Bind(R.id.project_search_et)
    ClearWriteEditText mSearchEt;

    @Bind(R.id.search_recyclerview)
    RecyclerView mRV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_search);
        ButterKnife.bind(this);
        setTitle("搜索");

    }
}
