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
    ArrayList<community_listitems> list_community_items;

    TextView textView_nickname;
    TextView textView_title;
    TextView textView_date;


    public CommunityAdapter(Context context, ArrayList<community_listitems> list_community_items) {
        this.context = context;
        this.list_community_items = list_community_items;
    }


    @Override
    public int getCount() {//리스트뷰가 몇 개의 아이템을 갖고 있는지
        return this.list_community_items.size();
    }

    @Override
    public Object getItem(int position) {//현재 어떤 아이템인지 알려줌
        return this.list_community_items.get(position);
    }

    @Override
    public long getItemId(int position) {//현재 어떤 포지션
        return position;
    }

    //리스트뷰-xml 연결, 반복문-한 칸씩 화면표시
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_community ,null);
            textView_nickname = (TextView)convertView.findViewById(R.id.textView_nickname);
            textView_title = (TextView)convertView.findViewById(R.id.textView_title);
            textView_date = (TextView)convertView.findViewById(R.id.textView_date);
        }

        textView_nickname.setText(list_community_items.get(position).getNickname());
        textView_title.setText(list_community_items.get(position).getTitle());
        textView_date.setText(list_community_items.get(position).getDate().toString());

        return convertView;
    }
}
