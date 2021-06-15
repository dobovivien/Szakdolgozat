package com.example.koltsegvetes_tervezo.utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class BottomBarPagerAdapter extends FragmentPagerAdapter {

    public BottomBarPagerAdapter(@NonNull FragmentManager fm, int behaviour) {
        super (fm, behaviour);
    }

    private List<Fragment> fragments = new ArrayList<Fragment>();

    public void AddFragment (Fragment fragment) {
        fragments.add(fragment);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
        //TODO position nagyobb e mint a fragment mérete
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
