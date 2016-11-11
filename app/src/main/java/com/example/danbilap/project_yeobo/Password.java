package com.example.danbilap.project_yeobo;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Password extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private AutoCompleteTextView mEmailView;

    private View mProgressView;
    private View mLoginFormView;

    String email, password, check,url;
    int num=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getpass);

        Intent share=getIntent();
        url=share.getStringExtra("url");
        

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);




        Button join = (Button) findViewById(R.id.get);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });


    }


    private void attemptLogin() {

                getPass(email);
                if (num == 1) {
                    Log.d("test", "test");
                    Toast.makeText(Password.this, "해당 email이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            else if(num==0){
                    Toast.makeText(Password.this, "해당 email로 비밀번호를 전송하였습니다.", Toast.LENGTH_SHORT).show(); //임시비밀번호 전송으로 할까?
                    Intent i = new Intent(Password.this,LoginActivity.class);
                    i.putExtra("url",url);
                    startActivity(i);
                    Password.this.finish();
                }
            else if (num == 2) {
                Log.d("test", "test");
                Toast.makeText(Password.this, "email전송이 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }

        }


    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }



    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(Password.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    int getPass(final String id_num){
        new Thread(new Runnable() {
            @Override
            public void run() {
                RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://203.252.182.94/yeoboH.php").build();
                Retrofit retrofit = restAdapter.create(Retrofit.class);
                retrofit.getPass(11, id_num, new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject jsonObject, Response response) {
                        JsonArray result = jsonObject.getAsJsonArray("result");
                        String errcode = ((JsonObject)result.get(0)).get("errorCode").getAsString();


                        if(errcode.equals("noEmail")){
                          num=1;
                        }

                        if(errcode.equals("success")){
                            num=0;
                        }
                        if(errcode.equals("fail")){
                            num=2;
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("test", error.toString());
                    }
                });
            }
        }).start();
        return num;
    }

}
