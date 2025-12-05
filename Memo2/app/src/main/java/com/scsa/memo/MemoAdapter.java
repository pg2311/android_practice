package com.scsa.memo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.scsa.memo.databinding.ListItemBinding;


public class MemoAdapter extends BaseAdapter {

    private final MemoManager memoManager;

    public MemoAdapter(MemoManager m) {
        memoManager = m;
    }

    @Override
    public int getCount() {
        return memoManager.getList().size();
    }

    @Override
    public MemoDto getItem(int position) {
        return memoManager.getList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListItemBinding listItemBinding = ListItemBinding.inflate(layoutInflater, parent, false);

        MemoDto dto = getItem(position);
        listItemBinding.listItemTitle.setText(dto.getTitle());
        listItemBinding.listItemRegDate.setText(Util.getFormattedDate(dto.getRegDate()));
        return listItemBinding.getRoot();
    }

}
