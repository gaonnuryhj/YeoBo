/*
package com.example.danbilap.project_yeobo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.UUID;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class JoinActivity extends AppCompatActivity {

    EditText nick;
    CheckBox check1;
    Button btn1;
    TextView warn;
    String password;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        init();
    }

    void init() {

        nick = (EditText)findViewById(R.id.nick);
        check1 = (CheckBox)findViewById(R.id.check1);
        warn = (TextView)findViewById(R.id.warn);
        btn1 = (Button)findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check1.isChecked()==true){
                    // *****핸드폰 고유번호 가져오기*****
                    final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

                    final String tmDevice, tmSerial, androidId;
                    tmDevice = "" + tm.getDeviceId();
                    tmSerial = "" + tm.getSimSerialNumber();
                    androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

                    UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
                    String deviceId = deviceUuid.toString();
                    // *****핸드폰 고유번호 가져오기*****
                    join(deviceId, nick.getText().toString(),password);
                }
                else{
                    warn.setText("동의함에 체크해주세요");
                    String strColor = "#FF4081";
                    warn.setTextColor(Color.parseColor(strColor));
                    warn.setTypeface(null, Typeface.BOLD);
                }
            }
        });
    }

    void join(final String id_num, final String nick,final String password){
        new Thread(new Runnable() {
            @Override
            public void run() {
                RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://203.252.166.146:8080").build();
                Retrofit retrofit = restAdapter.create(Retrofit.class);

                retrofit.join(2, id_num, nick,password, new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject jsonObject, Response response) {
                        JsonArray result = jsonObject.getAsJsonArray("result");
                        String errcode = ((JsonObject)result.get(0)).get("errorCode").getAsString();

                        if (errcode.equals("success")) {

                            Login login = new Login();
                            login.setId(id_num);
                            MyApp.getApp().setLogin(login);

                            Intent intent = new Intent(JoinActivity.this, MainActivity.class);
                            startActivity(intent);
                            JoinActivity.this.finish(); // MainActivity로 이동
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("test", error.toString());
                    }
                });
            }
        }).start();
    }
}
*/
