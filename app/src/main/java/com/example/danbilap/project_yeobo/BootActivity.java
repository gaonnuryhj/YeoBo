package com.example.danbilap.project_yeobo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;

public class BootActivity extends AppCompatActivity {
    CountDownTimer ctimer;
    SharedPreferences setting;



    public void finish() {
        super.finish();
        ctimer.cancel();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ctimer.start();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boot);
        setting = getSharedPreferences("setting", 0);
        init();
        ctimer.start();
    }
    void init(){
        ctimer = new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
               /* if(setting.getBoolean("Auto_Login_enabled", false)){
          *//*  mEmailView.setText(setting.getString("ID", ""));
            mPasswordView.setText(setting.getString("PW", ""));
            Auto_Login.setChecked(true);*//*
                    Intent myIntent = new Intent(BootActivity.this, MainActivity.class);
                    startActivity(myIntent);;
                }*/
                Intent myIntent = new Intent(BootActivity.this, LoginActivity.class);
                startActivity(myIntent);
                BootActivity.this.finish();  // 이전 액티비티 종료 시킴
            }
        };
    }
}
