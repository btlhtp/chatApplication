package com.example.chatapplication.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapplication.Fragments.OtherProfileFragment;
import com.example.chatapplication.Utils.ChangeFragment;
import com.example.chatapplication.Models.Kullanicilar;
import com.example.chatapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
   List<String>userKeysList;
    Activity activity;
    Context context;
FirebaseDatabase database;
DatabaseReference reference;
    public UserAdapter(List<String> userKeysList, Activity activity, Context context) {
        this.userKeysList = userKeysList;
        this.activity = activity;
        this.context = context;
        database=FirebaseDatabase.getInstance();
        reference=database.getReference();
    }

    //layout tanımlaması
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(context).inflate(R.layout.userlayout,parent,false);
        return new ViewHolder(view);
    }



    //view setlemeler
    @Override
    public void onBindViewHolder(final @NonNull ViewHolder holder,final int position) {
       // holder.usernameText.setText(userKeysList.get(position).toString());
       reference.child("Kullanıcılar").child(userKeysList.get(position).toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Kullanicilar k1 = snapshot.getValue(Kullanicilar.class);

                Boolean userState=Boolean.parseBoolean(snapshot.child("state").getValue().toString());
                Picasso.get().load(k1.getResim()).into(holder.userimage);
                holder.usernameText.setText(k1.getIsim());

                     if(userState==true){
                        holder.user_state_image.setImageResource(R.drawable.online);
                       }
                       else{
                           holder.user_state_image.setImageResource(R.drawable.offline);
                       }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

       holder.useranalayout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               ChangeFragment fragment=new ChangeFragment(context);
               fragment.changeWithParameter(new OtherProfileFragment(),userKeysList.get(position));
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
        TextView usernameText;
        CircleImageView userimage,user_state_image;
        LinearLayout useranalayout;

        ViewHolder(View itemView){
            super(itemView);
            usernameText=(TextView)itemView.findViewById(R.id.usernameText);
            userimage=(CircleImageView)itemView.findViewById(R.id.userimage);
            user_state_image=(CircleImageView)itemView.findViewById(R.id.user_state_image);

            useranalayout=(LinearLayout)itemView.findViewById(R.id.useranalayout);
        }

}


}
