package ru.myitschool.appgameball;

import android.app.Activity;
import android.content.Intent;

class Manager {

    static void changeActivityCompat(final Activity a, Intent nextIntent) {
        final Intent currentIntent = a.getIntent();
        nextIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);


        if (currentIntent.filterEquals(nextIntent)) {
            a.finish();
        }
        a.overridePendingTransition(0, 0);

        a.startActivity(nextIntent);
        a.overridePendingTransition(0, 0);
    }
}
