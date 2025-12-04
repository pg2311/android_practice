package com.scsa.memo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public class MemoAdapter extends BaseAdapter {
    private List<String> list;

    public MemoAdapter(List<String> list) {
        this.list = list;
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

    /**
     * Get a View that displays the data at the specified position in the data set.
     *
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     *               부모 뷰그룹 여기서는 ListView를 의미합니다
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
       com.scsa.memo.databinding.ListItemBinding itemBinding = com.scsa.memo.databinding.ListItemBinding.inflate(
               layoutInflater,
               parent,
               false //즉시 parent에 추가하지 않는다
       );
        itemBinding.tvTitle.setText(position + "번째 메뉴");
        itemBinding.tvContent.setText(list.get(position));

        return itemBinding.getRoot();//요소 뷰의 루트뷰객체를 반환
    }
}
