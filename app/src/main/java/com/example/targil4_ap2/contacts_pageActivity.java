package com.example.targil4_ap2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.targil4_ap2.adapters.ContactsListAdapter;
import com.example.targil4_ap2.items.Contact;
import com.example.targil4_ap2.items.MessageLast;
import com.example.targil4_ap2.items.UserToGet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class contacts_pageActivity extends AppCompatActivity {
    private FloatingActionButton btnAdd;
    private FloatingActionButton btnLogout;
    private List<Contact> contacts;
    private AppDB db;
    private PostDao postDao;
    private ContactsListAdapter adapter;

    /**
     * Function get userename that you just send him a meesage or got message by him, and get it the
     * top of the contact list.
     * @param username of the contact
     */
    public void moveContactToFirst(String username) {
        for (int i = 0; i < contacts.size(); i++) {
            Contact contact = contacts.get(i);
            if (contact.getUser().equals(username)) {
                contacts.remove(i);
                contacts.add(0, contact);
                break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_page);

        Intent intent = getIntent();

        // Retrieve the values from the intent extras
        String username = intent.getStringExtra("username");
        String displayName = intent.getStringExtra("displayName");
        String profilePic = intent.getStringExtra("profilePic");
        String password = intent.getStringExtra("password");


        RecyclerView lstContacts = findViewById(R.id.lstContacts);
        adapter = new ContactsListAdapter(this);
        lstContacts.setAdapter(adapter);
        lstContacts.setLayoutManager(new LinearLayoutManager(this));

        contacts = new ArrayList<>();
        UserToGet u1 = new UserToGet("ariel", "osCAR", "1");
        MessageLast m1 = new MessageLast("1", "12/06/23", "hi");
        contacts.add(new Contact("1", u1, m1));
        UserToGet u2 = new UserToGet("Bar", "Barben", "1");
        MessageLast m2 = new MessageLast("2", "18/06/23", "hi");
        contacts.add(new Contact("2", u2, m2));
        UserToGet u3 = new UserToGet("Ofek", "Ofekul", "1");
        MessageLast m3 = new MessageLast("1", "21/06/23", "hi");
        contacts.add(new Contact("3", u3, m3));
        UserToGet u4 = new UserToGet("Maya", "maya", "1");
        MessageLast m4 = new MessageLast("1", "16/04/23", "hi");
        contacts.add(new Contact("4", u4, m4));
        UserToGet u5 = new UserToGet("Shoam", "Shoameni", "1");
        MessageLast m5 = new MessageLast("1", "03/06/23", "hi");
        contacts.add(new Contact("5", u5, m5));
        UserToGet u6 = new UserToGet("Tohar", "Tohar", "1");
        MessageLast m6 = new MessageLast("1", "04/03/23", "hi");
        contacts.add(new Contact("6", u6, m6));
        adapter.setContacts(contacts);
        UserToGet u7 = new UserToGet("Haimon", "Haimon", "1");
        MessageLast m7 = new MessageLast("1", "04/07/23", "hi");
        contacts.add(new Contact("7", u7, m7));
        adapter.setContacts(contacts);
        UserToGet u8 = new UserToGet("Efrat", "foodi", "1");
        MessageLast m8 = new MessageLast("1", "30/05/23", "hi");
        contacts.add(new Contact("8", u8, m8));


        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "PostsDB").allowMainThreadQueries().build();
        postDao = db.postDao();
        List<DbObject> DbObj = postDao.index();
        List<Contact> chats = new ArrayList<>();
        for(int i = 0; i < DbObj.size(); i++){
            Contact con = DbObj.get(i).getContactName();
            if(con.getLastMessage() == null){
                con.setLastMessage(new MessageLast("", "", ""));
            }
            chats.add(con);
        }
        adapter.setContacts(chats);

        adapter.setOnContactClickListener(new ContactsListAdapter.OnContactClickListener() {
            @Override
            public void onContactClick(Contact contact) {
                // Handle the click event for the contact item
                // Start the chat activity
                Intent intent = new Intent(getApplicationContext(), chatActivity.class);

                // Pass any necessary data to the chat activity using intent extras
                intent.putExtra("contactId", contact.getId());
                intent.putExtra("contactName", contact.getUser().getDisplayName());
                intent.putExtra("contactImg", contact.getUser().getProfilePic());
                intent.putExtra("username", username);
                intent.putExtra("password", password);
                startActivity(intent);
            }
        });

        btnAdd= findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent thisIntent= getIntent();
                Intent intentAddCon = new Intent(getApplicationContext(), ChooseConNameActivity.class);
                intentAddCon.putExtra("username", thisIntent.getStringExtra("username"));
                intentAddCon.putExtra("password", thisIntent.getStringExtra("password"));

                startActivity(intentAddCon);
            }
        });



        btnLogout= findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<DbObject> DbObj = postDao.index();
        List<Contact> chats = new ArrayList<>();
        for(int i = 0; i < DbObj.size(); i++){
            Contact con = DbObj.get(i).getContactName();
            if(con.getLastMessage() == null){
                con.setLastMessage(new MessageLast("", "", ""));
            }
            chats.add(con);
        }
        adapter.setContacts(chats);
    }
}