package com.scsa.memo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scsa.memo.databinding.ActivityMemoInfoBinding;

import java.util.ArrayList;
import java.util.List;

public class MemoInfoActivity extends AppCompatActivity {

    private ActivityMemoInfoBinding binding;
    private List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMemoInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // TODO 1. 데이터 준비
        for(int i=1; i<=30; i++) {
            list.add("메모앱 만들기" + i + "2025-12-" + (2+i));

        }
        // TODO 2. RecyclerView에 LayoutManager 설정 (ListView처럼 수직 스크롤)
        //ListView와 달리, RecyclerView는 항목을 어떻게 배치할지를 LayoutManage에 위임합니다.
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // TODO 3. RecyclerView Adapter 설정
        MemoRecyclerViewAdapter myAdapter = new MemoRecyclerViewAdapter(list);
        binding.recyclerView.setAdapter(myAdapter);


        binding.back.setOnClickListener(v -> {
            finish();
        });
    }
}
class MemoRecyclerViewAdapter extends RecyclerView.Adapter<MemoRecyclerViewAdapter.MyViewHolder> {

    private final List<String> list;

    // 생성자
    MemoRecyclerViewAdapter(List<String> list) {
        this.list = list;
    }

    /**
     * ViewHolder 클래스: 항목 뷰의 구성 요소(위젯) 참조를 저장하여 뷰 재활용을 돕습니다.
     * ItemRowBinding을 사용하므로, ViewHolder는 Binding 객체 자체를 관리하도록 합니다.
     */
    static class MyViewHolder extends RecyclerView.ViewHolder {
        // View Binding로  findViewById() 호출을 없앱니다.
        com.scsa.memo.databinding.ListItemBinding listItemBinding;

        MyViewHolder(com.scsa.memo.databinding.ListItemBinding listItemBinding) {
            super(listItemBinding.getRoot());
            this.listItemBinding = listItemBinding;
        }
    }



    /**
     * 이 메서드는 재활용할 요소뷰가 없을 때만 호출됩니다.
     * 새로운 요소뷰를 생성하고, 요소뷰를 갖는 ViewHolder를 생성(inflate)하여 반환합니다
     * 이후, onBindViewHolder()가 자동호출됩니다
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // BaseAdapter의 getView()와 유사합니다.
        // View Binding을 사용하여 새로운 항목 뷰를 생성합니다.
        com.scsa.memo.databinding.ListItemBinding listItemBinding = com.scsa.memo.databinding.ListItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false // RecyclerView에서도 즉시 연결하지 않음
        );
        return new MyViewHolder(listItemBinding);
    }

    /**
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // ViewHolder에 저장된 Binding 객체를 사용하여 데이터 설정
        holder.listItemBinding.tvTitle.setText(position + 1 + "번째 메모");
        holder.listItemBinding.tvContent.setText(list.get(position)); //list를 Adapter의 데이터로 사용
    }

    /**
     * 데이터 항목의 총 개수를 반환합니다.
     */
    @Override
    public int getItemCount() {
        return list.size();
    }
}