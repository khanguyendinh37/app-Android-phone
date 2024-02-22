package com.example.appbanhang.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.Interface.ImageClickLintenner;
import com.example.appbanhang.Interface.ItemClickListener;
import com.example.appbanhang.R;
import com.example.appbanhang.Urls.Utils;
import com.example.appbanhang.activity.ChiTietActivity;
import com.example.appbanhang.model.EventBus.tinhTongEnvent;
import com.example.appbanhang.model.GioHang;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class gioHangAdapter extends RecyclerView.Adapter<gioHangAdapter.MyViewHolder> {
    Context context;
    List<GioHang> gioHangList;

    public gioHangAdapter(Context context, List<GioHang> gioHangList) {
        this.context = context;
        this.gioHangList = gioHangList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_giohang,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GioHang gioHang = gioHangList.get(position);
        holder.tenSp.setText(gioHang.getTenSp());
        holder.soLuong.setText(gioHang.getSoluong()+" ");
        Glide.with(context).load(gioHang.getHinhAnh()).into(holder.item_imageSp);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.gia.setText("Giá : " + decimalFormat.format(gioHang.getGiaSp())+" đ");
        long tong = gioHang.getSoluong() * gioHang.getGiaSp();
        holder.tongGiaSP.setText( decimalFormat.format(tong)+" đ");
        holder.setLintenner(new ImageClickLintenner() {
            @Override
            public void onImageClick(View view, int pos, int giatri) {
                if(giatri==1){
                    if(gioHangList.get(pos).getSoluong()>1){
                        int soluongmoi = gioHangList.get(pos).getSoluong()-1;
                        gioHangList.get(pos).setSoluong(soluongmoi);

                        holder.soLuong.setText(gioHangList.get(pos).getSoluong()+" ");
                        long tong = gioHangList.get(pos).getSoluong() * gioHangList.get(pos).getGiaSp();
                        holder.tongGiaSP.setText( decimalFormat.format(tong)+" đ");
                        EventBus.getDefault().postSticky(new tinhTongEnvent());
                    }else if (gioHangList.get(pos).getSoluong()==1){
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setTitle("Thông báo");
                        builder.setMessage("bạn có muốn xóa sản phẩm này khỏi giỏ hang không");
                        builder.setPositiveButton("đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Utils.manggiohang.remove(pos);
                                notifyDataSetChanged();
                                EventBus.getDefault().postSticky(new tinhTongEnvent());
                            }
                        });
                        builder.setNegativeButton("hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();

                    }

                } else if (giatri == 2) {
                    if(gioHangList.get(pos).getSoluong() < 11){
                        int soluongmoi = gioHangList.get(pos).getSoluong()+1;
                        gioHangList.get(pos).setSoluong(soluongmoi);
                    }
                    holder.soLuong.setText(gioHangList.get(pos).getSoluong()+" ");
                    long tong = gioHangList.get(pos).getSoluong() * gioHangList.get(pos).getGiaSp();
                    holder.tongGiaSP.setText( decimalFormat.format(tong)+" đ");
                    EventBus.getDefault().postSticky(new tinhTongEnvent());
                }

            }
        });
//        holder.setLintenner(new ImageClickLintenner() {
//            @Override
//            public void onClick(View view, int pos, boolean isLongClick) {
//                if(!isLongClick){
//                    //click
//                    Intent intent = new Intent(context, ChiTietActivity.class);
//                    intent.putExtra("chitiet",gioHang);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent);
//                }
//            }
//        });



    }

    @Override
    public int getItemCount() {
        return gioHangList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView item_imageSp,imgtru,imgcong;
        TextView tenSp,gia,soLuong,tongGiaSP;
        ImageClickLintenner lintenner;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_imageSp = itemView.findViewById(R.id.item_giohang_images);
            tenSp = itemView.findViewById(R.id.item_gioihang_tensp);
            gia = itemView.findViewById(R.id.item_giohang_giasp);
            soLuong = itemView.findViewById(R.id.item_giohang_sl);
            tongGiaSP = itemView.findViewById(R.id.item_giohang_tonggsp);
            imgtru = itemView.findViewById(R.id.item_giohang_tru);
            imgcong = itemView.findViewById(R.id.item_giohang_cong);
            imgcong.setOnClickListener(this);
            imgtru.setOnClickListener(this);
        }

        public void setLintenner(ImageClickLintenner lintenner) {
            this.lintenner = lintenner;
        }

        @Override
        public void onClick(View view) {
            if(view == imgtru){
                lintenner.onImageClick(view,getAdapterPosition(),1);
                //trừ
            }else if (view == imgcong)
                lintenner.onImageClick(view,getAdapterPosition(),2);
                //cong
        }
    }
}
