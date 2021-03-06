package com.fourth.ondaeng;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.fourth.ondaeng.R;
import com.fourth.ondaeng.databinding.ActivityDrawerBinding;
import com.fourth.ondaeng.databinding.ActivityVaccination1Binding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Vaccination1Activity extends AppCompatActivity {
    ActivityVaccination1Binding binding;
    Calendar myCalendar = Calendar.getInstance();
    private ActivityDrawerBinding activityDrawerBinding;
    private DrawerLayout drawerLayout;
    private View drawerView;
    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    DatePickerDialog.OnDateSetListener myDatePicker2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel2();
        }
    };

    DatePickerDialog.OnDateSetListener myDatePicker3 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel3();
        }
    };

    DatePickerDialog.OnDateSetListener myDatePicker4 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel4();
        }
    };

    DatePickerDialog.OnDateSetListener myDatePicker5 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel5();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVaccination1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activityDrawerBinding = ActivityDrawerBinding.inflate(getLayoutInflater());
        //??????????????? ?????? ??????
        drawerLayout = binding.drawerLayout;
        drawerView = (View)findViewById(R.id.drawer);

        Button btn_open = (Button)findViewById(R.id.btn_back);
        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerView);
            }
        });

        drawerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }

        });

        Intent myPageIntent = new Intent(this, myPage.class);
        Intent careIntent = new Intent(this, Vaccination1Activity.class);
        Intent commIntent = new Intent(this, CommunityActivity.class);
        Intent walkIntent = new Intent(this, WalkActivity.class);
        Intent hospIntent = new Intent(this, HospitalActivity.class);
        Intent dailyCareIntent = new Intent(this,DailyActivity.class);
        Intent healthCareIntent = new Intent(this,HealthCheck1Activity.class);
        Intent shopIntent = new Intent(this,Shop.class);
        Intent questIntent = new Intent(this, QuestActivity.class);

        //????????? ?????????, ?????? ??????.
        binding.goToMyCorrection.setText(appData.getDogName().toString());
        TextView textView = findViewById(R.id.nickNameOnNav);
        textView.setText(appData.getNickName().toString());
        String imgpath = getCacheDir() + "/profilePic.png";
        Bitmap bm = BitmapFactory.decodeFile(imgpath);
        ImageView imageView = findViewById(R.id.userPhoto);
        imageView.setImageBitmap(bm);
//???????????? ??????
        goToFunc(findViewById(R.id.goToHosp),hospIntent);
        goToFunc(findViewById(R.id.goToWalk),walkIntent);
        goToFunc(findViewById(R.id.goToComm),commIntent);
        goToFunc(findViewById(R.id.goToCareHealth),healthCareIntent);
        goToFunc(findViewById(R.id.goToCareVaccin),careIntent);
        goToFunc(findViewById(R.id.goToCareDaily),dailyCareIntent);
        goToFunc(findViewById(R.id.goToShop),shopIntent);
        goToFunc(findViewById(R.id.goToMyPage),myPageIntent);
        goToFunc(findViewById(R.id.goToQuest),questIntent);

        Button changeButton = findViewById(R.id.changeButton);
        Button registerButton = findViewById(R.id.registerButton);

        Button changeButton2 = findViewById(R.id.changeButton2);
        Button registerButton2 = findViewById(R.id.registerButton2);

        Button changeButton3 = findViewById(R.id.changeButton3);
        Button registerButton3 = findViewById(R.id.registerButton3);

        Button changeButton4 = findViewById(R.id.changeButton4);
        Button registerButton4 = findViewById(R.id.registerButton4);

        Button changeButton5 = findViewById(R.id.changeButton5);
        Button registerButton5 = findViewById(R.id.registerButton5);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Vaccination1Activity.this, myDatePicker,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                registerButton.setVisibility(View.GONE);
                changeButton.setVisibility(View.VISIBLE);

            }
        });

        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Vaccination1Activity.this, myDatePicker,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        registerButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Vaccination1Activity.this, myDatePicker2,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                registerButton2.setVisibility(View.GONE);
                changeButton2.setVisibility(View.VISIBLE);
            }
        });

        changeButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Vaccination1Activity.this, myDatePicker2,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        registerButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Vaccination1Activity.this, myDatePicker3,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                registerButton3.setVisibility(View.GONE);
                changeButton3.setVisibility(View.VISIBLE);
            }
        });

        changeButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Vaccination1Activity.this, myDatePicker3,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        registerButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Vaccination1Activity.this, myDatePicker4,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                registerButton4.setVisibility(View.GONE);
                changeButton4.setVisibility(View.VISIBLE);
            }
        });

        changeButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Vaccination1Activity.this, myDatePicker4,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        registerButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Vaccination1Activity.this, myDatePicker5,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                registerButton5.setVisibility(View.GONE);
                changeButton5.setVisibility(View.VISIBLE);
            }
        });

        changeButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Vaccination1Activity.this, myDatePicker5,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



    }

    //????????? ?????? ??? ??? ??? ?????? ?????????
    public void checkDog() {
        Intent addNewDogIntent = new Intent(this,addNewDog.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("??????");
        //???????????????
        String tv_text = "???????????? ??????????????????.";
        builder.setMessage(tv_text);
        //????????????
        builder.setNeutralButton("????????? ????????????", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(addNewDogIntent);
            }
        });
        builder.setPositiveButton("??????", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        builder.show();
    };
    //??????????????????
    public void goToFunc(View view, Intent intent) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(appData.getDogName()==""){
                    checkDog();
                }else{
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(intent);
                    overridePendingTransition(R.anim.horizon_enter,R.anim.none);
                    finish();
                }
            }
        });
    }



    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    //drawer????????????
    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        TextView dateTextView = findViewById(R.id.dateTextView);
        dateTextView.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabel2() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        TextView dateTextView2 = findViewById(R.id.dateTextView2);
        dateTextView2.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabel3() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        TextView dateTextView3 = findViewById(R.id.dateTextView3);
        dateTextView3.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabel4() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        TextView dateTextView4 = findViewById(R.id.dateTextView4);
        dateTextView4.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabel5() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        TextView dateTextView5 = findViewById(R.id.dateTextView5);
        dateTextView5.setText(sdf.format(myCalendar.getTime()));
    }


}