package ru.mtucifiit.mtucifiit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import ru.mtucifiit.mtucifiit.view.abit.activitys.InputDocumentActivity;
import ru.mtucifiit.mtucifiit.config.Config;
import ru.mtucifiit.mtucifiit.view.home.activity.HomeActivity;
import ru.mtucifiit.mtucifiit.view.sch.activitys.SchoolMainActivity;

public class CheckTypeUserActivity extends AppCompatActivity {

    private AppCompatButton stud, abit,sch;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_type_user);

        init();
    }

    public void init() {
        sharedPreferences = getSharedPreferences(Config.name_app_shared_preferences,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        abit = findViewById(R.id.abit);
        stud = findViewById(R.id.stud);
        sch = findViewById(R.id.sch);

        //abit.setClickable(false);
        abit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString(Config.type_user_shared_preferences,"ABIT");
                editor.commit();

                //runAbiturInputDocumentActivity();
            }
        });


        sch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CheckTypeUserActivity.this, SchoolMainActivity.class));
                finish();
            }
        });



        stud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editor.putString(Config.type_user_shared_preferences,"STUD");
                editor.commit();

                runHomeActivity();
            }
        });

    }

    private void runHomeActivity() {
        startActivity(new Intent(CheckTypeUserActivity.this, HomeActivity.class));
        finish();
    }


    public void runAbiturInputDocumentActivity(){

        startActivity(new Intent(CheckTypeUserActivity.this, InputDocumentActivity.class));
        finish();
    }
}