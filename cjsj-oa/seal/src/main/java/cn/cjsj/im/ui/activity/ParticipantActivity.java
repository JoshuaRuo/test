package cn.cjsj.im.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.cjsj.im.R;
import cn.cjsj.im.ui.adapter.ParticipantAdapter;

/**
 * Created by LuoYang on 2018/8/8 09:55
 * 参与人列表
 */
public class ParticipantActivity extends BaseActivity {

    @Bind(R.id.participant_recycler)
    RecyclerView mRv;

    private ParticipantAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant);
        ButterKnife.bind(this);
        setTitle("参与人");


        mRv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ParticipantAdapter(mRv);
        mRv.setAdapter(mAdapter);

    }


}
