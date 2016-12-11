package ru.myitschool.appgameball;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import static ru.myitschool.appgameball.Manager.changeActivityCompat;

public class MenuActivity extends Activity {

    private ListView listMenu = null;
    private String[] contentMenu = null;
    private ArrayAdapter<String> arrayAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        contentMenu = new String[] {"Новая игра",
                "Продолжить игру",
                "Выход"};


        listMenu = (ListView) findViewById(R.id.list_menu);

        arrayAdapter = new ArrayAdapter<String>(this,
                                                R.layout.menu_item,
                                                contentMenu);
        listMenu.setAdapter(arrayAdapter);

        listMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int i, long l) {
                switch (i){
                    case 0:
                        changeActivityCompat(MenuActivity.this,
                                new Intent(MenuActivity.this, ChooseDifficultyActivity.class));
                        break;
                    case 1:
                        //
                        break;
                    case 2:
                        finish();
                        break;
                }

            }
        });

    }
}
