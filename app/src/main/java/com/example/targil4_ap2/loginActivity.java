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
import com.example.targil4_ap2.items.Contact;
import com.example.targil4_ap2.items.MessageToGet;
import com.example.targil4_ap2.items.UserToGet;

import java.io.IOException;
import java.util.List;

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
        setContentView(R.layout.activity_login_page);
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
                                    Toast.makeText(loginActivity.this, "Incorrect username and/or password", Toast.LENGTH_LONG).show();
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
                                            //now we need to get the information out of the server

                                            //////////////////////////////////////////////////////
                                            dbInfo(intent, username, password);
                                            //startActivity(intent);
                                        }

                                        @Override
                                        public void onFailure(Call<UserToGet> call2, Throwable t) {
                                            Toast.makeText(loginActivity.this, "problem with connecting to the server", Toast.LENGTH_LONG).show();

                                        }
                                    });
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(loginActivity.this, "problem with connecting to the server", Toast.LENGTH_LONG).show();
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

    public void dbInfo(Intent intent, String username, String password) {
        Callback<ResponseBody> callbackForGetUserChatsInfo = new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (!response.isSuccessful()) {
                        Toast.makeText(loginActivity.this, "Incorrect username and/or password", Toast.LENGTH_LONG).show();
                    } else {
                        String token = response.body().string();
                        String authorizationHeader = "Bearer " + token;
                        Call<List<Contact>> call2 = webServiceAPI.getChats(authorizationHeader);
                        call2.enqueue(new Callback<List<Contact>>() {
                            @Override
                            public void onResponse(Call<List<Contact>> call2, Response<List<Contact>> response2) {
                                postDao.deleteAll();
                                List<Contact> serverReturn = response2.body();
                                //creat a dcObject for the inseration
                                List<DbObject> existingData = postDao.index();  // Retrieve existing data from the database
                                for (Contact newData : serverReturn) {
                                    boolean found = false;

                                    for (DbObject existingRecord : existingData) {
                                        if (newData.getUser().getUsername().equals(existingRecord.getContactName().getUser().getUsername())) {  // Compare using unique identifier
                                            found = true;
                                            break;
                                        }
                                    }

                                    if (!found) {
                                        // Insert new record
                                        DbObject newObj = new DbObject(newData, null);
                                        postDao.insert(newObj);
                                    }
                                }
                                List<DbObject> check = postDao.index();  // Retrieve existing data from the database
                                getAllMessagesForLogin(username,password);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(Call<List<Contact>> call2, Throwable t) {
                                Toast.makeText(loginActivity.this, "problem with connecting to the server", Toast.LENGTH_LONG).show();

                            }
                        });
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(loginActivity.this, "problem with connecting to the server", Toast.LENGTH_LONG).show();
            }
        };
        user.getChats(username, password, callbackForGetUserChatsInfo);
    }


    public void getAllMessagesForLogin(String userName, String password) {
        Callback<ResponseBody> getAllMessagesCallback =new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String token = response.body().string();
                    String authorizationHeader = "Bearer " + token;
                    List<DbObject> listCon= postDao.index();
                    for (DbObject contactDb:listCon) {
                        Call<List<MessageToGet>> call2 = webServiceAPI.getMessages(authorizationHeader, contactDb.getContactName().getId());
                        call2.enqueue(new Callback<List<MessageToGet>>() {
                            @Override
                            public void onResponse(Call<List<MessageToGet>> call2, Response<List<MessageToGet>> response2) {
                                List<MessageToGet> serverReturn = response2.body();
                                contactDb.setMsgList(serverReturn);
                                postDao.update(contactDb);
                            }

                            @Override
                            public void onFailure(Call<List<MessageToGet>> call2, Throwable t) {
                                Toast.makeText(loginActivity.this, "problem with connecting to the server", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(loginActivity.this, "problem with connecting to the server", Toast.LENGTH_LONG).show();
            }
        };
        user.getMessages(userName,password,getAllMessagesCallback);
    }
}

