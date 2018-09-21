package com.example.hanifkf.lostandfound.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hanifkf.lostandfound.Activity.HomeActivity;
import com.example.hanifkf.lostandfound.Activity.RegisterActivity;
import com.example.hanifkf.lostandfound.Activity.ForgotPass;
import com.example.hanifkf.lostandfound.R;
import com.example.hanifkf.lostandfound.Utils.Permissions;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private static final String TAG = "MainActivity";
    private static final int VERIFY_PERMISSIONS_REQUEST = 1;
    LinearLayout linearLayout, linearLayout2;
    Button button;
    TextView register,forget;
    Animation uptodown, downtoup;
    EditText username,password;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private static final int RC_SIGN_IN=1;
    private GoogleApiClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        register=(TextView)findViewById(R.id.txt_register);
        forget=(TextView)findViewById(R.id.txt_forget);
        button = (Button)findViewById(R.id.button);
        linearLayout = (LinearLayout) findViewById(R.id.linear);
        //linearLayout2 = (LinearLayout) findViewById(R.id.linear2);
        uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);
        linearLayout.setAnimation(uptodown);
        username=(EditText)findViewById(R.id.login_username);
        password=(EditText)findViewById(R.id.login_password);
      //  linearLayout2.setAnimation(downtoup);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(in);
                //finish();
            }
        });
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(MainActivity.this, ForgotPass.class);
                startActivity(in);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 String loginusername=username.getText().toString();
                 String loginpassword=password.getText().toString();
                 if(!TextUtils.isEmpty(loginpassword)&&!TextUtils.isEmpty(loginusername)){
                     progressDialog.setTitle("Masuk, Mohon Tunggu...");
                     progressDialog.show();
                    mAuth.signInWithEmailAndPassword(loginusername, loginpassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        user=mAuth.getCurrentUser();
                                        if(user.isEmailVerified()){

                                            sendToHome();
                                            Toast.makeText(getApplicationContext(), "Login Sukses",
                                                    Toast.LENGTH_SHORT).show();
                                        }else {
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "Email Belum diVerifikasi",
                                                    Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        progressDialog.dismiss();
                                        // If sign in fails, display a message to the user.
                                        Log.e(TAG, "signInWithEmail:failure", task.getException());
                                       Toast.makeText(getApplicationContext(), "Login Gagal",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });


                }
            }
            });
    }




    @Override
    protected void onStart() {
        super.onStart();
        if(checkPermissionsArray(Permissions.PERMISSIONS)){
            return;

        }else{
            verifyPermissions(Permissions.PERMISSIONS);
        }
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null&&currentUser.isEmailVerified()){
            sendToHome();
        }
        else{

        }

    }
    public void sendToHome(){
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        progressDialog.dismiss();
        progressDialog=null;
        finish();
    }

    public void verifyPermissions(String[] permissions){
        Log.d(TAG, "verifyPermissions: verifying permissions.");

        ActivityCompat.requestPermissions(
                MainActivity.this,
                permissions,
                VERIFY_PERMISSIONS_REQUEST
        );
    }

    public boolean checkPermissionsArray(String[] permissions){
        Log.d(TAG, "checkPermissionsArray: checking permissions array.");

        for(int i = 0; i< permissions.length; i++){
            String check = permissions[i];
            if(!checkPermissions(check)){
                return false;
            }
        }
        return true;
    }

    public boolean checkPermissions(String permission){
        Log.d(TAG, "checkPermissions: checking permission: " + permission);

        int permissionRequest = ActivityCompat.checkSelfPermission(MainActivity.this, permission);

        if(permissionRequest != PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "checkPermissions: \n Permission was not granted for: " + permission);
            return false;
        }
        else{
            Log.d(TAG, "checkPermissions: \n Permission was granted for: " + permission);
            return true;
        }
    }

}
