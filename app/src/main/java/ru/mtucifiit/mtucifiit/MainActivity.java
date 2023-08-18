package ru.mtucifiit.mtucifiit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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

    private final Long time_load = 4500L;
    private SharedPreferences sharedPreferences;
    private RequestService requestService;

    private TextView quotes;
    private Long timeStart = 0L;

    private SharedPreferences.Editor editor;
    private NetConfig netConfig = new NetConfig();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestService = new RequestService(this);

        init();

    }


    public void init() {
        sharedPreferences = getSharedPreferences(Config.name_app_shared_preferences, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        boolean firstEnter = sharedPreferences.getBoolean(Config.app_first_enter_shared_preferences, true);


        quotes = findViewById(R.id.sentence);
        String quotes_string = write_quotes();
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
                    if (System.currentTimeMillis() - last_time_update < 1000/* * 60 * 60 * 12*/ && quotes_string != null) {
                        Thread.sleep(time_load);
                        startActrivity(finalIntent);
                    } else {
                        timeStart = System.currentTimeMillis();

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

        this.quotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                write_quotes();
            }
        });
    }

    @Nullable
    private String write_quotes() {
        String quotes_string = sharedPreferences.getString(Config.groups_datas_shared_preferences, null);
        if (quotes_string != null) {
            try {
                JSONObject jsonObject = new JSONObject(quotes_string);
                JSONArray jsonArray = jsonObject.getJSONArray("quotes");
                int index = sharedPreferences.getInt(Config.index_sentence_quotes, 0);
                if (index >= jsonArray.length()) index = 0;
                JSONObject jsonObject1 = jsonArray.getJSONObject(index);
                String quotes = jsonObject1.getString("quote");
                String author = jsonObject1.getString("author");

                this.quotes.setText(quotes + " - " + author);

                editor.putInt(Config.index_sentence_quotes, index + 1);
                editor.commit();

            } catch (JSONException e) {
                e.printStackTrace();
//                throw new RuntimeException(e);
            }

        }
        return quotes_string;

    }

    private void startActrivity(Intent finalIntent) {
        startActivity(finalIntent);
        finish();
    }

    private void loadGroups(NetConfig netConfig, SharedPreferences.Editor editor, Intent finalIntent) {
        requestService.getRequest(netConfig.getMain, listener -> {
            try {
                Log.e("E", listener);
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
            Long dt = System.currentTimeMillis() - timeStart;

            if (dt >= time_load) {
                startActrivity(finalIntent);
            } else {
                try {
                    Thread.sleep(time_load - dt);
                } catch (InterruptedException e) {
                    //throw new RuntimeException(e);
                }
                startActrivity(finalIntent);
            }


        }, error -> {
            Long dt = System.currentTimeMillis() - timeStart;

            if (dt >= time_load) {
                startActrivity(finalIntent);
            } else {
                try {
                    Thread.sleep(time_load - dt);
                } catch (InterruptedException e) {
                    //throw new RuntimeException(e);
                }
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}