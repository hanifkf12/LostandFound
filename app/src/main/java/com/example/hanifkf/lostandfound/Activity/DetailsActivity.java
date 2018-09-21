package com.example.hanifkf.lostandfound.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hanifkf.lostandfound.Adapter.CommentsAdapter;
import com.example.hanifkf.lostandfound.Models.Comments;
import com.example.hanifkf.lostandfound.R;
import com.example.hanifkf.lostandfound.Adapter.DetailsAdapter;
import com.example.hanifkf.lostandfound.Models.Details;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailsActivity extends AppCompatActivity {

    ImageView back;
    private ImageView sendcomment;
    private EditText comment;
    private FirebaseFirestore firebaseFirestore;
    private String commentar;
    private FirebaseAuth mAuth;
    private String current_user;
    private ArrayList<Comments> commentsArrayList;
    private RecyclerView recyclerViewComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        firebaseFirestore=FirebaseFirestore.getInstance();
        sendcomment=(ImageView)findViewById(R.id.send_komentar);
        comment=(EditText) findViewById(R.id.komentar);
        mAuth=FirebaseAuth.getInstance();

        recyclerViewComments = (RecyclerView) findViewById(R.id.recycler_view_comment);

        //Tombol Back
        back=(ImageView)findViewById(R.id.back_details);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ArrayList<Details> detailsArrayList = new ArrayList<Details>();
        commentsArrayList = new ArrayList<Comments>();

        //Recycler View Details
        current_user=mAuth.getCurrentUser().getUid();
        Intent intent = getIntent();
        String kategori = intent.getStringExtra("category").toString();
        String info = intent.getStringExtra("info").toString();
        String nama = intent.getStringExtra("nama_barang").toString();
        String deskripsi = intent.getStringExtra("deskripsi").toString();
        String lokasi = intent.getStringExtra("lokasi").toString();
        String userid = intent.getStringExtra("user_id").toString();
        String image_url=intent.getStringExtra("img_url").toString();
        final String postid=intent.getStringExtra("postid").toString();

        //String nama_barang, String deskripsi, String lokasi, String category, String info, String image_url, String user_id
        Details details=new Details(nama,deskripsi,lokasi,kategori,info,image_url,userid).withId(postid);
        detailsArrayList.add(details);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        DetailsAdapter detailsAdapter = new DetailsAdapter(detailsArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(detailsAdapter);

        sendcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentar=comment.getText().toString();
                if (!TextUtils.isEmpty(commentar)) {
                    Map<String,Object> komentar=new HashMap<>();
                    komentar.put("user_id",current_user);
                    komentar.put("comment",commentar);
                    komentar.put("timestamp", FieldValue.serverTimestamp());
                    firebaseFirestore.collection("Posts/"+postid+"/Comments").add(komentar).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful()){
                                comment.setText(null);
                                Toast.makeText(getApplicationContext(),"Komentar Berhasil Terkirim",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getApplicationContext(),"Komentar Tidak Terkirim",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(getApplicationContext(),"Komentar Masih Kosong",Toast.LENGTH_SHORT).show();
                }

            }
        });
        loadComment(postid);
    }

    private void loadComment(String postid){
        FirebaseUser user=mAuth.getCurrentUser();
        if(user!=null){
            Query commentquery=firebaseFirestore.collection("Posts/"+postid+"/Comments").orderBy("timestamp", Query.Direction.ASCENDING);
            commentquery.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                    for (DocumentChange doc:documentSnapshots.getDocumentChanges()){
                        Comments com=doc.getDocument().toObject(Comments.class);
                        commentsArrayList.add(com);
                        CommentsAdapter commentsAdapter = new CommentsAdapter(commentsArrayList);
                        RecyclerView.LayoutManager layoutManagerComments = new LinearLayoutManager(getApplicationContext());
                        recyclerViewComments.setLayoutManager(layoutManagerComments);
                        recyclerViewComments.setAdapter(commentsAdapter);
                    }
                }
            });
        }
    }

}
