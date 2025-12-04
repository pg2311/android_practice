package com.example.c_menu;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;


public class MenuJavaActivity extends AppCompatActivity {
    MaterialToolbar toolbar;
    private static final int GROUP_ID = 0;

    //Menu.FIRST : 첫 번째 메뉴 항목에 부여되는 고유한 ID입니다
    private static final int ITEM_INSERT = Menu.FIRST;
    private static final int ITEM_DELETE = Menu.FIRST + 1;

    private static final int ITEM_BUTTON = Menu.FIRST + 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_java);
        toolbar = findViewById(R.id.toolbar);
        // 툴바 뷰 참조
        toolbar = findViewById(R.id.toolbar);
        // 툴바 설정
        setSupportActionBar(toolbar);
        // toolbar 뒤로가기 버튼 만들기 (XML로는 해결못합니다)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // sub title
        getSupportActionBar().setTitle("메인 타이틀");
        getSupportActionBar().setSubtitle("서브 타이틀");


        Button btn = findViewById(R.id.btn);
        //context menu 필수
        registerForContextMenu(btn);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 메인메뉴 추가
        int groupId = GROUP_ID; //groupId는 xml로 <group>과 같은 의미입니다
        int itemId = ITEM_INSERT; //itemId는 item의 고유값을 부여합니다
        int order = 0;//order 값은 이 툴바와 오버플로우 메뉴에 표시되는 순서를 결정합니다.order 값이 작은 항목이 툴바의 왼쪽(시작)에 가까이 배치, 오버플로우 메뉴에서 위에 배치됩니다
        String title = "추가하기";
        menu.add(groupId, itemId, order,title)
                .setIcon(android.R.drawable.ic_menu_add)
                .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(GROUP_ID, ITEM_DELETE, 0, "삭제하기");

        // 서브메뉴 추가
        String subMenuTitle = "서브메뉴";
        SubMenu subMenu = menu.addSubMenu(subMenuTitle);
        subMenu.add("메뉴1");
        subMenu.add("메뉴2");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case ITEM_INSERT:
                Toast.makeText(this, "첫번째 항목 선택", Toast.LENGTH_SHORT).show();
                break;

            case ITEM_DELETE:
                Toast.makeText(this, "두번째 항목 선택", Toast.LENGTH_SHORT).show();
                break;

            case android.R.id.home:  // Toolbar의 뒤로가기를 눌렀을 때 동작
                finish();
                return true;
        }

        Toast.makeText(this, "메뉴 이름: " + item.getTitle(), Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }

    // registerForContextMenu 메서드로 등록된 뷰를 Long Press 하면 실행되는 Callback 메서드
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(GROUP_ID, ITEM_BUTTON, 0, "버튼 메뉴");

        // 서브메뉴 추가
        SubMenu subMenu = menu.addSubMenu("버튼 서브메뉴");
        subMenu.add("메뉴1");
        subMenu.add("메뉴2");
    }

    /**
     *
     * @param item The context menu item that was selected.
     * @return
     */
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        Toast.makeText(this, "Context 항목 선택", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Context 메뉴 이름: " + item.getTitle(), Toast.LENGTH_SHORT).show();
        return super.onContextItemSelected(item);
    }
}