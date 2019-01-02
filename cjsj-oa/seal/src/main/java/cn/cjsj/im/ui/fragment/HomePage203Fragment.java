package cn.cjsj.im.ui.fragment;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import cn.cjsj.im.R;

/**
 * Created by LuoYang on 2018/12/27 15:40
 */
public class HomePage203Fragment extends Fragment {

    private Typeface mTypeface;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page_203,container,false);
        ButterKnife.bind(this,view);
        mTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/STSongti-SC-Bold-02.ttf");

        return view;
    }


}
