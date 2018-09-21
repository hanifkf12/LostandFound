package com.example.hanifkf.lostandfound.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.hanifkf.lostandfound.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class EditProfileActivity extends AppCompatActivity {

    private Uri mainImageURI = null;
    private boolean isChanged = false;

    ImageView back,save;
    CircleImageView setup_image;
    EditText nama,alamat,hp;
    ProgressBar setup_progress;

    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private String user_id=null;
    private Bitmap compressedImageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        back= (ImageView)findViewById(R.id.back_edit);
        save= (ImageView)findViewById(R.id.save_setup_profile);
        nama=(EditText)findViewById(R.id.setup_name);
        alamat=(EditText)findViewById(R.id.setup_alamat);
        hp=(EditText)findViewById(R.id.setup_hp);
        firebaseAuth=FirebaseAuth.getInstance();
        user_id=firebaseAuth.getCurrentUser().getUid();
        setup_progress=(ProgressBar)findViewById(R.id.progress_setup);
        storageReference= FirebaseStorage.getInstance().getReference();
        firebaseFirestore=FirebaseFirestore.getInstance();

        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        String namaku=task.getResult().getString("name");
                        String imageku=task.getResult().getString("image");
                        String alamatku=task.getResult().getString("alamat");
                        String hpku=task.getResult().getString("hp");

                        nama.setText(namaku);
                        alamat.setText(alamatku);
                        hp.setText(hpku);
                        mainImageURI=Uri.parse(imageku);
                        RequestOptions placeHolder=new RequestOptions();
                        placeHolder.placeholder(R.drawable.noimage);
                        Glide.with(EditProfileActivity.this).setDefaultRequestOptions(placeHolder).load(imageku).into(setup_image);
                    }else {
                        Toast.makeText(getApplicationContext(),"Lengkapi Profil Anda",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    String error=task.getException().getMessage();
                    Toast.makeText(getApplicationContext(),"FireStore Retrieve Eror "+error,Toast.LENGTH_SHORT).show();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setup_image=(CircleImageView)findViewById(R.id.setup_image);
        setup_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               imagePicker();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String randomName = UUID.randomUUID().toString();
                final String namauser=nama.getText().toString();
                final String alamat_user=alamat.getText().toString();
                final String no_hp=hp.getText().toString();
                if(isChanged) {
                    if (!TextUtils.isEmpty(namauser) && !TextUtils.isEmpty(alamat_user) && !TextUtils.isEmpty(no_hp) && mainImageURI != null) {
                        setup_progress.setVisibility(View.VISIBLE);
                        File newImageFile=new File(mainImageURI.getPath());
                        try {
                            compressedImageFile=new Compressor(EditProfileActivity.this)
                                    .setMaxHeight(200)
                                    .setMaxWidth(200)
                                    .setQuality(10)
                                    .compressToBitmap(newImageFile);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        ByteArrayOutputStream baos=new ByteArrayOutputStream();
                        compressedImageFile.compress(Bitmap.CompressFormat.JPEG,100,baos);
                        byte [] data=baos.toByteArray();

                        UploadTask uploadTask=storageReference.child("profile_images").child(user_id+".jpg").putBytes(data);
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                storeFireStore(taskSnapshot,namauser,alamat_user,no_hp);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //Penanganan erorr
                            }
                        });
                    }
                }else {
                    storeFireStore(null, namauser, alamat_user, no_hp);
                }
            }
        });

    }

    private void storeFireStore(UploadTask.TaskSnapshot taskSnapshot ,String namauser,String alamat_user,String no_hp) {
        Uri download_uri;
        if(taskSnapshot!=null) {
            download_uri = taskSnapshot.getDownloadUrl();
        }
        else {
            download_uri = mainImageURI;
        }
        Map<String,String> userMap=new HashMap<>();
        userMap.put("name",namauser);
        userMap.put("alamat",alamat_user);
        userMap.put("hp",no_hp);
        userMap.put("image",download_uri.toString());

        firebaseFirestore.collection("Users").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    finish();

                    Toast.makeText(getApplicationContext(),"Save Profile ",Toast.LENGTH_SHORT).show();
                }else{
                    String error=task.getException().getMessage();
                    Toast.makeText(getApplicationContext(),"FireStore Eror "+error,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void imagePicker(){
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(EditProfileActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageURI = result.getUri();
                setup_image.setImageURI(mainImageURI);
                isChanged = true;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

}
