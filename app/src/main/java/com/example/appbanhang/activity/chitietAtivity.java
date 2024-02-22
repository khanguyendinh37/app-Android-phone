package com.example.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.Urls.Utils;
import com.example.appbanhang.model.user;
import com.example.appbanhang.model.userModel;

public class chitietAtivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView avatar;
    TextView userName,fullName,phone,email,birthday,gender;
    user user = Utils.user_currant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitiet_ativity);
        Anhxa();
        ActionToolBar();
        initData();

    }

    private void initData() {
        userName.setText(user.getUsername());
        fullName.setText(user.getFullName());
        phone.setText(user.getMobile());
        email.setText(user.getEmail());
        birthday.setText(user.getBirthday());
        gender.setText(user.getGender());
        Glide.with(getApplicationContext()).load(user.getAvatar()).into(avatar);

    }

    private void Anhxa() {
        toolbar = findViewById(R.id.toobar1);
        avatar = findViewById(R.id.avatar1);
        userName = findViewById(R.id.userName);
        fullName = findViewById(R.id.fullName);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        birthday = findViewById(R.id.birtday);
        gender = findViewById(R.id.gender);

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

}