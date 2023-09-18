package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;

public class MapsFragment extends Fragment {
    private GoogleMap mMap;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
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
                                Geocoder geocoder = new Geocoder(requireContext());
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
                                    Log.d("map", "GeoCoding");
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
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}

