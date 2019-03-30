package com.blighter.algoprog.RETROFIT;

import com.blighter.algoprog.POJO.myUser;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface MyUserInterface {
    @GET("myUser")
    Call<myUser> getMyUserInfo(@Header("Cookie") String cookies);
}
