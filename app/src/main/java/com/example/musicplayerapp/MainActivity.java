package com.example.musicplayerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    TextView txt_time,music_title;
    ImageView play,pause,forward,backward;
    SeekBar seekBar;

    // Media player
    MediaPlayer mediaPlayer;

    //Handler
    Handler handler = new Handler();

    //Variables
    double startTime = 0;
    double finalTime = 0;
    int forwardTime = 10000;
    int backwardTime = 10000;
    static int oneTimeOnly = 0;

    @SuppressLint({ "DiscouragedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_time = findViewById(R.id.textView2);
        music_title = findViewById(R.id.textView3);
        play = findViewById(R.id.imageView2);
        pause = findViewById(R.id.imageView4);
        forward = findViewById(R.id.imageView5);
        backward = findViewById(R.id.imageView3);
        seekBar = findViewById(R.id.seekBar);


        mediaPlayer = MediaPlayer.create(this,R.raw.company);
        seekBar.setClickable(false);

        //Adding functionalities for buttons
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMusic();
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) startTime;
                if (temp+forwardTime <= finalTime){
                    startTime = startTime+forwardTime;
                    mediaPlayer.seekTo((int) startTime);
                }
                else {
                    Toast.makeText(MainActivity.this,"Can't Jump Forward",Toast.LENGTH_SHORT).show();
                }
            }
        });

        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) startTime;
                if (temp-backwardTime > 0){
                    startTime = startTime - backwardTime;
                    mediaPlayer.seekTo((int) startTime);
                }
                else {
                    Toast.makeText(MainActivity.this,"Can't Jump Backward",Toast.LENGTH_SHORT).show();
                }
            }
        });


        music_title.setText(getResources().getIdentifier(
                "company","raw",getPackageName()
        ));


    }



    @SuppressLint("DefaultLocale")
    private void playMusic() {
        mediaPlayer.start();

        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();

        if (oneTimeOnly == 0){
            seekBar.setMax((int)finalTime);
            oneTimeOnly = 1;
        }

        txt_time.setText(String.format(
                "%d min, %d sec ", TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime))
        ));

        seekBar.setProgress((int) startTime);
        handler.postDelayed(UpdateSongTime,100);

    }

    private Runnable UpdateSongTime = new Runnable() {
        @SuppressLint("DefaultLocale")
        @Override
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            txt_time.setText(String.format(
                    "%d min, %d sec ", TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))
            ));

            seekBar.setProgress((int) startTime);
            handler.postDelayed(this,100);
        }
    };


}