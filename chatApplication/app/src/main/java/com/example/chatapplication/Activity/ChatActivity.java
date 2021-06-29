package com.example.chatapplication.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chatapplication.Adapters.MessageAdapter;
import com.example.chatapplication.Models.MessageModel;
import com.example.chatapplication.R;
import com.example.chatapplication.Utils.GetDate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    TextView chat_username;
    DatabaseReference reference;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    FirebaseAuth auth;
    FloatingActionButton sendMessage;
    EditText messageText;
    List<MessageModel>messageModelList;
    RecyclerView chat_rv;
    MessageAdapter messageAdapter;
    List<String>keyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        tanimla();
        action();
        loadMessage();
    }

    public String getId() {
        String id = getIntent().getExtras().getString("id").toString();
        return id;
    }

    public String getUserName() {

        String userName = getIntent().getExtras().getString("userName");
        return userName;
    }

    public void tanimla() {
        chat_username = (TextView) findViewById(R.id.chat_username);
        chat_username.setText(getUserName());
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        sendMessage = (FloatingActionButton) findViewById(R.id.sendMessage);
        messageText = (EditText) findViewById(R.id.messageText);
        messageModelList=new ArrayList<>();
        keyList=new ArrayList<>();
        chat_rv=(RecyclerView)findViewById(R.id.chat_rv);
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(ChatActivity.this,1);
        chat_rv.setLayoutManager(layoutManager);
        messageAdapter=new MessageAdapter(keyList,ChatActivity.this,ChatActivity.this,messageModelList);
        chat_rv.setAdapter(messageAdapter);


    }

    public void action() {


        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  String message = messageText.getText().toString();
                messageText.setText("");
                sendMessage(firebaseUser.getUid(), getId(), "text", GetDate.getDate(), false, message);
            }
        });
    }

    public void sendMessage(String userID, String otherID, String textType, String date, Boolean seen, String messageText) {
        String mesajID = reference.child("Mesajlar").child(userID).child(otherID).push().getKey();
        Map messageMap = new HashMap();
        messageMap.put("type", textType);
        messageMap.put("seen", seen);
        messageMap.put("time", date);
        messageMap.put("text", messageText);
        messageMap.put("from",userID);


        reference.child("Mesajlar").child(userID).child(otherID).child(mesajID).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                reference.child("Mesajlar").child(otherID).child(userID).child(mesajID).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
            }
        });

    }
public void loadMessage(){
        reference.child("Mesajlar").child(firebaseUser.getUid()).child(getId()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MessageModel messageModel=snapshot.getValue(MessageModel.class);
                messageModelList.add(messageModel);
                messageAdapter.notifyDataSetChanged();
                keyList.add(snapshot.getKey());
                chat_rv.scrollToPosition(messageModelList.size());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
}
}