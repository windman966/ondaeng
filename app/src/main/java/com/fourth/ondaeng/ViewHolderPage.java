package com.fourth.ondaeng;

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fourth.ondaeng.R;
import com.fourth.ondaeng.dogIdCardData;
// 리싸이클러 뷰 안쪽에 데이터 추가하는 class
public class ViewHolderPage extends RecyclerView.ViewHolder {

    final TextView tv_name;
    final TextView tv_birth;
    final TextView tv_regist;
    final TextView tv_breed;
    final Button btn_select;

    dogIdCardData data;

    public ViewHolderPage(View itemView) {
        super(itemView);
        tv_name = itemView.findViewById(R.id.dogIdName);
        tv_birth = itemView.findViewById(R.id.dogIdBirth);
        tv_regist = itemView.findViewById(R.id.dogIdRegist);
        tv_breed = itemView.findViewById(R.id.dogIdBreed);

        btn_select = itemView.findViewById(R.id.selectDogBtn);


    }

    public void onBind(dogIdCardData data){
        this.data = data;

        tv_name.setText(data.getDogName());
        tv_birth.setText(data.getBirth());
        tv_regist.setText(data.getRegistNo());
        tv_breed.setText(data.getBreed());

        btn_select.setText(data.getDogName());
    }


}