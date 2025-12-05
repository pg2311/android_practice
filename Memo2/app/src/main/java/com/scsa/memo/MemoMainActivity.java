package com.scsa.memo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.scsa.memo.databinding.ActivityMemoMainBinding;

import java.util.Arrays;
import java.util.List;

public class MemoMainActivity extends AppCompatActivity {
    private static final String TAG = "MemoMainActivity";
    private ActivityMemoMainBinding binding;
    private MemoManager memoManager;
    private MemoAdapter memoAdapter;

    MaterialToolbar toolbar;
    private static final int GROUP_ID = 0;
    private static final int ITEM_INSERT = Menu.FIRST;
    private static final int ITEM_DELETE = Menu.FIRST + 1;

    private static final List<MemoDto> dummy = Arrays.asList(new MemoDto[]{
            new MemoDto("부서회의", "부서회의입니다."),
            new MemoDto("개발미팅", "개발미팅입니다."),
            new MemoDto("휴가계획", "휴가계획입니다."),
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMemoMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("툴바");

        memoManager = MemoManager.getInstance();
        memoAdapter = new MemoAdapter(memoManager);
        dummy.forEach((dto) -> {
            memoManager.add(dto);
        });

        ListView listView = binding.listView;
        listView.setAdapter(memoAdapter);

        binding.floatingActionButton.setOnClickListener((v) -> {
            moveToEdit(-1);
        });

        binding.listView.setOnItemClickListener((parent, view, position, id) -> {
            moveToEdit(position);
        });


        registerForContextMenu(binding.listView);

        binding.listView.setOnContextClickListener((view) -> {
            Log.i(TAG, "!!");
            return true;
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(GROUP_ID, ITEM_DELETE, 0, "삭제");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case ITEM_DELETE:
                Log.i(TAG, "position: " + adapterContextMenuInfo.position);
                createDeleteDialog(adapterContextMenuInfo.position).show();
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(GROUP_ID, ITEM_INSERT, 0, "추가하기")
                .setIcon(R.drawable.baseline_add_24)
                .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case ITEM_INSERT:
                moveToEdit(-1);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void moveToEdit(int position) {
        Intent intent = new Intent(MemoMainActivity.this, MemoEditActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        memoAdapter.notifyDataSetChanged();
        Log.i(TAG, "onResume");
    }

    private Dialog createDeleteDialog(int position) {
        return new AlertDialog.Builder(MemoMainActivity.this)
                .setIcon(R.drawable.baseline_warning_24)
                .setTitle("삭제 확인")
                .setMessage("정말로 삭제하시겠습니까?")
                .setPositiveButton("예", (dialog, which) -> {
                    memoManager.remove(position);
                    memoAdapter.notifyDataSetChanged();
                })
                .setNegativeButton("아니오", (dialog, which) -> {
                })
                .create();
    }
}