package com.example.appbanhang.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.appbanhang.Interface.ItemClickListener;
import com.example.appbanhang.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.activity.ChiTietActivity;
import com.example.appbanhang.model.SanPhamMoi;

import java.text.DecimalFormat;
import java.util.List;

public class PhoneAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<SanPhamMoi> array;

    private static final int VIEW_TYPE_DATA = 0;
    private static final int VIEW_TYPE_LOADING = 1;


    public PhoneAdapter(Context context, List<SanPhamMoi> array ) {
        this.array = array;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_DATA){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_samsung,parent,false);
            return new MyViewHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading,parent,false);
            return new LoadingViewHolder(view);
        }

    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
       if(holder instanceof   MyViewHolder){
           MyViewHolder viewHolder = (MyViewHolder) holder;
           SanPhamMoi sanPham = array.get(position);
           viewHolder.tensp.setText(sanPham.getTenSp().trim());
           viewHolder.mota.setText(sanPham.getMota());
           DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
           double gia = Double.parseDouble(sanPham.getGiaSp());
           viewHolder.giasp.setText("Giá : " + decimalFormat.format(gia)+" đ");
           Glide.with(context).load(sanPham.getHinhanh()).into(viewHolder.hinhanh);
           viewHolder.setItemClickListener(new ItemClickListener() {
               @Override
               public void onClick(View view, int pos, boolean isLongClick) {
                   if(!isLongClick){
                       //click
                       Intent intent = new Intent(context, ChiTietActivity.class);
                       intent.putExtra("chitiet",sanPham);
                       intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       context.startActivity(intent);
                   }
               }
           });
       }else {
           LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
           loadingViewHolder.progressBar.setIndeterminate(true);
       }
    }

    @Override
    public int getItemViewType(int position) {
        return array.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_DATA;
    }
    @Override
    public int getItemCount() {
        try{
            return array.size();

        }catch (Exception e){
            return 0;
        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressbar);
        }
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tensp,giasp,mota;
        ImageView hinhanh;
        private ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tensp = itemView.findViewById(R.id.itemSS_ten);

            giasp = itemView.findViewById(R.id.itemSS_gia);
            mota = itemView.findViewById(R.id.itemSS_mota);
            hinhanh = itemView.findViewById(R.id.itemSS_image);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition(),false);
        }
    }

}
