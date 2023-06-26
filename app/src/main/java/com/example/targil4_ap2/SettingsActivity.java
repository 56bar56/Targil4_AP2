package com.example.targil4_ap2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SettingsActivity extends AppCompatActivity {

    private FloatingActionButton btnCancelSettings;
    private Button btnDarkMode;
    private Boolean darkMode = false; // False = bright mode, true = dark mode.

    private EditText editServerAddress;
    private Button btnServerAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);

        btnDarkMode= findViewById(R.id.btnDarkMode);
        btnDarkMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout settingsRelativeLayout = findViewById(R.id.settingsRelativeLayout);

                if (darkMode) {
                    settingsRelativeLayout.setBackgroundColor(getResources().getColor(R.color.darkModeBackground));
                } else {
                    settingsRelativeLayout.setBackgroundColor(getResources().getColor(R.color.lightModeBackground));
                }
                darkMode = !darkMode;
            }
        });

        editServerAddress = findViewById(R.id.editServerAddress);
        btnServerAddress = findViewById(R.id.btnServerAddress);
        btnServerAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentFromChats = getIntent();
                String contactName = editServerAddress.getText().toString();

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
