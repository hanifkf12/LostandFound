package com.example.hanifkf.lostandfound.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.hanifkf.lostandfound.Models.Model;
import com.example.hanifkf.lostandfound.R;
import com.example.hanifkf.lostandfound.Utils.CustomItemClickListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by M Taufiq R on 25/02/2018.
 */

public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.RecentRecycleViewHolder> {

    private Activity context;
    private ArrayList<Model> dataList;
    private CustomItemClickListener listener;
    private FirebaseFirestore firebaseFirestore;
    private String username=null;
    private String userimage=null;

    public RecentAdapter(Activity context, ArrayList<Model> dataList, CustomItemClickListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;
    }

    @Override
    public RecentRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_view_recent, parent, false);
        firebaseFirestore=FirebaseFirestore.getInstance();
        final RecentRecycleViewHolder mholder=new RecentRecycleViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(view,mholder.getPosition());
            }
        });
        return mholder;
    }

    @Override
    public void onBindViewHolder(final RecentAdapter.RecentRecycleViewHolder holder, int position) {
        holder.txtItem.setText(dataList.get(position).getInfo());
        holder.txtHilang.setText(dataList.get(position).getNama_barang());

        final RequestOptions placeHolder=new RequestOptions();
        placeHolder.placeholder(R.drawable.noimage);

        String user_id=dataList.get(position).getUser_id();

        //load user data dari data User
        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    username=task.getResult().getString("name");
                    userimage=task.getResult().getString("image");
                    holder.userName.setText(username);
                    Glide.with(context).setDefaultRequestOptions(placeHolder).load(userimage).into(holder.user_image);
                }else {

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class RecentRecycleViewHolder extends RecyclerView.ViewHolder{
        private TextView txtHilang, txtItem, userName;
        private CircleImageView user_image;

        public RecentRecycleViewHolder(View itemView) {
            super(itemView);
            user_image=(CircleImageView)itemView.findViewById(R.id.profile_image);
            userName=(TextView)itemView.findViewById(R.id.txt_nama);
            txtHilang = (TextView) itemView.findViewById(R.id.txt_hilang);
            txtItem = (TextView) itemView.findViewById(R.id.txt_item);
        }
    }

}
