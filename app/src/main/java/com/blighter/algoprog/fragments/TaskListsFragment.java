package com.blighter.algoprog.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.blighter.algoprog.R;
import com.blighter.algoprog.pojo.Materials;

import java.util.ArrayList;

import static com.blighter.algoprog.api.ApiMethods.getTasksList;

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
        AsyncTask asyncTask = new AsyncTask(id);
        asyncTask.execute();
        return view;
    }
    private class AsyncTask extends android.os.AsyncTask<Void, Void, ArrayList<String>>{
        ArrayList<String> ids = new ArrayList<>();
        String id;

        public AsyncTask(String id) {
            this.id = id;
        }

        @Override
        protected ArrayList<String> doInBackground (Void...voids){
            Materials[] materials = getTasksList(id);
            ArrayList<String> titles = new ArrayList<>();
            for (int i = 0; i < materials.length; i++) {
                titles.add(i, materials[i].getTitle());
                ids.add(i, materials[i].get_id());
            }
            return titles;
        }

        @Override
        protected void onPostExecute ( final ArrayList<String> titles){
            final ArrayAdapter<String> adapter;
            ListView listView = (ListView) getActivity().findViewById(R.id.lv_for_tasks);
            adapter = new ArrayAdapter<String>(getContext(),
                    R.layout.list_item, R.id.tw_list_item, titles);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Bundle bundle = new Bundle();
                    bundle.putString("idForTask", ids.get(i));
                    TaskFragment taskFragment = new TaskFragment();
                    taskFragment.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_in_Main, taskFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
            super.onPostExecute(titles);
        }
    }
}
