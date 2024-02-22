package com.example.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.Urls.Utils;
import com.example.appbanhang.model.GioHang;
import com.example.appbanhang.model.SanPhamMoi;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;
import java.util.Objects;

public class ChiTietActivity extends AppCompatActivity {
    TextView nameSP,giaSP,mota;
    Button btnthem;
    ImageView imgHinhAnh;
    Spinner spinner;
    Toolbar toolbar;
    SanPhamMoi sanPhamMoi;
    NotificationBadge badge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);
        initView();
        ActionToolBar();
        initData();
        initControl();
    }

    private void initControl() {
        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themGioHang();

            }


        });
    }
    private void themGioHang() {
        if(Utils.manggiohang.size()>0){
            boolean flag = false;
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            for(int i = 0 ;i < Utils.manggiohang.size();i++){
                if(Utils.manggiohang.get(i).getIdsp() == sanPhamMoi.getId()){
                    Utils.manggiohang.get(i).setSoluong(soluong + Utils.manggiohang.get(i).getSoluong());


                    flag =true;
                }
            }
            if(flag == false){

                GioHang gioHang = new GioHang();
                gioHang.setGiaSp(Long.parseLong(sanPhamMoi.getGiaSp()));
                gioHang.setSoluong(soluong);
                gioHang.setIdsp(sanPhamMoi.getId());
                gioHang.setTenSp(sanPhamMoi.getTenSp());
                gioHang.setHinhAnh(sanPhamMoi.getHinhanh());
                Utils.manggiohang.add(gioHang);

            }

        }else {
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            long gia = Long.parseLong(sanPhamMoi.getGiaSp()) * soluong;
            GioHang gioHang = new GioHang();
            gioHang.setGiaSp(gia);
            gioHang.setSoluong(soluong);
            gioHang.setIdsp(sanPhamMoi.getId());
            gioHang.setTenSp(sanPhamMoi.getTenSp());
            gioHang.setHinhAnh(sanPhamMoi.getHinhanh());
            Utils.manggiohang.add(gioHang);
        }
        int totalItem = 0;
        for(int i = 0;i<Utils.manggiohang.size();i++ ){
            totalItem = totalItem + Utils.manggiohang.get(i).getSoluong();
        }
        badge.setText(String.valueOf(totalItem));
    }
    private void initData() {
        sanPhamMoi = (SanPhamMoi) getIntent().getSerializableExtra("chitiet");
        nameSP.setText(sanPhamMoi.getTenSp());
        mota.setText(sanPhamMoi.getMota());
        Glide.with(getApplicationContext()).load(sanPhamMoi.getHinhanh()).into(imgHinhAnh);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        double gia = Double.parseDouble(sanPhamMoi.getGiaSp());
        giaSP.setText("Giá : " + decimalFormat.format(gia)+" đ");
        Integer[] so = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,so);
        spinner.setAdapter(arrayAdapter);
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    private void initView() {
        nameSP = findViewById(R.id.itemDetail_name);
        giaSP = findViewById(R.id.itemDetail_price);
        mota = findViewById(R.id.itemDetail_detail);
        btnthem = findViewById(R.id.itemDetail_buttom);
        imgHinhAnh = findViewById(R.id.itemDetail_image);
        spinner = findViewById(R.id.spinnerDetail);
        toolbar = findViewById(R.id.toobar_detail);

        FrameLayout frameLayoutCart = findViewById(R.id.cart_layout_sp);
        frameLayoutCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gioHang = new Intent(getApplicationContext(),GioHangActivity.class);
                startActivity(gioHang);
            }
        });
        badge = findViewById(R.id.menu_sl);
        if (Utils.manggiohang != null){
            int totalItem = 0;
            for(int i = 0;i<Utils.manggiohang.size();i++ ){
                totalItem = totalItem + Utils.manggiohang.get(i).getSoluong();
            }
            badge.setText(String.valueOf(totalItem));
        }
    }
    // cập nhật lại số lượng
    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.manggiohang != null){
            int totalItem = 0;
            for(int i = 0;i<Utils.manggiohang.size();i++ ){
                totalItem = totalItem + Utils.manggiohang.get(i).getSoluong();
            }
            badge.setText(String.valueOf(totalItem));
        }
    }
}