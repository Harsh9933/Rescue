package com.example.myapplication;

import static android.app.Activity.RESULT_OK;
import static com.example.myapplication.register_activity.PICK_IMAGE_REQUEST;

import android.content.Intent;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;

public class bottomSheet extends BottomSheetDialogFragment {

    ImageView imageView;
    AppCompatButton selectButton,uploadButton;
    EditText captionInput;
    Uri uri;
    String imgURL1;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    String imageName;
    public bottomSheet() {
    }


    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.com_bottom_sheet, container, false);

        FirebaseApp.initializeApp(requireContext());

        imageView = view.findViewById(R.id.profilePic_com);
        selectButton = view.findViewById(R.id.selectButton_com);
        captionInput = view.findViewById(R.id.caption_com);
        uploadButton = view.findViewById(R.id.upload_com);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("ComImages");

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,PICK_IMAGE_REQUEST);


            }
        });

//        if(uri != null){
//            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("community_images");
//            String imageName =  UUID.randomUUID().toString() + "_profile.jpg";
//            StorageReference imageRef = storageRef.child(imageName);
//
//            UploadTask uploadTask = imageRef.putFile(uri);
//            uploadTask.addOnSuccessListener(taskSnapshot -> {
//                Uri downloadUrl = taskSnapshot.getUploadSessionUri();
//
//                imgURL1 = downloadUrl.toString();
//                Log.e("bottomsheet",uri.toString());
//
//
//                if (downloadUrl != null) {
//                    imgURL1 = downloadUrl.toString();
//                    Log.e("bottomsheet", imgURL1);
//
//                    // Now that you have the URL, you can proceed with the uploadButton click logic here
//                    String uid = databaseReference.push().getKey();
//                    HashMap<String, String> comMap = new HashMap<>();
//                    comMap.put("profileImageURL", imgURL1);
//                    databaseReference.child("ComImages").child(uid).setValue(comMap);
//
//                    Toast.makeText(requireContext(), "Posted!!", Toast.LENGTH_SHORT).show();
//                    Log.e("profileImageURL", imgURL1 + "sheeesh");
//
//                    dismiss();
//                } else {
//                    Log.e("bottomsheet", "Download URL is null");
//                }
//            }).addOnFailureListener(e -> {
//                Log.e("ImageUpload", "Image upload failed: " + e.getMessage());
//            });
//        } else {
//            Log.e("uri", "null uri");
//        }
////
////
////            }).addOnFailureListener(e -> {
////                Log.e("ImageUpload", "Image upload failed: " + e.getMessage());
////            });
////
////        }else{
////            Log.e("uri","null uri");
////        }
//
//        uploadButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String uid = databaseReference.push().getKey();
//                HashMap<String, String> comMap = new HashMap<>();
//                comMap.put("profileImageURL", imgURL1);
//                databaseReference.child("ComImages").child(uid).setValue(comMap);
//
//                Toast.makeText(requireContext(), "Posted!!", Toast.LENGTH_SHORT).show();
//                Log.e("profileImageURL",imgURL1 + "sheeesh");
//
//                dismiss();
//            }
//        });



        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            uri = selectedImageUri;


            Glide.with(requireContext()).load(selectedImageUri).into(imageView);
            uploadImage();
        }
    }

    private void uploadImage() {
        if (uri != null) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("community_images");
            imageName =  UUID.randomUUID().toString() + "_profile.jpg";
            StorageReference imageRef = storageRef.child(imageName);

            UploadTask uploadTask = imageRef.putFile(uri);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                Uri downloadUrl = taskSnapshot.getUploadSessionUri();

                if (downloadUrl != null) {
                    imgURL1 = downloadUrl.toString();
                    Log.e("bottomsheet", imgURL1);

                    // Now that you have the URL, you can proceed with the uploadButton click logic here
                    String uid = databaseReference.push().getKey();
                    HashMap<String, String> comMap = new HashMap<>();
                    comMap.put("imageUrl", imgURL1);
                    databaseReference.child("ComImages").child(uid).setValue(comMap);

                    Toast.makeText(requireContext(), "Posted!!", Toast.LENGTH_SHORT).show();
                    Log.e("profileImageURL", imgURL1 + "sheeesh");

                    dismiss();
                } else {
                    Log.e("bottomsheet", "Download URL is null");
                }
            }).addOnFailureListener(e -> {
                Log.e("ImageUpload", "Image upload failed: " + e.getMessage());
            });
        } else {
            Log.e("uri", "null uri");
        }
    }


}
