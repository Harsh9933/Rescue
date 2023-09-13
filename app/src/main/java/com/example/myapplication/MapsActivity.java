package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.myapplication.databinding.ActivityMapsBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    Location location;
    BottomNavigationView nav;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);



        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        nav = findViewById(R.id.nav_bar);
        nav.setSelectedItemId(R.id.map);

        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if   (id == R.id.home) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.map) {



                }
                return true;
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // address into latLong

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null && snapshot.getValue() != null) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        // Get the address field from each user node
                        String address = userSnapshot.child("address").getValue(String.class);
                        if (address != null) {
                            Geocoder geocoder = new Geocoder(getApplicationContext());
                            List<Address> addressList;

                            double lat = 0;
                            double lon = 0;

                            try {
                                addressList = geocoder.getFromLocationName(address, 1);
                                if (addressList != null) {
                                    lat = addressList.get(0).getLatitude();
                                    lon = addressList.get(0).getLongitude();
                                } else {
                                    Log.d("Geocoding", "No results found for the given address.");

                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            LatLng markerLatLng = new LatLng(lat, lon);
                            MarkerOptions marker = new MarkerOptions().position(new LatLng(lat, lon)).title("Test");
                            mMap.addMarker(marker);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng, 14));
                        } else {
                            Log.d("Firebase", "Address is null");
                        }
                    }
                } else {
                    Log.d("Firebase", "Users node is empty");
                }
            }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.d("Firebase", "DatabaseError: " + error.getMessage());
        }
        });



        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//
//        LatLng delhi = new LatLng(28.6442852, 76.9284231);
//        mMap.addMarker(new MarkerOptions().position(delhi).title("Marker in delhi"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(delhi));
    }

}