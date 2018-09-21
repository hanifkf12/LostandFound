package com.example.hanifkf.lostandfound.Utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hanifkf on 24/02/2018.
 */

public class SectionPagesAdapter extends FragmentPagerAdapter{

    private static final String TAG = "SectionPagesAdapter";
    private final List<Fragment> fragmentList=new ArrayList<>();

    public SectionPagesAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public void addFragment(Fragment fragment){
        fragmentList.add(fragment);
    }
}
