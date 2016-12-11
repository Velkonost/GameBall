package ru.myitschool.appgameball;

import android.media.SoundPool;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.List;

public class Ball {

    private int x, y, speedX, speedY;
    private int screenH, screenW;
    private ImageView image;

    private static int size;
    public static int countTouch = 0;

    public Ball(MainActivity mainActivity, int screenH, int screenW,
                int size, int beginSpeed, int endSpeed){

        image = new ImageView(mainActivity);

        this.screenH = screenH;
        this.screenW = screenW;
        this.size = size;

        this.x = (int) (Math.random() * screenW / 2);
        this.y = (int) (Math.random() * screenH / 2);

        this.speedX = (int) (beginSpeed + Math.random() * (endSpeed - beginSpeed) + 1);
        this.speedY = (int) (beginSpeed + Math.random() * (endSpeed - beginSpeed) + 1);

        mainActivity.addContentView(image,
                new RelativeLayout.LayoutParams(size, size));
        image.setImageResource(R.drawable.ball);
    }

    //движение мяча
    public void move(){
        x += speedX;
        y += speedY;
        image.setX(x);
        image.setY(y);

        if (x < 0 || x > screenW - size)
            speedX = - speedX;
        if (y < 0 || y > screenH - size)
            speedY = - speedY;
    }

    public static boolean touch(List<Ball> balls, float x, float y,
                             SoundPool soundPool, int streamSound){
        for (int i = 0; i < balls.size(); i++){
            if ((balls.get(i).x <= x) && (balls.get(i).x + size >= x) &&
                    (balls.get(i).y <= y) && (balls.get(i).y + size >= y)){
                balls.get(i).image.setImageAlpha(0);
                soundPool.play(streamSound, 1, 1, 1, 0, 1);
                countTouch++;

                balls.remove(i);
                return true;
            } else
                return false;
        }
        return false;
    }
}
