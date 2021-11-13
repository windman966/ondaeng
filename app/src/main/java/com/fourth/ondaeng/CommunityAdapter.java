package com.fourth.ondaeng;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;

import org.json.JSONObject;

import java.util.ArrayList;

public class CommunityAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<postData> post;

    public CommunityAdapter(Context context, ArrayList<postData> data) {
        mContext = (Context) context;
        post = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return post.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public postData getItem(int position) {
        return post.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.item_community, null);

        TextView idView = (TextView)view.findViewById(R.id.textView_userid);
        TextView titleView = (TextView)view.findViewById(R.id.textView_title);
        TextView dateView = (TextView)view.findViewById(R.id.textView_date);

        idView.setText(post.get(position).getid());
        titleView.setText(post.get(position).gettitle());
        dateView.setText(post.get(position).getdate());

        return view;
    }
}
