package com.example.hanifkf.lostandfound.Activity;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPass extends AppCompatActivity {

    private TextView login;
    private EditText email_pass;
    private Button reset;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseUser current_user;
    private static final String TAG = "RegisterActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        progressDialog=new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        login=(TextView)findViewById(R.id.txt_login_reset);
        email_pass=(EditText)findViewById(R.id.forgot_pass_email);
        reset=(Button)findViewById(R.id.reset_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(ForgotPass.this, MainActivity.class);
                startActivity(in);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setTitle("Mengirim, Mohon Tunggu...");
                progressDialog.show();
                String email=email_pass.getText().toString();
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ForgotPass.this, "Silahkan Cek Email Anda Untuk Setel Ulang Password", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onComplete: reset send to email");
                            sendToLogin();
                            finish();
                        }else {
                            Toast.makeText(ForgotPass.this, "Gagal Mengirim Email", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "onComplete: reset fail", task.getException() );
                        }
                    }
                });
            }
        });
    }
    private void sendToLogin(){
        Intent in=new Intent(ForgotPass.this, MainActivity.class);
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
