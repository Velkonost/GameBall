package ru.myitschool.appgameball;

import android.annotation.SuppressLint;
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
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static ru.myitschool.appgameball.Ball.countTouch;
import static ru.myitschool.appgameball.Constants.AMOUNT;
import static ru.myitschool.appgameball.Constants.DIFFICULTY;
import static ru.myitschool.appgameball.Constants.LOAD;
import static ru.myitschool.appgameball.Constants.ON_TIME;
import static ru.myitschool.appgameball.Constants.SCORE;
import static ru.myitschool.appgameball.Constants.TIME;
import static ru.myitschool.appgameball.Constants.X;
import static ru.myitschool.appgameball.Constants.Y;
import static ru.myitschool.appgameball.Constants.YES;
import static ru.myitschool.appgameball.Constants.ZERO;
import static ru.myitschool.appgameball.PhoneDataStorage.deleteText;
import static ru.myitschool.appgameball.PhoneDataStorage.loadText;
import static ru.myitschool.appgameball.PhoneDataStorage.saveText;

public class MainActivity extends AppCompatActivity {

    private static List<Ball> balls = null;
    private static int countBall;

    private int difficulty;

    private long timeLess;
    private int score;

    private int prevCountTouch;
    private static boolean isAbleToCont;

    private TextView textTimer, textScore;
    private int screenH, screenW;
    private MyTimer timer = null;
    private static SoundPool soundPool = null;
    private static int streamSoundTuck = 0;
    private int streamSoundWin = 0;
    private int streamSoundLose = 0;
    private boolean on_time;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        on_time = intent.getBooleanExtra(ON_TIME, true);
        difficulty = intent.getIntExtra(DIFFICULTY, 50);

        long timeToEnd;
        int amount;
        boolean load;

        if (loadText(this, LOAD).equals(YES) && MainActivity.isAbleToCont()) {
            timeToEnd = Long.parseLong(loadText(this, TIME)) * 1000 + 1000;
            amount = Integer.parseInt(loadText(this, AMOUNT));
            score = Integer.parseInt(loadText(MainActivity.this, SCORE));
            difficulty = Integer.parseInt(loadText(this, DIFFICULTY));
            load = true;

            deleteText(this, LOAD);
            deleteText(this, SCORE);
            deleteText(this, DIFFICULTY);
            deleteText(this, ON_TIME);
            deleteText(this, AMOUNT);
            deleteText(this, TIME);
        } else {
            timeToEnd = 20000;
            amount = 5;
            score = 0;
            load = false;
        }
        setCountBall(amount);

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
        if (!load) {
            createBalls();
        } else {
            for (int i = 0; i < amount; i++) {
                Ball ball = new Ball(MainActivity.this, screenH, screenW, (100 - difficulty) * 2 + 30,
                        5, 10);
                ball.setX(Integer.parseInt(loadText(this, i + X)));
                ball.setY(Integer.parseInt(loadText(this, i + Y)));

                balls.add(ball);
            }
        }

        textScore.setText(getResources().getString(R.string.score) + ZERO);

        timer = new MyTimer(timeToEnd, 10);
        timer.start();
        if (on_time) {
            new CountDownTimer(timeToEnd, 100) {

                //Здесь обновляем текст счетчика обратного отсчета с каждой секундой
                public void onTick(long millisUntilFinished) {
                    timeLess = millisUntilFinished / 1000;
                    textTimer.setText(getResources().getString(R.string.time_left) + timeLess);
                }

                public void onFinish() {
                    textTimer.setText(getResources().getString(R.string.lose));
                }
            }
                    .start();
        }
    }

    //создание звука
    private void createSoundPool () {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createNewSoundPool();
        } else {
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

    public void winGame() {
        if (getCountBall() == 0) {
            soundPool.play(streamSoundWin, 1, 1, 1, 0, 1);
            setIsAbleToCont(false);
            finish();
        }
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

        setIsAbleToCont(true);

        finish();
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
                setIsAbleToCont(false);
            } else {
                soundPool.play(streamSoundLose, 1, 1, 1, 0, 1);
                setIsAbleToCont(false);
            }
            finish();
        }
    }

    @SuppressLint("SetTextI18n")
    public void setScore() {
        if (countTouch > prevCountTouch) {
            if (on_time) score += timeLess;
            else score += 1.5 * countTouch;
        }
        prevCountTouch = countTouch;
        textScore.setText(getResources().getString(R.string.score) + score);
    }

    private void moveBalls() {
       for (Ball ball : balls){
           ball.move();
       }
    }

    public static List<Ball> getBalls() {
        return balls;
    }

    public static void setBalls(List<Ball> balls) {
        MainActivity.balls = balls;
    }

    public static SoundPool getSoundPool() {
        return soundPool;
    }

    public static int getStreamSoundTuck() {
        return streamSoundTuck;
    }

    public static int getCountBall() {
        return countBall;
    }

    public static void setCountBall(int countBall) {
        MainActivity.countBall = countBall;
    }

    public static boolean isAbleToCont() {
        return isAbleToCont;
    }

    public static void setIsAbleToCont(boolean isAbleToCont) {
        MainActivity.isAbleToCont = isAbleToCont;
    }
}
