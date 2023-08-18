package ru.mtucifiit.mtucifiit.view.home.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.mtucifiit.mtucifiit.R;
import ru.mtucifiit.mtucifiit.adapters.ScheduleAdapter;
import ru.mtucifiit.mtucifiit.config.Config;
import ru.mtucifiit.mtucifiit.config.NetConfig;
import ru.mtucifiit.mtucifiit.model.schedule.DaySchedule;
import ru.mtucifiit.mtucifiit.model.schedule.ScheduleModel;
import ru.mtucifiit.mtucifiit.service.RequestService;
import ru.mtucifiit.mtucifiit.view.home.activity.SettingsActivity;
import ru.mtucifiit.mtucifiit.view.home.activity.WebViewActivity;


public class ScheduleFragment extends Fragment {


    private HashMap<String, String> links = new HashMap<>();
    private ImageView setting;
    private ProgressBar progressBar;

    private RecyclerView recyclerView;

    private RequestService requestService;

    private String group;

    private ConstraintLayout files;
    private ImageView sch, exam;
    private TextView sch_, exam_;


    private EditText search_text;

    private Button schedule_check, files_check;

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

        files = view.findViewById(R.id.files);

        sch = view.findViewById(R.id.schedule_image_open);
        exam = view.findViewById(R.id.exam_image_open);

        search_text = view.findViewById(R.id.search_input);
        search_text.setClickable(false);

        schedule_of_task = view.findViewById(R.id.schedule_of_task);

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.animate().start();

        group = requestService.getStringSh(Config.my_group, "BFI2201");

        schedule_of_task.setText(schedule_of_task.getText() + " " + requestService.beautGroup(group));
        recyclerView = view.findViewById(R.id.schedule);

        setting = view.findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
            }
        });

        schedule_check = view.findViewById(R.id.schedule_check);
        files_check = view.findViewById(R.id.files_check);

        String files_string = requestService.getStringSh(Config.groups_datas_shared_preferences, null);
        if (files_string != null) {
            try {
                JSONArray jsonArray = new JSONObject(files_string).getJSONArray("files");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String id = jsonObject.getString("id");
                    String link = jsonObject.getString("link");
                    links.put(id, link);

                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        sch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("url", requestService.netConfig.base_url + "pdf/" + (group));
                intent.putExtra("header", "Расписание");
                startActivity(intent);
            }
        });


        schedule_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                files.setVisibility(View.GONE);
            }
        });

        files_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                files.setVisibility(View.VISIBLE);

            }
        });


        request();


    }


    public void request() {
        NetConfig netConfig = new NetConfig();


        requestService.getRequest(netConfig.getScheduleByGroup + group, listener -> {
            try {
                listener = new String(listener.getBytes(), StandardCharsets.US_ASCII);
                String[] days = {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота"};
                JSONObject jsonObject = new JSONObject(listener);
                ScheduleModel scheduleModel = null;
                ObjectMapper objectMapper = new ObjectMapper();
                scheduleModel = objectMapper.readValue(jsonObject.toString(), ScheduleModel.class);
                List<DaySchedule> daySchedules = new ArrayList<>();

                daySchedules.add(new DaySchedule(days[0], true, length(scheduleModel.schedule.even.MONDAY)>0));
                loadDayScedules(scheduleModel.schedule.even.MONDAY, daySchedules);
                daySchedules.add(new DaySchedule(days[1], true, length(scheduleModel.schedule.even.TUESDAY) > 0));
                loadDayScedules(scheduleModel.schedule.even.TUESDAY, daySchedules);

                daySchedules.add(new DaySchedule(days[2], true, length(scheduleModel.schedule.even.WEDNESDAY) > 0));
                loadDayScedules(scheduleModel.schedule.even.WEDNESDAY, daySchedules);

                daySchedules.add(new DaySchedule(days[3], true, length(scheduleModel.schedule.even.THURSDAY) > 0));
                loadDayScedules(scheduleModel.schedule.even.THURSDAY, daySchedules);

                daySchedules.add(new DaySchedule(days[4], true, length(scheduleModel.schedule.even.FRIDAY) > 0));
                loadDayScedules(scheduleModel.schedule.even.FRIDAY, daySchedules);

                daySchedules.add(new DaySchedule(days[5], true, length(scheduleModel.schedule.even.SATURDAY) > 0));
                loadDayScedules(scheduleModel.schedule.even.SATURDAY, daySchedules);


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

    private static void loadDayScedules(List<DaySchedule> list, List<DaySchedule> daySchedules) {
        for (DaySchedule daySchedule : list) {
            if (daySchedule.subjects.size() == 0 || daySchedule.subjects.get(0).isEmpty())
                continue;
            daySchedules.add(daySchedule);
        }
    }
    public int length(List<DaySchedule> list){
        int count = 0;
        for (DaySchedule daySchedule : list) {
            if (daySchedule.subjects.size() == 0 || daySchedule.subjects.get(0).isEmpty())
                continue;
           count++;
        }
        return count;
    }

}