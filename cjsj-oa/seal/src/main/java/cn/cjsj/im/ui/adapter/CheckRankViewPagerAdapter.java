package cn.cjsj.im.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.cjsj.im.ui.fragment.EarlyArrvalRankFragment;
import cn.cjsj.im.ui.fragment.HardWorkRankFragment;

/**
 * Created by LuoYang on 2018/11/23 10:53
 * 考勤排行榜适配器
 */
public class CheckRankViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList = new ArrayList<>();
    public List<String> mTileList = new ArrayList<>();

    public CheckRankViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentList.add(new EarlyArrvalRankFragment());
        fragmentList.add(new HardWorkRankFragment());

        mTileList.add("早到榜");
        mTileList.add("勤奋榜");
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTileList.get(position);
    }
}
