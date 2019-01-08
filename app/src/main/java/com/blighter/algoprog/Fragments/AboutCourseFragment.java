package com.blighter.algoprog.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blighter.algoprog.R;

import static com.blighter.algoprog.API.ApiMethods.setAboutCourse;

public class boutCourseFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_course, container, false);
        setAboutCourse(getContext(), getActivity());
        return view;
    }
}
