package ru.mtucifiit.mtucifiit.view.home.fragments;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.List;

import ru.mtucifiit.mtucifiit.R;
import ru.mtucifiit.mtucifiit.adapters.ProjectsAdapter;
import ru.mtucifiit.mtucifiit.model.project.ProjectModel;
import ru.mtucifiit.mtucifiit.model.project.ProjectType;
import ru.mtucifiit.mtucifiit.view.home.activity.SettingsActivity;

public class ProjectFragment extends Fragment {

    private RecyclerView list_of_projects;
    private ProjectsAdapter projectsAdapter;

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
        list_of_projects = view.findViewById(R.id.list_of_projects);


        ImageView setting = view.findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
            }
        });
        
        
        List<ProjectModel> projectModels = new ArrayList<>();

        for (int i =0;i<30;i++){
            Faker faker = new Faker();
            ProjectModel projectModel = new ProjectModel(0L+i,faker.lorem().characters(20), ProjectType.HISTORY.toString(),faker.lorem().sentence(50),"@"+faker.funnyName());
            projectModels.add(projectModel);
        }


        projectsAdapter = new ProjectsAdapter(getContext(),projectModels);
        list_of_projects.setAdapter(projectsAdapter);
    }
}