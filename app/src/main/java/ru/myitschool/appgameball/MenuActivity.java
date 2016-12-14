package ru.myitschool.appgameball;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import static ru.myitschool.appgameball.Constants.DIFFICULTY;
import static ru.myitschool.appgameball.Constants.LOAD;
import static ru.myitschool.appgameball.Constants.ON_TIME;
import static ru.myitschool.appgameball.Constants.YES;
import static ru.myitschool.appgameball.Manager.changeActivityCompat;
import static ru.myitschool.appgameball.PhoneDataStorage.loadText;
import static ru.myitschool.appgameball.PhoneDataStorage.saveText;

public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        String[] contentMenu = new String[]{"Новая игра",
                "Продолжить игру",
                "Выход"};


        ListView listMenu = (ListView) findViewById(R.id.list_menu);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                R.layout.menu_item,
                contentMenu);
        listMenu.setAdapter(arrayAdapter);

        listMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int i, long l) {
                switch (i) {
                    case 0:
                        changeActivityCompat(MenuActivity.this,
                                new Intent(MenuActivity.this, ChooseDifficultyActivity.class));
                        break;
                    case 1:
                        saveText(MenuActivity.this, LOAD, YES);
                        Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        intent.putExtra(ON_TIME, Boolean.parseBoolean(loadText(MenuActivity.this, ON_TIME)));
                        intent.putExtra(DIFFICULTY, (loadText(MenuActivity.this, DIFFICULTY)));
                        changeActivityCompat(MenuActivity.this,
                                new Intent(MenuActivity.this, MainActivity.class));

                        break;
                    case 2:
                        finish();
                        break;
                }
            }
        });
    }
}
