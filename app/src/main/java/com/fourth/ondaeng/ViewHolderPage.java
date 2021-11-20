package com.fourth.ondaeng;

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ComponentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.fourth.ondaeng.R;
import com.fourth.ondaeng.databinding.ActivityMainBinding;
import com.fourth.ondaeng.dogIdCardData;
// 리싸이클러 뷰 안쪽에 데이터 추가하는 class
public class ViewHolderPage extends RecyclerView.ViewHolder {

    final TextView tv_name;
    final TextView tv_birth;
    final TextView tv_regist;
    final TextView tv_breed;
    final Button btn_select;
//    final TextView header_tv_name;

    dogIdCardData data;
    public ViewHolderPage(View itemView) {
        super(itemView);
        tv_name = itemView.findViewById(R.id.dogIdName);
        tv_birth = itemView.findViewById(R.id.dogIdBirth);
        tv_regist = itemView.findViewById(R.id.dogIdRegist);
        tv_breed = itemView.findViewById(R.id.dogIdBreed);


        btn_select = itemView.findViewById(R.id.selectDogBtn);

//        header_tv_name = binding.goToMyCorrection;

    }

    public void onBind(dogIdCardData data){
        this.data = data;
        String dogName = data.getDogName();

        tv_name.setText(data.getDogName());
        tv_birth.setText(data.getBirth());
        tv_regist.setText(data.getRegistNo());
        tv_breed.setText(data.getBreed());


        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                appData.setDogName(data.getDogName());
                Toast.makeText(btn_select.getContext(), data.getDogName().toString(), Toast.LENGTH_SHORT).show();
                appData.getInstance().header_name_tv.setText(appData.getDogName().toString());

//                view.getContext().
//                header_tv_name.setText(appData.getDogName().toString());

            }
        });
        if(dogName == "addNewDog"){
            tv_name.setVisibility(View.GONE);
            tv_birth.setVisibility(View.GONE);
            tv_regist.setVisibility(View.GONE);
            tv_breed.setVisibility(View.GONE);
        }


    }


}
