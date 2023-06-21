package com.example.targil4_ap2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class registerPage extends AppCompatActivity {
    Button registerBtn;
    TextView toLogIn;
    private EditText editRegisterUsername;
    private EditText editRegisterPassword;
    private EditText editRegisterEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        registerBtn = findViewById(R.id.registerBtn);
        toLogIn = findViewById(R.id.noAccount);
        editRegisterUsername = findViewById(R.id.registerUsername);
        editRegisterPassword = findViewById(R.id.registerPassword);
        editRegisterEmail = findViewById(R.id.registerEmail);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=editRegisterUsername.getText().toString();
                String password=editRegisterPassword.getText().toString();
                String email=editRegisterEmail.getText().toString();
                //להוסיף שם פרופיל
                String profileName="prof";
                if(username.isEmpty()||password.isEmpty()||email.isEmpty()||profileName.isEmpty()) {
                    /**
                     * חייב להוסיף את כל הבדיקות על המייל יוזר ניים וססמא אם תקינות
                     */
                } else {



                }

                    startActivity(new Intent(getApplicationContext(), contacts_pageActivity.class));
            }
        });
        toLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), loginActivity.class));
            }
        });
    }
}