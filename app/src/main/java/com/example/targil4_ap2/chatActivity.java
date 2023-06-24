package com.example.targil4_ap2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.targil4_ap2.adapters.MessagesListAdapter;
import com.example.targil4_ap2.api.UsersApiToken;
import com.example.targil4_ap2.api.WebServiceAPI;
import com.example.targil4_ap2.items.MessageToGet;
import com.example.targil4_ap2.items.SenderName;
import com.example.targil4_ap2.items.messageContent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class chatActivity extends AppCompatActivity {

    private FloatingActionButton btnBack;
    private ImageView profileImg;
    private TextView displayName;
    private FloatingActionButton btnSend;
    private EditText inputMes;
    private AppDB db;
    private PostDao postDao;
    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;
    private UsersApiToken user;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);

        // Retrieve the extras from the intent
        Intent intent = getIntent();
        String contactId = intent.getStringExtra("contactId");
        //String contactUsername = intent.getStringExtra("contactUsername");
        String contactName = intent.getStringExtra("contactName");
        String contactImg = intent.getStringExtra("contactImg");
        String username= intent.getStringExtra("username");
        String password= intent.getStringExtra("password");

        // Find views by their IDs
        displayName = findViewById(R.id.displayName);
        profileImg = findViewById(R.id.profileImg);

        // Set the values
        displayName.setText(contactName);
        //TODO change to the user img
        profileImg.setImageResource(R.drawable.messi);

        RecyclerView lstMessages = findViewById(R.id.lstMessages);
        final MessagesListAdapter adapter = new MessagesListAdapter(this);
        lstMessages.setAdapter(adapter);
        lstMessages.setLayoutManager(new LinearLayoutManager(this));

        // Makes sure to show it from the latest message
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        lstMessages.setLayoutManager(layoutManager);


        List<MessageToGet> messages = new ArrayList<>();
        SenderName s1 = new SenderName(contactName);
        MessageToGet m1 = new MessageToGet("1", "15:55", "hello", s1);
        messages.add(m1);
        SenderName s2 = new SenderName("username");
        MessageToGet m2 = new MessageToGet("2", "16:05", "how are u??", s2);
        messages.add(m2);
        SenderName s3 = new SenderName(contactName);
        MessageToGet m3 = new MessageToGet("3", "18:05", "Hi, i'm good. how u doing bro?", s3);
        messages.add(m3);
        SenderName s4 = new SenderName("username");
        MessageToGet m4 = new MessageToGet("4", "18:15", "good, baruh hashem", s4);
        messages.add(m4);
        SenderName s5 = new SenderName(contactName);
        MessageToGet m5 = new MessageToGet("5", "18:38", "wanna go eat shreder?", s5);
        messages.add(m5);
        SenderName s6 = new SenderName("username");
        MessageToGet m6 = new MessageToGet("6", "19:01", "yes but what about crazy meat?", s6);
        messages.add(m6);
        SenderName s7 = new SenderName(contactName);
        MessageToGet m7 = new MessageToGet("7", "19:05", "next time", s7);
        messages.add(m7);
        SenderName s8 = new SenderName("username");
        MessageToGet m8 = new MessageToGet("8", "19:08", "see u at 21:00!", s8);
        messages.add(m8);
        SenderName s9 = new SenderName("username");
        MessageToGet m9 = new MessageToGet("9", "20:57", "are u here?", s9);
        messages.add(m9);
        SenderName s10 = new SenderName(contactName);
        MessageToGet m10 = new MessageToGet("10", "20:58", "yea im parking. Long message to see how it loooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooook", s10);
        messages.add(m10);
        SenderName s11 = new SenderName(contactName);
        MessageToGet m11 = new MessageToGet("11", "21:05", "i see u!", s11);
        messages.add(m11);


        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "PostsDB").allowMainThreadQueries().build();
        postDao = db.postDao();
        List<DbObject> DbObj = postDao.index();
        List<MessageToGet> msgs = new ArrayList<>();
        int index = -1;
        for (int i = 0; i < DbObj.size(); i++) {
            if (contactName.equals(DbObj.get(i).getContactName().getUser().getDisplayName())) {
                index = i;
            }
        }
        /*
        for(int i=0; i < DbObj.get(index).getMsgList().size(); i++){
            msgs.set(i, DbObj.get(index).getMsgList().get(i));
        }
        */
        msgs = DbObj.get(index).getMsgList();

        adapter.setMessages(msgs);


        // Makes sure to show it from the latest message
        int lastPosition = messages.size() - 1;
        lstMessages.scrollToPosition(lastPosition);


        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        btnSend = findViewById(R.id.btnSend);
        user = new UsersApiToken(db, postDao);
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
        inputMes = findViewById(R.id.etMessageInput);
        /*
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newMessage = inputMes.getText().toString();
                if (newMessage.isEmpty()) {
                    Toast.makeText(chatActivity.this, "type the a message!", Toast.LENGTH_LONG).show(); //error message for server
                } else {
                    Callback<ResponseBody> callbackPostMes = new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                String token = response.body().string();
                                String authorizationHeader = "Bearer " + token;

                                Call<ResponseBody> call2 = webServiceAPI.postMessage(authorizationHeader, id, new messageContent(newMessage));
                                call2.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call2, Response<ResponseBody> response2) {
                                        try {
                                            String serverReturn = response2.body().string();
                                            System.out.println(serverReturn);
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call1, Throwable t) {
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
                }
            }
        });*/
    }
}
