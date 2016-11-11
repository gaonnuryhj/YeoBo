/*

package com.example.danbilap.project_yeobo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class SharedActivity extends Activity {

    String sharedText ;
    String sharedUrl;
    String sharedDescription;
    String sharedTitle;
    int travel_number;




    SharedInstance s1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shared_context);
        init();

    }
    void init(){
        //디비에서 받아온 정보 array에 저장. 및 어댑터 연결.




        Uri imageUri = null;//데이터 받기 전 초기값은 null
       s1 = new SharedInstance(); //defaut 생성자

        Intent i = getIntent(); //secondActivity로 받은 Intent
          travel_number = i.getIntExtra("travel_number", 0); //여행정보번호


        sharedText = i.getStringExtra("sharedText"); //sharedInstance로 보낼 데이터2
        imageUri = i.getParcelableExtra("imageUri");


        //textView.append("카테고리는 "+category+"\n");
        if(sharedText!=null){ //웹페이지 공유했을 때 실행되는 부분

            handleSendText(sharedText);

        }
        if(imageUri!=null){ //이미지 공유했을 때 실행되는 부분.
            //Toast.makeText(this,"URI 들어왔음",Toast.LENGTH_SHORT).show();
         }

    }

    void handleSendText(String sharedText) {


        int start = sharedText.indexOf("http");

        sharedUrl = sharedText.substring(start); //sharedInstance로 보낼 데이터3
        s1.setSharedUrl(sharedUrl);

        Toast.makeText(this,"url 주소 " + sharedUrl,Toast.LENGTH_LONG).show();
        try {
            ShareTask shareTask = new ShareTask();
            shareTask.execute(sharedUrl); //여기까지 수행하면 보낼 객체 다 받아짐.


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public class ShareTask extends AsyncTask<String, Void, String> { //공유하기를 눌렀을 때 실제로 사용자에게 보여주는 데이터를 처리하는 부분.
        //태그의 파싱이 필요하다.
        HttpURLConnection conn = null;

        @Override
        protected String doInBackground(String... urls) {

            String page = "";
            try {
                URL url = new URL(urls[0]);
                conn = (HttpURLConnection) url.openConnection();
                BufferedInputStream buf = new BufferedInputStream(conn.getInputStream());//바이트 단위로 데이터 저장
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(buf, "utf-8")); //텍스트 형태로 데이터 읽어들임.
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    page += line;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                conn.disconnect();
                return page;
            }


        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Document doc = (Document) Jsoup.parse(result);
           // Elements elements = doc.select("meta");
            Elements elements = (Elements) doc.getElementsByTagName("meta");

            String imageUrl = null;
            for (int i = 0; i < elements.size(); i++) {
                String property = elements.get(i).attr("property");

                if (property.equals("og:image")) {
                    if(imageUrl==null){ //이미지가 아무것도 들어오지 않은 상태일때만 실행.(이미지가 여러개 들어오는 경우 위해)
                        imageUrl = elements.get(i).attr("content");
                       // new ImageTask().execute(imageUrl); //이미지의 url을 Bitmap 형태로 바꾼다.
                    }
                }
                if (property.equals("og:description")) {
                    sharedDescription = elements.get(i).attr("content"); //sharedInstance로 보낼 데이터4
                    s1.setSharedDescription(sharedDescription);
                    //Toast.makeText(getApplicationContext(),"부제목 " +sharedDescription,Toast.LENGTH_LONG).show();
                }
                if (property.equals("og:title")) {
                    sharedTitle = elements.get(i).attr("content");//sharedInstance로 보낼 데이터5
                    s1.setSharedTitle(sharedTitle);
                    //Toast.makeText(getApplicationContext(),"제목 " +sharedTitle,Toast.LENGTH_LONG).show();
*/
/**//*

                }




            }



            if(sharedDescription==null || sharedTitle==null){ //for문 끝난 후에 제목과 부제목이 null인 상태이면.
                sharedTitle = sharedText; //타이틀 부분에 공유하기를 통해 intent로 받아온 값 자체를 뿌려줌.

            }

            if(imageUrl==null){ //for문 끝난 후에도 imgUrl 널값이라면.

            }
            else{
                share_write(travel_number,imageUrl,sharedDescription,sharedTitle);
            }


        }//onPostExecute 함수


    }

    //DB에 값 저장.
    void share_write(final int travel_number, final String sharedUrl, final String sharedDescription, final String sharedTitle){
        new Thread(new Runnable() {
            @Override
            public void run() {
                RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://203.252.182.94/yeoboH.php").build();
                Retrofit retrofit = restAdapter.create(Retrofit.class);
                retrofit.share_write(0,travel_number,sharedUrl,sharedDescription,sharedTitle, new Callback<JsonObject>() {


                    @Override
                    public void success(JsonObject jsonObject, Response response) {
                        JsonArray result = jsonObject.getAsJsonArray("result");
                        String errorCode = ((JsonObject)result.get(0)).get("errorCode").getAsString();
                        if(errorCode.equals("success")){

                            Toast.makeText(SharedActivity.this, "성공적으로 등록 되었습니다.", Toast.LENGTH_SHORT).show();
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
*/
/*

    //DB 값 읽어오기
    void share_read(final int travel_number,final String sharedCategory,int flag){
        new Thread(new Runnable() {
            @Override
            public void run() {
                RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://203.252.166.146:8080").build();
                Retrofit retrofit = restAdapter.create(Retrofit.class);
                retrofit.share_read(travel_number,sharedCategory, 1, new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject jsonObject, Response response) {

                        JsonArray result = jsonObject.getAsJsonArray("result");
                        text1.setText(result.size());
                        for (int i = 0; i < result.size(); i++) {
                            JsonObject obj = (JsonObject) result.get(i); //해당 결과의 행만큼 for문 돌린다.
                            String url = obj.get("share_url").getAsString();
                            String title = obj.get("share_title").getAsString();
                            String subtitle = obj.get("share_subtitle").getAsString();
                            String img = obj.get("share_img").getAsString();
                            Bitmap bitmap = StringToBitMap(img);
                            shared_array.add(new SharedInstance(url,sharedCategory,bitmap,subtitle,title));

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
*//*




}//MainActivity class

*/
