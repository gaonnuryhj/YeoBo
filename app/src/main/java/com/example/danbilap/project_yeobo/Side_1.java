package com.example.danbilap.project_yeobo;
import android.app.Activity;
import android.os.Bundle;


public class Side_1 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.side_1_layout);

        Bundle bundle = getIntent().getExtras();//intent로 페이지 넘어옴.

    }
}