package com.example.danbilap.project_yeobo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TestFragment2 extends Fragment {

    TextView info_nation, nation_name_en;
    int n_id;

    public static TestFragment2 newInstance(int n_id){
        TestFragment2 instance = new TestFragment2();
        instance.n_id = n_id;
        ////
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_test_2, container, false);
        info_nation = (TextView)rootView.findViewById(R.id.info_nation);
        nation_name_en = (TextView)rootView.findViewById(R.id.nation_name_en);

     //   info_nation(n_id); // n_id는 도착 완료 상태

        return rootView;
    }

    /*void info_nation(final int n_id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://203.252.166.146:8080").build();
                Retrofit retrofit = restAdapter.create(Retrofit.class);
                retrofit.info_nation(7, n_id, new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject jsonObject, Response response) {
                        JsonArray result = jsonObject.getAsJsonArray("result");
                        JsonObject obj = (JsonObject) result.get(0);
                        String nation_basic = obj.get("nation_basic").getAsString();
                        String travel_nation_en = obj.get("travel_nation_en").getAsString();

                        nation_name_en.setText(travel_nation_en);
                        info_nation.setText(nation_basic);

                        }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("test", error.toString());
                    }
                });
            }
        }).start();
    }*/
}