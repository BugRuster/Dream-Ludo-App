package com.definiteautomation.dreamludo.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.definiteautomation.dreamludo.R;

import java.util.ArrayList;
import java.util.List;


public class MatchFragment extends Fragment {

    public View view;
    public TabLayout tabLayout;
    public ViewPager viewPager;
    public ViewPagerAdapter pagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_match, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(3);

        pagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        pagerAdapter.addFragment(new UpcomingFragment(),"UPCOMING");
        pagerAdapter.addFragment(new OngoingFragment(),"ONGOING");
        pagerAdapter.addFragment(new CompletedFragment(),"COMPLETED");

        viewPager.setAdapter(pagerAdapter);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String clickAction = bundle.getString("click_action","default");
            if (clickAction.equals("MainActivity")) {
                viewPager.setCurrentItem(1);
            }
            else {
                viewPager.setCurrentItem(0);
            }
        }

        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}