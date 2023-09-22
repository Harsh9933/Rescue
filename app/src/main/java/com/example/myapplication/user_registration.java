package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class user_registration extends AppCompatActivity {

    private EditText editEmail , editPass , editName , editAddress;
    private AppCompatButton button,imgButton;
    private FirebaseAuth mAuth;
    private ImageButton back;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    ImageView imageView;
    String imgURL;
    Uri uri;
    public static final int PICK_IMAGE_REQUEST = 1;
    Button getLocation;
    FusedLocationProviderClient fusedLocationProviderClient;



    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);


        editName = findViewById(R.id.username);
        editEmail  = findViewById(R.id.useEmail);
        editPass = findViewById(R.id.userpassword);
        editAddress = findViewById(R.id.useraddress);



        getLocation = findViewById(R.id.usergetLocation);


        back = findViewById(R.id.back);
        imageView = findViewById(R.id.profilePic);
        imgButton = findViewById(R.id.selectButton);



        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        button = findViewById(R.id.registration);
        mAuth = FirebaseAuth.getInstance();


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference  = firebaseDatabase.getReference();

        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,PICK_IMAGE_REQUEST);
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                String name , email , password ,address ;




                name = String.valueOf(editName.getText());
                email = String.valueOf(editEmail.getText());
                password = String.valueOf(editPass.getText());
                address = String.valueOf(editAddress.getText());

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    uploadImageAndRegisterUser(name, email, address);
                                    Toast.makeText(user_registration.this, "database Added", Toast.LENGTH_SHORT).show();

//                                    databaseReference.child("Users").child(name).setValue(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                } else {
                                    Log.d("mine", "Account Registration Failed");
                                    Toast.makeText(user_registration.this, "Account Registration Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });




            }
        });
    }
    private void getLocation() {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        Geocoder geocoder = new Geocoder(user_registration.this, Locale.getDefault());
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        Log.d("Location", "Latitude: " + latitude + ", Longitude: " + longitude);
                        List<Address> address = null;
                        try {
                            address = geocoder.getFromLocation(latitude, longitude, 1);
                            editAddress.setText(address.get(0).getAddressLine(0));
                        } catch (IOException e) {
                            Log.e("Current Location", "Error");
                        }

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("Location","NO last location");
                }
            });
        }else {
            askPermission();
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(user_registration.this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION},100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == 100){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLocation();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            imageView.setImageURI(uri);
        }else {
            Toast.makeText(this, "required permission", Toast.LENGTH_SHORT).show();
        }
    }
    private void uploadImageAndRegisterUser(String name, String email, String address) {
        if (uri != null) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("profile_images");
            String imageName = name + "_profile.jpg";
            StorageReference imageRef = storageRef.child(imageName);


            imageRef.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadUri) {
                                    imgURL = downloadUri.toString();
                                    String uid = databaseReference.child("consumer").push().getKey();
                                    // Add user data to Firebase Realtime Database
                                    HashMap<String, String> userMap = new HashMap<>();
                                    userMap.put("name", name);
                                    userMap.put("email", email);
                                    userMap.put("address", address);
                                    userMap.put("profileImageURL", imgURL);
                                    userMap.put("UID",uid);

                                    databaseReference.child("consumer").child(uid).setValue(userMap)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(user_registration.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(getApplicationContext(), login_activity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(user_registration.this, "Database write failed", Toast.LENGTH_SHORT).show();
                                                    Log.e("FirebaseError", "Database write failed: " + e.getMessage());
                                                }
                                            });
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(user_registration.this, "Image upload failed.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // No image selected, continue with user registration without an image

            String uid = databaseReference.child("consumer").push().getKey();
            HashMap<String, String> userMap = new HashMap<>();
            userMap.put("name", name);
            userMap.put("email", email);
            userMap.put("address", address);
            userMap.put("UID",uid);

            databaseReference.child("consumer").child(uid).setValue(userMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(user_registration.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), login_activity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(user_registration.this, "Database write failed", Toast.LENGTH_SHORT).show();
                            Log.e("FirebaseError", "Database write failed: " + e.getMessage());
                        }
                    });
        }
    }
}