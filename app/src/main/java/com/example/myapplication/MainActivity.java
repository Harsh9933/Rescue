package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;




public class MainActivity extends AppCompatActivity {
    ImageButton button , search;
    FirebaseAuth mAuth;
    FirebaseUser user;
    BottomNavigationView nav;
//    RecyclerView recyclerView;
//    List<DataClass> dataList;
//    DatabaseReference databaseReference;
//    ValueEventListener eventListener;
    MapsFragment mapsFragment;
    profileFragment profileFragment;
    chat_fragment chatFragment;
    RecycclerFragment recycclerFragment;
    CommunityFragment communityFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.back);
        search = findViewById(R.id.search);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if(user == null){
            Intent intent = new Intent(this,login_activity.class);
            startActivity(intent);
            finish();
        }

        mapsFragment = new MapsFragment();
        profileFragment = new profileFragment();
        chatFragment = new chat_fragment();
        recycclerFragment = new RecycclerFragment();
        communityFragment = new CommunityFragment();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(),login_activity.class);
                startActivity(intent);
                finish();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SearchUserActivity.class);
                startActivity(intent);
                finish();
            }
        });

        nav = findViewById(R.id.nav_bar);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if   (id == R.id.home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, recycclerFragment).commit();

                } else if (id == R.id.map) {
                   getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,mapsFragment).commit();

                } else if (id==R.id.community) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,communityFragment).commit();

                } else if (id  == R.id.chat) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,chatFragment).commit();
                } else if (id==R.id.profile) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,profileFragment).commit();

                }
                return true;
            }
        });
        nav.setSelectedItemId(R.id.home);

    }
}