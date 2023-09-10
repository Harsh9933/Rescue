package com.example.myapplication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityLoginBinding;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class register_activity extends AppCompatActivity {

    private EditText editEmail , editPass , editName , editAddress , editNoOfMem , editExpertise ;
    private Spinner myspinner;
    private Button button;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;


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
        setContentView(R.layout.activity_register);

        progressBar = findViewById(R.id.progressBar);

        editName = findViewById(R.id.name);
        editEmail  = findViewById(R.id.email);
        editPass = findViewById(R.id.password);
        editAddress = findViewById(R.id.address);
        editNoOfMem = findViewById(R.id.noOfMem);
        myspinner  = (Spinner) findViewById(R.id.spinner);
        String editExpertise = String.valueOf(myspinner.getSelectedItem());

        button = findViewById(R.id.registration);
        mAuth = FirebaseAuth.getInstance();


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference  = firebaseDatabase.getReference();

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.expertise));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myspinner.setAdapter(myAdapter);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                progressBar.setVisibility(View.VISIBLE);
                String name , email , password ,address , noOfMem , expertise ;


                name = String.valueOf(editName.getText());
                email = String.valueOf(editEmail.getText());
                password = String.valueOf(editPass.getText());
                address = String.valueOf(editAddress.getText());
                noOfMem = String.valueOf(editNoOfMem.getText());
                expertise = editExpertise;

                HashMap<String , String> userMap = new HashMap<>();

                userMap.put("name" , name);
                userMap.put("email" , email);
                userMap.put("address" , address);
                userMap.put("noOfMem" , noOfMem);
                userMap.put("expertise" , expertise);

//                databaseReference.child("Users").child(name).setValue(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        Toast.makeText(register_activity.this, "database Added", Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(register_activity.this, "databse not added", Toast.LENGTH_SHORT).show();
//                        Log.e("FirebaseError", "Database write failed: " + e.getMessage());                    }
//                });
//
//                if(TextUtils.isEmpty(email)){
//                    Toast.makeText(register_activity.this, "Enter Email Address!!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if(TextUtils.isEmpty(password)){
//                    Toast.makeText(register_activity.this, "Enter PassWord!!", Toast.LENGTH_SHORT).show();
//                    return;
//                }




                //Registration Authentication
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {


                                    databaseReference.child("Users").child(name).setValue(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(register_activity.this, "database Added", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(register_activity.this, "databse not added", Toast.LENGTH_SHORT).show();
                                            Log.e("FirebaseError", "Database write failed: " + e.getMessage());                    }
                                    });




                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(register_activity.this, "Account Registered Successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(),login_activity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.d("mine","Account Registration Failed");
                                    Toast.makeText(register_activity.this, "Account Registration Failed", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });


    }
}