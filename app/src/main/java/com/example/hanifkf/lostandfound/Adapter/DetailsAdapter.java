package com.example.hanifkf.lostandfound.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.hanifkf.lostandfound.Models.Details;
import com.example.hanifkf.lostandfound.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by M Taufiq R on 25/02/2018.
 */

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.DetailsViewHolder> {

    private ArrayList<Details> dataList;
    private String username=null;
    private String userimage=null;
    private FirebaseFirestore firebaseFirestore;

    private Context context;
    public DetailsAdapter(ArrayList<Details> dataList) {
        this.dataList = dataList;
    }

    @Override
    public DetailsAdapter.DetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_view_details, parent, false);
        context=parent.getContext();
        firebaseFirestore=FirebaseFirestore.getInstance();
        return new DetailsAdapter.DetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DetailsAdapter.DetailsViewHolder holder, int position) {

        String postid=dataList.get(position).HomeId;
        holder.txtKategori.setText("Kategori : " + dataList.get(position).getCategory());
        holder.txtItem.setText(dataList.get(position).getInfo());
        holder.txtNama.setText(dataList.get(position).getNama_barang());
        holder.txtDeskripsi.setText(dataList.get(position).getDeskripsi());
        holder.txtLokasi.setText("Lokasi : " + dataList.get(position).getLokasi());

        String user_id=dataList.get(position).getUser_id();

        final RequestOptions placeHolder=new RequestOptions();
        placeHolder.placeholder(R.drawable.noimage);
        Glide.with(context).setDefaultRequestOptions(placeHolder).load(dataList.get(position).getImage_url()).into(holder.post_image);

        //load user data dari data User
        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                    username=task.getResult().getString("name");
                    userimage=task.getResult().getString("image");

                    holder.userName.setText(username);
                    Glide.with(context).setDefaultRequestOptions(placeHolder).load(userimage).into(holder.userimage);

                }else {

                }
            }
        });
        firebaseFirestore.collection("Posts/"+postid+"/Comments").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(!documentSnapshots.isEmpty()){
                    int count=documentSnapshots.size();
                    holder.jml_comment.setText(count+" Komentar");
                }else {
                    holder.jml_comment.setText(0+" Komentar");
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class DetailsViewHolder extends RecyclerView.ViewHolder{
        private TextView txtKategori, txtItem, txtNama, txtDeskripsi, txtLokasi,userName,jml_comment;
        private ImageView post_image;
        private CircleImageView userimage;
        public DetailsViewHolder(View itemView) {
            super(itemView);
            jml_comment=(TextView)itemView.findViewById(R.id.txt_comment);
            userName=(TextView)itemView.findViewById(R.id.username_details);
            post_image=(ImageView)itemView.findViewById(R.id.image_post_detail);
            userimage=(CircleImageView)itemView.findViewById(R.id.profile_image_details);
            txtKategori=(TextView)itemView.findViewById(R.id.txt_suggest);
            txtItem = (TextView) itemView.findViewById(R.id.txt_details);
            txtNama = (TextView) itemView.findViewById(R.id.txt_hilang);
            txtDeskripsi = (TextView) itemView.findViewById(R.id.deskripsi_home);
            txtLokasi = (TextView) itemView.findViewById(R.id.txt_item);
        }
    }

}
