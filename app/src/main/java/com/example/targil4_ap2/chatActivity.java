package com.example.targil4_ap2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.targil4_ap2.adapters.MessagesListAdapter;
import com.example.targil4_ap2.items.MessageToGet;
import com.example.targil4_ap2.items.SenderName;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class chatActivity extends AppCompatActivity {

    private FloatingActionButton btnBack;
    private ImageView profileImg;
    private TextView displayName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);

        // Retrieve the extras from the intent
        Intent intent = getIntent();
        String contactId = intent.getStringExtra("contactId");
        //String contactUsername = intent.getStringExtra("contactUsername");
        String contactName = intent.getStringExtra("contactName");
        String contactImg = intent.getStringExtra("contactImg");

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
        adapter.setMessages(messages);

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
    }
}
