package com.blighter.algoprog.retrofit;

import com.blighter.algoprog.pojo.Cookies;
import com.blighter.algoprog.pojo.UserData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthorizationInterface {
    @POST("login")
    Call<Cookies> getCookies(@Body UserData userData);
}
