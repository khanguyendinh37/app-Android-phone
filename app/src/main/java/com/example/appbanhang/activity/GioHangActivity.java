package com.example.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.appbanhang.R;
import com.example.appbanhang.Urls.Utils;
import com.example.appbanhang.adapter.gioHangAdapter;
import com.example.appbanhang.model.EventBus.tinhTongEnvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;

public class GioHangActivity extends AppCompatActivity {
    TextView giohangTrong,tongTien;
    Button buttonMua;
    Toolbar toolbar;
    RecyclerView recyclerView;
    gioHangAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        initView();
        initControl();
        tongTien();
    }

    private void tongTien() {
        long tongtiensp = 0;
        for(int i = 0;i<Utils.manggiohang.size();i++){
            tongtiensp = tongtiensp+(Utils.manggiohang.get(i).getSoluong() * Utils.manggiohang.get(i).getGiaSp());
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tongTien.setText(decimalFormat.format(tongtiensp));
    }

    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if (Utils.manggiohang.size() == 0){
            giohangTrong.setVisibility(View.VISIBLE);
        }else {
            adapter = new gioHangAdapter(getApplicationContext(),Utils.manggiohang);
            recyclerView.setAdapter(adapter);
        }
    }

    private void initView() {
        giohangTrong = findViewById(R.id.txtgiohangtrong);
        toolbar = findViewById(R.id.toobar_gh);
        recyclerView = findViewById(R.id.recycleview_gh);
        tongTien = findViewById(R.id.txtTongTien);
        buttonMua = findViewById(R.id.btnMuaHang);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public  void eventTinhTien(tinhTongEnvent envent){
        if(envent != null){
            tongTien();
        }
    }
}