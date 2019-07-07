package com.blighter.algoprog.retrofit;

import com.blighter.algoprog.pojo.Solutions;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface SolutionsInterface {
    @GET("{task}")
    Call<Solutions> getSolutions(@Header("Cookie") String cookies, @Path("task") String taskId);
}
