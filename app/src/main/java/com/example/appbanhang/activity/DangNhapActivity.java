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
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbanhang.R;
import com.example.appbanhang.Urls.Utils;
import com.example.appbanhang.model.user;
import com.example.appbanhang.retrofit.ApiBanHang;
import com.example.appbanhang.retrofit.RetrofitClient;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangNhapActivity extends AppCompatActivity {
    TextView txtdangky,QMK;
    Toolbar toolbar;
    EditText email,pass;
    Button login;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        initView();
        initControll();
        ActionToolBar();
    }

    private void initControll() {
        txtdangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),DangKyActivity.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_email = email.getText().toString().trim();
                String str_pass = pass.getText().toString().trim();
                boolean emailbl = Patterns.EMAIL_ADDRESS.matcher(str_email).matches();
                if(TextUtils.isEmpty(str_email)){
                    Toast.makeText(getApplicationContext(),"bạn chưa nhập Email!",Toast.LENGTH_SHORT).show();
                } else if(TextUtils.isEmpty(str_pass)){
                    Toast.makeText(getApplicationContext(),"bạn chưa nhập mật khẩu!",Toast.LENGTH_SHORT).show();
                }else if(!emailbl){
                    Toast.makeText(getApplicationContext(),"bạn chưa nhập đúng địa chỉ email!",Toast.LENGTH_SHORT).show();
                } else {
                    if(str_pass.equals(str_pass)){

                        compositeDisposable.add(apiBanHang.dangnhap(str_email,str_pass)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        userModel ->{
                                            if(userModel.isSuccess()){
                                                //save paper
                                                Paper.book().write("email",str_email);
                                                Paper.book().write("pass",str_pass);
                                                Utils.user_currant = userModel.getResult().get(0);
                                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                                startActivity(intent);
                                                Toast.makeText(getApplicationContext(),"bạn dã đăng nhập thành công!",Toast.LENGTH_SHORT).show();
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
        });
    }

    private void initView() {
        Paper.init(this);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        txtdangky = findViewById(R.id.Dangky);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        login = findViewById(R.id.buttonLogin);
        QMK = findViewById(R.id.Qpassword);
        toolbar = findViewById(R.id.toobarDN);
        //read data paper
        if(Paper.book().read("email") != null && Paper.book().read("pass") != null){
            email.setText(Paper.book().read("email"));
            pass.setText(Paper.book().read("pass"));
        }
    }
    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent trangchu = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(trangchu);
                finish();
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        if(Utils.user_currant.getEmail() != null && Utils.user_currant.getPass() != null){
            email.setText(Utils.user_currant.getEmail());
            pass.setText(Utils.user_currant.getPass());
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}