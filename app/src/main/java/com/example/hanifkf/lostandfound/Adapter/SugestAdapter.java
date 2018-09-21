package com.example.hanifkf.lostandfound.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hanifkf.lostandfound.R;
import com.example.hanifkf.lostandfound.Models.Model;
import com.example.hanifkf.lostandfound.Utils.CustomItemClickListener;

import java.util.ArrayList;

/**
 * Created by hanifkf on 05/03/2018.
 */

public class SugestAdapter extends RecyclerView.Adapter<SugestAdapter.SugestRecycleViewHolder> {

    private Activity context;
    private ArrayList<Model> dataList;
    private CustomItemClickListener listener;

    public SugestAdapter(Activity context, ArrayList<Model> dataList, CustomItemClickListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;
    }

    @Override
    public SugestAdapter.SugestRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_view_sugest, parent, false);
        final SugestRecycleViewHolder mholder=new SugestRecycleViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(view,mholder.getPosition());
            }
        });
        return mholder;
    }

    @Override
    public void onBindViewHolder(SugestAdapter.SugestRecycleViewHolder holder, int position) {
        holder.txtHilang.setText(dataList.get(position).getNama_barang());
        holder.txtItem.setText(dataList.get(position).getInfo());
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class SugestRecycleViewHolder extends RecyclerView.ViewHolder{
        private TextView txtHilang, txtItem;

        public SugestRecycleViewHolder(View itemView) {
            super(itemView);
            txtHilang = (TextView) itemView.findViewById(R.id.txt_hilang);
            txtItem = (TextView) itemView.findViewById(R.id.txt_item);
        }
    }

}
