package com.example.android.f_viewpage2;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * ViewPager2와 TabLayout을 연결하고 탭의 제목을 설정합니다.
 */
public class MainActivity extends AppCompatActivity {
    //TabLayout: 탭을 표시하는 UI 구성 요소입니다.
    private TabLayout tabLayout;

    //ViewPager2: 여러 페이지를 스와이프하여 전환할 수 있도록 하는 컴포넌트입니다.
    private ViewPager2 viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);


//ViewPagerAdapter: ViewPager2의 어댑터로, 탭에 따라 다른 프래그먼트를 생성합니다.
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

//TabLayoutMediator: TabLayout과 ViewPager2를 연결하여 탭과 페이지를 동기화합니다
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("탭 1");
                        break;
                    case 1:
                        tab.setText("탭 2");
                        break;
                    case 2:
                        tab.setText("탭 3");
                        break;
                    case 3:
                        tab.setText("탭 4");
                        break;
                }
            }
        }).attach();


    }
}
