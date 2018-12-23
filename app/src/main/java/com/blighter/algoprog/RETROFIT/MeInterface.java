package com.blighter.algoprog.RETROFIT;

import com.blighter.algoprog.POJO.me;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface MeInterface {
    @GET("me")
    Call<me> getMeInfo(@Header("Cookie") String cookies);
}
