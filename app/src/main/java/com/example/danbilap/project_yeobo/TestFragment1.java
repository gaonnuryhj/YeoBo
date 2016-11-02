package com.example.danbilap.project_yeobo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


//    int test;
//
//    public static TestFragment1 newInstance(int test){
//        TestFragment1 instance = new TestFragment1();
//        instance.test = test;
//        ////
//        return instance;
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_test_1, container, false);
//
//        TextView textView = (TextView) rootView.findViewById(R.id.test_text);
//        textView.setText(""+test);
//
//        return rootView;
//    }
public class TestFragment1 extends Fragment {
    int travel_number;
    String sharedText = null;
    Uri imageUri = null;

    Button spButton; //쇼핑 카테고리 버튼
    Button mkButton; //먹방 카테고리 버튼

    TextView textView;
    String category=null;
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_test_1, container, false);
    textView = (TextView)rootView.findViewById(R.id.textView);

    spButton = (Button)rootView.findViewById(R.id.shoppingButton);
    mkButton = (Button)rootView.findViewById(R.id.mukbangButton);

    spButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            category = spButton.getText().toString();

        }
    });
    mkButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            category = spButton.getText().toString();

        }
    });

    Bundle bundle=new Bundle();
    bundle.putString("category",category);
    bundle.putString("sharedText", sharedText);
    bundle.putString("imageUri"," ");
    bundle.putString("travel_number", " ");

    Intent intent = new Intent(getActivity().getApplication(),SharedActivity.class);
    intent.putExtras(bundle);
    startActivity(intent);
    return rootView;

}


    }

