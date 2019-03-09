package com.blighter.algoprog.RETROFIT;

import com.blighter.algoprog.POJO.MaterialsInTaskList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ButtonsInTaskListInterface {
    @GET("material/{id}")
    Call<MaterialsInTaskList> getTasks(String id);
}
