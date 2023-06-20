package com.example.targil4_ap2.api;

import com.example.targil4_ap2.MyApplication;
import com.example.targil4_ap2.R;
import com.example.targil4_ap2.items.User;
import com.example.targil4_ap2.loginInfo;

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
            Call<ResponseBody> call = webServiceAPI.getTokenFromServer(new loginInfo(username,password));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    System.out.println("hii2");
                    String token= response.body().toString();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    System.out.println("filed");
                }
            });
        }
    }
