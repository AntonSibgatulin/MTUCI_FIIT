package ru.mtucifiit.mtucifiit.view.home.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ru.mtucifiit.mtucifiit.R;
import ru.mtucifiit.mtucifiit.adapters.ProjectsAdapter;
import ru.mtucifiit.mtucifiit.config.NetConfig;
import ru.mtucifiit.mtucifiit.model.project.History;
import ru.mtucifiit.mtucifiit.service.RequestService;
import ru.mtucifiit.mtucifiit.view.home.activity.AuthenticationActivity;
import ru.mtucifiit.mtucifiit.view.home.activity.CreateHistoryActivity;
import ru.mtucifiit.mtucifiit.view.home.activity.SettingsActivity;

public class ProjectFragment extends Fragment {

    private RecyclerView list_of_projects;
    private ProjectsAdapter projectsAdapter;

    private ProgressBar progressBar;

    private RequestService requestService;

    private TextView error;

    private ImageView create_project;

    private  NetConfig netConfig = new NetConfig();


    public ProjectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_project, container, false);


        init(view);

        return view;
    }

    private void init(View view) {
        requestService = new RequestService(getContext());
        error = view.findViewById(R.id.error);

        create_project = view.findViewById(R.id.create_project);

        list_of_projects = view.findViewById(R.id.list_of_projects);
        progressBar = view.findViewById(R.id.progressBarProject);
        progressBar.animate().start();

        ImageView setting = view.findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
            }
        });






        create_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(requestService.checkAuth()==false){
                    startActivity(new Intent(getActivity(), AuthenticationActivity.class));
                }else{
                    startActivityForResult(new Intent(getActivity(), CreateHistoryActivity.class),200);
                }
            }
        });



        requestService.getRequest(netConfig.getProjects+"0", listener -> {
            try {
                JSONArray jsonArray = new JSONArray(listener);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("list",jsonArray);
                ObjectMapper objectMapper = new ObjectMapper();
                History history = objectMapper.readValue(jsonObject.toString(), History.class);

                projectsAdapter = new ProjectsAdapter(getContext(),history.list);
                list_of_projects.setAdapter(projectsAdapter);
                list_of_projects.setVisibility(View.VISIBLE);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            } catch (JsonMappingException e) {
                throw new RuntimeException(e);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        }, error -> {
            ProjectFragment.this.error.setText(error.toString());
            ProjectFragment.this.error.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            error.printStackTrace();
        });
       /* List<ProjectModel> projectModels = new ArrayList<>();

        for (int i =0;i<30;i++){
            Faker faker = new Faker();
            ProjectModel projectModel = new ProjectModel(0L+i,faker.lorem().characters(20), ProjectType.HISTORY.toString(),faker.lorem().sentence(50),"@"+faker.funnyName());
            projectModels.add(projectModel);
        }

        */

    }



    public void update(){
        requestService.getRequest(netConfig.getProjects+"0", listener -> {
            try {
                JSONArray jsonArray = new JSONArray(listener);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("list",jsonArray);
                ObjectMapper objectMapper = new ObjectMapper();
                History history = objectMapper.readValue(jsonObject.toString(), History.class);

              projectsAdapter.projectModels.clear();
              for(int i = 0;i<history.list.size();i++){
                  projectsAdapter.projectModels.add(history.list.get(i));
              }
              projectsAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                throw new RuntimeException(e);
            } catch (JsonMappingException e) {
                throw new RuntimeException(e);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        }, error -> {
            ProjectFragment.this.error.setText(error.toString());
            ProjectFragment.this.error.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            error.printStackTrace();
        });
    }
}