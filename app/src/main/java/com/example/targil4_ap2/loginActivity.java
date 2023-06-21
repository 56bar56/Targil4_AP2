package com.example.targil4_ap2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.targil4_ap2.api.UsersApiToken;
import com.example.targil4_ap2.api.WebServiceAPI;
import com.example.targil4_ap2.items.UserToGet;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class loginActivity extends AppCompatActivity {
    Button loginBtn;
    TextView toRegister;
    private EditText editLoginUsername;
    private EditText editLoginPassword;
    private AppDB db;
    private PostDao postDao;
    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;
    private UsersApiToken user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginBtn = findViewById(R.id.logInBtn);
        toRegister = findViewById(R.id.noAccount);
        editLoginUsername = findViewById(R.id.LoginUsername);
        editLoginPassword = findViewById(R.id.LoginPassword);
        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "PostsDB").allowMainThreadQueries().build();
        postDao = db.postDao();
        user = new UsersApiToken(db, postDao);
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editLoginUsername.getText().toString();
                String password = editLoginPassword.getText().toString();
                if (username.isEmpty() || password.isEmpty()) {
                    //is empty
                } else {
                    Callback<ResponseBody> callbackForGetUserInfo = new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                if (!response.isSuccessful()) {
                                    Toast.makeText(loginActivity.this, "Incorrect username and/or password", Toast.LENGTH_SHORT).show();
                                } else {
                                    String token = response.body().string();
                                    String authorizationHeader = "Bearer " + token;
                                    String funcUserName = username;
                                    Call<UserToGet> call2 = webServiceAPI.getUser(authorizationHeader, funcUserName);
                                    call2.enqueue(new Callback<UserToGet>() {
                                        @Override
                                        public void onResponse(Call<UserToGet> call2, Response<UserToGet> response2) {
                                            UserToGet serverReturn = response2.body();
                                            Intent intent = new Intent(getApplicationContext(), contacts_pageActivity.class);
                                            intent.putExtra("username", serverReturn.getUsername());
                                            intent.putExtra("displayName", serverReturn.getDisplayName());
                                            intent.putExtra("profilePic", serverReturn.getProfilePic());
                                            intent.putExtra("password", password);

                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onFailure(Call<UserToGet> call2, Throwable t) {
                                            Toast.makeText(loginActivity.this, "problem with connecting to the server", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(loginActivity.this, "problem with connecting to the server", Toast.LENGTH_SHORT).show();
                        }
                    };
                    user.getUser(username, password, callbackForGetUserInfo);
                }
            }
        });
        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), registerPage.class));
            }
        });
    }
}