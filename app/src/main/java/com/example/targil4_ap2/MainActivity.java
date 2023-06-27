package com.example.targil4_ap2;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.targil4_ap2.api.UsersApiToken;
import com.example.targil4_ap2.api.WebServiceAPI;
import com.example.targil4_ap2.items.Contact;
import com.example.targil4_ap2.items.MessageToGet;
import com.example.targil4_ap2.items.logInSave;


import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    Button loginBtn, registerBtn;
    private UsersApiToken user;
    private AppDB db;
    private AppDB db2;
    private PostDao postDao;
    private LogInSaveDao logInSaveDao;
    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "PostsDB").allowMainThreadQueries().build();
        db2=Room.databaseBuilder(getApplicationContext(),AppDB.class,"sveLogin").allowMainThreadQueries().build();
        postDao = db.postDao();
        logInSaveDao=db2.logInSaveDao();
        retrofit = new Retrofit.Builder()
                .baseUrl(globalVars.server)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
        List<logInSave> userLoged=logInSaveDao.index();
        if(userLoged.size()==0) {
            setContentView(R.layout.activity_main_page);
            loginBtn = findViewById(R.id.loginPage);
            registerBtn = findViewById(R.id.registerPage);
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), loginActivity.class));
                }
            });
            registerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), registerPage.class));
                }
            });
        } else {
            globalVars.username=userLoged.get(0).getUsername();
            globalVars.password=userLoged.get(0).getPassword();
            user= new UsersApiToken(db,postDao);

            Callback<ResponseBody> callbackForGetUserChatsInfo = new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (!response.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Incorrect username and/or password", Toast.LENGTH_LONG).show();
                        } else {
                            String token = response.body().string();
                            String authorizationHeader = "Bearer " + token;
                            Call<List<Contact>> call2 = webServiceAPI.getChats(authorizationHeader);
                            call2.enqueue(new Callback<List<Contact>>() {
                                @Override
                                public void onResponse(Call<List<Contact>> call2, Response<List<Contact>> response2) {
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
                                    getAllMessagesForLogin(globalVars.username, globalVars.password);
                                    Intent intent=new Intent(getApplicationContext(),contacts_pageActivity.class);
                                    startActivity(intent);
                                }

                                @Override
                                public void onFailure(Call<List<Contact>> call2, Throwable t) {
                                    Toast.makeText(MainActivity.this, "problem with connecting to the server", Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent(getApplicationContext(),contacts_pageActivity.class);
                                    startActivity(intent);

                                }
                            });
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "problem with connecting to the server", Toast.LENGTH_LONG).show();
                }
            };
            user.getChats(globalVars.username, globalVars.password, callbackForGetUserChatsInfo);

        }

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
                                Toast.makeText(MainActivity.this, "problem with connecting to the server", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, "problem with connecting to the server", Toast.LENGTH_LONG).show();
            }
        };
        user.getMessages(userName,password,getAllMessagesCallback);
    }

}