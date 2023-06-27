package com.example.targil4_ap2;

import static com.example.targil4_ap2.MyApplication.context;

import android.Manifest;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.room.Room;

import com.example.targil4_ap2.adapters.ContactsListAdapter;
import com.example.targil4_ap2.adapters.MessagesListAdapter;
import com.example.targil4_ap2.api.UsersApiToken;
import com.example.targil4_ap2.api.WebServiceAPI;
import com.example.targil4_ap2.items.Contact;
import com.example.targil4_ap2.items.MessageToGet;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyService extends FirebaseMessagingService {
    private String contactUserName;
    private MessagesListAdapter adapter;

    public void setAdapterCon(ContactsListAdapter adapterCon) {
        this.adapterCon = adapterCon;
    }

    private ContactsListAdapter adapterCon=null;
    private boolean refreshChat = false;
    private Handler handler = new Handler(Looper.getMainLooper());
    private static MyService myService;

    public void setContactUserName(String contactUserName) {
        this.contactUserName = contactUserName;
    }

    public void setOurAdapter(MessagesListAdapter adapter) {
        this.adapter = adapter;
    }

    public static MyService getInstance() {
        if (myService == null) {
            myService = new MyService();
        }
        return myService;
    }

    public MyService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            createNotificationChannel();
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1")
                    .setSmallIcon(R.drawable.messi)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            notificationManager.notify(1, builder.build());
            if (globalVars.username == "" && globalVars.password == "") {

            } else {
                AppDB db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "PostsDB").allowMainThreadQueries().build();
                PostDao postDao = db.postDao();
                UsersApiToken user = new UsersApiToken(db, postDao);
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(globalVars.server)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                WebServiceAPI webServiceAPI = retrofit.create(WebServiceAPI.class);
                Callback<ResponseBody> getAllMessagesCallback = new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String token = response.body().string();
                            String authorizationHeader = "Bearer " + token;


                            Call<List<Contact>> call3 = webServiceAPI.getChats(authorizationHeader);


                            call3.enqueue(new Callback<List<Contact>>() {
                                @Override
                                public void onResponse(Call<List<Contact>> call3, Response<List<Contact>> response2) {
                                    List<Contact> l = response2.body();
                                    //creat a dcObject for the inseration
                                    List<DbObject> existingData = postDao.index();  // Retrieve existing data from the database
                                    String[] words = remoteMessage.getNotification().getTitle().split(" ");
                                    String reciveUsername = "";
                                    for (int i = 0; i < words.length; i++) {
                                        if (i > 2) {
                                            reciveUsername += words[i];
                                            if (i < words.length - 1) {
                                                reciveUsername += " ";
                                            }
                                        }
                                    }
                                    for (int i = 0; i < l.size(); i++) {
                                        if (l.get(i).getUser().getUsername().equals(reciveUsername)) {
                                            if (reciveUsername.equals(myService.contactUserName)) {
                                                refreshChat = true;
                                            }
                                            DbObject newDb = existingData.get(i);
                                            postDao.delete(existingData.get(i));
                                            Contact c = newDb.getContactName();
                                            c.setLastMessage(l.get(i).getLastMessage());
                                            newDb.setContactName(c);
                                            postDao.insert(newDb);

                                            Call<List<MessageToGet>> call2 = webServiceAPI.getMessages(authorizationHeader, newDb.getContactName().getId());
                                            call2.enqueue(new Callback<List<MessageToGet>>() {
                                                @Override
                                                public void onResponse(Call<List<MessageToGet>> call2, Response<List<MessageToGet>> response2) {
                                                    List<MessageToGet> serverReturn = response2.body();
                                                    newDb.setMsgList(serverReturn);
                                                    postDao.update(newDb);
                                                    handler.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            if (refreshChat) {
                                                                myService.adapter.setMessages(serverReturn);
                                                                refreshChat = false;
                                                            }
                                                            if(myService.adapterCon!=null) {
                                                                myService.adapterCon.setContacts(l);
                                                            }

                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onFailure(Call<List<MessageToGet>> call2, Throwable t) {
                                                }
                                            });


                                        }

                                    }
                                }

                                @Override
                                public void onFailure(Call<List<Contact>> call3, Throwable t) {
                                }
                            });


                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                    }
                };
                user.getMessages(globalVars.username, globalVars.password, getAllMessagesCallback);
            }

        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", "my Channel", importance);
            channel.setDescription("channel for whatsapp");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}