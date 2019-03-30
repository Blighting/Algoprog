package com.blighter.algoprog.RETROFIT;

import com.blighter.algoprog.POJO.MaterialsInTaskList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TaskListInterface {
    @GET("{id}")
    Call<MaterialsInTaskList> getTasks(@Path("id") String id);
}
