package com.example.danbilap.project_yeobo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Gawon on 2016-06-19.
 */
public class SharedAdapter extends ArrayAdapter<SharedInstance> {//리스트뷰에 sharedInstance의 ArrayList를 세팅시켜줄 adapter class

    ArrayList<SharedInstance> s_items;
    Context context;



    public SharedAdapter(Context context, int resource, ArrayList<SharedInstance> objects) {
        super(context, resource, objects);
        this.context = context;
        this.s_items = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null){//뷰가 없을 시 row 레이아웃 만들어준다.
            LayoutInflater vi =
                    (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.shared,null);
        }
        final SharedInstance si = s_items.get(position);//final로 선언해줘야 한다.
        if(si != null) {
            final ImageView imageView = (ImageView)v.findViewById(R.id.imageView);
            final TextView titleView = (TextView) v.findViewById(R.id.titleView);
            final TextView subtitleView = (TextView) v.findViewById(R.id.subTitleView);



            //레이아웃에 객체 값을 세팅해준다.
            if(imageView!=null) {

                imageView.setImageBitmap(si.getSharedBitmap());//공유해서 가져온 이미지로 보여준다. //default로 세팅.

                imageView.setOnClickListener(new View.OnClickListener() {//이미지뷰에 해당페이지로 링크 걸기.

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse(si.getSharedUrl()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //***activity가 아닌 곳에서 intent 실행할 때 필요.
                        v.getContext().startActivity(intent);
                    }
                });
            }

            if(titleView!=null) {
                titleView.setText(si.getSharedTitle());
            }

            if(subtitleView!=null) {
                subtitleView.setText(si.getSharedDescription());//부제목은 null값 들어오면 아무것도 보이지 않은 상태로 두면 됨.
            }

        }

        return v;
    }



}
