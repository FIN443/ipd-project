package com.example.tab3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.like.IconType;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;
import java.util.List;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

public class CookActivity extends AppCompatActivity {
Button btn_home3,btn_cook3,btn_timer3;
ImageButton btn_food1,btn_food2,btn_food3,btn_food4,btn_food5,btn_food6;
LikeButton hbtn1, hbtn2, hbtn3, hbtn4, hbtn5, hbtn6;
LinearLayout linear1, linear2,linear3, linear4,linear5, linear6;

    Handler mhandler = new Handler();
    long prevtime = 0;
    MyRunnable runnable = new MyRunnable();
    boolean mIsRunning = false;
    class MyRunnable implements Runnable {
        @Override
        public void run() {
            //
            if(mIsRunning == true) {
                if (SystemClock.elapsedRealtime() - prevtime > 1000) {
                    if(((MainActivity)MainActivity.context_main).temp_ppm > 50 && ((MainActivity)MainActivity.context_main).state_count == 0) {
                        mIsRunning = false;
                        ((MainActivity)MainActivity.context_main).state_count = 1;
                        Intent intent = new Intent(CookActivity.this, PopupActivity.class);
                        intent.putExtra("check", 0);
                        startActivityForResult(intent, 4321);
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
        if(requestCode == 4321 && resultCode == RESULT_OK) {
            mIsRunning = true;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_cook);

        btn_home3=findViewById(R.id.button_home3);
        btn_timer3=findViewById(R.id.button_timer3);
        btn_food1=findViewById(R.id.button_food1);
        btn_food2=findViewById(R.id.button_food2);
        btn_food3=findViewById(R.id.button_food3);
        btn_food4=findViewById(R.id.button_food4);
        btn_food5=findViewById(R.id.button_food5);
        btn_food6=findViewById(R.id.button_food6);
        hbtn1 = findViewById(R.id.heart_button1);
        hbtn2 = findViewById(R.id.heart_button2);
        hbtn3 = findViewById(R.id.heart_button3);
        hbtn4 = findViewById(R.id.heart_button4);
        hbtn5 = findViewById(R.id.heart_button5);
        hbtn6 = findViewById(R.id.heart_button6);
        linear1 = findViewById(R.id.linear1);
        linear2 = findViewById(R.id.linear2);
        linear3 = findViewById(R.id.linear3);
        linear4 = findViewById(R.id.linear4);
        linear5 = findViewById(R.id.linear5);
        linear6 = findViewById(R.id.linear6);

        linear1.bringToFront();
        linear2.bringToFront();
        linear3.bringToFront();
        linear4.bringToFront();
        linear5.bringToFront();
        linear6.bringToFront();

        hbtn1.setIcon(IconType.Heart);
        hbtn2.setIcon(IconType.Heart);
        hbtn3.setIcon(IconType.Heart);
        hbtn4.setIcon(IconType.Heart);
        hbtn5.setIcon(IconType.Heart);
        hbtn6.setIcon(IconType.Heart);

        if(mIsRunning == false) {
            mIsRunning = true;
            mhandler.post(runnable);
        }


        if(((MainActivity)MainActivity.context_main).hcheck1 == true) {
            hbtn1.setLiked(true);
        }

        hbtn1.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                ((MainActivity)MainActivity.context_main).hcheck1 = true;
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                ((MainActivity)MainActivity.context_main).hcheck1 = false;
            }
        });

        if(((MainActivity)MainActivity.context_main).hcheck2 == true) {
            hbtn2.setLiked(true);
        }

        hbtn2.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                ((MainActivity)MainActivity.context_main).hcheck2 = true;
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                ((MainActivity)MainActivity.context_main).hcheck2 = false;
            }
        });

        if(((MainActivity)MainActivity.context_main).hcheck3 == true) {
            hbtn3.setLiked(true);
        }

        hbtn3.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                ((MainActivity)MainActivity.context_main).hcheck3 = true;
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                ((MainActivity)MainActivity.context_main).hcheck3 = false;
            }
        });

        if(((MainActivity)MainActivity.context_main).hcheck4 == true) {
            hbtn4.setLiked(true);
        }

        hbtn4.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                ((MainActivity)MainActivity.context_main).hcheck4 = true;
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                ((MainActivity)MainActivity.context_main).hcheck4 = false;
            }
        });

        if(((MainActivity)MainActivity.context_main).hcheck5 == true) {
            hbtn5.setLiked(true);
        }

        hbtn5.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                ((MainActivity)MainActivity.context_main).hcheck5 = true;
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                ((MainActivity)MainActivity.context_main).hcheck5 = false;
            }
        });

        if(((MainActivity)MainActivity.context_main).hcheck6 == true) {
            hbtn6.setLiked(true);
        }

        hbtn6.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                ((MainActivity)MainActivity.context_main).hcheck6 = true;
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                ((MainActivity)MainActivity.context_main).hcheck6 = false;
            }
        });

        btn_home3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_timer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent( getApplicationContext(),TimerActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_food1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/oAb2SbY35KU"));
                startActivity(myIntent);

            }
        });

        btn_food2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=-E6aQuF64bs"));
                startActivity(myIntent);
            }
        });

        btn_food3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=gyVEaJ_BOA0"));
                startActivity(myIntent);

            }
        });

        btn_food4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=qWbHSOplcvY&t=1s"));
                startActivity(myIntent);

            }
        });

        btn_food5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=BtdbSTxN37c"));
                startActivity(myIntent);

            }
        });

        btn_food6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=eIo2BaE6LxI"));
                startActivity(myIntent);

            }
        });
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        mIsRunning = false;
//        mhandler.removeCallbacks(runnable);
//    }

    protected void onDestroy() {
        super.onDestroy();
        mhandler.removeCallbacks(runnable);
    }

}
