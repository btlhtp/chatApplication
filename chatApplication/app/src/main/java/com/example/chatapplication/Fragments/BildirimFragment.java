package com.example.chatapplication.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapplication.Adapters.Friend_req_Adapter;
import com.example.chatapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class BildirimFragment extends Fragment {

    View view;
    FirebaseUser firebaseUser;
    FirebaseAuth auth;
String userID;
FirebaseDatabase firebaseDatabase;
DatabaseReference reference;
List<String>friend_req_key_list;
RecyclerView friend_req_rv;
Friend_req_Adapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_bildirim, container, false);
        tanimla();
        istekler();
        return view;
    }
    public void tanimla(){
        auth=FirebaseAuth.getInstance();
        firebaseUser=auth.getCurrentUser();
        userID=firebaseUser.getUid();
        firebaseDatabase=FirebaseDatabase.getInstance();
        reference=firebaseDatabase.getReference().child("Arkadaslik_istek");
        friend_req_key_list=new ArrayList<>();
        friend_req_rv=(RecyclerView)view.findViewById(R.id.friend_req_rv);
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(getContext(),1);
        friend_req_rv.setLayoutManager(layoutManager);
        adapter=new Friend_req_Adapter(friend_req_key_list,getActivity(),getContext());
        friend_req_rv.setAdapter(adapter);




    }
    public void istekler(){
reference.child(userID).addChildEventListener(new ChildEventListener() {
    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        String kontrol=snapshot.child("tip").getValue().toString();
        if(kontrol.equals("aldi")){
            if(friend_req_key_list.indexOf(snapshot.getKey())==-1) {
                friend_req_key_list.add(snapshot.getKey());
            }
        adapter.notifyDataSetChanged();
    }
        }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

        friend_req_key_list.remove(snapshot.getKey());
        adapter.notifyDataSetChanged();
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