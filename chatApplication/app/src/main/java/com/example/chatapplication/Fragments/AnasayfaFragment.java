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

import com.example.chatapplication.Adapters.UserAdapter;
import com.example.chatapplication.Models.Kullanicilar;
import com.example.chatapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class AnasayfaFragment extends Fragment {

FirebaseDatabase firebaseDatabase;
DatabaseReference reference;
List<String>userKeysList;
RecyclerView userlistrv;
View view;
UserAdapter userAdapter;
FirebaseAuth auth;
FirebaseUser user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_anasayfa, container, false);
        tanimla();
        kullaniciGetir();
        return view;
    }


    public void tanimla()
    {
        userKeysList=new ArrayList<>();
        firebaseDatabase=FirebaseDatabase.getInstance();
        reference=firebaseDatabase.getReference();
        userlistrv=view.findViewById(R.id.userlistrv);
        RecyclerView.LayoutManager mng=new GridLayoutManager(getContext(),2);
        userlistrv.setLayoutManager(mng);
        userAdapter=new UserAdapter(userKeysList,getActivity(),getContext());
        userlistrv.setAdapter(userAdapter);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
    }
    public void kullaniciGetir(){
       reference.child("Kullan??c??lar").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded( DataSnapshot snapshot, String previousChildName) {
                reference.child("Kullan??c??lar").child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Kullanicilar k1=snapshot.getValue(Kullanicilar.class);

                        if(!k1.getIsim().equals("null") && !snapshot.getKey().equals(user.getUid())){
                            if(userKeysList.indexOf(snapshot.getKey())==-1) {
                                userKeysList.add(snapshot.getKey());
                            }
                            userAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

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