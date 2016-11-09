package com.example.danbilap.project_yeobo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SecondActivity extends AppCompatActivity {

    String t_title, id,url,image;
    int t_num;
    TextView title;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        init();





    }

    void init(){

        title = (TextView)findViewById(R.id.title);

        Bundle bundle = getIntent().getExtras();

        t_title  = bundle.getString("t_title");
        title.setText(t_title);

        t_num = bundle.getInt("t_num");
        id=bundle.getString("u_id");
        url=bundle.getString("url");
//        image=bundle.getString("imgurl");
        if(url!=null){
            saveUrl(url,t_num);
        }
        Toast.makeText(this,image,Toast.LENGTH_LONG).show();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);

        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(new TestFragment1());
        fragmentArrayList.add(TestFragment2.newInstance(1));
        fragmentArrayList.add(new TestFragment3());

        TestViewPagerAdapter adapter = new TestViewPagerAdapter(getSupportFragmentManager(), fragmentArrayList);
        pager.setAdapter(adapter);

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);




    }

    public void saveUrl(final String share_url,final int t_num){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://203.252.182.94/yeobo.php").build();
                    Retrofit retrofit = restAdapter.create(Retrofit.class);
                    retrofit.share_write(0,t_num,share_url, new Callback<JsonObject>() {
                        @Override
                        public void success(JsonObject jsonObject, Response response) {
                            JsonArray result = jsonObject.getAsJsonArray("result");
                            String errorCode = ((JsonObject) result.get(0)).get("errorCode").getAsString();
                            if (errorCode.equals("success")) {

                                Toast.makeText(SecondActivity.this, "성공적으로 등록 되었습니다.", Toast.LENGTH_SHORT).show();
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



    @Override
    public void onBackPressed() { // 뒤로 가기 했을 때 MainActivity로 돌아가도록
        super.onBackPressed();
        SecondActivity.this.finish();
    }
}
