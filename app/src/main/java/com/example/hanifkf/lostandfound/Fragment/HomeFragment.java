package com.example.hanifkf.lostandfound.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.hanifkf.lostandfound.Activity.DetailsActivity;
import com.example.hanifkf.lostandfound.Adapter.HomeAdapter;
import com.example.hanifkf.lostandfound.Models.Home;
import com.example.hanifkf.lostandfound.R;
import com.example.hanifkf.lostandfound.Utils.CustomItemClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by hanifkf on 24/02/2018.
 */

public class HomeFragment extends Fragment{

    private RecyclerView recyclerView;
    private ArrayList<Home> homeArrayList;
    private FirebaseFirestore firebaseFirestore;
    private RelativeLayout relativeLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home,container,false);

        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){

        }else{
            Toast.makeText(getActivity(), "No internet Connection", Toast.LENGTH_LONG).show();
        }

        homeArrayList = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.empty_view);
        firebaseFirestore=FirebaseFirestore.getInstance();

        recyclerView.setVisibility(View.GONE);
        relativeLayout.setVisibility(View.VISIBLE);

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {
            Query firstQuery=firebaseFirestore.collection("Posts").orderBy("timestamp",Query.Direction.DESCENDING);
            firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                        if(doc != null){
                            recyclerView.setVisibility(View.VISIBLE);
                            relativeLayout.setVisibility(View.GONE);
                        }
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            String home_id = doc.getDocument().getId();
                            Home home = doc.getDocument().toObject(Home.class).withId(home_id);
                            homeArrayList.add(home);
                            HomeAdapter adapter = new HomeAdapter(getActivity(), homeArrayList, new CustomItemClickListener() {
                                @Override
                                public void onItemClick(View v, int position) {
                                    Log.d(TAG, "onItemClick: " + homeArrayList.get(position).getNama_barang());
                                    Intent in = new Intent(getContext(), DetailsActivity.class);
                                    in.putExtra("category", homeArrayList.get(position).getCategory());
                                    in.putExtra("info", homeArrayList.get(position).getInfo());
                                    in.putExtra("nama_barang", homeArrayList.get(position).getNama_barang());
                                    in.putExtra("deskripsi", homeArrayList.get(position).getDeskripsi());
                                    in.putExtra("lokasi", homeArrayList.get(position).getLokasi());
                                    in.putExtra("img_url", homeArrayList.get(position).getImage_url());
                                    in.putExtra("user_id", homeArrayList.get(position).getUser_id());
                                    in.putExtra("timestamp", homeArrayList.get(position).getTimestap());
                                    in.putExtra("postid", homeArrayList.get(position).HomeId);
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
        else {
            getActivity().finish();
        }

        return view;
    }

}
