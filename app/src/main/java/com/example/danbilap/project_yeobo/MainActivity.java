package com.example.danbilap.project_yeobo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.UUID;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity
        {

    // 해당 여행 나라의 국기 이미지
    ArrayList<Travel> t_arr;
    int[] background = {R.drawable.sample1, R.drawable.sample2, R.drawable.sample3};

    // 각 여행마다 보여주어야할 것들
    TextView title_travel, start_date, end_date;
    ImageView flag;

    String u_id;

    GridView gridView1;
    GridAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i=getIntent();
        u_id=i.getStringExtra("id");
        init();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                intent.putExtra("id", u_id);
                startActivity(intent);
            }
        });

    }

    void init() {

        t_arr = new ArrayList<Travel>();

        gridView1 = (GridView) findViewById(R.id.gridView1);


        show_travel(u_id);

        gridAdapter = new GridAdapter(this, R.layout.gridview1_item, background, t_arr);
        // 커스텀 어댑터를 GridView 에 적용
        gridView1.setAdapter(gridAdapter);
        // 클릭하면 뷰페이저로 넘어감
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Travel t = t_arr.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("t_title", t.getT_title());
                bundle.putInt("t_num", t.getT_id());
                bundle.putString("u_id",t.getU_id());
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


    }

    void show_travel(final String u_id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://203.252.182.94/yeobo.php").build();
                Retrofit retrofit = restAdapter.create(Retrofit.class);
                retrofit.show_travel(6, u_id, new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject jsonObject, Response response) {
                        JsonArray result = jsonObject.getAsJsonArray("result");

                        for (int i = 0; i < result.size(); i++) {
                            JsonObject obj = (JsonObject) result.get(i);
                            int t_num = obj.get("travel_number").getAsInt();
                            String t_title = obj.get("travel_title").getAsString();
                            String t_start = obj.get("travel_start").getAsString();
                            String t_finish = obj.get("travel_finish").getAsString();
                            // 국기 이미지 가져와야함
                            Travel t = new Travel(R.drawable.united_states_of_america, t_num, t_title, t_start, t_finish,u_id);
                            t_arr.add(t);
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

