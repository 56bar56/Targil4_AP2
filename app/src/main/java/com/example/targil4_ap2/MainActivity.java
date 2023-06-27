package com.example.targil4_ap2;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.targil4_ap2.api.UsersApiToken;

import com.example.targil4_ap2.items.logInSave;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;


import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button loginBtn, registerBtn;
    private UsersApiToken user;
    private AppDB db2;
    private LogInSaveDao logInSaveDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db2 = Room.databaseBuilder(getApplicationContext(), AppDB.class, "sveLogin").allowMainThreadQueries().build();
        logInSaveDao = db2.logInSaveDao();
        List<logInSave> userLoged = logInSaveDao.index();
        if (userLoged.size() == 0) {
            setContentView(R.layout.activity_main_page);
            loginBtn = findViewById(R.id.loginPage);
            registerBtn = findViewById(R.id.registerPage);
            loginBtn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), loginActivity.class)));
            registerBtn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), registerPage.class)));
        } else {
            globalVars.username = userLoged.get(0).getUsername();
            globalVars.password = userLoged.get(0).getPassword();
            user=new UsersApiToken();
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(MainActivity.this, instanceIdResult -> {
                String newToken= instanceIdResult.getToken();
                user.getTokenWithFireBase(globalVars.username,globalVars.password,newToken);
            });
            Intent intent = new Intent(getApplicationContext(), contacts_pageActivity.class);
            intent.putExtra("CheckWithServer","yes");
            startActivity(intent);
        }

    }
}