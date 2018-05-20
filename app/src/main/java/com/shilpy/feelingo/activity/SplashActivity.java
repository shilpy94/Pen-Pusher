package com.shilpy.feelingo.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.shilpy.feelingo.R;

/**
  this is launcher activity
 */
public class SplashActivity extends AppCompatActivity {

    private RelativeLayout splash_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splash_layout= (RelativeLayout) findViewById(R.id.splash_layout);
        gotoNextScreen();
    }

    /**
     * this method applies fade animation on Splash screen
     * on end of animation Home activity will be opened
     */
    private void gotoNextScreen(){
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(splash_layout, "alpha",  1f, 0.3f);
        fadeOut.setDuration(1000);
        fadeOut.setStartDelay(1000);
        fadeOut.start();
        fadeOut.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {

                Intent intent=new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
    }

}
