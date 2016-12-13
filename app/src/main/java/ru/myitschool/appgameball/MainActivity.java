package ru.myitschool.appgameball;

import android.annotation.TargetApi;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static ru.myitschool.appgameball.Ball.countTouch;
import static ru.myitschool.appgameball.Constants.AMOUNT;
import static ru.myitschool.appgameball.Constants.DIFFICULTY;
import static ru.myitschool.appgameball.Constants.ON_TIME;
import static ru.myitschool.appgameball.Constants.SCORE;
import static ru.myitschool.appgameball.Constants.TIME;
import static ru.myitschool.appgameball.PhoneDataStorage.loadText;
import static ru.myitschool.appgameball.PhoneDataStorage.saveText;

public class MainActivity extends AppCompatActivity {

    private List<Ball> balls = null;
    private int countBall = 5;

    private int difficulty;

    private int timeToEnd;
    private long timeLess;
    private int score;
    private int amount;

    private int prevCountTouch;

    private TextView textTimer, textScore;
    private int screenH, screenW;
    private MyTimer timer = null;
    private SoundPool soundPool = null;
    private int streamSoundTuck = 0;
    private int streamSoundWin = 0;
    private int streamSoundLose = 0;
    private boolean on_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        on_time = intent.getBooleanExtra(ON_TIME, true);
        Log.i("TIME", String.valueOf(on_time));
        difficulty = intent.getIntExtra(DIFFICULTY, 50);

        score = intent.getIntExtra(SCORE, 0);
        timeToEnd = intent.getIntExtra(TIME, 20000);
        amount = intent.getIntExtra(AMOUNT, 0);

        textTimer = (TextView) findViewById(R.id.text_timer);
        textScore = (TextView) findViewById(R.id.text_score);


        if (!on_time) {
            timeToEnd = 1000000000;
            textTimer.setVisibility(View.INVISIBLE);
        }


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenH = displayMetrics.heightPixels;
        screenW = displayMetrics.widthPixels;

        prevCountTouch = countTouch;
        createSoundPool();
        balls = new ArrayList<>();
        if (amount == 0) {
            createBalls();
        } else {
            for (int i = 0; i < amount; i++) {
                Ball ball = new Ball(MainActivity.this, screenH, screenW, (100 - difficulty) * 2 + 30,
                        5, 10);
                ball.setX(Integer.parseInt(loadText(this, i + "x")));
                ball.setY(Integer.parseInt(loadText(this, i + "y")));

                balls.add(ball);
            }
        }

        textScore.setText("Очки: " + 0);

        timer = new MyTimer(timeToEnd, 10);
        timer.start();
        if (on_time) {
            new CountDownTimer(timeToEnd, 100) {

                //Здесь обновляем текст счетчика обратного отсчета с каждой секундой
                public void onTick(long millisUntilFinished) {
                    timeLess = millisUntilFinished / 1000;
                    textTimer.setText("Осталось: " + timeLess);
                }

                public void onFinish() {
                    textTimer.setText("Не успел!");
                }
            }
                    .start();
        }
    }

    //создание звука
    private void createSoundPool () {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createNewSoundPool();
        }else{
            createOldSoundPool();
        }
        streamSoundTuck = soundPool.load(this, R.raw.tuck, 1);
        streamSoundWin = soundPool.load(this, R.raw.win, 2);
        streamSoundLose = soundPool.load(this, R.raw.lose, 3);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void createNewSoundPool() {

        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
    }

    @SuppressWarnings("deprecation")
    protected void createOldSoundPool(){
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
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

    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();

        saveText(this, ON_TIME, String.valueOf(on_time));
        saveText(this, DIFFICULTY, String.valueOf(difficulty));
        saveText(this, SCORE, String.valueOf(score));
        saveText(this, TIME, String.valueOf(timeLess));
        saveText(this, AMOUNT, String.valueOf(balls.size()));

        for (int i = 0; i < balls.size(); i++) {
            saveText(this, i + "x", String.valueOf(balls.get(i).getX()));
            saveText(this, i + "y", String.valueOf(balls.get(i).getY()));
        }
    }

    public class MyTimer extends CountDownTimer{

        MyTimer(long millisInFuture, long countDownInterval) {
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
