package com.example.c_menu;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;


public class MenuXMLActivity extends AppCompatActivity {
    MaterialToolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_xml);
        // 툴바 뷰 참조
        toolbar = findViewById(R.id.toolbar);
        // 툴바 설정
        setSupportActionBar(toolbar);
    }

    /**
     *
     * @param menu The options menu in which you place your items.
     *
     * @return 메뉴구성이 성공되면 true를 반환
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_xml, menu);
        return super.onCreateOptionsMenu(menu); //return true;
    }


    /**
     * Menu 버튼을 눌렀을 때 실행되는 Callback 메서드
     * @param item The menu item that was selected.
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == R.id.item_first) {
            Toast.makeText(this, "첫번째 항목 선택", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.item_second) {
            Toast.makeText(this, "두번째 항목 선택", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.item_third) {
            Toast.makeText(this, "세번째 항목 선택", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.item_submenu_first) {
            Toast.makeText(this, "서브메뉴 첫번째 항목 선택", Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(this, "메뉴 이름: " + item.getTitle(), Toast.LENGTH_SHORT).show();

        return super.onOptionsItemSelected(item);
    }
}