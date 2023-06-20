package com.example.targil4_ap2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.targil4_ap2.api.UsersApiToken;

public class MainActivity extends AppCompatActivity {
    Button loginBtn, registerBtn;
    private AppDB db;
    private PostDao postDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //room db
        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "PostsDB").allowMainThreadQueries().build();
        postDao = db.postDao();

        //continue
        UsersApiToken user=new UsersApiToken(db, postDao);
        user.postChat("ofek","o123$","ariel");
        setContentView(R.layout.activity_main);
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
    }
}