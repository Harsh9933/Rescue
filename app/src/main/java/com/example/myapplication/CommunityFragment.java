package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.adapter.ImageAdapter;
import com.example.myapplication.adapter.MyAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class CommunityFragment extends Fragment {


    public CommunityFragment() {
        // Required empty public constructor
    }
    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    ValueEventListener valueEventListener;
    FirebaseAuth mAuth;
    FirebaseUser user;
    List<ImageItem> imageList;
    DatabaseReference databaseReference;
    ImageAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_community, container, false);
        floatingActionButton = view.findViewById(R.id.floatButton);
        recyclerView = view.findViewById(R.id.rec_com);

        mAuth = FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        FirebaseApp.initializeApp(requireContext());

        imageList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        adapter = new ImageAdapter(requireContext(),imageList);
        recyclerView.setAdapter(adapter);


        databaseReference = FirebaseDatabase.getInstance().getReference("ComImages").child("ComImages");
        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                imageList.clear();


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ImageItem imageItem = snapshot.getValue(ImageItem.class);
                    if (imageItem != null && imageItem.getImageUrl() != null) {
                        imageList.add(imageItem);
                    } else {
                        Log.e("FirebaseData", "Invalid ImageItem data: " + snapshot.getKey());
                    }
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet bottomSheet = new bottomSheet();
                bottomSheet.show(requireActivity().getSupportFragmentManager(), "Tag");
            }
        });
        return view;

    }
//    public void updateImageList(List<ImageItem> newList) {
//        imageList.clear();
//        imageList.addAll(newList);
//        adapter.notifyDataSetChanged();
//    }
}
