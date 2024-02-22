package com.example.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appbanhang.R;
import com.example.appbanhang.Urls.Utils;
import com.example.appbanhang.retrofit.ApiBanHang;
import com.example.appbanhang.retrofit.RetrofitClient;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangKyActivity extends AppCompatActivity {
    EditText email,phone,name,pass,repass;
    Button dangky;
    ApiBanHang apiBanHang;
    Toolbar toolbar;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        initView();
        initControll();
        ActionToolBar();
    }

    private void initControll() {
        dangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dangKy();
            }
        });
    }

    private void dangKy() {
        String str_email = email.getText().toString().trim();
        String str_pass = pass.getText().toString().trim();
        String str_repass = repass.getText().toString().trim();
        String str_phone = phone.getText().toString().trim();
        String str_username =name.getText().toString().trim();
        boolean emailbl = Patterns.EMAIL_ADDRESS.matcher(str_email).matches();
        if(TextUtils.isEmpty(str_email)){
            Toast.makeText(getApplicationContext(),"bạn chưa nhập Email!",Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(str_phone)){
            Toast.makeText(getApplicationContext(),"bạn chưa nhập số điện thoại!",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(str_username)){
            Toast.makeText(getApplicationContext(),"bạn chưa nhập họ & tên!",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(str_pass)){
            Toast.makeText(getApplicationContext(),"bạn chưa nhập mật khẩu !",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(str_repass)){
            Toast.makeText(getApplicationContext(),"bạn chưa nhập lại mật khẩu",Toast.LENGTH_SHORT).show();
        }else if(!emailbl){
            Toast.makeText(getApplicationContext(),"bạn chưa nhập đúng địa chỉ email!",Toast.LENGTH_SHORT).show();
        } else {
            if(str_pass.equals(str_repass)){
                compositeDisposable.add(apiBanHang.dangKy(str_email,str_pass,str_username,str_phone)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                userModel ->{
                                    if(!userModel.isSuccess()){
                                        Utils.user_currant.setEmail(str_email);
                                        Utils.user_currant.setPass(str_pass);
                                        Intent intent = new Intent(getApplicationContext(),DangNhapActivity.class);
                                        startActivity(intent);
                                        Toast.makeText(getApplicationContext(),"bạn dã đăng ký thành công!",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }else {
                                        Toast.makeText(getApplicationContext(),userModel.getMessage(),Toast.LENGTH_SHORT).show();
                                    }

                                },
                                throwable ->{
                                    Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                        ));
            }else {
                Toast.makeText(getApplicationContext(),"mật khẩu nhập lại của bạn chưa đúng mời kiểm tra lại!",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        email = findViewById(R.id.userEmail);
        phone = findViewById(R.id.userphone);
        name = findViewById(R.id.userName);
        pass = findViewById(R.id.userPassword);
        repass = findViewById(R.id.userRePassword);
        dangky = findViewById(R.id.buttonDangky);
        toolbar = findViewById(R.id.toobarDK);
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
    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}