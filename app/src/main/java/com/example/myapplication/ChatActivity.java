package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.adapter.messageAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {
    ImageButton backButton;
    EditText messageInput;
    ImageButton sendMessageBtn;
    ImageButton backBtn;
    TextView otherUsername;
    ImageView recDisplayImage;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;

    messageAdapter messageAdapter;
    FirebaseDatabase database;

    String reciverRoom,sendersRoom;

    FirebaseUser user;
    public static String recName ,recImg , recUID;
    public  static String senderImg;


    ArrayList<msgModelClass> msgModelClassArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);

        backButton = findViewById(R.id.back_btn);
        msgModelClassArrayList = new ArrayList<>();
        messageInput = findViewById(R.id.chat_message_input);
        sendMessageBtn = findViewById(R.id.message_send_btn);
        otherUsername = findViewById(R.id.other_username);

        Log.d("ChatActivity", "recUID: " + recUID);

        Log.d("MessageListSize", "Message list size: " + msgModelClassArrayList.size());



        recDisplayImage  = findViewById(R.id.profileChat);

        user = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = findViewById(R.id.chat_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        messageAdapter = new messageAdapter(ChatActivity.this,msgModelClassArrayList);
        recyclerView.setAdapter(messageAdapter);

        String uid = user.getUid();

//        String uid = null;
//        if(user != null){
//            uid = user.getUid();
//        }


        if(uid != null){
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild("profileImageURL") && snapshot.child("profileImageURL").getValue().toString() != null){
                        senderImg = snapshot.child("profileImageURL").getValue().toString();
                    }else {
                        senderImg = "default_image_url";
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else {
            Log.d("mine", "onCreate: uid is null");
        }




        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            Log.d("ChatActivity", "user is not null");
        }
        else{
            Log.d("ChatActivity", "user is  null");        }

        String senderUid = FirebaseAuth.getInstance().getUid();
        recName = getIntent().getStringExtra("name");
        recImg = getIntent().getStringExtra("profile");
        recUID = getIntent().getStringExtra("uid");

        if (senderUid != null) {
            reciverRoom = "chats/" + senderUid + recUID;
            sendersRoom = "chats/" + recUID + senderUid;
            Log.d("ChatActivity", "senderUid is not null");
        } else {
            Log.d("ChatActivity", "senderUid is null");
        }

        if (recUID != null) {
            reciverRoom = "chats/" + senderUid + recUID;
            sendersRoom = "chats/" + recUID + senderUid;
            Log.d("ChatActivity", "recUID is not null");
        } else {
            Log.d("ChatActivity", "recUID is null");
        }









        if(uid != null){

            String messageInp = messageInput.getText().toString();

            DatabaseReference chatReference = FirebaseDatabase.getInstance().getReference().child("chats").child(sendersRoom).child("message");
            chatReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    msgModelClassArrayList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        msgModelClass messgaes = dataSnapshot.getValue(msgModelClass.class);
                        msgModelClassArrayList.add(messgaes);
                        Log.d("MessageAdded", "Message added: " + messgaes.getMessage());
                    }
                    messageAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("FirebaseError", "Error fetching messages: " + error.getMessage());
                }
            });
        }else {
            Log.d("mine", "onCreate: uid is null 2");
        }


        Glide.with(this).load(recImg).into(recDisplayImage);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String message = bundle.getString("name");
            String image = bundle.getString("profile");
            if (message != null) {
                otherUsername.setText(message);

            } else {

            }
        } else {
            onBackPressed();
        }

        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageInput.getText().toString();
                if(message.isEmpty()){
                    Toast.makeText(ChatActivity.this, "Empty :)", Toast.LENGTH_SHORT).show();
                }
                messageInput.setText("");
                Date date = new Date();
                msgModelClass msgs = new msgModelClass(message,senderUid,date.getTime());
                database = FirebaseDatabase.getInstance();
                database.getReference().child("chats").child(sendersRoom).child("message").push()
                        .setValue(msgs).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                database.getReference().child("chats").child(reciverRoom).child("message").push()
                                        .setValue(msgs).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });
                            }
                        });
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



    }
}