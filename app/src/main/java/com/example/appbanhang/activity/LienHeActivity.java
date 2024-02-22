package com.example.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.appbanhang.R;
import com.example.appbanhang.Urls.Utils;
import com.example.appbanhang.model.user;

public class LienHeActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText email,message;
    Button submit;
    user user = Utils.user_currant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lien_he);
        anhXa();
        ActionToolBar();
        initData();
    }

    private void initData() {
        email.setText(user.getEmail());
    }

    private void anhXa() {
        toolbar = findViewById(R.id.toobar2);
        email = findViewById(R.id.email1);
        message = findViewById(R.id.editTextMessage);
        submit = findViewById(R.id.Gui);
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