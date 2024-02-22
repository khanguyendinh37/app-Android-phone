package com.example.appbanhang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.Urls.Utils;
import com.example.appbanhang.model.sanpham;
import com.example.appbanhang.model.user;

import java.util.List;

public class LoaSpAdapter extends BaseAdapter {
    List<sanpham> array;
    Context context;
    user user = Utils.user_currant;
    public LoaSpAdapter( Context context,List<sanpham> array) {
        this.array = array;
        this.context = context;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    public class ViewHolder{
        TextView textensp;
        ImageView imghinhanh;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder  viewHolder = null;
        if (view == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_sanpham,null);
            viewHolder.textensp = view.findViewById(R.id.item_tensp);
            viewHolder.imghinhanh = view.findViewById(R.id.item_Image);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();

        }
        if(array.get(i).getTenSanPham().equals("Đăng xuất")){

            if(user.getUsername() == null){
                viewHolder.textensp.setText("Đăng nhập");
                Glide.with(context).load(array.get(i).getAnhSanPham()).into(viewHolder.imghinhanh);
            }else {
                viewHolder.textensp.setText(array.get(i).getTenSanPham());
                Glide.with(context).load(array.get(i).getAnhSanPham()).into(viewHolder.imghinhanh);
            }

        }else {
            viewHolder.textensp.setText(array.get(i).getTenSanPham());
            Glide.with(context).load(array.get(i).getAnhSanPham()).into(viewHolder.imghinhanh);
        }



        return view;
    }
}
