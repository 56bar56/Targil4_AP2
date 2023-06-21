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
import com.example.targil4_ap2.items.UserToGet;
import com.example.targil4_ap2.items.UserToPost;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class registerPage extends AppCompatActivity {
    Button registerBtn;
    TextView toLogIn;
    private EditText editRegisterUsername;
    private EditText editRegisterPassword;
    private EditText editRegisterEmail;
    private AppDB db;
    private PostDao postDao;
    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;
    private UsersApiToken user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        registerBtn = findViewById(R.id.registerBtn);
        toLogIn = findViewById(R.id.noAccount);
        editRegisterUsername = findViewById(R.id.registerUsername);
        editRegisterPassword = findViewById(R.id.registerPassword);
        editRegisterEmail = findViewById(R.id.registerEmail);
        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "PostsDB").allowMainThreadQueries().build();
        postDao = db.postDao();
        user = new UsersApiToken(db, postDao);
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean informationOk = true;
                String username = editRegisterUsername.getText().toString();
                String password = editRegisterPassword.getText().toString();
                String email = editRegisterEmail.getText().toString();
                //להוסיף שם פרופיל ותמונה
                String profileName = "prof";
                String profilePic = "";
                String errorMsg="";
                if (password.length() > 16 || password.length() < 4) {
                    informationOk = false;
                    errorMsg+="Password is not valid\n";
                } else {
                    boolean have$ = false;
                    boolean haveNum = false;
                    boolean haveLet = false;
                    boolean haveEmpjy = false;
                    for (int i = 0; i < password.length(); i++) {
                        if (password.charAt(i) == '$') {
                            have$ = true;
                        }
                        if (Character.isDigit(password.charAt(i))) {
                            haveNum = true;
                        }
                        if (Character.isLetter(password.charAt(i))) {
                            haveLet = true;
                        }
                        if (password.charAt(i) >= 0x1F300 && password.charAt(i) <= 0x1FFFF) {
                            haveEmpjy = true;
                        }
                    }
                    if (!have$ || !haveNum || !haveLet || haveEmpjy) {
                        informationOk = false;
                        errorMsg+="Password is not valid\n";
                    }
                }
                if(username.isEmpty()) {
                    informationOk = false;
                    errorMsg+="Username is not valid\n";
                }
                if(profileName.isEmpty()) {
                    informationOk = false;
                    errorMsg+="profileName is not valid\n";

                }
                String regex3 = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
                if (!email.matches(regex3)) {
                    informationOk = false;
                    errorMsg+="email is not valid\n";
                }

                if (informationOk) {
                    Callback<ResponseBody> callbackForPostUser = new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                if(!response.isSuccessful()) {
                                    Toast.makeText(registerPage.this, "This user name is already used", Toast.LENGTH_LONG).show();
                                }
                                String serverReturn = response.body().string();
                                    chatsPageAfterRegister(username, password);

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(registerPage.this, "problem with connecting to the server", Toast.LENGTH_LONG).show(); //error message for server
                        }
                    };
                    user.postUser(new UserToPost(username, password, profileName, profilePic), callbackForPostUser);
                } else {
                    Toast.makeText(registerPage.this, errorMsg, Toast.LENGTH_LONG).show();//error message
                }

            }
        });
        toLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), loginActivity.class));
            }
        });
    }

    public void chatsPageAfterRegister(String username, String password) {
        Callback<ResponseBody> callbackForGetUserInfo = new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
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

                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<UserToGet> call2, Throwable t) {
                            Toast.makeText(registerPage.this, "problem with connecting to the server", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(registerPage.this, "problem with connecting to the server", Toast.LENGTH_LONG).show();
            }
        };
        user.getUser(username, password, callbackForGetUserInfo);

    }
}