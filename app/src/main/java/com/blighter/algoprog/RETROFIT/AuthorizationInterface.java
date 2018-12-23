package com.blighter.algoprog.RETROFIT;

import com.blighter.algoprog.POJO.Cookies;
import com.blighter.algoprog.POJO.UserData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthorizationInterface {
    @POST("login")
    Call<Cookies> getCookies(@Body UserData userData);
}
