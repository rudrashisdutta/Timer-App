package com.android.timerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    MediaPlayer media;
    Button startTimer;
    Button stopButton;
    TextView displayTime;
    SeekBar setTime;
    int time;
    Handler timerHandler;
    Runnable timerRun;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startTimer = (Button) findViewById(R.id.startTimer);
        displayTime = (TextView) findViewById(R.id.showTime);
        setTime = (SeekBar) findViewById(R.id.timeSelecter);
        stopButton = (Button) findViewById(R.id.stopButton);

        createTheSong();
        setTime.setMax(3600);
        displayTime.setText("00:00");

        timerHandler = new Handler();
        timerRun = new Runnable() {
            @Override
            public void run() {
                if (time > 0) {
                    startTimer.setEnabled(false);
                    time--;
                    setTime.setProgress(time);
                    updateDisplayTime(time);
                    timerHandler.postDelayed(this, 1000);
                }
                else if(time == 0){
                    startTimer.setEnabled(true);
                    disableSetTime(false);
                    animateStopButtonIn(500);
                    setTime.setProgress(0);
                }
            }
        };

        animateStopButtonOut(1);

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateStopButtonOut(500);
            }
        });
        setTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                updateDisplayTime(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                animateStopButtonOut(500);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        startTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateStopButtonOut(500);
                time = setTime.getProgress();
                if(time>0) {
                    disableSetTime(true);
                    timerRun.run();
                }
            }
        });


    }
    private String convertIntoMinutes(int seconds){
        String minS = Integer.toString(seconds/60);
        if(minS.length() == 1)
            minS = "0" + minS;
        return minS;
    }
    private String convertIntoRemainingSeconds(int seconds){
        String secS = Integer.toString(seconds%60);
        if(secS.length() == 1)
            secS = "0" + secS;
        return secS;
    }
    private void updateDisplayTime(int seconds){
        String timeS = convertIntoMinutes(seconds) + ":" + convertIntoRemainingSeconds(seconds);
        displayTime.setText(timeS);
    }
    private void animateStopButtonOut(long duration){
        if(media.isPlaying()) {
            media.stop();
            createTheSong();
        }
        stopButton.animate().scaleX(0).scaleY(0).alpha(0.5f).setDuration(duration);
    }
    private void animateStopButtonIn(long duration){
        media.start();
        stopButton.setScaleX(0);
        stopButton.setScaleY(0);
        stopButton.setAlpha(0);
        stopButton.animate().scaleY(1).scaleX(1).alpha(1).setDuration(duration);
    }
    @SuppressLint("ResourceAsColor")
    private void disableSetTime(final boolean shouldNotWork){
        setTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return shouldNotWork;
            }
        });
    }
    private void createTheSong(){
        media = MediaPlayer.create(this,R.raw.take_it_off);
        media.setLooping(true);
    }
}