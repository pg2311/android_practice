package com.example.c_navigation_drawer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView textView = view.findViewById(R.id.fragment_text_view);
        if (textView != null) {
            // R.string.default_fragment_text는 "현재 화면: " 입니다.
            String text = getString(R.string.default_fragment_text) + "홈";
            textView.setText(text);
        }

        return view;
    }
}