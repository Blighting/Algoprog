package com.blighter.algoprog.retrofit;

import com.blighter.algoprog.pojo.Cookies;
import com.blighter.algoprog.pojo.UserData;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthorizationInterface {
    @POST("login")
    Observable<Response<Cookies>> getCookies(@Body UserData userData);
}
