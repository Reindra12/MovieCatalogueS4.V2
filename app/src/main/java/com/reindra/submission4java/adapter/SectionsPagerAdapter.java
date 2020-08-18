package com.reindra.submission4java.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.reindra.submission4java.R;
import com.reindra.submission4java.fragment.favorite.FavMovieFragment;
import com.reindra.submission4java.fragment.favorite.FavTVFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
    }

    @StringRes
    private final int[] TAB_TITLES = new int[]{
            R.string.tab_text_1,
            R.string.tab_text_2,
    };

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new FavMovieFragment();
                break;
            case 1:
                fragment = new FavTVFragment();
                break;
            default:
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

}
