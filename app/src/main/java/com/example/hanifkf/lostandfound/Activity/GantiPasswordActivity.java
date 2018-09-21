package com.example.hanifkf.lostandfound.Activity;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hanifkf.lostandfound.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GantiPasswordActivity extends AppCompatActivity {
    private EditText passlama,pass1,pass2;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ImageView back,save;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganti_password);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        progressDialog=new ProgressDialog(this);
        passlama=(EditText)findViewById(R.id.password_lama);
        pass1=(EditText)findViewById(R.id.password_baru1);
        pass2=(EditText)findViewById(R.id.password_baru2);
        back=(ImageView)findViewById(R.id.back_ganti);
        save=(ImageView)findViewById(R.id.save_pass_baru);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passw1 = pass1.getText().toString();
                String passw2 = pass2.getText().toString();
                String passwlama=passlama.getText().toString();
                if (!passwlama.isEmpty() && passw1.equals(passw2)) {
                    progressDialog.setTitle("Mohon Tunggu...");
                    progressDialog.show();
                    mUser.updatePassword(passw2).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(GantiPasswordActivity.this, "Kata Sandi Berhasil Diganti", Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(GantiPasswordActivity.this, "Kata Sandi Gagal Diganti", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(GantiPasswordActivity.this, "Kata Sandi Tidak Cocok", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        progressDialog.dismiss();
        progressDialog=null;
        finish();
    }
}
