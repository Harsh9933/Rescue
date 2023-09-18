package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.MyAdapter;
import com.example.myapplication.adapter.SearchUserAdapter;
import com.example.myapplication.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchUserActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    FirebaseUser user;
    private FirebaseAuth mAuth;
    EditText searchInput;
    ImageButton searchButton, back;

    public SearchUserActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchuser);


        searchInput = findViewById(R.id.seach_username_input);
        searchButton = findViewById(R.id.search_user_btn);
        recyclerView = findViewById(R.id.recSearchView);
        back = findViewById(R.id.back);

        searchInput.requestFocus();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTerm = searchInput.getText().toString();
                if (searchTerm.isEmpty() || searchTerm.length() < 3) {
                    searchInput.setError("Invalid Username");
                    return;
                }
                setupSearchRecyclerView(searchTerm);
            }
        });


//        recyclerView =findViewById(R.id.recSearchView);
//
//        mAuth = FirebaseAuth.getInstance();
//
//        user = mAuth.getCurrentUser();
//
//
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),1);
//        recyclerView.setLayoutManager(gridLayoutManager);
//
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
//        builder.setCancelable(false);
//        builder.setView(R.layout.progress_layout);
//        AlertDialog searchDialog = builder.create();
//        searchDialog.show();
//
//
//        List<DataClass> userSearchList = new ArrayList<>();
//        SearchUserAdapter SearchUserAdapter = new SearchUserAdapter(getApplicationContext(), userSearchList);
//        recyclerView.setAdapter(SearchUserAdapter);
//
//        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
//        searchDialog.show();
//        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                userSearchList.clear();
//
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    DataClass dataClass = snapshot.getValue(DataClass.class);
//
//                    userSearchList.add(dataClass);
//                }
//                SearchUserAdapter.notifyDataSetChanged();
//                searchDialog.dismiss();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                searchDialog.dismiss();
//            }
//        });
//
//
//
//    }

    }
    public void setupSearchRecyclerView(String searchTerm){

    }
}

