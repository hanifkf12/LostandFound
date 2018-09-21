package com.example.hanifkf.lostandfound.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.hanifkf.lostandfound.Activity.EditPostActivity;
import com.example.hanifkf.lostandfound.R;
import com.example.hanifkf.lostandfound.Utils.CustomItemClickListener;
import com.example.hanifkf.lostandfound.Adapter.RecentAdapter;
import com.example.hanifkf.lostandfound.Models.Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

/**
 * Created by hanifkf on 24/02/2018.
 */

public class RecentFragment extends Fragment{

    private RecyclerView recyclerView;
    private ArrayList<Model> modelArrayList;
    CircleImageView profile_image;
    TextView username,email;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String user_id=null;
    private String email_user=null;
    private RelativeLayout relativeLayout;

    String recent_id = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_recent,container,false);
        profile_image=(CircleImageView)view.findViewById(R.id.profile_image);
        username=(TextView)view.findViewById(R.id.username);
        email=(TextView)view.findViewById(R.id.emaiil);
        firebaseAuth=FirebaseAuth.getInstance();
        user_id=firebaseAuth.getCurrentUser().getUid();
        email_user=firebaseAuth.getCurrentUser().getEmail();
        modelArrayList = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recentpost);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.empty_view);

        recyclerView.setVisibility(View.GONE);
        relativeLayout.setVisibility(View.VISIBLE);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        String namaku=task.getResult().getString("name");
                        String imageku=task.getResult().getString("image");

                        username.setText(namaku);
                        email.setText(email_user);
                        RequestOptions placeHolder=new RequestOptions();
                        placeHolder.placeholder(R.drawable.images);
                        Glide.with(RecentFragment.this).setDefaultRequestOptions(placeHolder).load(imageku).into(profile_image);

                        listRecent();
                    }else {
                        Toast.makeText(getContext(),"Data Doesnt Exist",Toast.LENGTH_SHORT).show();
                    }

                }else {
                    String error=task.getException().getMessage();
                    Toast.makeText(getContext(),"FireStore Retrieve Eror "+error,Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public void listRecent(){
        CollectionReference reference=firebaseFirestore.collection("Posts");
        Query query = reference.whereEqualTo("user_id", user_id);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                modelArrayList.clear();

                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if(doc != null){
                        recyclerView.setVisibility(View.VISIBLE);
                        relativeLayout.setVisibility(View.GONE);
                    }
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        recent_id = doc.getDocument().getId();
                        Model model = doc.getDocument().toObject(Model.class).withId(recent_id);
                        modelArrayList.add(model);
                        RecentAdapter adapter = new RecentAdapter(getActivity(), modelArrayList, new CustomItemClickListener() {
                            @Override
                            public void onItemClick(View v, int position) {
                                Log.d(TAG, "onItemClick: " + modelArrayList.get(position).getNama_barang());
                                Intent in = new Intent(getContext(), EditPostActivity.class);
                                in.putExtra("category", modelArrayList.get(position).getCategory());
                                in.putExtra("info", modelArrayList.get(position).getInfo());
                                in.putExtra("nama_barang", modelArrayList.get(position).getNama_barang());
                                in.putExtra("deskripsi", modelArrayList.get(position).getDeskripsi());
                                in.putExtra("lokasi", modelArrayList.get(position).getLokasi());
                                in.putExtra("img_url", modelArrayList.get(position).getImage_url());
                                in.putExtra("user_id", modelArrayList.get(position).getUser_id());
                                in.putExtra("timestamp", modelArrayList.get(position).getTimestap());
                                in.putExtra("postid", modelArrayList.get(position).HomeId);
                                startActivity(in);
                            }
                        });
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerView.setAdapter(adapter);

                    }
                }
            }
        });
    }
}