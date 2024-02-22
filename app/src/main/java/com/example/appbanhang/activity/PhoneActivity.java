package com.example.appbanhang.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbanhang.R;
import com.example.appbanhang.Urls.Utils;

import com.example.appbanhang.adapter.PhoneAdapter;
import com.example.appbanhang.model.SanPhamMoi;

import com.example.appbanhang.retrofit.ApiBanHang;
import com.example.appbanhang.retrofit.RetrofitClient;
import com.google.android.material.color.ColorContrast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PhoneActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    int page = 1;
    int loai;

    List<SanPhamMoi> sanPhamMois;
    LinearLayoutManager linearLayoutManager;
    Handler handler = new Handler();
    boolean isLoading = false;
    PhoneAdapter samSungAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_phone);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        loai = getIntent().getIntExtra("loai",1);
        Anhxa();
        settextToobar(loai);
        ActionToolBar();
        getData(page);
        addEventLoad();
    }

    private void settextToobar(int loai) {
        if (loai == 1){
            toolbar.setTitle("Sam Sung");
        }else if(loai == 2){
            toolbar.setTitle("IPhone");
        }else {
            toolbar.setTitle("Xiaomi");
        }
    }

    private void addEventLoad() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(isLoading == false){
                    if(linearLayoutManager.findLastCompletelyVisibleItemPosition() == sanPhamMois.size()-1){
                        isLoading = true;
                        loadMore();
                    }
                }
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadMore() {
        handler.post(() -> {
            sanPhamMois.add(null);
            samSungAdapter.notifyItemInserted(sanPhamMois.size()-1);
        });
        handler.postDelayed(() -> {
            sanPhamMois.remove(sanPhamMois.size()-1);
            samSungAdapter.notifyItemRemoved(sanPhamMois.size());
            page = page +1;
            getData(page);
            samSungAdapter.notifyDataSetChanged();
            isLoading = false;

        },1000);
    }

    private void getData(int page) {
        compositeDisposable.add(apiBanHang.getSanPham(page,loai)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if (sanPhamMoiModel.isSuccess()) {  // Xử lý dữ liệu ở đây nếu thành công
                                if(samSungAdapter == null){
                                    sanPhamMois= sanPhamMoiModel.getResult();
                                    samSungAdapter = new PhoneAdapter(getApplicationContext(),sanPhamMois);
                                    recyclerView.setAdapter(samSungAdapter);

                                }else {
                                    int vitri = sanPhamMois.size()-1;
                                    int soluongadd = sanPhamMoiModel.getResult().size();
                                    for (int i = 0;i < soluongadd ;i++){
                                        sanPhamMois.add(sanPhamMoiModel.getResult().get(i));
                                    }
                                    samSungAdapter.notifyItemRangeInserted(vitri,soluongadd);
                                    samSungAdapter.notifyDataSetChanged();

                                }


                            }
                            else {
//                                // Xử lý lỗi từ API
                                Toast.makeText(getApplicationContext(), "Đã hết dữ liệu! ",Toast.LENGTH_LONG).show();

                                isLoading = true;
                            }

                        },
                        throwable -> {
                            // Xử lý lỗi trong quá trình gọi API hoặc phân tích JSON
                            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                            Log.e("MyTag", "Lỗi: " + throwable.getMessage());
                        }
                )
        );

    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
//
            }
        });
    }

    private void Anhxa() {
        toolbar = findViewById(R.id.toobar);
        recyclerView = findViewById(R.id.recycleview_ss1);
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        sanPhamMois = new ArrayList<>();

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}