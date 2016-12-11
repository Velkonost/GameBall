package ru.myitschool.appgameball;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class LoaderActivity extends Activity {

    private Animation animFadeIn, animFadeOut;
    private TextView textLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);

        textLogo = (TextView) findViewById(R.id.text_logo);
        animFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        animFadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);

        animFadeIn.setAnimationListener(animationListenerFadeIn);
        animFadeOut.setAnimationListener(animationListenerFadeOut);
        textLogo.startAnimation(animFadeIn);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                Manager.changeActivityCompat(this, new Intent(this, MenuActivity.class));
                break;
        }
        return true;
    }

    Animation.AnimationListener animationListenerFadeIn = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            textLogo.startAnimation(animFadeOut);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    Animation.AnimationListener animationListenerFadeOut = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            textLogo.startAnimation(animFadeIn);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };
}
