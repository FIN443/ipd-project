package com.example.tab3;

import android.content.Context;
import android.content.Intent;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

public class TimerActivity extends AppCompatActivity {
    Button btn_home2,btn_cook2,btn_start1,btn_stop1,btn_reset1,btn_timer,btn_stopwatch,btn_start2,btn_stop2,btn_reset2;
    TextView timerview;


    final static int lint =0;
    final static int Run =1;
    final static int Pause =2;

    int cur_Status = lint;
    int myCount=1;
    long myBaseTime;
    long myPauseTime;
    long prevtime = 0;
    long pauseTime;
    long mstatTime;
    boolean timerRunning = false;

    Handler mhandler = new Handler();
    MyRunnable runnable = new MyRunnable();
    boolean mIsRunning = false;
    class MyRunnable implements Runnable {
        @Override
        public void run() {
            //
            if(mIsRunning == true) {
                if(timerRunning == true) {
                    long t = SystemClock.elapsedRealtime();
                    long lap = pauseTime + t - mstatTime;

                    long ms, sec, min;
                    String ms_s, sec_s, min_s;

                    ms = lap % 1000;
                    ms_s = ""+ms;
                    if(ms < 10)
                        ms_s = "00" + ms;
                    else if(ms < 100)
                        ms_s = "0" + ms;

                    sec = lap / 1000 % 60;
                    sec_s = "" + sec;
                    if(sec < 10)
                        sec_s = "0" + sec;

                    min = lap / 1000 / 60 % 100;
                    min_s = "" + min;
                    if(min < 10)
                        min_s = "0" + min;

                    timerview.setText(min_s+":"+sec_s+":"+ms_s);
                }
                if (SystemClock.elapsedRealtime() - prevtime > 1000) {
                    if(((MainActivity)MainActivity.context_main).temp_ppm > 50 && ((MainActivity)MainActivity.context_main).state_count == 0) {
                        mIsRunning = false;
                        ((MainActivity)MainActivity.context_main).state_count = 1;
                        Intent intent = new Intent(TimerActivity.this, PopupActivity.class);
                        intent.putExtra("check", 0);
                        startActivityForResult(intent, 1234);
                    }
                    prevtime = SystemClock.elapsedRealtime();
                }
            }
            mhandler.post(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1234 && resultCode == RESULT_OK) {
            mIsRunning = true;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_timer);
        btn_home2=findViewById(R.id.button_home2);
        btn_cook2=findViewById(R.id.button_cook2);
        btn_start1=findViewById(R.id.button_start1);
        timerview=findViewById(R.id.textView_timer);
        btn_stop1=findViewById(R.id.button_stop1);
        btn_reset1=findViewById(R.id.button_reset1);
        btn_stopwatch=findViewById(R.id.button_stopwatch);
        btn_timer=findViewById(R.id.button_timer);
        btn_timer=findViewById(R.id.button_timer);
        btn_stop2=findViewById(R.id.button_stop2);
        btn_reset2=findViewById(R.id.button_reset2);
        btn_start2=findViewById(R.id.button_start2);
        btn_start1.setVisibility(View.INVISIBLE);
        btn_stop1.setVisibility(View.INVISIBLE);
        btn_reset1.setVisibility(View.INVISIBLE);
        btn_start2.setVisibility(View.INVISIBLE);
        btn_stop2.setVisibility(View.INVISIBLE);
        btn_reset2.setVisibility(View.INVISIBLE);

        if(mIsRunning == false) {
            mIsRunning = true;
            mhandler.post(runnable);
        }

        btn_home2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        btn_cook2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent( getApplicationContext(),CookActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_start1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timerRunning == false) {
                    timerRunning = true;
//                    mIsRunning = true;
                    btn_start1.setBackgroundResource(R.drawable.startbutton);
                    mstatTime = SystemClock.elapsedRealtime();
//                    mhandler.post(runnable);
                    btn_reset1.setBackgroundResource(R.drawable.reset1);
                    btn_stop1.setBackgroundResource(R.drawable.stopbutton);
                }
                else if(timerRunning == true) {
                    timerRunning = false;
//                    mhandler.removeCallbacks(runnable);
//                    mIsRunning = false;
                    StringBuilder str = new StringBuilder("");
                    str.append(timerview.getText().toString().substring(0,2));
                    str.append(timerview.getText().toString().substring(3,5));
                    str.append(timerview.getText().toString().substring(6,9));
                    pauseTime = Long.parseLong(str.toString());
                    btn_start1.setBackgroundResource(R.drawable.start2);

                }

            }

        });

            btn_stop1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(timerRunning == true) {
                        btn_stop1.setBackgroundResource(R.drawable.stop2);
                        btn_start1.setBackgroundResource(R.drawable.start2);
                    }
                    timerRunning = false;
                    pauseTime = 0;
//                    mhandler.removeCallbacks(runnable);
//                    mIsRunning = false;
                    //btn_stop1.setBackgroundResource(R.drawable.stopbutton);
                    btn_reset1.setBackgroundResource(R.drawable.reset1);
                }
            });

            btn_reset1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(timerRunning == true) {
//                        mhandler.post(runnable);
                        pauseTime = 0;
                        mstatTime = SystemClock.elapsedRealtime();
//                        mhandler.post(runnable);
                    }
                    else if(timerRunning == false) {
                        pauseTime = 0;
                        timerview.setText("00:00:000");
                        btn_start1.setBackgroundResource(R.drawable.start2);
                        btn_stop1.setBackgroundResource(R.drawable.stopbutton);
                        btn_reset1.setBackgroundResource(R.drawable.reset2);
                    }
                }
            });


            btn_stopwatch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btn_stopwatch.setBackgroundResource(R.drawable.stopwatchbutton2);
                    if(btn_start1.getVisibility() == view.INVISIBLE) {
                        btn_timer.setBackgroundResource(R.drawable.timerbutton);
                        btn_start1.setVisibility(view.VISIBLE);
                        btn_stop1.setVisibility(view.VISIBLE);
                        btn_reset1.setVisibility(view.VISIBLE);
                        btn_start2.setVisibility(view.INVISIBLE);
                        btn_stop2.setVisibility(view.INVISIBLE);
                        btn_reset2.setVisibility(view.INVISIBLE);

                    } else {
                        btn_start2.setVisibility(view.INVISIBLE);
                        btn_stop2.setVisibility(view.INVISIBLE);
                        btn_reset2.setVisibility(view.INVISIBLE);
                    }

                }
            });


        btn_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_timer.setBackgroundResource(R.drawable.timerbutton2);
                if(btn_start2.getVisibility() == view.INVISIBLE) {
                    btn_stopwatch.setBackgroundResource(R.drawable.stopwatchbutton1);
                    btn_start2.setVisibility(view.VISIBLE);
                    btn_stop2.setVisibility(view.VISIBLE);
                    btn_reset2.setVisibility(view.VISIBLE);
                    btn_start1.setVisibility(view.INVISIBLE);
                    btn_stop1.setVisibility(view.INVISIBLE);
                    btn_reset1.setVisibility(view.INVISIBLE);


                } else {
                    btn_start1.setVisibility(view.INVISIBLE);
                    btn_stop1.setVisibility(view.INVISIBLE);
                    btn_reset1.setVisibility(view.INVISIBLE);
                }

            }
        });

        btn_start2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TimerActivity.this, PopupActivity.class);
                startActivityForResult(intent, 1);
            }
        });




       /* btn_start2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mIsRunning == false) {
                    timerRunning = true;
                    mIsRunning = true;
                    btn_start2.setBackgroundResource(R.drawable.startbutton);
                    mstatTime = SystemClock.elapsedRealtime();
                    mhandler.post(runnable);
                    btn_reset2.setBackgroundResource(R.drawable.reset1);
                }
                else if(mIsRunning == true) {
                    timerRunning = false;
                    mhandler.removeCallbacks(runnable);
                    mIsRunning = false;
                    StringBuilder str = new StringBuilder("");
                    str.append(timerview.getText().toString().substring(0,2));
                    str.append(timerview.getText().toString().substring(3,5));
                    str.append(timerview.getText().toString().substring(6,9));
                    pauseTime = Long.parseLong(str.toString());
                    btn_start2.setBackgroundResource(R.drawable.start2);

                }

                mstatTime = SystemClock.elapsedRealtime();
                mhandler.post(runnable);

            }

        });

        btn_stop2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timerRunning == true) {
                    btn_stop2.setBackgroundResource(R.drawable.stop2);
                    btn_start2.setBackgroundResource(R.drawable.start2);
                }
                timerRunning = false;
                pauseTime = 0;
                mhandler.removeCallbacks(runnable);
                mIsRunning = false;
                //btn_stop1.setBackgroundResource(R.drawable.stopbutton);
                btn_reset2.setBackgroundResource(R.drawable.reset1);
            }
        });

        btn_reset2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timerRunning == true) { mhandler.post(runnable);

                    pauseTime = 0;
                    mstatTime = SystemClock.elapsedRealtime();
                    mhandler.post(runnable);
                }
                else if(timerRunning == false) {
                    pauseTime = 0;
                    timerview.setText("00:00:000");
                    btn_start2.setBackgroundResource(R.drawable.start2);
                    btn_stop2.setBackgroundResource(R.drawable.stopbutton);
                    btn_reset2.setBackgroundResource(R.drawable.reset2);
                }
            }
        });*/

    }

    @Override
    protected void onStop() {
        super.onStop();
        mIsRunning = false;
        mhandler.removeCallbacks(runnable);
    }

    protected void onDestroy() {
        super.onDestroy();
        mhandler.removeCallbacks(runnable);
    }
}