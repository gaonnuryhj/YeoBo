package com.example.danbilap.project_yeobo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


// gridView에 text, img를 연결해주는 GridAdapter
class GridAdapter extends BaseAdapter {

    Context context;
    int layout;
    int[] background;
    ArrayList<Travel> t_arr;
    LayoutInflater inf;

    public GridAdapter(Context context, int layout, int[] background, ArrayList<Travel> t_arr) {
        this.context = context;
        this.layout = layout;
        this.background = background;
        this.t_arr = t_arr;

        // 넣어주기

        inf = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // 배열에 있는 개수만큼 수를 셈
        return t_arr.size();
    }

    @Override
    public Object getItem(int position) {
        return t_arr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null)
            convertView = inf.inflate(layout, null);

        convertView.setBackgroundResource(background[position % 3]);


        Travel t = t_arr.get(position);
        TextView title_travel = (TextView)convertView.findViewById(R.id.title_travel);
        title_travel.setText(t.t_title);
        TextView start_date = (TextView)convertView.findViewById(R.id.start_date);
        start_date.setText(t.t_start);
        TextView end_date = (TextView)convertView.findViewById(R.id.end_date);
        end_date.setText(t.t_end);
        ImageView flag = (ImageView)convertView.findViewById(R.id.flag);
        flag.setImageResource(t.t_flag);

        return convertView;
    }
}