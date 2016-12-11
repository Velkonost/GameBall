package ru.myitschool.appgameball;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static ru.myitschool.appgameball.Ball.countTouch;
import static ru.myitschool.appgameball.ChooseDifficultyActivity.DIFFICULTY;
import static ru.myitschool.appgameball.ChooseDifficultyActivity.ON_TIME;

public class MainActivity extends AppCompatActivity {

    private List<Ball> balls = null;
    private int countBall = 5;

    private boolean on_time;
    private int difficulty;

    private int timeToEnd = 20000;
    private long timeLess;
    private int score = 0;

    private int prevCountTouch;

    private TextView textTimer, textScore;
    private int screenH, screenW;
    private MyTimer timer = null;
    private SoundPool soundPool = null;
    private int streamSoundTuck = 0;
    private int streamSoundWin = 0;
    private int streamSoundLose = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        on_time = intent.getBooleanExtra(ON_TIME, true);
        difficulty = intent.getIntExtra(DIFFICULTY, 50);

        if (!on_time)
            timeToEnd = 1000000000;

        textTimer = (TextView) findViewById(R.id.text_timer);
        textScore = (TextView) findViewById(R.id.text_score);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenH = displayMetrics.heightPixels;
        screenW = displayMetrics.widthPixels;

        prevCountTouch = countTouch;
        createSoundPool();

        balls = new ArrayList<>();
        createBalls();

        textScore.setText("Очки: " + 0);

        timer = new MyTimer(timeToEnd, 10);
        timer.start();

        new CountDownTimer(timeToEnd, 100) {

            //Здесь обновляем текст счетчика обратного отсчета с каждой секундой
            public void onTick(long millisUntilFinished) {
                timeLess = millisUntilFinished / 1000;
                textTimer.setText("Осталось: " + timeLess);
            }

            //Задаем действия после завершения отсчета (высвечиваем надпись "Бабах!"):
            public void onFinish() {
                textTimer.setText("Не успел!");
            }
        }
                .start();
    }

    //создание звука
    private void createSoundPool () {
        soundPool = new SoundPool(1, AudioAttributes.CONTENT_TYPE_MUSIC, 1);
        streamSoundTuck = soundPool.load(this, R.raw.tuck, 1);
        streamSoundWin = soundPool.load(this, R.raw.win, 2);
        streamSoundLose = soundPool.load(this, R.raw.lose, 3);
    }

    private void createBalls(){
        for (int i = 0; i < countBall; i++){
            balls.add(new Ball(MainActivity.this, screenH, screenW, (100 - difficulty) * 2 + 30,
                    5, 10));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x, y;
        x = event.getX();
        y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                boolean flag = Ball.touch(balls, x, y, soundPool, streamSoundTuck);
                if (flag) {
                    countBall --;
                    if (countBall == 0) {
                        soundPool.play(streamSoundWin, 1, 1, 1, 0, 1);
                        finish();
                    }
                }
                break;
        }
        return true;
    }

    public class MyTimer extends CountDownTimer{

        public MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            moveBalls();
            setScore();
        }

        @Override
        public void onFinish() {
            if (countBall == 0) {
                soundPool.play(streamSoundWin, 1, 1, 1, 0, 1);
            } else {
                soundPool.play(streamSoundLose, 1, 1, 1, 0, 1);
            }
            finish();
        }
    }

    public void setScore() {
        if (countTouch > prevCountTouch) {
            score += timeLess;
        }
        prevCountTouch = countTouch;
        textScore.setText("Очки: " + score);
    }

    private void moveBalls() {
       for (Ball ball : balls){
           ball.move();
       }
    }

}
