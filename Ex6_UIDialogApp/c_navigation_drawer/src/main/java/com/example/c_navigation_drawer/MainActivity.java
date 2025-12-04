package com.example.c_navigation_drawer;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 뷰 참조
        toolbar = findViewById(R.id.toolbar);
        // 툴바 설정
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);


        // 햄버거 아이콘(Toggle) 설정 및 연결
        //스크린 리더 사용자에게 드로어가 열렸을 때의 설명과"드로어가 닫혔을 때의 설명을 제공
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // 네비게이션 뷰 리스너 설정
        navigationView.setNavigationItemSelectedListener(this);

        // 초기 화면 설정 (홈 프래그먼트 로드)
        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.nav_home);
            replaceFragment(new HomeFragment(), "홈");
        }

        //뒤로 가기 버튼 처리: 드로어가 열려있으면 닫기
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // 뒤로 가기 버튼을 눌렀을 때 실행될 동작을 여기에 구현합니다.
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    // 1. 드로어가 열려있다면 닫기
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    // 2. 드로어가 닫혀있다면 Activity 종료 (기본 동작)
                    // 이 콜백의 enable=true이기 때문에 반드시 super.onBackPressed() 대신
                    // isEnabled = false로 변경 후 이 메서드를 호출해야 기본동작을 수행합니다.
                    setEnabled(false); // 콜백 비활성화
                    MainActivity.super.onBackPressed(); // Activity의 기본 뒤로가기 동작 호출
                }
            }
        });
    }

    /**
     * 메뉴 항목 클릭 이벤트 처리
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;
        String title = "";
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            selectedFragment = new HomeFragment();
            title = "홈";
        } else if (id == R.id.nav_gallery) {
            selectedFragment = new GalleryFragment();
            title = "갤러리";
        } else if (id == R.id.nav_slideshow) {
            selectedFragment = new SlideshowFragment();
            title = "슬라이드쇼";
        } else if (id == R.id.nav_settings) {
            selectedFragment = new SettingsFragment();
            title = "설정";
        }

        if (selectedFragment != null) {
            replaceFragment(selectedFragment, title);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    // 프래그먼트 교체 함수
    private void replaceFragment(Fragment fragment, String title) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();

        // 툴바 제목 업데이트
        toolbar.setTitle(title);
    }
}