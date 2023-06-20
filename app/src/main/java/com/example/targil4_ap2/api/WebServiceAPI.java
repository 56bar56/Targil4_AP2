package com.example.targil4_ap2.api;

import com.example.targil4_ap2.items.Contact;
import com.example.targil4_ap2.items.ContactForCreate;
import com.example.targil4_ap2.items.AddNewContact;
import com.example.targil4_ap2.items.UserToGet;
import com.example.targil4_ap2.items.UserToPost;
import com.example.targil4_ap2.items.messageContent;
import com.example.targil4_ap2.loginInfo;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WebServiceAPI {
    @POST("Tokens")
    Call<ResponseBody> getTokenFromServer(@Body loginInfo info);

    @POST("Users")
    Call<ResponseBody> postUser(@Header("authorization") String authorization, @Body UserToPost user);

    @GET("Users/{id}")
    Call<List<UserToGet>> getUser(@Header("authorization") String authorization, @Path("id") String id);

    @GET("Chats")
    Call<List<Contact>> getChats(@Header("authorization") String authorization);

    @POST("Chats")
    Call<AddNewContact> postChat(@Header("authorization") String authorization, @Body ContactForCreate user);

    @POST("Chats/{id}/Messages")
    Call<ResponseBody> postMessage(@Header("authorization") String authorization, @Path("id") String id, @Body messageContent mes);
    @GET("Chats/{id}/Messages")
    Call<ResponseBody> getMessages(@Header("authorization") String authorization, @Path("id") String id, @Body messageContent mes);


}

