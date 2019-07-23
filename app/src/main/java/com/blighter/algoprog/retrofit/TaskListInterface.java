package com.blighter.algoprog.retrofit;

import com.blighter.algoprog.pojo.MaterialsInTaskList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TaskListInterface {
    @GET("material/{id}")
    Observable<MaterialsInTaskList> getTasks(@Path("id") String id);
}
