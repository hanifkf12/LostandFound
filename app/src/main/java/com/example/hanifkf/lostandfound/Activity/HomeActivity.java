package com.example.hanifkf.lostandfound.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.hanifkf.lostandfound.Fragment.HomeFragment;
import com.example.hanifkf.lostandfound.Login.MainActivity;
import com.example.hanifkf.lostandfound.R;
import com.example.hanifkf.lostandfound.Utils.BottomNavigationViewHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Context mContext=HomeActivity.this;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private String user_id=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: started");
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            setupBottomNavigationView();
            setupFragment();
        }else {
            finish();
        }

        firebaseAuth=FirebaseAuth.getInstance();
        user_id=firebaseAuth.getCurrentUser().getUid();
        storageReference= FirebaseStorage.getInstance().getReference();
        firebaseFirestore=FirebaseFirestore.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        if(task.getResult().exists()){
                            //isi jika ada
                        }else {
                            Intent edit=new Intent(getApplicationContext(),EditProfileActivity.class);
                            startActivity(edit);
                        }
                    }else {
                        String error=task.getException().getMessage();
                        Toast.makeText(getApplicationContext(),"FireStore Retrieve Eror "+error,Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Intent login= new Intent(getApplicationContext(), MainActivity.class);
            startActivity(login);
            Toast.makeText(getApplicationContext(),"Anda Belum Login",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    //FragmentHomesetup
    public void setupFragment(){
        Fragment homeFragment=null;
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        homeFragment= new HomeFragment();
        fragmentManager.beginTransaction().replace(R.id.content,homeFragment).commit();
    }

    //Bottom Navigation Setup
    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationVIew");
        BottomNavigationViewEx bottomNavigationViewEx=(BottomNavigationViewEx)findViewById(R.id.bottom_nav_view);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext,this,bottomNavigationViewEx);
        Menu menu=bottomNavigationViewEx.getMenu();
        MenuItem menuItem=menu.getItem(0);
        menuItem.setChecked(true);
    }

}
