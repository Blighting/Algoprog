package com.blighter.algoprog.network

import retrofit2.Call
import retrofit2.http.*

interface AuthorizationInterface {
    @POST("login")
    fun getCookies(@Body userData: UserData?): Call<Cookies?>?
}

interface BestSolutionsInterface {
    @GET("bestSubmits/{task}")
    fun getBestSolutions(@Header("Cookie") cookies: String?, @Path("task") taskId: String?): Call<List<BestSolution?>?>
}

interface MeInterface {
    @GET("me")
    fun getMeInfo(@Header("Cookie") cookies: String?): Call<Me?>?
}

interface MyUserInterface {
    @GET("myUser")
    fun getMyUserInfo(@Header("Cookie") cookies: String?): Call<MyUser?>?
}

interface SolutionsInterface {
    @GET("submits/{id}/{task}")
    fun getSolutions(@Header("Cookie") cookies: String?, @Path("id") userId: String?, @Path("task") taskId: String?): Call<List<Solution?>?>
}

interface TaskInterface {
    @GET("material/{id}")
    fun getTask(@Path("id") id: String?): Call<Task?>
}

interface TaskListInterface {
    @GET("material/{id}")
    fun getTasks(@Path("id") id: String?): Call<MaterialsInTaskList?>
}
