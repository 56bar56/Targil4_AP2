package com.example.targil4_ap2.api;

import com.example.targil4_ap2.AppDB;
import com.example.targil4_ap2.DbObject;
import com.example.targil4_ap2.MyApplication;
import com.example.targil4_ap2.PostDao;
import com.example.targil4_ap2.R;
import com.example.targil4_ap2.items.AddNewContact;
import com.example.targil4_ap2.items.Contact;
import com.example.targil4_ap2.items.ContactForCreate;
import com.example.targil4_ap2.items.MessageToGet;
import com.example.targil4_ap2.items.UserToGet;
import com.example.targil4_ap2.items.UserToPost;
import com.example.targil4_ap2.items.messageContent;
import com.example.targil4_ap2.loginInfo;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsersApiToken {
    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;
    private AppDB DB;
    private PostDao postDao;

    public UsersApiToken(AppDB DB, PostDao postDao) {
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
        this.DB = DB;
        this.postDao = postDao;
    }

    public void getToken(String username, String password) {
        Call<ResponseBody> call = webServiceAPI.getTokenFromServer(new loginInfo(username, password));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String tokenRec = response.body().string();
                    System.out.println(tokenRec);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("filed");
            }
        });
    }

    public void postUser(UserToPost user) {
        Call<ResponseBody> call = webServiceAPI.postUser(user);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String serverReturn = response.body().string();
                    System.out.println(serverReturn);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("filed");
            }
        });
    }

    public void getUser(String username, String password) {

        Call<ResponseBody> call = webServiceAPI.getTokenFromServer(new loginInfo(username, password));
        call.enqueue(new Callback<ResponseBody>() {
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
                            System.out.println(serverReturn);
                        }

                        @Override
                        public void onFailure(Call<UserToGet> call2, Throwable t) {
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
        });

    }

    public void getChats(String username, String password) {


        Call<ResponseBody> call = webServiceAPI.getTokenFromServer(new loginInfo(username, password));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
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
                                        break;
                                    }
                                }

                                if (!found) {
                                    // Insert new record
                                    postDao.insert(new DbObject(newData));
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Contact>> call2, Throwable t) {
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
        });

    }

    public void postChat(String username, String password, String contactName) {
        Call<ResponseBody> call = webServiceAPI.getTokenFromServer(new loginInfo(username, password));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String token = response.body().string();
                    String authorizationHeader = "Bearer " + token;

                    Call<AddNewContact> call2 = webServiceAPI.postChat(authorizationHeader, new ContactForCreate(contactName));
                    call2.enqueue(new Callback<AddNewContact>() {
                        @Override
                        public void onResponse(Call<AddNewContact> call2, Response<AddNewContact> response2) {
                            AddNewContact serverReturn = response2.body();
                            System.out.println(serverReturn);
                        }

                        @Override
                        public void onFailure(Call<AddNewContact> call2, Throwable t) {
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
        });
    }

    public void postMessage(String username, String password, String id, String msg) {
        Call<ResponseBody> call = webServiceAPI.getTokenFromServer(new loginInfo(username, password));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String token = response.body().string();
                    String authorizationHeader = "Bearer " + token;

                    Call<ResponseBody> call2 = webServiceAPI.postMessage(authorizationHeader, id, new messageContent(msg));
                    call2.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call2, Response<ResponseBody> response2) {
                            try {
                                String serverReturn = response2.body().string();
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
        });
    }

    public void getMessages(String username, String password, String id) {
        Call<ResponseBody> call = webServiceAPI.getTokenFromServer(new loginInfo(username, password));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String token = response.body().string();
                    String authorizationHeader = "Bearer " + token;

                    Call<List<MessageToGet>> call2 = webServiceAPI.getMessages(authorizationHeader, id);
                    call2.enqueue(new Callback<List<MessageToGet>>() {
                        @Override
                        public void onResponse(Call<List<MessageToGet>> call2, Response<List<MessageToGet>> response2) {
                            List<MessageToGet> serverReturn = response2.body();
                            System.out.println(serverReturn);
                        }

                        @Override
                        public void onFailure(Call<List<MessageToGet>> call2, Throwable t) {
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
        });
    }
}
