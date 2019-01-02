package cn.cjsj.im.ui.viewholder;

import android.view.View;
import android.widget.TextView;

import cn.cjsj.im.R;
import cn.cjsj.im.gty.bean.ProjectChatBean;

/**
 * Created by LuoYang on 2018/8/8 16:25
 * 项目讨论收到Holder
 */
public class ProjectReceiveHolder extends BaseRecyclerHolder {
    private TextView mTime,mName,mContent;
    private View mView;
    public ProjectReceiveHolder(View itemView) {
        super(itemView);
        this.mView = itemView;
    }

    public void bindView(ProjectChatBean bean,int pos){
        mTime = mView.findViewById(R.id.receive_tvTime);
        mName = mView.findViewById(R.id.receive_tvName);
        mContent = mView.findViewById(R.id.receive_tvText);

        mTime.setVisibility(View.GONE);
        mName.setText(bean.getName());
        mContent.setText(bean.getMessage());
    }
}
