package com.blighter.algoprog.retrofit;

import com.blighter.algoprog.pojo.Task;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TaskInterface {
    @GET("material/{id}")
    Observable<Task> getTask(@Path("id") String id);
}
