package com.example.hanifkf.lostandfound.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.hanifkf.lostandfound.Models.Comments;
import com.example.hanifkf.lostandfound.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by M Taufiq R on 19/03/2018.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {

    private ArrayList<Comments> dataList;
    private Context context;
    FirebaseFirestore firebaseFirestore;
    private String user_name=null;
    private String userimage=null;

    public CommentsAdapter(ArrayList<Comments> dataList) {
        this.dataList = dataList;
    }

    @Override
    public CommentsAdapter.CommentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();
        firebaseFirestore=FirebaseFirestore.getInstance();
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_view_comments, parent, false);
        return new CommentsAdapter.CommentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CommentsAdapter.CommentsViewHolder holder, int position) {

        String user_id=dataList.get(position).getUser_id();
        String isi=dataList.get(position).getComment();
        Date waktu=dataList.get(position).getTimestamp();
        final RequestOptions placeHolder=new RequestOptions();
        placeHolder.placeholder(R.drawable.noimage);

        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        user_name=task.getResult().getString("name");
                        userimage=task.getResult().getString("image");
                        holder.username.setText(user_name);
                        Glide.with(context).setDefaultRequestOptions(placeHolder).load(userimage).into(holder.profil);
                    }

                }else {
                    Toast.makeText(context,"Eroro",Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.isi.setText(isi);

    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class CommentsViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView profil;
        private TextView username,isi,waktu;

        public CommentsViewHolder(View itemView) {
            super(itemView);
            profil=(CircleImageView) itemView.findViewById(R.id.user_profile_comment);
            username=(TextView)itemView.findViewById(R.id.txt_username_comment);
            isi=(TextView)itemView.findViewById(R.id.txt_isi_comment);
            waktu=(TextView)itemView.findViewById(R.id.txt_waktu_comment);
        }
    }

}
