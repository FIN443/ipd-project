package com.example.tab3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class MainActivity extends AppCompatActivity {
    Button btn_timer,btn_cook,btn_on_off;
    Button btnConnect; // 블루투스 연결버튼
    private BluetoothSPP bt;
    TextView tv1;
    TextView tv2;
    ImageView background, condition;
    ImageView[] circle_blue = new ImageView[20];
    Animation anim_lotate;
    Animation anim_start;
    Animation anim_inc1;
    Animation anim_inc2;
    Animation anim_inc3;
    Animation anim_deg1;
    Animation anim_deg2;
    Animation anim_deg3;

    boolean hcheck1 = false,hcheck2 = false,hcheck3 = false,hcheck4 = false,hcheck5 = false,hcheck6 = false;

    TranslateAnimation anim1;
    int distance = 600;
    double direction;
    int translationX;
    int translationY;

    int temp_ppm;
    int state_count = 0;
    long prevhour = 0;
    long prevtime1 = 0;

    public static Context context_main; // context 변수 선언
    public int ppm; // 다른 Activity에서 접근할 변수

    int[] durtime = new int[20];
    long[] prevtime = new long[20];
    long prevtime_data;
    Handler mhandler = new Handler();
    MyRunnable runnable = new MyRunnable();
    boolean mIsRunning = false;
    boolean sCheck = false;
    class MyRunnable implements Runnable {
        @Override
        public void run() {
            if(SystemClock.elapsedRealtime() - prevhour > 20000) {
                state_count = 0;
                prevhour = SystemClock.elapsedRealtime();
            }
            if (sCheck == true) {
                for (int i = 0; i < prevtime.length; i++) {
                    if (SystemClock.elapsedRealtime() - prevtime[i] > durtime[i]) {
                        circle_blue[i].setAlpha(1f);
                        direction = Math.random() * 2 * Math.PI;
                        translationX = (int) (Math.cos(direction) * distance);
                        translationY = (int) (Math.sin(direction) * distance);
                        anim1 = new TranslateAnimation
                                (translationX,
                                        0,
                                        translationY,
                                        0);
                        anim1.setDuration(durtime[i]);
                        int temp1 = (int) (durtime[i] * 0.8);
                        circle_blue[i].animate().alpha(0f).setDuration(temp1);
                        circle_blue[i].startAnimation(anim1);

                        prevtime[i] = SystemClock.elapsedRealtime();
                    }
                }
            }
            mhandler.post(this);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context_main = this; // onCreate에서 this 할당

        Intent intent = new Intent(this, LoadingActivity.class);
        startActivity(intent);
        tv1 = findViewById(R.id.textView1);
        tv2 = findViewById(R.id.textView2);
        btnConnect = findViewById(R.id.onoffbutton); //연결시도
        btn_timer = findViewById(R.id.button_timer2);
        btn_cook = findViewById(R.id.button_cook1);
        btn_on_off=findViewById(R.id. onoffbutton);

        // 영역 설정
        background = findViewById(R.id.background_oval);
        condition = findViewById(R.id.imageView3);
        //애니메이션 이미지 리소스
        circle_blue[0] = findViewById(R.id.circle_blue);
        circle_blue[1] = findViewById(R.id.circle_blue2);
        circle_blue[2] = findViewById(R.id.circle_blue3);
        circle_blue[3] = findViewById(R.id.circle_blue4);
        circle_blue[4] = findViewById(R.id.circle_blue5);
        circle_blue[5] = findViewById(R.id.circle_blue6);
        circle_blue[6] = findViewById(R.id.circle_blue7);
        circle_blue[7] = findViewById(R.id.circle_blue8);
        circle_blue[8] = findViewById(R.id.circle_blue9);
        circle_blue[9] = findViewById(R.id.circle_blue10);
        circle_blue[10] = findViewById(R.id.circle_blue11);
        circle_blue[11] = findViewById(R.id.circle_blue12);
        circle_blue[12] = findViewById(R.id.circle_blue13);
        circle_blue[13] = findViewById(R.id.circle_blue14);
        circle_blue[14] = findViewById(R.id.circle_blue15);
        circle_blue[15] = findViewById(R.id.circle_blue16);
        circle_blue[16] = findViewById(R.id.circle_blue17);
        circle_blue[17] = findViewById(R.id.circle_blue18);
        circle_blue[18] = findViewById(R.id.circle_blue19);
        circle_blue[19] = findViewById(R.id.circle_blue20);

        bt = new BluetoothSPP(this); // initializing

        if (!bt.isBluetoothAvailable()) { //블루투스 사용 불가
            Toast.makeText(getApplicationContext()
                    , "Bluetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            finish();
        }

        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() { //데이터 수신
            public void onDataReceived(byte[] data, String message) {
                temp_ppm = Integer.parseInt(message); // 저장
                tv1.setText(message);
                int temp = temp_ppm;
                if (temp == 0) {
                    sCheck = false;
                    if (Integer.parseInt(tv1.getText().toString()) >= 50 && Integer.parseInt(tv1.getText().toString()) < 100) {
                        background.startAnimation(anim_deg2);
                        condition.setBackgroundResource(R.drawable.condition_basic);
                        // 나쁨에서 꺼짐
                    } else if (Integer.parseInt(tv1.getText().toString()) >= 100) {
                        background.startAnimation(anim_deg3);
                        condition.setBackgroundResource(R.drawable.condition_basic);
                        // 매우나쁨에서 꺼짐
                    } else {
                        condition.setBackgroundResource(R.drawable.condition_basic);
                        // 좋음에서 꺼짐
                    }
                    tv1.setTextColor(Color.BLACK);
                    tv2.setTextColor(Color.BLACK);
                    background.setBackground(getResources().getDrawable(R.drawable.black_gra));
                    for (int i = 0; i < circle_blue.length; i++) {
                        circle_blue[i].setAlpha(0f);
                    }
                    tv2.setText("꺼짐");
                } else if (temp >= 1 && temp < 50) {
                    sCheck = true;
                    if (Integer.parseInt(tv1.getText().toString()) >= 50 && Integer.parseInt(tv1.getText().toString()) < 100) {
                        background.startAnimation(anim_deg2);
                        condition.setBackgroundResource(R.drawable.condition_bar0);
                        // 나쁨에서 좋음
                    } else if (Integer.parseInt(tv1.getText().toString()) >= 100) {
                        background.startAnimation(anim_deg3);
                        condition.setBackgroundResource(R.drawable.condition_bar0);
                        // 매우 나쁨에서 좋음
                    } else {
                        condition.setBackgroundResource(R.drawable.condition_bar0);
                        // 꺼짐에서 좋음
                    }
                    tv1.setTextColor(Color.parseColor("#239CED"));
                    tv2.setTextColor(Color.parseColor("#239CED"));
                    background.setBackground(getResources().getDrawable(R.drawable.blue));
                    condition.setBackgroundResource(R.drawable.condition_bar0);
                    for (int i = 0; i < circle_blue.length; i++) {
                        circle_blue[i].setBackground(getResources().getDrawable(R.drawable.mole));
                    }
                    tv2.setText("좋음");
                } else if (temp >= 50 && temp < 100) {
                    sCheck = true;
                    if (Integer.parseInt(tv1.getText().toString()) >= 0 && Integer.parseInt(tv1.getText().toString()) < 50) {
                        background.startAnimation(anim_inc1);
                        condition.setBackgroundResource(R.drawable.condition_bar1);
                        // 좋음(꺼짐)에서 나쁨
                    } else if (Integer.parseInt(tv1.getText().toString()) >= 100) {
                        background.startAnimation(anim_deg1);
                        condition.setBackgroundResource(R.drawable.condition_bar1);
                        // 매우 나쁨에서 나쁨
                    }
                    tv1.setTextColor(Color.parseColor("#FFBA63"));
                    tv2.setTextColor(Color.parseColor("#FFBA63"));
                    background.setBackground(getResources().getDrawable(R.drawable.orange));
                    condition.setBackgroundResource(R.drawable.condition_bar1);
                    for (int i = 0; i < circle_blue.length; i++) {
                        circle_blue[i].setBackground(getResources().getDrawable(R.drawable.mole2));
                    }
                    tv2.setText("나쁨");
                } else {
                    sCheck = true;
                    if (Integer.parseInt(tv1.getText().toString()) >= 0 && Integer.parseInt(tv1.getText().toString()) < 50) {
                        background.startAnimation(anim_inc3);
                        condition.setBackgroundResource(R.drawable.condition_bar2);
                        // 좋음(꺼짐)에서 매우 나쁨
                    } else if (Integer.parseInt(tv1.getText().toString()) >= 50 && Integer.parseInt(tv1.getText().toString()) < 100) {
                        background.startAnimation(anim_inc2);
                        condition.setBackgroundResource(R.drawable.condition_bar2);
                        // 나쁨에서 매우 나쁨
                    }
                    tv1.setTextColor(Color.parseColor("#FF8281"));
                    tv2.setTextColor(Color.parseColor("#FF8281"));
                    background.setBackground(getResources().getDrawable(R.drawable.red));
                    condition.setBackgroundResource(R.drawable.condition_bar2);
                    for (int i = 0; i < circle_blue.length; i++) {
                        circle_blue[i].setBackground(getResources().getDrawable(R.drawable.mole3));
                    }
                    tv2.setText("매우 나쁨");
                }

            }
        });

        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() { //연결됐을 때
            public void onDeviceConnected(String name, String address) {
                btn_on_off.setBackgroundResource(R.drawable.on_button);
                Toast.makeText(getApplicationContext()
                        , "Connected to " + name + "\n" + address
                        , Toast.LENGTH_SHORT).show();
                sCheck = true;
            }

            public void onDeviceDisconnected() { //연결해제
                btn_on_off.setBackgroundResource(R.drawable.off_button);
                Toast.makeText(getApplicationContext()
                        , "Connection lost", Toast.LENGTH_SHORT).show();
                sCheck = false;
                if (Integer.parseInt(tv1.getText().toString()) >= 50 && Integer.parseInt(tv1.getText().toString()) < 100) {
                    background.startAnimation(anim_deg2);
                    condition.setBackgroundResource(R.drawable.condition_basic);
                    // 나쁨에서 꺼짐
                } else if (Integer.parseInt(tv1.getText().toString()) >= 100) {
                    background.startAnimation(anim_deg3);
                    condition.setBackgroundResource(R.drawable.condition_basic);
                    // 매우나쁨에서 꺼짐
                } else {
                    condition.setBackgroundResource(R.drawable.condition_basic);
                    // 좋음에서 꺼짐
                }
                tv1.setTextColor(Color.BLACK);
                tv2.setTextColor(Color.BLACK);
                background.setBackground(getResources().getDrawable(R.drawable.black_gra));
                for (int i = 0; i < circle_blue.length; i++) {
                    circle_blue[i].setAlpha(0f);
                }
                tv1.setText("0");
                tv2.setText("꺼짐");
            }

            public void onDeviceConnectionFailed() { //연결실패
                Toast.makeText(getApplicationContext()
                        , "Unable to connect", Toast.LENGTH_SHORT).show();
            }
        });

        btnConnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bt.disconnect();
                } else {
                    Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });

        for (int i = 0; i < durtime.length; i++) {
            durtime[i] = (int) ((Math.random() * 8000) + 4000);
        }

        if (mIsRunning == false) {
            for (int i = 0; i < prevtime.length; i++) {
                prevtime[i] = SystemClock.elapsedRealtime();
                ;
            }
            prevtime_data = SystemClock.elapsedRealtime();
            mhandler.post(runnable);
            mIsRunning = true;
        }

        for (int i = 0; i < circle_blue.length; i++) {
            circle_blue[i].setAlpha(0f);
            circle_blue[i].setScaleX((float) 1.3);
            circle_blue[i].setScaleY((float) 1.3);
            if (i < (circle_blue.length / 2)) {
                circle_blue[i].setScaleX((float) 0.85);
                circle_blue[i].setScaleY((float) 0.85);
            }
        }

        tv1.setTextColor(Color.BLACK);
        tv2.setTextColor(Color.BLACK);
        tv1.setText("0");
        tv2.setText("꺼짐");
        anim_start = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha);
        anim_lotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        anim_inc1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_v1);
        anim_inc2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_v2);
        anim_inc3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_v3);
        anim_deg1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_d1);
        anim_deg2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_d2);
        anim_deg3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_d3);
        condition.setBackgroundResource(R.drawable.condition_basic);
        background.startAnimation(anim_start);

        btn_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent =new Intent( getApplicationContext(),TimerActivity.class);
                startActivity(intent);
                Toast.makeText(getApplication(), "타이머 기능을 실행합니다. ", Toast.LENGTH_LONG).show();

            }
        });

        btn_cook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent =new Intent( getApplicationContext(),CookActivity.class);
                startActivity(intent);
                Toast.makeText(getApplication(), "요리 기능을 실행합니다. ", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bt.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!bt.isBluetoothEnabled()) { //
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if (!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER); //DEVICE_ANDROID는 안드로이드 기기 끼리
            }
        }
    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        //bt.stopService(); //블루투스 중지
//        mhandler.removeCallbacks(runnable);
//        mIsRunning = false;
//    }

    @Override
    protected void onDestroy() {
        bt.stopService(); //블루투스 중지
        super.onDestroy();
        mhandler.removeCallbacks(runnable);
        mIsRunning = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mIsRunning == false) {
            mhandler.post(runnable);
            for(int i=0; i<prevtime.length; i++) {
                prevtime[i] = SystemClock.elapsedRealtime();;
            }
            mIsRunning = true;
        }
    }
}