package com.fourth.ondaeng;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class HealthCheckAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<HealthcheckData> health;

    public HealthCheckAdapter(Context context, ArrayList<HealthcheckData> data) {
        mContext = (Context) context;
        health = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return health.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public HealthcheckData getItem(int position) {
        return health.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.item_healthcheck, null);

        TextView dNameView = (TextView)view.findViewById(R.id.textView_diseaseName);
        TextView explanationView = (TextView)view.findViewById(R.id.textView_explanation);
        TextView causeView = (TextView)view.findViewById(R.id.textView_cause);
        TextView symptomView = (TextView)view.findViewById(R.id.textView_symptom);
        TextView preventiveView = (TextView)view.findViewById(R.id.textView_preventive);


        dNameView.setText(health.get(position).getdiseaseName());
        explanationView.setText(health.get(position).getexplanation());
        causeView.setText(health.get(position).getcause());
        symptomView.setText(health.get(position).getsymptom());
        preventiveView.setText(health.get(position).getpreventive());

        return view;
    }
}
