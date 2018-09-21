package com.example.hanifkf.lostandfound.Activity;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hanifkf.lostandfound.Login.MainActivity;
import com.example.hanifkf.lostandfound.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser current_user;
    private Button register;
    private TextView loginhere;
    private ProgressDialog progressDialog;
    private EditText emailtxt,password,repeatpassword;
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        register=(Button)findViewById(R.id.regist_button);
        loginhere=(TextView)findViewById(R.id.txt_login);
        emailtxt=(EditText)findViewById(R.id.regist_email);
        password=(EditText)findViewById(R.id.regist_password);
        repeatpassword=(EditText)findViewById(R.id.regist_repeat_password);

        loginhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToLogin();
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailString=emailtxt.getText().toString();
                String passwordString=password.getText().toString();
                String passwordRepeatString=repeatpassword.getText().toString();
                if (passwordString.equals(passwordRepeatString)) {
                    progressDialog.setTitle("Mendaftar, Mohon Tunggu...");
                    progressDialog.show();
                    mAuth.createUserWithEmailAndPassword(emailString,passwordRepeatString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Log.d(TAG, "onComplete: Pendaftaran Berhasil");
                                current_user=mAuth.getCurrentUser();
                                if(!current_user.isEmailVerified()){
                                    current_user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Log.d(TAG, "onComplete: Pendaftaran Berhasil");
                                                Toast.makeText(getApplicationContext(),"Email Verifikasi telah dikirim",Toast.LENGTH_SHORT).show();

                                                sendToLogin();
                                                finish();

                                            }else {
                                                Toast.makeText(getApplicationContext(),"Email Verifikasi gagal dikirim",Toast.LENGTH_SHORT).show();
                                                Log.e(TAG, "onComplete: Pendaftaran Gagal", task.getException());
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });

                }else {
                    Toast.makeText(getApplicationContext(),"Password Tidak Sama",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void sendToLogin(){
        Intent in=new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(in);
    }
    @Override
    protected void onStop() {
        super.onStop();
        progressDialog.dismiss();
        progressDialog=null;
        finish();
    }

}
