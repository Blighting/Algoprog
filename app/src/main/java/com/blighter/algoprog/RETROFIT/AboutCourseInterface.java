package com.blighter.algoprog.RETROFIT;


import com.blighter.algoprog.POJO.Materials;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AboutCourseInterface {
    @GET("material/0")
    Call<Materials> getMaterials();
}
