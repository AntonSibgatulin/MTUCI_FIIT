package ru.mtucifiit.mtucifiit.view.abit.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ru.mtucifiit.mtucifiit.CheckTypeUserActivity;
import ru.mtucifiit.mtucifiit.R;
import ru.mtucifiit.mtucifiit.config.Config;

public class InputDocumentActivity extends AppCompatActivity {

    private static final String UNVALID_DOCUMENT = "Document invalid!";

    private boolean clicked = false;
    private AppCompatButton enter, exit;
    private EditText document;
    private TextView error_document;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_document);

        init();
    }


    public void init() {
        sharedPreferences = getSharedPreferences(Config.name_app_shared_preferences, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        enter = findViewById(R.id.enter);
        document = findViewById(R.id.document);
        error_document = findViewById(R.id.error_text);
        exit = findViewById(R.id.exit);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit();
            }
        });

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!valid()) {
                    error_document.setText(UNVALID_DOCUMENT);
                    return;
                } else {
                    error_document.setText("");

                }
                //save our entered document in memory
                editor.putString(Config.document_data_shared_preferences, document.getText().toString());
                editor.commit();

                clicked = true;
                if (!clicked) {
                    Toast.makeText(InputDocumentActivity.this, "Прием закрыт!", Toast.LENGTH_SHORT).show();
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            exit();
                        }
                    });
                }

            }
        });

    }


    public void exit() {
        startActivity(new Intent(InputDocumentActivity.this, CheckTypeUserActivity.class));
        finish();
    }


    public boolean valid() {
        String doc = document.getText().toString();
        return !doc.isEmpty() && doc.length() >= 6;
    }
}