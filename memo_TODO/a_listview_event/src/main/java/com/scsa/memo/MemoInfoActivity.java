package com.scsa.memo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;

import com.scsa.memo.databinding.ActivityMemoInfoBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MemoInfoActivity extends AppCompatActivity {
    private static final String TAG = "MemoInfoActivity";
    private ActivityMemoInfoBinding binding;
    private List<String> list1 = new ArrayList<>();
    private List<String> list2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMemoInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        list1.add("가");
        list1.add("나");
        list1.add("다");

        list2 = Arrays.asList("A", "B", "C");

//        TODO 1. list1데이터를 MemoAdapter에 등록하고 listView1에 Adapt합니다
        MemoAdapter memoAdapter = new MemoAdapter(list1);
        binding.listView1.setAdapter(memoAdapter);

//        TODO 2. listView1의 각 요소를 클릭했을때 요소데이터를 Log로 출력합니다
//         ** 콜백 패턴 **
//         ListView 내에서 항목 클릭이라는 특정 이벤트가 발생하면,
//         ListView는 자신이 가진 OnItemClickListener의 onItemClick 메서드를 호출합니다.
//         즉, 안드로이드 프레임워크가 개발자가 정의한 코드를 '역으로 호출'하는 것입니다.
        binding.listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 이 position이 클릭된 아이템의 인덱스입니다.
                Log.i(TAG + " listView1", "onItemClick position=" + position + ", 요소데이터=" + list1.get(position));
            }
        });

//       JavaScript의 기본 이벤트 흐름은 이벤트가 자식에서 부모로 버블링되는 것이 기본이며, 개발자가 stopPropagation을 사용하지 않는 한 이벤트가 상위로 전파됩니다.
//         안드로이드의 이벤트 흐름은 자식 뷰가 이벤트를 잡아서 소모하여 상위 전파를 막는 것이 기본 동작입니다.
//         listView2의 각 요소뷰에는 삭제버튼, 이미지뷰등의 자식요소가 있습니다.

//         요소를 클릭했을 때 자식인 삭제버튼이나 이미지뷰가 클릭이벤트핸들러를 갖고 있다면 자식이 이벤트를 잡아 처리하고 소모하여 부모인 요소뷰에게 이벤트 전파를 안합니다. 즉 요소뷰의 onItemClick()는 호출되지 않습니다.
//        1. 자식에서 부모로 이벤트 전파는 방법 : 개별 요소뷰의 루트(list_item_with_button.xml의 Root)에 android:descendantFocusability="blocksDescendants"를 추가합니다

//        2. 자식뷰에서의 해야할 이벤트처리작업과
//        부모에서의 해야할 이벤트처리가 같다면(대표예: 자식인 이미지뷰를 클릭해면 상세화면으로 이동, 부모인 요소뷰를 클릭해도 상세화면으로 이동해야하는 경우) 아래 작업으로 해결합니다
//         2-1) Activity에 요소뷰가 클릭되었을 때 할 일을 갖는 콜백메서드 만들기
//         2-2) MemoButtonAdapter에서 이미지가 클릭되었을 때 Activity의 콜백메서드 호출하기

//         TODO 3. list2데이터를 MemoButtonAdapter에 등록하고 listView2에 Adapt합니다
        MemoButtonAdapter memoButtonAdapter;
        memoButtonAdapter = new MemoButtonAdapter(list2, this);

        binding.listView2.setAdapter(memoButtonAdapter);

//        TODO 4. listView2의 각 요소를 클릭했을때 메모 상세정보보기화면으로 이동 작업(지금은 로그만 출력)을 합니다
        binding.listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.i(TAG, "상세보기용 화면으로 이동작업 position=" + position + ", 요소데이터=" + list2.get(position));
                customItemClick(position);
            }
        });

        binding.back.setOnClickListener(v -> {
            finish();
        });
    }

    /**
     * 사용자 정의 콜백 메서드입니다. 이메서드는 MemoButtonAdapter에서 호출합니다
     * @param position
     */
    public void customItemClick(int position) {
        Log.i(TAG, "상세보기용 콜백메서드 position=" + position + ", 요소데이터=" + list2.get(position));
    }
}