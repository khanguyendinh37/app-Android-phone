package com.example.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;

import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.Urls.Utils;
import com.example.appbanhang.adapter.LoaSpAdapter;

import com.example.appbanhang.adapter.SanPhamMoiAdapter;
import com.example.appbanhang.model.GioHang;
import com.example.appbanhang.model.SanPhamMoi;
import com.example.appbanhang.model.sanpham;
import com.example.appbanhang.model.user;
import com.example.appbanhang.model.userModel;
import com.example.appbanhang.retrofit.ApiBanHang;
import com.example.appbanhang.retrofit.RetrofitClient;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewTrangChu;
    NavigationView navigationView;
    ListView listViewTrangChu;
    DrawerLayout drawerLayout;
    LoaSpAdapter loaSpAdapter;
    List<sanpham> mangloaisp;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    List<SanPhamMoi> mangSpMoi;
    SanPhamMoiAdapter sanPhamMoiAdapter;
    FrameLayout frameLayoutCart;
    NotificationBadge badge1;
    List<GioHang> gioHangList= Utils.manggiohang;
    user user = Utils.user_currant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        Anhxa();
        ActionBar();

        if(isConnected(this)){

            ActionViewFliper();
            getLoaiSanPham();
            getSpMoi();
            getEventClick();
        }else {
            Toast.makeText(getApplicationContext(),"không có internet,vui lòng kết nối internet",Toast.LENGTH_LONG).show();
        }
    }

    private void getEventClick() {
        listViewTrangChu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Intent trangchu = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(trangchu);
                        break;
                    case 1:
                        Intent samsung = new Intent(getApplicationContext(), PhoneActivity.class);
                        samsung.putExtra("loai",1);

                        startActivity(samsung);
                        break;
                    case 2:
                        Intent IPhone = new Intent(getApplicationContext(), PhoneActivity.class);
                        IPhone.putExtra("loai",2);
                        startActivity(IPhone);
                        break;
                    case 3:
                        Intent Xiaomi = new Intent(getApplicationContext(), PhoneActivity.class);
                        Xiaomi.putExtra("loai",3);
                        startActivity(Xiaomi);
                        break;
                    case 4:
                        Intent lienHe = new Intent(getApplicationContext(),LienHeActivity.class);
                        startActivity(lienHe);
                        break;
                    case 5:
                        if(user.getUsername() == null){
                            Intent Login= new Intent(getApplicationContext(),DangNhapActivity.class);
                            startActivity(Login);

                        }else {
                            Intent thongTin = new Intent(getApplicationContext(),chitietAtivity.class);
                            startActivity(thongTin);
                        }
                        break;
                    case 6:
                        if(user.getUsername()!=null){
                            user.setUsername(null) ;
                            user.setPass(null);
                            Intent Logout= new Intent(getApplicationContext(),DangNhapActivity.class);
                            startActivity(Logout);
                        }else {
                            Intent Logout= new Intent(getApplicationContext(),DangNhapActivity.class);
                            startActivity(Logout);
                        }


                        break;
                }
            }
        });
    }

    private void getSpMoi() {
        compositeDisposable.add(apiBanHang.getSpMoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( sanPhamMoiModel -> {
                            if (sanPhamMoiModel.isSuccess()) {  // Xử lý dữ liệu ở đây nếu thành công
                                mangSpMoi = sanPhamMoiModel.getResult();
                                sanPhamMoiAdapter = new SanPhamMoiAdapter(getApplicationContext(),mangSpMoi);
                                recyclerViewTrangChu.setAdapter(sanPhamMoiAdapter);



                            } else {
                                // Xử lý lỗi từ API
                                Toast.makeText(getApplicationContext(), "Lỗi từ API: " + sanPhamMoiModel.getMessage(), Toast.LENGTH_LONG).show();

                                // In ra dữ liệu JSON lỗi (nếu cần)
                                String errorJson = sanPhamMoiModel.getRawJson();
                                Log.e("API Error JSON", errorJson);
                            }
                        },
                        throwable -> {
                            // Xử lý lỗi trong quá trình gọi API hoặc phân tích JSON
                            Toast.makeText(getApplicationContext(), "Lỗi: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                            Log.e("MyTag", "Lỗi: " + throwable.getMessage());
                        })
        );
    }

    private void getLoaiSanPham() {

        compositeDisposable.add(apiBanHang.getLoaiSp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        loaiSpModel -> {
                            if (loaiSpModel.isSuccess()) {  // Xử lý dữ liệu ở đây nếu thành công
                                mangloaisp = loaiSpModel.getResult();
                                loaSpAdapter = new LoaSpAdapter(getApplicationContext(),mangloaisp);
                                listViewTrangChu.setAdapter(loaSpAdapter);



                            } else {
                                // Xử lý lỗi từ API
                                Toast.makeText(getApplicationContext(), "Lỗi từ API: " + loaiSpModel.getMessage(), Toast.LENGTH_LONG).show();

                                // In ra dữ liệu JSON lỗi (nếu cần)
                                String errorJson = loaiSpModel.getRawJson();
                                Log.e("API Error JSON", errorJson);
                            }
                        },
                        throwable -> {
                            // Xử lý lỗi trong quá trình gọi API hoặc phân tích JSON
                            Toast.makeText(getApplicationContext(), "Lỗi: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                            Log.e("MyTag", "Lỗi: " + throwable.getMessage());
                        }
                )
        );
//        JsonReader jsonReader = new JsonReader(new InputStreamReader(response.body().byteStream()));
//        jsonReader.setLenient(true);
    }


    private void ActionViewFliper() {
        List<String> mangquangcao = new ArrayList<>();
        mangquangcao.add("https://cdn.tgdd.vn/hoi-dap/1355217/banner-tgdd-800x300.jpg");
        mangquangcao.add("https://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-HC-Tra-Gop-800-300.png");
        mangquangcao.add("https://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-big-ky-nguyen-800-300.jpg");
        for(int i = 0;i<mangquangcao.size();i++){
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(mangquangcao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.side_in_night);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);

    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        int totalItem = 0;
        if(gioHangList == null){
            Utils.manggiohang = new ArrayList<>();
            badge1.setText(String.valueOf(totalItem));
        }else{

            for(int i = 0;i<Utils.manggiohang.size();i++ ){
                totalItem = totalItem + Utils.manggiohang.get(i).getSoluong();
            }
            badge1.setText(String.valueOf(totalItem));
        }
    }

    private void Anhxa() {
        toolbar = findViewById(R.id.toolbarTrangchu);
        viewFlipper = findViewById(R.id.viewliper);
        recyclerViewTrangChu = findViewById(R.id.recycleview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerViewTrangChu.setLayoutManager(layoutManager);
        recyclerViewTrangChu.setHasFixedSize(true);
        listViewTrangChu = findViewById(R.id.listviewtrangchu);
        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawlayout);
        frameLayoutCart = findViewById(R.id.cart_layout_sp);

        mangloaisp = new ArrayList<>();
        mangSpMoi = new ArrayList<>();
        badge1 = findViewById(R.id.menu_sl1);
        frameLayoutCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gioHang = new Intent(getApplicationContext(),GioHangActivity.class);
                startActivity(gioHang);
            }
        });



    }
    private boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());

            if (networkCapabilities != null) {
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    // Kết nối Wi-Fi
                    return true;
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    // Kết nối Dữ liệu di động
                    return true;
                } else {
                    // Các loại kết nối mạng khác (Ethernet, Bluetooth, vv.)
                    return true;
                }
            }
        }

        // Không có kết nối mạng hoạt động
        return true;
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
    //cập nhật lại số lượng
    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.manggiohang != null){
            int totalItem = 0;
            for(int i = 0;i<Utils.manggiohang.size();i++ ){
                totalItem = totalItem + Utils.manggiohang.get(i).getSoluong();
            }
            badge1.setText(String.valueOf(totalItem));
        }
    }
}