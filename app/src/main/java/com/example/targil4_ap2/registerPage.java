package com.example.targil4_ap2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.targil4_ap2.api.UsersApiToken;
import com.example.targil4_ap2.api.WebServiceAPI;
import com.example.targil4_ap2.items.Contact;
import com.example.targil4_ap2.items.MessageToGet;
import com.example.targil4_ap2.items.UserToGet;
import com.example.targil4_ap2.items.UserToPost;
import com.example.targil4_ap2.items.logInSave;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


@RequiresApi(api = Build.VERSION_CODES.R)
public class registerPage extends AppCompatActivity {
    Button registerBtn;
    TextView toLogIn;
    private EditText editRegisterUsername;
    private EditText editRegisterPassword;
    private EditText editRegisterEmail;
    private EditText editProfileName;
    private CircleImageView imageView;
    private AppDB db;
    private PostDao postDao;
    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;
    private UsersApiToken user;
    private AppDB db2;
    private LogInSaveDao logInSaveDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        registerBtn = findViewById(R.id.registerBtn);
        toLogIn = findViewById(R.id.noAccount);
        editRegisterUsername = findViewById(R.id.registerUsername);
        editRegisterPassword = findViewById(R.id.registerPassword);
        editRegisterEmail = findViewById(R.id.registerEmail);
        editProfileName = findViewById(R.id.registerDisplayName);
        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "PostsDB").allowMainThreadQueries().build();
        postDao = db.postDao();
        db2=Room.databaseBuilder(getApplicationContext(),AppDB.class,"sveLogin").allowMainThreadQueries().build();
        logInSaveDao=db2.logInSaveDao();
        postDao.deleteAll();
        logInSaveDao.deleteAll();
        user = new UsersApiToken();
        retrofit = new Retrofit.Builder()
                .baseUrl(globalVars.server)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
        imageView = findViewById(R.id.registerProfileImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                function();
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean informationOk = true;
                String username = editRegisterUsername.getText().toString();
                String password = editRegisterPassword.getText().toString();
                String email = editRegisterEmail.getText().toString();
                String profileName = editProfileName.getText().toString();
                //String profilePic = imageView.toString();
                String profilePic = "";
                Bitmap resizedBitmap = null;

                try {
                    ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                    Bitmap bm = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    resizedBitmap = Bitmap.createScaledBitmap(bm, 30, 25, false);
                    resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
                    byte[] profileBitmap = byteArray.toByteArray();
                    profilePic = android.util.Base64.encodeToString(profileBitmap, android.util.Base64.DEFAULT);

                } catch (Exception e) {
                    Toast.makeText(registerPage.this, "Error with the image", Toast.LENGTH_LONG).show();
                    return;
                }

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
                                globalVars.username=username;
                                globalVars.password=password;

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

    private void function() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        launchSomeActivity.launch(i);
    }

    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        Bitmap selectedBitmap;
                        try {
                            selectedBitmap = MediaStore.Images.Media.getBitmap(
                                    getContentResolver(), selectedImageUri);
                            imageView.setImageBitmap(selectedBitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    public void chatsPageAfterRegister(String username, String password) {
        Callback<ResponseBody> callbackForGetUserInfo = new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String token = response.body().string();
                    String authorizationHeader = "Bearer " + token;
                    logInSaveDao.insert(new logInSave(globalVars.username,globalVars.password));
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
                            globalVars.username=serverReturn.getUsername();
                            globalVars.password=password;
                            dbInfo(intent, username, password);
                            //startActivity(intent);
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

    public void dbInfo(Intent intent, String username, String password) {
        Callback<ResponseBody> callbackForGetUserChatsInfo = new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (!response.isSuccessful()) {
                        Toast.makeText(registerPage.this, "Incorrect username and/or password", Toast.LENGTH_LONG).show();
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
                                            DbObject newDb = existingRecord;
                                            postDao.delete(existingRecord);
                                            Contact c = newDb.getContactName();
                                            c.setLastMessage(newData.getLastMessage());
                                            newDb.setContactName(c);
                                            postDao.insert(newDb);
                                            break;
                                        }
                                    }

                                    if (!found) {
                                        // Insert new record
                                        DbObject newObj = new DbObject(newData, null);
                                        postDao.insert(newObj);
                                    }
                                }
                                getAllMessagesForRegister(username, password);
                                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(registerPage.this, instanceIdResult -> {
                                    String newToken= instanceIdResult.getToken();
                                    user.getTokenWithFireBase(globalVars.username,globalVars.password,newToken);
                                });
                                intent.putExtra("CheckWithServer","not");
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(Call<List<Contact>> call2, Throwable t) {
                                Toast.makeText(registerPage.this, "problem with connecting to the server", Toast.LENGTH_LONG).show();

                            }
                        });
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(registerPage.this, "problem with connecting to the server", Toast.LENGTH_LONG).show();
            }
        };
        user.getChats(username, password, callbackForGetUserChatsInfo);
    }



    public void getAllMessagesForRegister(String userName, String password) {
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
                                Toast.makeText(registerPage.this, "problem with connecting to the server", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(registerPage.this, "problem with connecting to the server", Toast.LENGTH_LONG).show();
            }
        };
        user.getMessages(userName,password,getAllMessagesCallback);
    }
}