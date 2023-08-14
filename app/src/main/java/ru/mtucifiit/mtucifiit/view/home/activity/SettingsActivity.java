package ru.mtucifiit.mtucifiit.view.home.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import ru.mtucifiit.mtucifiit.CheckTypeUserActivity;
import ru.mtucifiit.mtucifiit.R;
import ru.mtucifiit.mtucifiit.config.Config;
import ru.mtucifiit.mtucifiit.service.RequestService;

public class SettingsActivity extends AppCompatActivity {

    private AppCompatButton exit,change_group;
    private TextView group_name_setting;
    private RequestService requestService;

    private AppCompatButton change_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();
    }


    public void init() {
        requestService = new RequestService(this);

        exit = findViewById(R.id.exit);
        change_group = findViewById(R.id.change_group);

        change_profile = findViewById(R.id.change_profile);

        group_name_setting = findViewById(R.id.group_name_setting);
        String group = requestService.getStringSh(Config.my_group,"BFI2201");
        group_name_setting.setText(requestService.beautGroup(group));

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        change_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this,GroupSelectActivity.class));
            }
        });

        change_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, CheckTypeUserActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                startActivity(intent);

                finish();
            }
        });
    }
}