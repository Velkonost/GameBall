package ru.myitschool.appgameball;

import android.media.SoundPool;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.List;

class Ball {

    private int x, y, speedX, speedY;
    private int screenH, screenW;
    private ImageView image;
    private boolean live;

    private static int size;
    static int countTouch = 0;

    Ball(final MainActivity mainActivity, int screenH, int screenW,
         final int size, int beginSpeed, int endSpeed){

        image = new ImageView(mainActivity);
        live = true;
        this.screenH = screenH;
        this.screenW = screenW;
        Ball.size = size;

        this.x = (int) (Math.random() * screenW / 2);
        this.y = (int) (Math.random() * screenH / 2);

        this.speedX = (int) (beginSpeed + Math.random() * (endSpeed - beginSpeed) + 1);
        this.speedY = (int) (beginSpeed + Math.random() * (endSpeed - beginSpeed) + 1);

        mainActivity.addContentView(image,
                new RelativeLayout.LayoutParams(size, size));
        image.setImageResource(R.drawable.ball);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (live) {
                    List<Ball> balls = MainActivity.getBalls();
                    SoundPool soundPool = MainActivity.getSoundPool();
                    int streamSound = MainActivity.getStreamSoundTuck();

                    balls.remove(Ball.this);
                    image.setImageAlpha(0);
                    soundPool.play(streamSound, 1, 1, 1, 0, 1);
                    countTouch++;
                    MainActivity.setBalls(balls);
                    MainActivity.setCountBall(MainActivity.getCountBall() - 1);
                    live = false;
                    mainActivity.winGame();
                }
            }
        });
    }

    //движение мяча
    void move(){
        x += speedX;
        y += speedY;
        image.setX(x);
        image.setY(y);

        if (x < 0 || x > screenW - size)
            speedX = - speedX;
        if (y < 0 || y > screenH - size)
            speedY = - speedY;
    }

    int getX() {
        return x;
    }

    void setX(int x) {
        this.x = x;
    }

    int getY() {
        return y;
    }

    void setY(int y) {
        this.y = y;
    }


}
