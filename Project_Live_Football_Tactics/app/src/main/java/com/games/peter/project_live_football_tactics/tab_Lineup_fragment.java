package com.games.peter.project_live_football_tactics;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Peter on 19/4/2018.
 */

public class tab_Lineup_fragment extends Fragment implements TabLayout.OnTabSelectedListener{
    private SectionPageAdapter sectionPageAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    tab_home_lineup_fragment mtaTab_home_lineup_fragment;
    tab_away_lineup_fragment mtaTab_away_lineup_fragment;
    TextView tv_hometeam_name,tv_awayteam_name;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.tab_lineup_fragment,container,false);
        sectionPageAdapter=new SectionPageAdapter(getFragmentManager());
        viewPager=view.findViewById(R.id.container2);
        viewPager.setOffscreenPageLimit(2);
        setupViewPage(viewPager);
        tabLayout=view.findViewById(R.id.tabs2);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(this);
        tv_hometeam_name=view.findViewById(R.id.tv_match_fragment_home_team_name);
        tv_awayteam_name=view.findViewById(R.id.tv_match_fragment_away_team_name);
        Intent intent=getActivity().getIntent();
        if (intent.hasExtra("HOME_TEAM_NAME"))
            tv_hometeam_name.setText(intent.getStringExtra("HOME_TEAM_NAME"));
        if (intent.hasExtra("AWAY_TEAM_NAME"))
            tv_awayteam_name.setText(intent.getStringExtra("AWAY_TEAM_NAME"));
        setupTabIcons();
        return view;
    }

    private void setupViewPage(ViewPager viewPager){
        SectionPageAdapter sectionPageAdapter=new SectionPageAdapter(getFragmentManager());
        mtaTab_home_lineup_fragment=new tab_home_lineup_fragment();
        mtaTab_away_lineup_fragment=new tab_away_lineup_fragment();

        sectionPageAdapter.addfragment(mtaTab_home_lineup_fragment,"");
        sectionPageAdapter.addfragment(mtaTab_away_lineup_fragment,"");
        viewPager.setAdapter(sectionPageAdapter);
    }
    private void setupTabIcons() {
        int[] tabIcons = {
                R.drawable.circle,
                R.drawable.circle2,
        };
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
    }
    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


}
