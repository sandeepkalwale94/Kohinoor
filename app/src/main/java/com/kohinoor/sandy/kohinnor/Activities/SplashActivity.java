package com.kohinoor.sandy.kohinnor.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.kohinoor.sandy.kohinnor.R;

public class SplashActivity extends Activity {

    private TextView textView;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        textView = (TextView) findViewById(R.id.textview);
        imageView = (ImageView) findViewById(R.id.image);
        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.animationfromup);
        Animation textViewAnim = AnimationUtils.loadAnimation(this,R.anim.animationfrombottom);
        textView.startAnimation(textViewAnim);
        imageView.startAnimation(myanim);
        final Intent intent = new Intent(this,HomeActivity.class);
        Thread timer = new Thread() {
            public void run(){
                try{
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(intent);
                    finish();
                }
            }
        };
        timer.start();
    }
}
