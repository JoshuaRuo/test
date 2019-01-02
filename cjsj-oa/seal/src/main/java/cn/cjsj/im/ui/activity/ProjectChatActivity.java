package cn.cjsj.im.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.ProjectChatBean;
import cn.cjsj.im.ui.adapter.ProjectChatAdapter;

/**
 * Created by LuoYang on 2018/8/8 11:31
 * 项目讨论
 */
public class ProjectChatActivity extends BaseActivity {

    @Bind(R.id.project_chat_recycler)
    RecyclerView mRv;

    private ProjectChatAdapter mAdapter;

    private List<ProjectChatBean> mList;

    private static final String[] NAME = {"张三", "李四", "王五", "赵大"};
    private static final String[] CONTENT = {"哈哈哈哈哈", "呵呵呵呵呵呵呵", "咯咯咯咯咯咯咯", "啧啧啧啧啧啧啧啧啧啧"};
    private static final int[] TYPE = {0, 1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_chat);
        ButterKnife.bind(this);
        setTitle("项目聊天");

        initData();
        initView();

        try {
            if (mList != null) {
                mRv.scrollToPosition(mList.size() - 1);
            }
        }catch (IndexOutOfBoundsException indexException){
            indexException.printStackTrace();
        }
    }

    private void initView() {
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ProjectChatAdapter(this, mList);
        mRv.setAdapter(mAdapter);
    }

    private void initData() {
        mList = new ArrayList<>();
        ProjectChatBean bean;
        Random random = new Random();
        for (int i = 0; i < 15; i++) {
            bean = new ProjectChatBean();
            bean.setName(NAME[random.nextInt(4)]);
            bean.setMessage(CONTENT[random.nextInt(4)]);
            bean.setType(TYPE[random.nextInt(2)]);
            mList.add(bean);
        }

    }

}
