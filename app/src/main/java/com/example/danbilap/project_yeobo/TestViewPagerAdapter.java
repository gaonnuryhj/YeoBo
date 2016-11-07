package com.example.danbilap.project_yeobo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class TestViewPagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<Fragment> fragmentArrayList;

    public TestViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragmentArrayList) {
        super(fm);
        this.fragmentArrayList = fragmentArrayList;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0 :
                return "공유하기";  // 후기
            case 1 :
                return "국가정보";  // 후기
            case 2 :
                return "긴급번호";  // 멘토 리스트
        }
        return "position "+position;
    }

    @Override
    public Fragment getItem(int position) {
        // 해당하는 page의 Fragment를 생성합니다.
        return fragmentArrayList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }

}