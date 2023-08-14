package ru.mtucifiit.mtucifiit.view.home.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.mtucifiit.mtucifiit.R;
import ru.mtucifiit.mtucifiit.adapters.ScheduleAdapter;
import ru.mtucifiit.mtucifiit.config.Config;
import ru.mtucifiit.mtucifiit.config.NetConfig;
import ru.mtucifiit.mtucifiit.model.schedule.DaySchedule;
import ru.mtucifiit.mtucifiit.model.schedule.ScheduleModel;
import ru.mtucifiit.mtucifiit.service.RequestService;
import ru.mtucifiit.mtucifiit.view.home.activity.SettingsActivity;


public class ScheduleFragment extends Fragment {


    private ImageView setting;
    private ProgressBar progressBar;

    private RecyclerView recyclerView;

    private RequestService requestService;

    private String group;

    private TextView schedule_of_task;
    public ScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        init(view);
        return view;
    }


    public void init(View view) {
        requestService = new RequestService(getActivity());

        schedule_of_task = view.findViewById(R.id.schedule_of_task);

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.animate().start();

        group = requestService.getStringSh(Config.my_group, "BFI2201");

        schedule_of_task.setText(schedule_of_task.getText()+" "+requestService.beautGroup(group));
        recyclerView = view.findViewById(R.id.schedule);

        setting = view.findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
            }
        });

        request();


    }


    public void request() {
        NetConfig netConfig = new NetConfig();


        requestService.getRequest(netConfig.getScheduleByGroup + group, listener -> {
            try {
                listener = new String(listener.getBytes(), StandardCharsets.US_ASCII);

                JSONObject jsonObject = new JSONObject(listener);
                ScheduleModel scheduleModel = null;
                ObjectMapper objectMapper = new ObjectMapper();
                scheduleModel = objectMapper.readValue(jsonObject.toString(), ScheduleModel.class);
                List<DaySchedule> daySchedules = new ArrayList<>();

                for (DaySchedule daySchedule : scheduleModel.schedule.even.MONDAY) {
                    if (daySchedule.subjects.size() == 0 || daySchedule.subjects.get(0).isEmpty())
                        continue;
                    daySchedules.add(daySchedule);
                }
                for (DaySchedule daySchedule : scheduleModel.schedule.even.TUESDAY) {
                    if (daySchedule.subjects.size() == 0 || daySchedule.subjects.get(0).isEmpty())
                        continue;
                    daySchedules.add(daySchedule);
                }
                for (DaySchedule daySchedule : scheduleModel.schedule.even.WEDNESDAY) {
                    if (daySchedule.subjects.size() == 0 || daySchedule.subjects.get(0).isEmpty())
                        continue;
                    daySchedules.add(daySchedule);
                }
                for (DaySchedule daySchedule : scheduleModel.schedule.even.THURSDAY) {
                    if (daySchedule.subjects.size() == 0 || daySchedule.subjects.get(0).isEmpty())
                        continue;
                    daySchedules.add(daySchedule);
                }
                for (DaySchedule daySchedule : scheduleModel.schedule.even.FRIDAY) {
                    if (daySchedule.subjects.size() == 0 || daySchedule.subjects.get(0).isEmpty())
                        continue;
                    daySchedules.add(daySchedule);
                }
                for (DaySchedule daySchedule : scheduleModel.schedule.even.SATURDAY) {
                    if (daySchedule.subjects.size() == 0 || daySchedule.subjects.get(0).isEmpty())
                        continue;
                    daySchedules.add(daySchedule);
                }


                ScheduleAdapter scheduleAdapter = new ScheduleAdapter(daySchedules, getContext());
                recyclerView.setAdapter(scheduleAdapter);
                recyclerView.setVisibility(View.VISIBLE);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            } catch (JsonMappingException e) {
                throw new RuntimeException(e);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        }, error -> {
            error.printStackTrace();
        });

    }

}