package com.games.peter.project_live_football_tactics;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peter on 3/30/18.
 */

public class SectionPageAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments=new ArrayList<>();
    private List<String> fragmentsTitle=new ArrayList<>();

    public void addfragment(Fragment fragment,String title){
        fragments.add(fragment);
        fragmentsTitle.add(title);
    }
    public SectionPageAdapter(FragmentManager fm) {
        super(fm);
    }
    public CharSequence getPageTitle(int position){
        return fragmentsTitle.get(position);
    }
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
