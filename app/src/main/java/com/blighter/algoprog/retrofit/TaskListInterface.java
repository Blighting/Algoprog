package com.blighter.algoprog.retrofit;

import com.blighter.algoprog.pojo.MaterialsInTaskList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TaskListInterface {
    @GET("{id}")
    Call<MaterialsInTaskList> getTasks(@Path("id") String id);
}
