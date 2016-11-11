package com.example.danbilap.project_yeobo;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String t_title, id, url, image;
    int t_num;
    TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        init();


    }

    void init() {

        title = (TextView) findViewById(R.id.title);

        Bundle bundle = getIntent().getExtras();

        t_title = bundle.getString("t_title");
        title.setText(t_title);

        t_num = bundle.getInt("t_num");
        id = bundle.getString("u_id");
        url = bundle.getString("url");
//        image=bundle.getString("imgurl");
        if (url != null) {
            // saveUrl(url,t_num);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //은슬
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //은슬
        ViewPager pager = (ViewPager) findViewById(R.id.pager);

        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(new TestFragment1());
        fragmentArrayList.add(TestFragment2.newInstance(1));
        fragmentArrayList.add(new TestFragment3());
        fragmentArrayList.add(new TestFragment4());


        TestViewPagerAdapter adapter = new TestViewPagerAdapter(getSupportFragmentManager(), fragmentArrayList);
        pager.setAdapter(adapter);

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);


    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }
}
  /*  public void saveUrl(final String share_url,final int t_num){
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
<<<<<<< HEAD
 //       super.onBackPressed();
//        SecondActivity.this.finish();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
 else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_camera) {
//            Intent intent = new Intent(SecondActivity.this, Side_1.class);
//            startActivity(intent);
//            // Handle the camera action
//        }
        if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
=======
        super.onBackPressed();
        SecondActivity.this.finish();
    }*/

