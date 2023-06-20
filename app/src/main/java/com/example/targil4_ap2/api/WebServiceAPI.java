package com.example.targil4_ap2.api;
import com.example.targil4_ap2.items.User;
import com.example.targil4_ap2.loginInfo;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WebServiceAPI {

    @GET("Users")
    Call<List<User>> getPosts();

    @POST("Tokens")
    Call<ResponseBody> getTokenFromServer(@Body loginInfo info);

    @GET("Users/{id}")
    Call<List<User>> getUser(@Path("id") String id);

    @POST("Users")
    Call<Void> createPost(@Body User user);

}

