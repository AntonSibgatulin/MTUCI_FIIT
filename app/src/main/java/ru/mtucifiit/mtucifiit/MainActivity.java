package ru.mtucifiit.mtucifiit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ru.mtucifiit.mtucifiit.config.NetConfig;
import ru.mtucifiit.mtucifiit.service.RequestService;
import ru.mtucifiit.mtucifiit.view.abit.activitys.InputDocumentActivity;
import ru.mtucifiit.mtucifiit.config.Config;
import ru.mtucifiit.mtucifiit.view.home.activity.HomeActivity;
import ru.mtucifiit.mtucifiit.model.user.UserType;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private RequestService requestService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestService = new RequestService(this);

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
                intent = new Intent(MainActivity.this, CheckTypeUserActivity.class);
            } else {
                UserType userType = UserType.valueOf(type);
                if (userType == UserType.ABIT) {
                    intent = new Intent(MainActivity.this, InputDocumentActivity.class);
                } else if (userType == UserType.STUD) {
                    intent = new Intent(MainActivity.this, HomeActivity.class);

                }
            }
        }


        String groups_json = sharedPreferences.getString(Config.groups_datas_shared_preferences, null);
        Long last_time_update = sharedPreferences.getLong(Config.groups_datas_last_time_update_shared_preferences, 0L);

        String quotes = sharedPreferences.getString(Config.sentence_quotes, null);

        Intent finalIntent = intent;
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    if (System.currentTimeMillis() - last_time_update < 1000 * 60 * 60 * 12) {
                        Thread.sleep(2500);
                        startActrivity(finalIntent);
                    } else {
                        NetConfig netConfig = new NetConfig();
                       /* if(quotes == null){

                        }else {
                            loadGroups(netConfig, editor);
                        }

                        */

                        loadGroups(netConfig, editor, finalIntent);

                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }


            }
        });
    }

    private void startActrivity(Intent finalIntent) {
        startActivity(finalIntent);
        finish();
    }

    private void loadGroups(NetConfig netConfig, SharedPreferences.Editor editor, Intent finalIntent) {
        requestService.getRequest(netConfig.getGroups, listener -> {
            try {
                JSONObject jsonObject = new JSONObject(listener);
                JSONArray jsonArray = jsonObject.getJSONArray("dir");
                String group = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    String group_ = jsonArray.getString(i);
                    if (group_.contains("BFI")) {
                        group = group_;
                        break;
                    }
                }
                editor.putString(Config.my_group, group);

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            editor.putString(Config.groups_datas_shared_preferences, listener);
            editor.putLong(Config.groups_datas_last_time_update_shared_preferences, System.currentTimeMillis());
            editor.commit();

            startActrivity(finalIntent);
        }, error -> {
            startActivity(new Intent(MainActivity.this, MainActivity.class));
            finish();
        });
    }
}