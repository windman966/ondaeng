package com.fourth.ondaeng;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CommunityAdapter extends BaseAdapter {
    Context context;
    ArrayList<Community_listitem> Community_listitem;

    TextView textView_nickname;
    TextView textView_title;
    TextView textView_date;


    public CommunityAdapter(Context context, ArrayList<Community_listitem> community_listitem) {
        this.context = context;
        this.Community_listitem = community_listitem;
    }


    @Override
    public int getCount() {
        return this.Community_listitem.size();
    }

    @Override
    public Object getItem(int position) {
        return this.Community_listitem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_community ,null);
            textView_nickname = (TextView)convertView.findViewById(R.id.textView_nickname);
            textView_title = (TextView)convertView.findViewById(R.id.textView_title);
            textView_date = (TextView)convertView.findViewById(R.id.textView_date);
        }

        textView_nickname.setText(Community_listitem.get(position).getNickname());
        textView_title.setText(Community_listitem.get(position).getTitle());
        textView_date.setText(Community_listitem.get(position).getDate().toString());

        return convertView;
    }
}
