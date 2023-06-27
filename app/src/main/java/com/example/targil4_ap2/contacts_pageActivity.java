package com.example.targil4_ap2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
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
    private FloatingActionButton btnSettings;
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
        // Retrieve the values from the intent extras
        String username = globalVars.username;
        String password = globalVars.password;


        RecyclerView lstContacts = findViewById(R.id.lstContacts);
        adapter = new ContactsListAdapter(this);
        lstContacts.setAdapter(adapter);
        lstContacts.setLayoutManager(new LinearLayoutManager(this));


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
                intent.putExtra("contactUserName",contact.getUser().getUsername());
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
                Intent intentAddCon = new Intent(getApplicationContext(), ChooseConNameActivity.class);
                intentAddCon.putExtra("username", globalVars.username);
                intentAddCon.putExtra("password", globalVars.password);

                startActivity(intentAddCon);
            }
        });



        btnLogout= findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), loginActivity.class);
                startActivity(intent);
            }
        });
        btnSettings=findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSettings= new Intent(getApplicationContext(),SettingsActivity.class);
                goToSettings.putExtra("contactLayout",R.id.contactPageRelativeLayout);
                startActivity(goToSettings);

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