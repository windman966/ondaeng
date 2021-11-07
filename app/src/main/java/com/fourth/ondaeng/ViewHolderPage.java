package com.fourth.ondaeng;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fourth.ondaeng.R;
import com.fourth.ondaeng.dogIdCardData;
// 리싸이클러 뷰 안쪽에 데이터 추가하는 class
public class ViewHolderPage extends RecyclerView.ViewHolder {

    private TextView tv_title;


    dogIdCardData data;

    public ViewHolderPage(View itemView) {
        super(itemView);
        tv_title = itemView.findViewById(R.id.dogIdName);

    }

    public void onBind(dogIdCardData data){
        this.data = data;

        tv_title.setText(data.getDogName());

    }
}