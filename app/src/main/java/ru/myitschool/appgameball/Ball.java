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

    static boolean touch(List<Ball> balls, float x, float y,
                         SoundPool soundPool, int streamSound){
        for (int i = 0; i < balls.size(); i++){
            if ((balls.get(i).x <= x) && (balls.get(i).x + size >= x) &&
                    (balls.get(i).y <= y) && (balls.get(i).y + size >= y)){
//                balls.get(i).image.setImageAlpha(0);
//                soundPool.play(streamSound, 1, 1, 1, 0, 1);
                countTouch++;

//                balls.remove(i);
                return true;
            } else
                return false;
        }
        return false;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }


}
