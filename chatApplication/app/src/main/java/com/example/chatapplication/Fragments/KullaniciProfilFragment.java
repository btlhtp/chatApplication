package com.example.chatapplication.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatapplication.Models.Kullanicilar;
import com.example.chatapplication.R;
import com.example.chatapplication.Utils.ChangeFragment;
import com.example.chatapplication.Utils.RandomName;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class KullaniciProfilFragment extends Fragment {
    String imageUrl;
    FirebaseAuth auth;
    FirebaseUser user;
    View view;
    DatabaseReference reference;
    FirebaseDatabase database;
    EditText kullaniciismi, input_egitim, input_dogumtarih, input_hakkimda;
    CircleImageView resim;
    Button btn_kayitol,btn_bilgiarkadas,btn_bilgiistek;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_kullanici_profil, container, false);
        tanimla();
        bilgiGetir();
        return view;
    }

    public void tanimla() {
        btn_bilgiarkadas = (Button) view.findViewById(R.id.btn_bilgiarkadas);
        btn_bilgiistek = (Button) view.findViewById(R.id.btn_bilgiistek);

        kullaniciismi = (EditText) view.findViewById(R.id.kullaniciismi);
        input_egitim = (EditText) view.findViewById(R.id.input_egitim);
        input_dogumtarih = (EditText) view.findViewById(R.id.input_dogumtarih);
        input_hakkimda = (EditText) view.findViewById(R.id.input_hakkimda);
        resim = (CircleImageView) view.findViewById(R.id.profile_image);
        btn_kayitol = (Button) view.findViewById(R.id.btn_kayitol);
        firebaseStorage=FirebaseStorage.getInstance();
          storageReference= FirebaseStorage.getInstance().getReference();
        btn_kayitol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guncelle();
            }
        });
        resim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galeriAc();
            }
        });
        //  storageReference= FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Kullanıcılar").child(user.getUid());

        btn_bilgiarkadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment changeFragment=new ChangeFragment(getContext());
                changeFragment.change(new ArkadaslarFragment());
            }
        });
        btn_bilgiistek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment changeFragment=new ChangeFragment(getContext());
                changeFragment.change(new BildirimFragment());
            }
        });
    }


    private void galeriAc() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 5);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

  if(requestCode==5 && resultCode== Activity.RESULT_OK) {

      Uri filepath = data.getData();
      StorageReference ref = storageReference.child("kullaniciresimleri").child(RandomName.getSaltString()+".jpg");
      ref.putFile(filepath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
          @Override
          public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
              if (task.isSuccessful()) {
                  Toast.makeText(getContext(), "Resim Güncellendi...", Toast.LENGTH_LONG).show();
                  String isim = kullaniciismi.getText().toString();
                  String egitim = input_egitim.getText().toString();
                  String dogum = input_dogumtarih.getText().toString();
                  String hakkimda = input_hakkimda.getText().toString();
                  reference = database.getInstance().getReference().child("Kullanıcılar").child(auth.getUid());
                  Map map = new HashMap();

                  map.put("isim", isim);
                  map.put("egitim", egitim);
                  map.put("dogumtarihi", dogum);
                  map.put("hakkimda", hakkimda);
                  map.put("resim",task.getResult().getMetadata().getReference().getDownloadUrl().toString());
                  reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                      @Override
                      public void onComplete(@NonNull Task<Void> task) {
                          if (task.isSuccessful()) {
                              ChangeFragment fragment = new ChangeFragment(getContext());
                              fragment.change(new KullaniciProfilFragment());
                              Toast.makeText(getContext(), "Bilgiler Başarıyla Güncellendi...", Toast.LENGTH_LONG).show();

                          } else {
                              Toast.makeText(getContext(), "Bilgiler Güncellenemedi...", Toast.LENGTH_LONG).show();
                          }

                      }
                  });

              }
                       /* String isim = kullaniciismi.getText().toString();
                        String egitim = input_egitim.getText().toString();
                        String dogum = input_dogumtarih.getText().toString();
                        String hakkimda = input_hakkimda.getText().toString();
                        reference = database.getReference().child("Kullanıcılar").child(auth.getUid());
                        Map map = new HashMap();
                        map.put("isim", isim);
                        map.put("egitim", egitim);
                        map.put("dogumtarihi", dogum);
                        map.put("hakkimda", hakkimda);
                        map.put("resim", task.getResult().getMetadata().getReference().getDownloadUrl().toString());

                        reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    ChangeFragment fragment = new ChangeFragment(getContext());
                                    fragment.change(new KullaniciProfilFragment());
                                    Toast.makeText(getContext(), "Bilgiler Başarıyla Güncellendi...", Toast.LENGTH_LONG).show();

                                } */else {
                                    Toast.makeText(getContext(), "Bilgiler Güncellenemedi...", Toast.LENGTH_LONG).show();
                                }

        /*                    }
                        });
                    }
                    else {

                        Toast.makeText(getContext(), "Resim Güncellenmedi...", Toast.LENGTH_LONG).show();

                    }
                }
*/
              }

          ;

      });
  }}





        public void bilgiGetir () {

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Kullanicilar k1 = snapshot.getValue(Kullanicilar.class);

                    kullaniciismi.setText(k1.getIsim());
                    input_egitim.setText(k1.getEgitim());
                    input_dogumtarih.setText(k1.getDogumtarihi());
                    input_hakkimda.setText(k1.getHakkimda());
                    imageUrl = k1.getResim();

                    if (!k1.getResim().equals("null")) {
                        Picasso.get().load(k1.getResim()).into(resim);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }
        public void guncelle () {

            String isim = kullaniciismi.getText().toString();
            String egitim = input_egitim.getText().toString();
            String dogum = input_dogumtarih.getText().toString();
            String hakkimda = input_hakkimda.getText().toString();
            reference = database.getInstance().getReference().child("Kullanıcılar").child(auth.getUid());
            Map map = new HashMap();

            map.put("isim", isim);
            map.put("egitim", egitim);
            map.put("dogumtarihi", dogum);
            map.put("hakkimda", hakkimda);
            if (imageUrl.equals("null")) {

                map.put("resim", "null");
            } else {
                map.put("resim", imageUrl);
            }

            reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        ChangeFragment fragment = new ChangeFragment(getContext());
                        fragment.change(new KullaniciProfilFragment());
                        Toast.makeText(getContext(), "Bilgiler Başarıyla Güncellendi...", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getContext(), "Bilgiler Güncellenemedi...", Toast.LENGTH_LONG).show();
                    }

                }
            });

        }

    }