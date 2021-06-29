package com.example.chatapplication.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapplication.Fragments.OtherProfileFragment;
import com.example.chatapplication.Models.Kullanicilar;
import com.example.chatapplication.R;
import com.example.chatapplication.Utils.ChangeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Friend_req_Adapter extends RecyclerView.Adapter<Friend_req_Adapter.ViewHolder> {
   List<String>userKeysList;
    Activity activity;
    Context context;
FirebaseDatabase database;
DatabaseReference reference;
FirebaseAuth auth;
FirebaseUser firebaseUser;
String userID;
    public Friend_req_Adapter(List<String> userKeysList, Activity activity, Context context) {
        this.userKeysList = userKeysList;
        this.activity = activity;
        this.context = context;
        database=FirebaseDatabase.getInstance();
        reference=database.getReference();
        auth=FirebaseAuth.getInstance();
        firebaseUser=auth.getCurrentUser();
        userID=firebaseUser.getUid();
    }

    //layout tanımlaması
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(context).inflate(R.layout.friend_req_layout,parent,false);
        return new ViewHolder(view);
    }



    //view setlemeler
    @Override
    public void onBindViewHolder(final @NonNull ViewHolder holder, int position) {
        // holder.usernameText.setText(userKeysList.get(position).toString());
        reference.child("Kullanıcılar").child(userKeysList.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Kullanicilar k1 = snapshot.getValue(Kullanicilar.class);


                Picasso.get().load(k1.getResim()).into(holder.friend_req_image);
                holder.friend_req_Text.setText(k1.getIsim());
                holder.friend_req_btnkabul.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        kabulEt(userID, userKeysList.get(position));
                    }
                });

                holder.friend_req_btnred.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        redEt(userID, userKeysList.get(position));
                    }
                });

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void kabulEt(String userID, String otherID) {


        DateFormat df=new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date today= Calendar.getInstance().getTime();
      final  String reportDate=df.format(today);


        reference.child("Arkadaslar").child(userID).child(otherID).child("tarih").setValue(reportDate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                reference.child("Arkadaslar").child(otherID).child(userID).child("tarih").setValue(reportDate).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Arkadaşlık isteğini kabul ettiniz..", Toast.LENGTH_SHORT).show();
                            reference.child("Arkadaslik_istek").child(userID).child(otherID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            });

                        }
                    }
                });
            }
        });
    }
    public void redEt(String userID, String otherID) {
        reference.child("Arkadaslik_istek").child(userID).child(otherID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Arkadaşlık isteğini reddettiniz..", Toast.LENGTH_SHORT).show();

            }
        });
    }


    //adapter oluşturacak list size
    @Override
    public int getItemCount() {
        return userKeysList.size();
    }
//view tanımla
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView friend_req_Text;
        CircleImageView friend_req_image;
     Button friend_req_btnkabul,friend_req_btnred;

        ViewHolder(View itemView){
            super(itemView);
            friend_req_Text=(TextView)itemView.findViewById(R.id.friend_req_Text);
            friend_req_image=(CircleImageView)itemView.findViewById(R.id.friend_req_image);
            friend_req_btnkabul=(Button)itemView.findViewById(R.id.friend_req_btnkabul);
            friend_req_btnred=(Button)itemView.findViewById(R.id.friend_req_btnred);


        }

}


}
