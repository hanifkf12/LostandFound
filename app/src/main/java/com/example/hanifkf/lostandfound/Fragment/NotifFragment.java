package com.example.hanifkf.lostandfound.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Toast;

import com.example.hanifkf.lostandfound.Activity.DetailsActivity;
import com.example.hanifkf.lostandfound.Adapter.NotificationAdapter;
import com.example.hanifkf.lostandfound.Models.Model;
import com.example.hanifkf.lostandfound.R;
import com.example.hanifkf.lostandfound.Utils.CustomItemClickListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import static android.content.ContentValues.TAG;

/**
 * Created by hanifkf on 24/02/2018.
 */

public class NotifFragment extends Fragment{

    private RecyclerView recyclerView;
    private ArrayList<Model> modelArrayList;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String user_id=null;
    private RelativeLayout relativeLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view=inflater.inflate(R.layout.fragment_notif,container,false);

        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){

        }else{
            Toast.makeText(getActivity(), "No internet Connection", Toast.LENGTH_LONG).show();
        }

        firebaseAuth=FirebaseAuth.getInstance();
        user_id=firebaseAuth.getCurrentUser().getUid();
        modelArrayList = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.list_notif);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.empty_view);

        recyclerView.setVisibility(View.GONE);
        relativeLayout.setVisibility(View.VISIBLE);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        listNotif();
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

    public void listNotif(){
        Query query = firebaseFirestore.collection("Posts").orderBy("timestamp",Query.Direction.DESCENDING);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if(doc != null){
                        recyclerView.setVisibility(View.VISIBLE);
                        relativeLayout.setVisibility(View.GONE);
                    }
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        String home_id = doc.getDocument().getId();
                        final Model model = doc.getDocument().toObject(Model.class).withId(home_id);
                        modelArrayList.add(model);
                        NotificationAdapter adapter = new NotificationAdapter(getActivity(), modelArrayList, new CustomItemClickListener() {
                            @Override
                            public void onItemClick(View v, int position) {
                                Log.d(TAG, "onItemClick: " + modelArrayList.get(position).getNama_barang());
                                Intent in = new Intent(getContext(), DetailsActivity.class);
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
