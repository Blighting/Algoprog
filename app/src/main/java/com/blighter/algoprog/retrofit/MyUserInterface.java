package com.blighter.algoprog.retrofit;

import com.blighter.algoprog.pojo.myUser;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface MyUserInterface {
    @GET("myUser")
    Call<myUser> getMyUserInfo(@Header("Cookie") String cookies);
}
