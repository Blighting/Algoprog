package com.blighter.algoprog.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.blighter.algoprog.R;
import com.blighter.algoprog.pojo.Materials;
import com.blighter.algoprog.pojo.MaterialsInTaskList;
import com.blighter.algoprog.retrofit.Client;
import com.blighter.algoprog.retrofit.TaskListInterface;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

//Fragment class which responsible for TaskList
public class TaskListsFragment extends Fragment {
    private CompositeDisposable disposables;

    //getting contest tasks
    public static CompositeDisposable getTasksList(String id, Context context, TextView title) {
        TaskListInterface taskListInterface = Client.getClient().create(TaskListInterface.class);
        CompositeDisposable disposables = new CompositeDisposable();
        taskListInterface.getTasks(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MaterialsInTaskList>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onNext(MaterialsInTaskList materialsInTaskList) {
                        title.setText(materialsInTaskList.getTitle());
                        Materials[] materials = materialsInTaskList.getMaterials();
                        ArrayList<String> titles = new ArrayList<>();
                        ArrayList<String> ids = new ArrayList<>();
                        //building arrays with task's titles and ids
                        for (int i = 0; i < materials.length; i++) {
                            titles.add(i, materials[i].getTitle());
                            ids.add(i, materials[i].get_id());
                        }
                        ListView listView = ((FragmentActivity) context).findViewById(R.id.lv_for_tasks);
                        TaskAdapter adapter = new TaskAdapter(context, titles);
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener((adapterView, view, i, l) -> {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("id", ids.get(i));
                                    TaskFragment taskFragment = new TaskFragment();
                                    taskFragment.setArguments(bundle);
                                    FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.container_in_Main, taskFragment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                }
                        );
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return disposables;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        String id = null;
        Bundle bundle = getArguments();
        if (bundle != null) {
            id = bundle.getString("idForTaskList");
        }
        TextView textView = view.findViewById(R.id.tw_task_list_title);
        disposables = getTasksList(id, getContext(), textView);
        return view;
    }

    @Override
    public void onDestroyView() {
        if (disposables != null) {
            disposables.dispose();
        }
        super.onDestroyView();
    }

    //customAdapter for TaskList
    private static class TaskAdapter extends ArrayAdapter<String> {
        ArrayList<String> titles;
        Context context;

        TaskAdapter(@NonNull Context context1, ArrayList<String> titlesList) {
            super(context1, 0, titlesList);
            context = context1;
            titles = titlesList;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.list_item, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.tw_list_item);
            textView.setText(titles.get(position));
            return rowView;
        }
    }
}
