package com.example.arravilar.mysecapp;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;

public class MainActivity extends AppCompatActivity {

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this.mContext;
        AudioManager am = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
        am.

        //MediaPlayer mplayer = new MediaPlayer();


    }

    public void testButton(View v){
        i
    }
}
