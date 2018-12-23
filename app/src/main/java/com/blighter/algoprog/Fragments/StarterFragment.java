package com.blighter.algoprog.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.blighter.algoprog.R;

public class StarterFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_starting_menu, container, false);
        final Button pupil = (Button) view.findViewById(R.id.b_i_am_pupil);
        pupil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pupil.setBackgroundColor(Color.RED);
                pupil.setTextColor(Color.WHITE);
            }
        });
        return view;

    }
}
