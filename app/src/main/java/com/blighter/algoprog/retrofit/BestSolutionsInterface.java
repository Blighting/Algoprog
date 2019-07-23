package com.blighter.algoprog.retrofit;

import com.blighter.algoprog.pojo.BestSolution;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface BestSolutionsInterface {
    @GET("bestSubmits/{task}")
    Observable<List<BestSolution>> getBestSolutions(@Header("Cookie") String cookies, @Path("task") String taskId);
}
