package com.example.targil4_ap2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SettingsActivity extends AppCompatActivity {

    private FloatingActionButton btnCancelSettings;
    private Button btnDarkMode;
    private EditText editServerAddress;
    private Button btnServerAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);
        RelativeLayout settingsRelativeLayout = findViewById(R.id.settingsRelativeLayout);

        btnDarkMode= findViewById(R.id.btnDarkMode);
        btnDarkMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalVars.lightOn = !globalVars.lightOn;
                if (globalVars.lightOn) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
            }
        });

        editServerAddress = findViewById(R.id.editServerAddress);
        btnServerAddress = findViewById(R.id.btnServerAddress);
        btnServerAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newServer=editServerAddress.getText().toString();
                if(newServer.isEmpty()) {
                    Toast.makeText(SettingsActivity.this, "put new server address!", Toast.LENGTH_LONG).show();
                } else {
                    globalVars.server=newServer;
                }

            }
        });

        btnCancelSettings= findViewById(R.id.btnCancelSettings);
        btnCancelSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
