package com.teresa.joke;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import java.util.List;

/**
 * Created by Administrator on 15-11-16.
 */
public class MyFragmentAdapter extends FragmentPagerAdapter{
    private List<Fragment> fragments;
private Context context;
    private List<String> tab_titles;
    private int[] tabImg;
    public MyFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public MyFragmentAdapter(FragmentManager fm, List<Fragment> fragments, Context context, List<String> tab_titles) {
        super(fm);
        this.fragments = fragments;
        this.context = context;
        this.tab_titles = tab_titles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return  tab_titles.get(position);
    }
}
