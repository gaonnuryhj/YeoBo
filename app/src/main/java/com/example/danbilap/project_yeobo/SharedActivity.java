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

/**
 * Created by DANBI LAP on 2016-06-22.
 */
public class SharedActivity extends Activity {

    String sharedText ;
    String sharedUrl;
    String sharedCategory ;
    Bitmap sharedBitmap;
    String sharedStringBitmap;
    String sharedDescription;
    String sharedTitle;
    int travel_number;



    ArrayList<SharedInstance> shared_array; //db에서 불러온 객체 담는 리스트. 리스트뷰에 띄워줄것임.
    SharedAdapter sharedAdapter;
    ListView listView;
    SharedInstance s1;

    TextView text1;
    TextView text2;
    TextView text3;
    TextView text4;
    TextView text5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shared_context);
        init();
        text1 = (TextView)findViewById(R.id.text1);
        text2 = (TextView)findViewById(R.id.text2);
        text3 = (TextView)findViewById(R.id.text3);
        text4 = (TextView)findViewById(R.id.text4);
        text5 = (TextView)findViewById(R.id.text5);
    }
    void init(){
        //디비에서 받아온 정보 array에 저장. 및 어댑터 연결.




        Uri imageUri = null;//데이터 받기 전 초기값은 null
        shared_array = new ArrayList<SharedInstance>();
        listView = (ListView)findViewById(R.id.listView);
        s1 = new SharedInstance(); //defaut 생성자

        Intent i = getIntent(); //secondActivity로 받은 Intent
        sharedCategory = i.getStringExtra("category"); //sharedInstance로 보낼 데이터1
        travel_number = i.getIntExtra("travel_number", 0); //여행정보번호
        Toast.makeText(getApplicationContext(), "sharedCategory는 " + sharedCategory, Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(),"travel_number는 "+travel_number,Toast.LENGTH_SHORT).show();
        s1.setSharedCategory(sharedCategory);

        //share_read(travel_number, sharedCategory, ); //해당 여행의 해당 카테고리의 공유된 데이터들 디비에서 받아옴.
        sharedAdapter = new SharedAdapter(getApplicationContext(),R.layout.shared,shared_array);
        listView.setAdapter(sharedAdapter);

        sharedText = i.getStringExtra("sharedText"); //sharedInstance로 보낼 데이터2
        imageUri = i.getParcelableExtra("imageUri");


        //textView.append("카테고리는 "+category+"\n");
        if(sharedText!=null){ //웹페이지 공유했을 때 실행되는 부분

            handleSendText(sharedText);

        }
        if(imageUri!=null){ //이미지 공유했을 때 실행되는 부분.
            handleSendImage(imageUri);
            //Toast.makeText(this,"URI 들어왔음",Toast.LENGTH_SHORT).show();
            shared_array.add(new SharedInstance(sharedBitmap)); //이미지만 넣어준다.
            sharedAdapter = new SharedAdapter(getApplicationContext(),R.layout.shared,shared_array);
            listView.setAdapter(sharedAdapter);
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

    void handleSendImage(Uri imageUri) {

        try {
            sharedBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            String sharedStringBitmap = bitmapToString(sharedBitmap);//디비에 bitmap을 string 형태로 저장할것임

        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }

    }


    public String bitmapToString(Bitmap bitmap){ //Bitmap을 db에 저장하기 위해 String 형태로 바꾼다.
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String temp= Base64.encodeToString(byteArray, Base64.DEFAULT);
        return temp;
    }

    public Bitmap StringToBitMap(String encodedString){//String을 다시 Bitmap 형태로 바꾸기.
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    public Bitmap resizeBitmap(Bitmap bitmap){
        int width=(int)(getWindowManager().getDefaultDisplay().getWidth()); // 가로 사이즈 지정
        int height=(int)(getWindowManager().getDefaultDisplay().getHeight() * 0.8); // 세로 사이즈 지정
        Bitmap resizedbitmap=Bitmap.createScaledBitmap(bitmap, width, height, true); // 이미지 사이즈 조정
        return resizedbitmap;
    }

//    void handleSendMultipleImages(Intent intent) {
//        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
//        if (imageUris != null) {
//            Toast.makeText(this, "이미지 여러개 들어옴", Toast.LENGTH_SHORT).show();
//        }
//    }

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
                        new ImageTask().execute(imageUrl); //이미지의 url을 Bitmap 형태로 바꾼다.
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

                }



            }


            //for문으로 태그 파싱이 끝난 후에

            if(sharedDescription==null || sharedTitle==null){ //for문 끝난 후에 제목과 부제목이 null인 상태이면.
                sharedTitle = sharedText; //타이틀 부분에 공유하기를 통해 intent로 받아온 값 자체를 뿌려줌.

            }

            if(imageUrl==null){ //for문 끝난 후에도 imgUrl 널값이라면.
                //보낼 비트맵을 기본 이미지로 세팅.
                sharedBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.defaultshared); //drawble에서 bitmap 가져오기.
                sharedStringBitmap = bitmapToString(sharedBitmap);

                //imageUrl, sharedDescription, sharedTItle이 들어왔을 때는 실행되지 않도록 if문 안에 넣어준다.
                //****이 부분에서 db insert, insert 성공하면 shared_array에 add하는 부분******

                Toast.makeText(getApplicationContext(),"travel number는 "+travel_number,Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),"sharedUrl는 "+sharedUrl,Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),"sharedCategory는 "+sharedCategory,Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),"sharedStringBitmap은 "+sharedStringBitmap,Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),"sharedDescription은 "+sharedDescription,Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),"sharedTitle은 "+sharedTitle,Toast.LENGTH_SHORT).show();

                //db insert
                //share_write(travel_number,sharedUrl, sharedCategory, "1", sharedDescription, sharedTitle,0);

                shared_array.add(new SharedInstance(sharedUrl,sharedCategory,sharedBitmap,sharedDescription,sharedTitle));
                shared_array.add(new SharedInstance(sharedUrl,sharedCategory,sharedBitmap,sharedDescription,sharedTitle));
                shared_array.add(new SharedInstance(sharedUrl,sharedCategory,sharedBitmap,sharedDescription,sharedTitle));
                shared_array.add(new SharedInstance(sharedUrl,sharedCategory,sharedBitmap,sharedDescription,sharedTitle));
                sharedAdapter = new SharedAdapter(getApplicationContext(),R.layout.shared,shared_array);
                listView.setAdapter(sharedAdapter);
            }


        }//onPostExecute 함수


    }

    public class ImageTask extends AsyncTask<String, Void, Bitmap> { //imgUrl을 Bitmap 형태로 바꿔주기 위해 만든 AsyncTask

        @Override
        protected Bitmap doInBackground(String... imageUrl) {

            Bitmap bm = null;
            try {
                URL url = new URL(imageUrl[0]);
                URLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                int nSize = conn.getContentLength();
                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(), nSize);
                bm = BitmapFactory.decodeStream(bis);
                bis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bm;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            sharedBitmap = bitmap; //sharedInstance로 보낼 데이터3
            sharedStringBitmap = bitmapToString(sharedBitmap); //비트맵을 스트링으로 바꿈.
            //s1.setSharedBitmap(sharedBitmap);

            Toast.makeText(getApplicationContext(),"travel number는 "+travel_number,Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),"sharedUrl는 "+sharedUrl,Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),"sharedCategory는 "+sharedCategory,Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),"sharedStringBitmap은 "+sharedStringBitmap,Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),"sharedDescription은 "+sharedDescription,Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),"sharedTitle은 "+sharedTitle,Toast.LENGTH_SHORT).show();

            //ImageTask onPostExecute가 실행되면 sharedInstance의 생성자 인자값으로 들어가는 값이 모두 들어온 상태이다.
            //따라서 여기서 객체를 생성해준다.
            //디비 저장.

            //share_write(travel_number,sharedUrl, sharedCategory, "1", sharedDescription, sharedTitle,0);
            shared_array.add(new SharedInstance(sharedUrl,sharedCategory,sharedBitmap,sharedDescription,sharedTitle));
            shared_array.add(new SharedInstance("http://naver.com", "여행", BitmapFactory.decodeResource(getResources(), R.drawable.defaultshared),
                    "여행가자", "랄랄라라"));
            shared_array.add(new SharedInstance(sharedUrl,sharedCategory,sharedBitmap,sharedDescription,sharedTitle));
            sharedAdapter = new SharedAdapter(getApplicationContext(),R.layout.shared,shared_array);
            listView.setAdapter(sharedAdapter);


        }
    }


    public class ImageHelper { //이미지뷰에 띄운 이미지 테두리를 둥글게 만드는 클래스.
        public Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                    .getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(rect);
            final float roundPx = pixels;

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

            return output;
        }
    }

    //DB에 값 저장.
    void share_write(final int travel_number, final String sharedUrl, final String sharedCategory, final String sharedStringBitmap, final String sharedDescription, final String sharedTitle, final int flag){
        new Thread(new Runnable() {
            @Override
            public void run() {
                RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://203.252.166.146:8080").build();
                Retrofit retrofit = restAdapter.create(Retrofit.class);
                retrofit.share_write(travel_number,sharedUrl,sharedCategory,sharedStringBitmap,sharedDescription,sharedTitle, flag, new Callback<JsonObject>() {
                   /* Toast.makeText(getApplicationContext(),"sharedText는 "+sharedText,Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),"sharedUrl는 "+sharedUrl,Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),"sharedCategory는 "+sharedCategory,Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),"sharedTitle은 "+sharedTitle,Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),"sharedDescription "+sharedDescription,Toast.LENGTH_SHORT).show();*/

                    @Override
                    public void success(JsonObject jsonObject, Response response) {
                        JsonArray result = jsonObject.getAsJsonArray("result");
                        String errorCode = ((JsonObject)result.get(0)).get("errorCode").getAsString();
                        if(errorCode.equals("success")){

                            Toast.makeText(SharedActivity.this, "성공적으로 등록 되었습니다.", Toast.LENGTH_SHORT).show();
                            shared_array.add(new SharedInstance(sharedUrl,sharedCategory,sharedBitmap,sharedDescription,sharedTitle));
                            sharedAdapter = new SharedAdapter(getApplicationContext(),R.layout.shared,shared_array);
                            listView.setAdapter(sharedAdapter);
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



}//MainActivity class
