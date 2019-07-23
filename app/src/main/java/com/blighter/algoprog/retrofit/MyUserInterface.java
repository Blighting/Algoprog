package com.blighter.algoprog.retrofit;

import com.blighter.algoprog.pojo.myUser;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface MyUserInterface {
    @GET("myUser")
    Observable<myUser> getMyUserInfo(@Header("Cookie") String cookies);
}
