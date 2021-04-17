package com.example.tab3;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class PopupActivity extends AppCompatActivity {

    TextView date1;

    MyRunnable runnable = new MyRunnable();
    Handler mhandler = new Handler();
    class MyRunnable implements Runnable {
        @Override
        public void run() {

            mhandler.post(this);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        date1 = (TextView) findViewById(R.id.Date);
        String format = new String("yyyy'년 'MM'월 'dd'일 'HH'시간 'mm'분 'ss'초" +
                "'");
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.KOREA);
        date1.setText(sdf.format(new Date()));



        mhandler.post(runnable);
    }

    // 동작 버튼 클릭
    public void mOk(View v) {
        Intent intent = new Intent();
        intent.putExtra("check", 1);
        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    protected void onDestroy() {
        super.onDestroy();
        mhandler.removeCallbacks(runnable);
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}

