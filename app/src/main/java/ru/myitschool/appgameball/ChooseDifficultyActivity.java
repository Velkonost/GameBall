package ru.myitschool.appgameball;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import static ru.myitschool.appgameball.Constants.DIFFICULTY;
import static ru.myitschool.appgameball.Constants.ON_TIME;

public class ChooseDifficultyActivity extends AppCompatActivity {

    private final int LAYOUT = R.layout.activity_choose_difficulty;

    private Button mButton;
    private SwitchCompat mSwitchCompat;
    private SeekBar mSeekBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(LAYOUT);

        mButton = (Button) findViewById(R.id.startGame);
        mSwitchCompat = (SwitchCompat) findViewById(R.id.switch_compat);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseDifficultyActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(ON_TIME, mSwitchCompat.getShowText());
                intent.putExtra(DIFFICULTY, mSeekBar.getProgress());
                ChooseDifficultyActivity.this.startActivity(intent);
            }
        });
    }

}
