package com.example.targil4_ap2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.example.targil4_ap2.api.UsersApiToken;

public class MainActivity extends AppCompatActivity {
    Button loginBtn, registerBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UsersApiToken user=new UsersApiToken();
        user.getToken("ofek","o123$");
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