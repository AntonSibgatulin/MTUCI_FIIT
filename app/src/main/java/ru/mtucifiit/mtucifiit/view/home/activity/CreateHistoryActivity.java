package ru.mtucifiit.mtucifiit.view.home.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import ru.mtucifiit.mtucifiit.R;
import ru.mtucifiit.mtucifiit.config.NetConfig;
import ru.mtucifiit.mtucifiit.model.project.HistoryModel;
import ru.mtucifiit.mtucifiit.model.project.HistoryType;
import ru.mtucifiit.mtucifiit.service.RequestService;

public class CreateHistoryActivity extends AppCompatActivity {


    public HistoryType historyType = HistoryType.HISTORY;


    private EditText name, description;
    private RadioButton yellow, sea, red;


    private AppCompatButton button;


    private RequestService requestService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_history);
        init();
    }


    public void init() {

        NetConfig netConfig = new NetConfig();

        requestService = new RequestService(this);

        name = findViewById(R.id.name_history);
        description = findViewById(R.id.description);

        yellow = findViewById(R.id.tick);
        sea = findViewById(R.id.history);
        red = findViewById(R.id.important);

        sea.setChecked(true);

        button = findViewById(R.id.create_button);


        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                historyType = HistoryType.TICK;
            }
        });


        sea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                historyType = HistoryType.HISTORY;
            }
        });

        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                historyType = HistoryType.IMPORTANT;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = CreateHistoryActivity.this.name.getText().toString();
                String description = CreateHistoryActivity.this.description.getText().toString();
                String historyType = CreateHistoryActivity.this.historyType.toString();

                requestService.post(netConfig.url_create_history, listener -> {

                    try {


                        JSONObject jsonObject = new JSONObject(listener);
                        requestService.checkAuthByJSON(jsonObject);

                        ObjectMapper objectMapper = new ObjectMapper();
                        HistoryModel historyModel = objectMapper.readValue(listener, HistoryModel.class);

                        /*Intent intent = new Intent(CreateHistoryActivity.this, HomeActivity.class);
                        intent.putExtra("project_menu","update");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        startActivity(intent);
                        finish();

                         */
                        Intent intent = new Intent();
                        intent.putExtra("project_menu","update");
                        setResult(Activity.RESULT_OK,intent);

                        finish();

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    } catch (JsonMappingException e) {
                        throw new RuntimeException(e);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }


                }, error -> {
                    error.printStackTrace();
                }, "name=" + name, "description=" + description, "historyType=" + historyType);


            }
        });


    }


}