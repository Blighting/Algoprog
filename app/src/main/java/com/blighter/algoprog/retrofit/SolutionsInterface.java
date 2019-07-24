package com.blighter.algoprog.retrofit;

import com.blighter.algoprog.pojo.Solution;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface SolutionsInterface {
    @GET("submits/{id}/{task}")
    Observable<List<Solution>> getSolutions(@Header("Cookie") String cookies, @Path("id") String userId, @Path("task") String taskId);
}
