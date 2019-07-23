package com.blighter.algoprog.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

public class TaskListsFragment extends Fragment {
    private CompositeDisposable disposables;


    public static CompositeDisposable getTasksList(String id, Context context) {
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
                        Materials[] materials = materialsInTaskList.getMaterials();
                        ArrayList<String> titles = new ArrayList<String>();
                        ArrayList<String> ids = new ArrayList<String>();
                        for (int i = 0; i < materials.length; i++) {
                            titles.add(i, materials[i].getTitle());
                            ids.add(i, materials[i].get_id());
                        }
                        final ArrayAdapter<String> adapter;
                        ListView listView = ((FragmentActivity) context).findViewById(R.id.lv_for_tasks);
                        adapter = new ArrayAdapter<String>(context,
                                R.layout.list_item, R.id.tw_list_item, titles);
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener((adapterView, view, i, l) -> {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("idForTask", ids.get(i));
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
        disposables = getTasksList(id, getContext());
        return view;
    }

    @Override
    public void onDestroyView() {
        if (disposables != null) {
            disposables.dispose();
        }
        super.onDestroyView();
    }
}
