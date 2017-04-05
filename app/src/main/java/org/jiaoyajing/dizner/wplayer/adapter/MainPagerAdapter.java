package org.jiaoyajing.dizner.wplayer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dizner on 2017/2/26.
 */
public class MainPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> list;
    private List<String> titleList;

    public MainPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
        titleList = new ArrayList<>();
        titleList.add("我的");
        titleList.add("音乐馆");
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
