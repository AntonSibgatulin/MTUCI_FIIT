package ru.mtucifiit.mtucifiit.view.home.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ru.mtucifiit.mtucifiit.R;
import ru.mtucifiit.mtucifiit.adapters.SettingGroupAdapter;
import ru.mtucifiit.mtucifiit.config.Config;
import ru.mtucifiit.mtucifiit.model.group.Group;
import ru.mtucifiit.mtucifiit.service.RequestService;

public class GroupSelectActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private RequestService requestService;

    private AppCompatButton exit_from_this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_select);
        init();

    }

    public void init(){
        recyclerView = findViewById(R.id.recyclerViewGroups);
        progressBar = findViewById(R.id.progressBarGroup);
        exit_from_this = findViewById(R.id.exit_from_this);
        progressBar.animate().start();
        requestService = new RequestService(this);
        List<Group> groupList = new ArrayList<>();
        String groups = requestService.getStringSh(Config.groups_datas_shared_preferences,null);
        if(groups == null)finish();
        exit_from_this.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        try {
            JSONObject jsonObject = new JSONObject(groups);
            JSONArray jsonArray = jsonObject.getJSONArray("dir");
            for(int i = 0;i<jsonArray.length();i++){
                String id = jsonArray.getString(i);
                Group group = new Group(requestService.beautGroup(id),id);
                groupList.add(group);
            }
            SettingGroupAdapter settingGroupAdapter = new SettingGroupAdapter(groupList,this,this);
            recyclerView.setAdapter(settingGroupAdapter);
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }

    public void checkGroup(Group group) {
        requestService.saveGroup(group);
        Intent intent = new Intent(this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
        startActivity(intent);

        finish();
    }
}