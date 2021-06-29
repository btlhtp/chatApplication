package com.example.chatapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.chatapplication.Fragments.AnasayfaFragment;
import com.example.chatapplication.Fragments.BildirimFragment;
import com.example.chatapplication.Utils.ChangeFragment;
import com.example.chatapplication.Fragments.KullaniciProfilFragment;
import com.example.chatapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class AnaActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;

   private ChangeFragment changeFragment;
    private BottomNavigationView.OnNavigationItemReselectedListener mOnNavigationItemReselectedListener=new BottomNavigationView.OnNavigationItemReselectedListener() {
        @Override
        public void onNavigationItemReselected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()){
                case R.id.navigation_home:
                    changeFragment.change(new AnasayfaFragment());
                    break;
                case R.id.navigation_dashboard:
                    changeFragment.change(new BildirimFragment());
                    break;
                case R.id.navigation_notifications:
                  changeFragment.change(new KullaniciProfilFragment());
                    break;
                case R.id.navigation_exit:
                    exit();

                    break;

            }
         /*   getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commit();*/

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        tanimla();
        kontrol();

        changeFragment=new ChangeFragment(AnaActivity.this);
        changeFragment.change(new AnasayfaFragment());
        navView.setOnNavigationItemReselectedListener(mOnNavigationItemReselectedListener);
      /*  AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);*/
    }

    public void exit(){
     auth.signOut();
        Intent intent=new Intent(AnaActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
       FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference reference=firebaseDatabase.getReference().child("Kullanıcılar");
        reference.child(user.getUid()).child("state").setValue(false);

    }
    public void tanimla(){
        auth= FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference reference=firebaseDatabase.getReference().child("Kullanıcılar");
        reference.child(user.getUid()).child("state").setValue(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference reference=firebaseDatabase.getReference().child("Kullanıcılar");
        reference.child(user.getUid()).child("state").setValue(true);
    }

    public void kontrol(){
        if(user==null){
            Intent intent=new Intent(AnaActivity.this,RegisterActivity.class);
            startActivity(intent);
            finish();

        }
        else {
            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
            DatabaseReference reference=firebaseDatabase.getReference().child("Kullanıcılar");
            reference.child(user.getUid()).child("state").setValue(true);
        }

}


}