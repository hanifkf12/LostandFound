package com.example.hanifkf.lostandfound.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hanifkf.lostandfound.Activity.DetailsActivity;
import com.example.hanifkf.lostandfound.Adapter.RecentAdapter;
import com.example.hanifkf.lostandfound.R;
import com.example.hanifkf.lostandfound.Models.Model;
import com.example.hanifkf.lostandfound.Utils.CustomItemClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
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

public class SugestFragment extends Fragment{

    private RecyclerView recyclerView;
    private ArrayList<Model> modelArrayList;
    private FirebaseFirestore firebaseFirestore;
    private LinearLayout linearLayout;
    private RelativeLayout relativeLayout;
    private Spinner spinner;
    private String kategori = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_sugest,container,false);

        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){

        }else{
            Toast.makeText(getActivity(), "No internet Connection", Toast.LENGTH_LONG).show();
        }

        modelArrayList = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.list_sugest);
        firebaseFirestore=FirebaseFirestore.getInstance();

        //set visible
        linearLayout = (LinearLayout)view.findViewById(R.id.linear_view);
        relativeLayout = (RelativeLayout)view.findViewById(R.id.empty_view);
        linearLayout.setVisibility(View.GONE);

        //data spinner ambil dari string
        spinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.kategori, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 1){
                    kategori = "Aksesoris";
                    linearLayout.setVisibility(View.VISIBLE);
                    relativeLayout.setVisibility(View.GONE);
                    displaySuggest();
                }else if(position == 2){
                    kategori = "Gadget";
                    linearLayout.setVisibility(View.VISIBLE);
                    relativeLayout.setVisibility(View.GONE);
                    displaySuggest();
                }else if(position == 3){
                    kategori = "Hewan";
                    linearLayout.setVisibility(View.VISIBLE);
                    relativeLayout.setVisibility(View.GONE);
                    displaySuggest();
                }else if(position == 4){
                    kategori = "Otomotif";
                    linearLayout.setVisibility(View.VISIBLE);
                    relativeLayout.setVisibility(View.GONE);
                    displaySuggest();
                }else if(position == 5){
                    kategori = "Lainnya";
                    linearLayout.setVisibility(View.VISIBLE);
                    relativeLayout.setVisibility(View.GONE);
                    displaySuggest();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //kondisi
            }

        });

        return view;
    }

    public void displaySuggest(){
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {
            CollectionReference reference=firebaseFirestore.collection("Posts");
            Query query = reference.whereEqualTo("category", kategori);
            query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            String home_id = doc.getDocument().getId();
                            final Model model = doc.getDocument().toObject(Model.class).withId(home_id);
                            modelArrayList.add(model);
                            RecentAdapter adapter = new RecentAdapter(getActivity(), modelArrayList, new CustomItemClickListener() {
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

        } else {
            getActivity().finish();
        }
    }

}
