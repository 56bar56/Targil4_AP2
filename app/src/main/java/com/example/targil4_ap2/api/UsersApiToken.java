package com.example.targil4_ap2.api;

import com.example.targil4_ap2.MyApplication;
import com.example.targil4_ap2.R;
import com.example.targil4_ap2.items.Contact;
import com.example.targil4_ap2.items.ContactForCreate;
import com.example.targil4_ap2.items.AddNewContact;
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
        Call<List<Contact>> call = webServiceAPI.getChats(token);
        call.enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                List<Contact> serverReturn = response.body();
            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
                System.out.println("filed");
            }
        });
    }
    public void postChat(String token, String username) {
        Call<AddNewContact> call = webServiceAPI.postChat(token,new ContactForCreate(username));
        call.enqueue(new Callback<AddNewContact>() {
            @Override
            public void onResponse(Call<AddNewContact> call, Response<AddNewContact> response) {
                AddNewContact serverReturn = response.body();
            }

            @Override
            public void onFailure(Call<AddNewContact> call, Throwable t) {
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
