package com.blighter.algoprog.retrofit;

import com.blighter.algoprog.pojo.BestSolution;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface BestSolutionsInterface {
    @GET("{task}")
    Call<List<BestSolution>> getBestSolutions(@Header("Cookie") String cookies, @Path("task") String taskId);
}
