package ru.mtucifiit.mtucifiit.view.home.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ru.mtucifiit.mtucifiit.R;
import ru.mtucifiit.mtucifiit.adapters.ProjectsAdapter;
import ru.mtucifiit.mtucifiit.model.project.HistoryModel;
import ru.mtucifiit.mtucifiit.model.project.HistoryType;
import ru.mtucifiit.mtucifiit.service.RequestService;

public class HistoryViewActivity extends AppCompatActivity {

    public View left_panel;
    public AppCompatButton header;

    public TextView name, description, author, count_like_text, count_dislike_text;
    private ImageView like, dislike;

    private HistoryModel historyModel;

    private RequestService requestService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_view);
        init();
    }

    private void init() {
        requestService = new RequestService(this);
        ObjectMapper objectMapper = new ObjectMapper();
        String data_ = getIntent().getStringExtra("history");
        try {
            historyModel = objectMapper.readValue(data_, HistoryModel.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        left_panel = findViewById(R.id.left_panel);
        header = findViewById(R.id.header2);
        name = findViewById(R.id.name);
        description = findViewById(R.id.desc);
        author = findViewById(R.id.author);
        count_dislike_text = findViewById(R.id.count_dislike_text);
        count_like_text = findViewById(R.id.count_like_text);
        like = findViewById(R.id.like_image);
        dislike = findViewById(R.id.dislike_image);


        HistoryType projectType = (historyModel.historyType);
        int textColor = getColor(getColorText(projectType));

        left_panel.setBackgroundTintList(getColorStateList(getColorText(projectType)));
        header.setTextColor(textColor);
        header.setBackgroundTintList(getColorStateList(getBgColorText(projectType)));

        author.setText("@" + historyModel.user.username);

        name.setText(historyModel.name);
        description.setText(resizeText(historyModel.description, 0, 35) + "...");


        loadLikeByObject(historyModel);


        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int type = 1;
                if (historyModel.ilike) {
                    type = 0;
                }
                requestService.like(historyModel, type, listener -> {
                    updateHistoryData(listener, historyModel);
                }, error -> {
                    error.printStackTrace();
                });
            }
        });


        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int type = -1;
                if (historyModel.idisLike) {
                    type = 0;
                }
                requestService.like(historyModel, type, listener -> {
                    updateHistoryData(listener, historyModel);

                }, error -> {
                    error.printStackTrace();
                });
            }
        });

    }




    private void loadLikeByObject(HistoryModel projectModel) {
        if (projectModel.ilike) {
            like.setImageDrawable(getDrawable(R.drawable.like_solid));
        } else {
            like.setImageDrawable(getDrawable(R.drawable.like));

        }

        if (projectModel.idisLike) {
            dislike.setImageDrawable(getDrawable(R.drawable.dislike_solid));
        } else {
            dislike.setImageDrawable(getDrawable(R.drawable.dislike));
        }
        count_like_text.setText(String.valueOf(projectModel.count_like));
        count_dislike_text.setText(String.valueOf(projectModel.count_dislike));


    }

    public String resizeText(String text, int start, int end) {
        if (end >= text.length()) {
            return text;
        } else {
            return text.substring(start, end);
        }
    }

    public int getColorText(HistoryType projectType) {
        if (projectType == HistoryType.TICK) {
            return (R.color.tick);
        } else if (projectType == HistoryType.HISTORY) {
            return (R.color.history);
        } else if (projectType == HistoryType.SLOW_HISTORY) {
            return (R.color.slow_history);
        } else if (projectType == HistoryType.IMPORTANT) {
            return (R.color.important);
        } else {
            return (R.color.important);
        }

    }

    public int getBgColorText(HistoryType projectType) {
        if (projectType == HistoryType.TICK) {
            return (R.color.bg_tick);
        } else if (projectType == HistoryType.HISTORY) {
            return (R.color.bg_history);
        } else if (projectType == HistoryType.SLOW_HISTORY) {
            return (R.color.bg_slow_history);
        } else if (projectType == HistoryType.IMPORTANT) {
            return (R.color.bg_important);
        } else {
            return (R.color.bg_important);
        }

    }

    private void updateHistoryData(String listener, HistoryModel projectModel) {
        Log.e("DATA", listener);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            HistoryModel historyModel = objectMapper.readValue(listener, HistoryModel.class);
            projectModel.ilike = historyModel.ilike;
            projectModel.idisLike = historyModel.idisLike;

            projectModel.count_dislike = historyModel.count_dislike;
            projectModel.count_like = historyModel.count_like;

            loadLikeByObject(projectModel);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


}
