package com.example.targil4_ap2.api;

import com.example.targil4_ap2.MyApplication;
import com.example.targil4_ap2.R;
import com.example.targil4_ap2.items.Chat;
import com.example.targil4_ap2.items.ChatForCreate;
import com.example.targil4_ap2.items.ContactToGet;
import com.example.targil4_ap2.items.UserToGet;
import com.example.targil4_ap2.items.UserToPost;
import com.example.targil4_ap2.items.messageContent;
import com.example.targil4_ap2.loginInfo;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsersApiToken {
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;

    public UsersApiToken() {
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void getToken(String username, String password) {
        Call<ResponseBody> call = webServiceAPI.getTokenFromServer(new loginInfo(username, password));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String token = response.body().string();
                    System.out.println(token);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("filed");
            }
        });
    }

    public String postUser(String token, UserToPost user) {
        Call<ResponseBody> call = webServiceAPI.postUser(token, user);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String serverReturn = response.body().string();
                    System.out.println(serverReturn);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("filed");
            }
        });
        return "";
    }

    public void getUser(String token, String username) {
        Call<List<UserToGet>> call = webServiceAPI.getUser(token, username);
        call.enqueue(new Callback<List<UserToGet>>() {
            @Override
            public void onResponse(Call<List<UserToGet>> call, Response<List<UserToGet>> response) {
                List<UserToGet> serverReturn = response.body();
            }

            @Override
            public void onFailure(Call<List<UserToGet>> call, Throwable t) {
                System.out.println("filed");
            }
        });
    }
    public void getChats(String token) {
        Call<List<Chat>> call = webServiceAPI.getChats(token);
        call.enqueue(new Callback<List<Chat>>() {
            @Override
            public void onResponse(Call<List<Chat>> call, Response<List<Chat>> response) {
                List<Chat> serverReturn = response.body();
            }

            @Override
            public void onFailure(Call<List<Chat>> call, Throwable t) {
                System.out.println("filed");
            }
        });
    }
    public void postChat(String token, String username) {
        Call<ContactToGet> call = webServiceAPI.postChat(token,new ChatForCreate(username));
        call.enqueue(new Callback<ContactToGet>() {
            @Override
            public void onResponse(Call<ContactToGet> call, Response<ContactToGet> response) {
                ContactToGet serverReturn = response.body();
            }

            @Override
            public void onFailure(Call<ContactToGet> call, Throwable t) {
                System.out.println("filed");
            }
        });
    }

    public void postMessage(String token, String id, String msg) {
        Call<ResponseBody> call = webServiceAPI.postMessage(token,id,new messageContent(msg));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String serverReturn = response.body().string();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("filed");
            }
        });
    }



}
