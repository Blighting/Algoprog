package com.blighter.algoprog.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.blighter.algoprog.POJO.Materials;
import com.blighter.algoprog.R;

import java.util.ArrayList;

import static com.blighter.algoprog.API.ApiMethods.getTasksList;

public class TaskListsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        String id = null;
        Bundle bundle = getArguments();
        if (bundle != null) {
            id = bundle.getString("idForTaskList");
        }
        ArrayList<String> titles = new ArrayList<String>();
        AsyncTask asyncTask = new AsyncTask(id);
        asyncTask.execute();
        return view;
    }

    private class AsyncTask extends android.os.AsyncTask<Void, Void, ArrayList<String>> {
        String id;

        public AsyncTask(String id) {
            this.id = id;
        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            Materials[] materials = getTasksList(id);
            ArrayList<String> titles = new ArrayList<String>();
            for (int i = 0; i<materials.length; i++) {
                titles.add(i,materials[i].getTitle());
            }
            titles.add(materials.length,"");
            return titles;
        }

        @Override
        protected void onPostExecute(ArrayList<String> titles) {
            final ArrayAdapter<String> adapter;
            ListView listView = (ListView) getActivity().findViewById(R.id.lv_for_tasks);
            adapter = new ArrayAdapter<String>(getContext(),
                    R.layout.list_item, titles);
            listView.setAdapter(adapter);
            super.onPostExecute(titles);
        }
    }
}
