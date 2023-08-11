package ru.mtucifiit.mtucifiit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import ru.mtucifiit.mtucifiit.view.abit.activitys.InputDocumentActivity;
import ru.mtucifiit.mtucifiit.config.Config;
import ru.mtucifiit.mtucifiit.view.home.activity.HomeActivity;
import ru.mtucifiit.mtucifiit.model.user.UserType;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

    }


    public void init() {
        sharedPreferences = getSharedPreferences(Config.name_app_shared_preferences, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        boolean firstEnter = sharedPreferences.getBoolean(Config.app_first_enter_shared_preferences, true);

        Intent intent = null;
        if (firstEnter) {
            intent = new Intent(MainActivity.this, CheckTypeUserActivity.class);
            editor.putBoolean(Config.app_first_enter_shared_preferences, false);
            editor.commit();
        } else {
            String type = sharedPreferences.getString(Config.type_user_shared_preferences, null);
            if (type == null) {
                intent = new Intent(MainActivity.this,CheckTypeUserActivity.class);
            } else {
                UserType userType = UserType.valueOf(type);
                if (userType == UserType.ABIT) {
                    intent = new Intent(MainActivity.this, InputDocumentActivity.class);
                } else if (userType == UserType.STUD) {
                    intent = new Intent(MainActivity.this, HomeActivity.class);

                }
            }
        }


        Intent finalIntent = intent;
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2500);
                    startActivity(finalIntent);
                    finish();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }


            }
        });
    }
}