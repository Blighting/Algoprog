package com.blighter.algoprog.retrofit;

import com.blighter.algoprog.pojo.Task;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TaskInterface {
    @GET("{id}")
    Call<Task> getTask(@Path("id") String id);
}
