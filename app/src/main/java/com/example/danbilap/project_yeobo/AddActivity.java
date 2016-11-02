package com.example.danbilap.project_yeobo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AddActivity extends Activity {

    String key="07UK0uckUlnSndXPdizZft8520tdDINQjVajDra043xgq7zZOviP9kwPFt%2BXTGWkGEpL920rLjkYwpbNx7Ijbg%3D%3D";
    NameIDSearchTask nameIDSearchTask;
    ArrayList<String> countryenname;
    ArrayList<String> countryname;
    ArrayList<String> countryid;

    Button btn1;
    Button btn_start, btn_end;
    Spinner spinner1;
    ArrayAdapter<String> spinner1Adapter;

    EditText title;

    String t_title, t_nation_en, t_nation, n_id, t_start, t_finish;

    InformationSearchTask informationSearchTask;
    ContactSearchTask contactSearchTask;
   // String travel_nation;
    //int nationid;
    String nationbasic;   //id를 통해 받은 국가 정보
    String nationphone;   //국가이름을 통해 받은 긴급연락처 정보
   // String travelnationen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        init();
    }

    void init(){

        title = (EditText)findViewById(R.id.title);

        final TextView start = (TextView)findViewById(R.id.start);
        final TextView end = (TextView)findViewById(R.id.end);

        spinner1 = (Spinner)findViewById(R.id.spinner1);

        btn_start = (Button)findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String strDate =
                                Integer.toString(year) + "-" +
                                Integer.toString(monthOfYear+1) + "-" +
                                Integer.toString(dayOfMonth);
                        start.setText(strDate);
                        start.setTextColor(Color.BLACK);


                    }
                }, 2016, 0, 1); // month는 -1한 값으로 지정
                datePickerDialog.show();
            }
        });

        btn_end = (Button)findViewById(R.id.btn_end);
        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String strDate =
                                Integer.toString(year) + "-" +
                                        Integer.toString(monthOfYear+1) + "-" +
                                        Integer.toString(dayOfMonth);
                        end.setText(strDate);
                        end.setTextColor(Color.BLACK);

                    }
                }, 2016, 0, 1); // month는 -1한 값으로 지정
                datePickerDialog.show();
            }
        });

        btn1 = (Button)findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 입력한 것들 DB에 넣기
                t_title = title.getText().toString();
                t_start = start.getText().toString();
                t_finish = end.getText().toString();
                // t_nation, n_id은 onPostExecute에서

                if (TextUtils.isEmpty(t_title)  || TextUtils.isEmpty(t_nation_en) || TextUtils.isEmpty(n_id) || TextUtils.isEmpty(t_start) || TextUtils.isEmpty(t_finish)) {
                    Toast.makeText(AddActivity.this, "모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    // *****핸드폰 고유번호 가져오기*****
                    final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

                    final String tmDevice, tmSerial, androidId;
                    tmDevice = "" + tm.getDeviceId();
                    tmSerial = "" + tm.getSimSerialNumber();
                    androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

                    UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
                    String deviceId = deviceUuid.toString();
                    // *****핸드폰 고유번호 가져오기*****
                    create_travel(deviceId, t_title, t_nation_en, t_nation, Integer.parseInt(n_id), t_start, t_finish);
                }

                AddActivity.this.finish();

                informationSearchTask = new InformationSearchTask();
                informationSearchTask.execute();

                contactSearchTask = new ContactSearchTask();
                contactSearchTask.execute();
            }
        });

        countryname = new ArrayList<String>();
        countryid = new ArrayList<String>();
        countryenname = new ArrayList<String>();

        nameIDSearchTask = new NameIDSearchTask();
        nameIDSearchTask.execute();


    }

    class InformationSearchTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... country) {

            String url = "http://apis.data.go.kr/1262000/CountryBasicService/getCountryBasicInfo"   //요청 URL
                    + "?ServiceKey=" + key                       //인증키 값
                    + "&id=" + n_id;                 //id 요청값
            XmlPullParserFactory factory;
            XmlPullParser parser;
            URL xmlUrl;
            String returnResult = "";
            String returnResult2 = "";

            try {
                boolean flag1 = false;

                xmlUrl = new URL(url);
                xmlUrl.openConnection().getInputStream();
                factory = XmlPullParserFactory.newInstance();
                parser = factory.newPullParser();
                parser.setInput(xmlUrl.openStream(), "utf-8");

                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            if (parser.getName().equals("basic")) { //국가 정보를 담고 있는 태그
                                flag1 = true;
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            break;
                        case XmlPullParser.TEXT:
                            if (flag1) {
                                Spanned spanned = Html.fromHtml(parser.getText()); //html 태그 제거해주는 작업.
                                String strData = spanned.toString();
                                returnResult += strData + "\n";
                                flag1 = false;
                            }
                            break;
                    }
                    eventType = parser.next();
                }
            } catch (Exception e) {

            }
            return returnResult;
        }

        @Override
        protected void onPostExecute(String result) {

            nationbasic = result;
        }

    }

    class ContactSearchTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... country) {

//            Toast.makeText(getApplicationContext(), t_nation, Toast.LENGTH_SHORT).show();

            String url = "http://apis.data.go.kr/1262000/ContactService/getContactList"   //요청 URL
                    + "?ServiceKey=" + key                        //인증키 값
                    + "&countryName=" + t_nation;

            XmlPullParserFactory factory;
            XmlPullParser parser;
            URL xmlUrl;
            String returnResult = "";

            try {
                boolean flag1 = false;

                xmlUrl = new URL(url);
                xmlUrl.openConnection().getInputStream();
                factory = XmlPullParserFactory.newInstance();
                parser = factory.newPullParser();
                parser.setInput(xmlUrl.openStream(), "utf-8");

                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            if (parser.getName().equals("contact")) { //현지 연락처 정보를 담고 있는 태그
                                flag1 = true;
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            break;
                        case XmlPullParser.TEXT:
                            if (flag1) {
                                Spanned spanned = Html.fromHtml(parser.getText()); //html 태그 제거해주는 작업.
                                String strData = spanned.toString();
                                returnResult += strData + "\n";
                                flag1 = false;
                            }
                            break;
                    }
                    eventType = parser.next();
                }
            } catch (Exception e) {

            }

            return returnResult;
        }

        @Override
        protected void onPostExecute(String result) {
            nationphone = result;

            info_write(t_nation, Integer.parseInt(n_id), nationbasic,nationphone,t_nation_en);
            Toast.makeText(getApplicationContext(),"travel_nation는 "+t_nation,Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),"nationid는 "+n_id,Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),"nationbasic은 "+nationbasic,Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),"nationphone은 "+nationphone,Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),"travelnationen은 "+t_nation_en,Toast.LENGTH_SHORT).show();
        }
    }

    void info_write(final String t_nation, final int n_id, final String nationbasic, final String nationphone, final String t_nation_en) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://203.252.166.146:8080").build();
                Retrofit retrofit = restAdapter.create(Retrofit.class);
                retrofit.info_write(5, t_nation, n_id, nationbasic, nationphone, t_nation_en, new Callback<JsonObject>() {

                    @Override
                    public void success(JsonObject jsonObject, Response response) {
                        JsonArray result = jsonObject.getAsJsonArray("result");
                        String errorCode = ((JsonObject) result.get(0)).get("errorCode").getAsString();
                        if (errorCode.equals("success")) {
                            Toast.makeText(getApplicationContext(), "성공적으로 등록 되었습니다.", Toast.LENGTH_SHORT).show();
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

    void create_travel(final String id_num, final String t_title, final String t_nation_en, final String t_nation, final int n_id, final String t_start, final String t_finish){
        new Thread(new Runnable() {
            @Override
            public void run() {
                RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://203.252.166.146:8080").build();
                Retrofit retrofit = restAdapter.create(Retrofit.class);
                retrofit.create_travel(4,id_num, t_title, t_nation_en, t_nation, n_id, t_start, t_finish, new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject jsonObject, Response response) {
                        JsonArray result = jsonObject.getAsJsonArray("result");
                        String errcode = ((JsonObject)result.get(0)).get("errorCode").getAsString();

                        if (errcode.equals("success")) {
                            Toast.makeText(AddActivity.this, "새로운 여행이 추가되었습니다!", Toast.LENGTH_SHORT).show();
                            Intent myIntent = new Intent(AddActivity.this, MainActivity.class);
                            startActivity(myIntent);
                            AddActivity.this.finish();
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

    public void onBackPressed() { // 뒤로 가기 했을 때 MainActivity로 돌아가도록
        super.onBackPressed();
        AddActivity.this.finish();
    }

    class NameIDSearchTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... country) {

            String url=" http://apis.data.go.kr/1262000/CountryBasicService/getCountryBasicList"   //요청 URL
                    +"?ServiceKey="+key                        //인증키 값
                    +"&numOfRows=999&pageSize=999&pageNo=1&startPage=1";
            XmlPullParserFactory factory;
            XmlPullParser parser;
            URL xmlUrl;
            String returnResult = "";
            String returnResult2 = "";
            String returnResult3 = "";

            try {
                boolean flag1 = false;
                boolean flag2 = false;
                boolean flag3 = false;

                xmlUrl = new URL(url);
                xmlUrl.openConnection().getInputStream();
                factory = XmlPullParserFactory.newInstance();
                parser = factory.newPullParser();
                parser.setInput(xmlUrl.openStream(), "utf-8");

                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.END_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            if (parser.getName().equals("countryName")) {
                                flag1 = true;
                            }
                            if(parser.getName().equals("id"))
                            {
                                flag2 = true;
                            }
                            if(parser.getName().equals("countryEnName"))
                            {
                                flag3 = true;
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            break;
                        case XmlPullParser.TEXT:

                            if (flag1) {
                                String data = parser.getText();
                                returnResult += data+"\n";
                                countryname.add(data);
                                flag1 = false;

//                                String replaceResult = data.replace(" ", "_");

                            }

                            if(flag2)
                            {
                                String data2 = parser.getText();
                                returnResult2 += data2+"\n";
                                countryid.add(data2);
                                flag2 = false;
                            }

                            if(flag3)
                            {
                                String data3 = parser.getText();
                                returnResult3 += data3+"\n";
                                countryenname.add(data3);
                                flag3 = false;
                            }

                            break;
                    }
                    eventType = parser.next();
                }
            } catch (Exception e) {

            }
            return returnResult+returnResult2;
        }

        @Override
        protected void onPostExecute(String result) {

            spinner1Adapter = new ArrayAdapter<String>(getApplicationContext()
                    , R.layout.spinner1_item, countryname);
            spinner1.setAdapter(spinner1Adapter);
            spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    t_nation = countryname.get(position);
                    t_nation_en = countryenname.get(position);
                    n_id = countryid.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }
}
