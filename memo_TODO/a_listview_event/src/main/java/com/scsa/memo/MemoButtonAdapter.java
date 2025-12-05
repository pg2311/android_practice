package com.scsa.memo;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.scsa.memo.databinding.ListItemWithButtonBinding;

import java.util.List;

public class MemoButtonAdapter extends BaseAdapter {
    private static final String TAG = "MemoButtonAdapter";
    private List<String> list;

    //TODO 1. 콜백메서드를 갖는 Activity를 갖습니다
    private MemoInfoActivity activity;

    public MemoButtonAdapter(List<String> list, MemoInfoActivity activity) {
        this.list = list;
        this.activity = activity;
    }
    /**
     * 데이터 항목의 총 개수를 반환
     * @return
     */

    @Override
    public int getCount() {
        return list.size();
    }


    /**
     * 주어진 위치에 있는 데이터를 반환합니다.
     * @param position Position of the item whose data we want within the adapter's
     * data set.
     * @return
     */

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListItemWithButtonBinding itemRowBinding = ListItemWithButtonBinding.inflate(
                layoutInflater,
                parent,
                false //즉시 parent에 추가하지 않는다
        );
        itemRowBinding.tvTitle.setText(position + "번째 메뉴");
        itemRowBinding.tvContent.setText(list.get(position));
        itemRowBinding.btDel.setOnClickListener(v->{
            Log.d("MyAdapter", "버튼 클릭! position: " + position + ", 데이터: " + itemRowBinding.tvTitle.getText());
        });
        itemRowBinding.imageView.setOnClickListener(v->{
            //TODO 2. 콜백메서드 호출하기
            activity.customItemClick(position);
        });

        return itemRowBinding.getRoot();//요소 뷰의 루트뷰객체를 반환
    }
}
