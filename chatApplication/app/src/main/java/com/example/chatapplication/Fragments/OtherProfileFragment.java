package com.example.chatapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapplication.Activity.ChatActivity;
import com.example.chatapplication.Models.Kullanicilar;
import com.example.chatapplication.R;
import com.example.chatapplication.Utils.ShowToastMessage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import de.hdodenhof.circleimageview.CircleImageView;


public class OtherProfileFragment extends Fragment {


    View view;
    String otherID, userID;
    TextView userprofilename, userprofileEgitim, userprofildogum, userprofilehakkimda, userprofilename2, userProfileArkadas, userProfileTakip;
    ImageView userprofilTakipsay, userprofilArkadassay, userprofilArkadasEkle, userprofilMesajat, userprofilTakipEt;
    CircleImageView profile_image_kullanici;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference, reference_Ark;
    FirebaseAuth auth;
    FirebaseUser user;
    String kontrol = "", begenikontrol = "";
    ShowToastMessage showToastMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        tanimla();
        action();
        getbegeniText();
        getArkadasText();
        return view;
    }

    public void tanimla() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        reference_Ark = firebaseDatabase.getReference().child("Arkadaslik_istek");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userID = user.getUid();
        otherID = getArguments().getString("userid");

        userProfileArkadas = (TextView) view.findViewById(R.id.userProfileArkadas);
        userProfileTakip = (TextView) view.findViewById(R.id.userProfileTakip);
        userprofilename = (TextView) view.findViewById(R.id.userprofilename);
        userprofileEgitim = (TextView) view.findViewById(R.id.userprofileEgitim);
        userprofildogum = (TextView) view.findViewById(R.id.userprofildogum);
        userprofilehakkimda = (TextView) view.findViewById(R.id.userprofilehakkimda);
        userprofilename2 = (TextView) view.findViewById(R.id.userprofilename2);
        userprofilTakipsay = (ImageView) view.findViewById(R.id.userprofilTakipsay);
        userprofilArkadassay = (ImageView) view.findViewById(R.id.userprofilArkadassay);
        userprofilArkadasEkle = (ImageView) view.findViewById(R.id.userprofilArkadasEkle);
        userprofilMesajat = (ImageView) view.findViewById(R.id.userprofilMesajat);
        userprofilTakipEt = (ImageView) view.findViewById(R.id.userprofilTakipEt);
        profile_image_kullanici = (CircleImageView) view.findViewById(R.id.profile_image_kullanici);
        reference_Ark.child(otherID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(userID)) {
                    kontrol = "istek";
                    userprofilArkadasEkle.setImageResource(R.drawable.sil);
                } else {

                    userprofilArkadasEkle.setImageResource(R.drawable.ark_ekle);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference.child("Arkadaslar").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(otherID)) {
                    kontrol = "arkadas";
                    userprofilArkadasEkle.setImageResource(R.drawable.delete);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference.child("Begeniler").child(otherID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(userID)) {
                    begenikontrol = "begendi";
                    userprofilTakipEt.setImageResource(R.drawable.on);
                } else {
                    userprofilTakipEt.setImageResource(R.drawable.takip_off);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        showToastMessage = new ShowToastMessage(getContext());
    }

    private void arkadasTabloCikar(final String otherID, final String userID) {
        reference.child("Arkadaslar").child(otherID).child(userID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                reference.child("Arkadaslar").child(userID).child(otherID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        kontrol = "";
                        userprofilArkadasEkle.setImageResource(R.drawable.ark_ekle);
                        showToastMessage.showToast("Arkadaşlıktan Çıkarıldı.");
                        getArkadasText();


                    }
                });
            }
        });
    }

    public void action() {
        reference.child("Kullanıcılar").child(otherID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Kullanicilar k1 = snapshot.getValue(Kullanicilar.class);
                userprofilename.setText("İsim:" + k1.getIsim());
                userprofileEgitim.setText("Eğitim:" + k1.getEgitim());
                userprofildogum.setText("Doğum Tarihi:" + k1.getDogumtarihi());
                userprofilehakkimda.setText("Hakkımda:" + k1.getHakkimda());
                userprofilename2.setText(k1.getIsim());

                if (!k1.getResim().equals("null")) {
                    Picasso.get().load(k1.getResim()).into(profile_image_kullanici);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        userprofilArkadasEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kontrol.equals("istek")) {
                    arkadasIptal(otherID, userID);
                } else if (kontrol.equals("arkadas")) {
                    arkadasTabloCikar(otherID, userID);
                } else {
                    arkadasEkle(otherID, userID);
                }
            }
        });

        userprofilTakipEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (begenikontrol.equals("begendi")) {
                    begeniIptal(userID, otherID);
                } else {
                    begen(userID, otherID);
                }
            }
        });
        userprofilMesajat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("userName",userprofilename2.getText().toString());
                intent.putExtra("id",otherID);
                startActivity(intent);
            }
        });
    }

    private void begeniIptal(String userID, String otherID) {
        reference.child("Begeniler").child(otherID).child(userID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                userprofilTakipEt.setImageResource(R.drawable.takip_off);
                begenikontrol = "";
                showToastMessage.showToast("Beğenme iptal edildi.");
                getbegeniText();


            }
        });
    }

    private void arkadasIptal(final String otherID, String userID) {
        reference_Ark.child(otherID).child(userID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                reference_Ark.child(userID).child(otherID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        kontrol = "";
                        userprofilArkadasEkle.setImageResource(R.drawable.ark_ekle);
                        showToastMessage.showToast("Arkadaşlık isteği iptal edildi.");


                    }
                });
            }
        });
    }

    public void arkadasEkle(String otherID, String userID) {
        reference_Ark.child(userID).child(otherID).child("tip").setValue("gonderdi").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    kontrol = "istek";
                    reference_Ark.child(otherID).child(userID).child("tip").setValue("aldi").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Arkadaşlık isteği başarıyla gönderildi", Toast.LENGTH_SHORT).show();
                                userprofilArkadasEkle.setImageResource(R.drawable.sil);


                            } else {
                                Toast.makeText(getContext(), "Bir problem meydana geldi", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Bir problem meydana geldi", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void begen(String userID, String otherID) {
        reference.child("Begeniler").child(otherID).child(userID).child("tip").setValue("begendi").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    showToastMessage.showToast("Profili Beğendiniz");
                    userprofilTakipEt.setImageResource(R.drawable.on);
                    begenikontrol = "begendi";
                    getbegeniText();
                }
            }
        });
    }

    public void getbegeniText() {

       // userProfileTakip.setText("0 Beğeni");
        reference.child("Begeniler").child(otherID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userProfileTakip.setText(snapshot.getChildrenCount() + "Beğeni");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void getArkadasText() {
       // userProfileArkadas.setText("0 Arkadaş");
        reference.child("Arkadaslar").child(otherID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userProfileArkadas.setText(snapshot.getChildrenCount() + "Arkadaş");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

