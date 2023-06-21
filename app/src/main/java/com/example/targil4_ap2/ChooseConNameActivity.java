package com.example.targil4_ap2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.targil4_ap2.api.UsersApiToken;
import com.example.targil4_ap2.api.WebServiceAPI;
import com.example.targil4_ap2.items.AddNewContact;
import com.example.targil4_ap2.items.ContactForCreate;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ChooseConNameActivity extends AppCompatActivity {

    private EditText editConName;
    private Button buttonSave;
    private Button buttonCancel;
    private AppDB db;
    private PostDao postDao;
    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;
    private UsersApiToken user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_con_name);
        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "PostsDB").allowMainThreadQueries().build();
        postDao = db.postDao();
        user = new UsersApiToken(db, postDao);
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);

        editConName = findViewById(R.id.editNewConName);
        buttonSave = findViewById(R.id.btnSaveCon);
        buttonCancel = findViewById(R.id.btnCancelAdd);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contactName = editConName.getText().toString();
                if (contactName.isEmpty()) {
                    //is empty
                } else {
                    Callback<ResponseBody> postContactCallback = new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                String token = response.body().string();
                                String authorizationHeader = "Bearer " + token;

                                Call<AddNewContact> call2 = webServiceAPI.postChat(authorizationHeader, new ContactForCreate(contactName));
                                call2.enqueue(new Callback<AddNewContact>() {
                                    @Override
                                    public void onResponse(Call<AddNewContact> call2, Response<AddNewContact> response2) {
                                        AddNewContact serverReturn = response2.body();
                                        //להוסיף פה גט מהשרת
                                    }

                                    @Override
                                    public void onFailure(Call<AddNewContact> call2, Throwable t) {
                                        System.out.println("filed");
                                    }
                                });
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            System.out.println("filed");
                        }
                    };
                    Intent intentFromChats = getIntent();
                    user.postChat(intentFromChats.getStringExtra("username"),intentFromChats.getStringExtra("password"),postContactCallback);
                }

            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
