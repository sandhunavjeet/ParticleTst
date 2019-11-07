package com.example.particle1test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.particle.android.sdk.cloud.ParticleCloud;
import io.particle.android.sdk.cloud.ParticleCloudSDK;
import io.particle.android.sdk.cloud.ParticleDevice;
import io.particle.android.sdk.cloud.exceptions.ParticleCloudException;
import io.particle.android.sdk.utils.Async;

import android.nfc.Tag;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // MARK: Debug info
    private final String TAG = "particle";

    // MARK: Particle Account Info
    private final String PARTICLE_USERNAME = "navsandhu343@gmail.com";
    private final String PARTICLE_PASSWORD = "Ajaib@111";

    // MARK: Particle device-specific info
    private final String DEVICE_ID = "370034001047363333343437";

    // MARK: Particle Publish / Subscribe variables
    private long subscriptionId;

    // MARK: Particle device
    private ParticleDevice mDevice;
    TextView tv2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b = findViewById(R.id.button);
        tv2 =findViewById(R.id.textView2);
        ParticleCloudSDK.init(this.getApplicationContext());

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final MyCounter timer = new MyCounter(21000,1000);
                timer.start();
            }
        });
        // 1. Initialize your connection to the Particle API


        //getDeviceFromCloud();
    }



    public class MyCounter extends CountDownTimer{
    long startTime;
        public MyCounter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            startTime = millisInFuture;
        }

        @Override
        public void onFinish() {
            System.out.println("Timer Completed.");
          //  tv2.setText("Timer Completed.");
        }

        @Override
        public void onTick(long millisUntilFinished) {
           long timeElapsed = startTime - millisUntilFinished;
           long f = timeElapsed/1000;
            tv2.setText(f+"");

            updateParticle(f);
        }
    }


    public void updateParticle(long time) {
      Log.d(TAG,time +  " called" );
        Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {

            @Override
            public Object callApi(@NonNull ParticleCloud particleCloud) throws ParticleCloudException, IOException {
                particleCloud.logIn(PARTICLE_USERNAME, PARTICLE_PASSWORD);
                mDevice = particleCloud.getDevice(DEVICE_ID);

                Log.d(TAG, "connected  Successfully got device from Cloud");
                List<String> functionParameters = new ArrayList<String>();
                functionParameters.add(time+"");

                try {
                    int a = mDevice.callFunction("turnonled", functionParameters);
                    Log.d(TAG, "called  " + a);
                }
                catch(Exception e)
                {
                    e.printStackTrace();;
                }
                return -1;

            }

            @Override
            public void onSuccess(Object o) {

                Log.d(TAG, "Successfully got device from Cloud");
            }

            @Override
            public void onFailure(ParticleCloudException exception) {
                Log.d(TAG, exception.getBestMessage());
            }
        });
    }


}
