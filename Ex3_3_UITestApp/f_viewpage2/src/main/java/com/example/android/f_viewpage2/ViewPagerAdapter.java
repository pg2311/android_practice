package com.example.android.f_viewpage2;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**
 * FragmentStateAdapter를 상속받아 탭에 따라 다른 프래그먼트를 반환합니다.
 */
public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @Override
    public int getItemCount() {
        return 4; // 탭의 개수
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FirstFragment(); // 첫 번째 탭의 프래그먼트
            case 1:
                return new SecondFragment();
            case 2:
                return new ThirdFragment(); // 세 번째 탭의 프래그먼트
            default:
                return new Fragment(); // 기본값 (실제로는 반환되지 않음)
        }
    }
}
