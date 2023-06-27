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
        db2 = Room.databaseBuilder(getApplicationContext(), AppDB.class, "sveLogin").allowMainThreadQueries().build();
        postDao = db.postDao();
        logInSaveDao = db2.logInSaveDao();
        retrofit = new Retrofit.Builder()
                .baseUrl(globalVars.server)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
        List<logInSave> userLoged = logInSaveDao.index();
        if (userLoged.size() == 0) {
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
            globalVars.username = userLoged.get(0).getUsername();
            globalVars.password = userLoged.get(0).getPassword();
            Intent intent = new Intent(getApplicationContext(), contacts_pageActivity.class);
            intent.putExtra("CheckWithServer","yes");
            startActivity(intent);
        }

    }
}