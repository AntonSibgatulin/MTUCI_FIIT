package ru.mtucifiit.mtucifiit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.mtucifiit.mtucifiit.R;
import ru.mtucifiit.mtucifiit.model.schedule.DaySchedule;
import ru.mtucifiit.mtucifiit.model.schedule.ScheduleModel;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleView> {

    private List<DaySchedule> scheduleModels;
    private Context context;

    public ScheduleAdapter(List<DaySchedule> scheduleModels, Context context) {
        this.scheduleModels = scheduleModels;
        this.context = context;
    }


    @NonNull
    @Override
    public ScheduleView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.schedule_item, parent, false);
        return new ScheduleView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleView holder, int position) {
        DaySchedule daySchedule = scheduleModels.get(position);

        holder.timeStart.setText(daySchedule.timeStart.replaceAll("\\.",":"));
        holder.timeEnd.setText(daySchedule.timeEnd.replaceAll("\\.",":"));

        holder.teacher.setText(daySchedule.teachers.get(0));
        holder.subject.setText(daySchedule.subjects.get(0).replaceAll("\n"," "));

        holder.room.setText(daySchedule.classroom);
        holder.type.setText(getType(daySchedule.lessonType));

        holder.view.setBackgroundTintList(context.getColorStateList(getCode(daySchedule.lessonType)));
    }

    public String getType(String lesson_type) {
        if (lesson_type == null || lesson_type.isEmpty()) return "Пара";
        else if (lesson_type.equals("лаб.")) {
            return "Лаболаторная работа";
        } else if (lesson_type.equals("пр.")) {
            return "Практическая работа";
        }else{
            return "Лаб./Прак. работа";
        }
    }

    public int getCode(String lesson_type) {
        if (lesson_type == null || lesson_type.isEmpty()) return R.color.bg_tick;
        else if (lesson_type.equals("лаб.")) {
            return R.color.bg_history;
        } else if (lesson_type.equals("пр.")) {
            return R.color.history;
        }else{
            return R.color.important;
        }
    }




    @Override
    public int getItemCount() {
        return scheduleModels.size();
    }

    public class ScheduleView extends RecyclerView.ViewHolder {
        public TextView timeStart, timeEnd, subject, teacher, room, type;
        private View view;


        public ScheduleView(@NonNull android.view.View itemView) {
            super(itemView);
            timeStart = itemView.findViewById(R.id.timeStart);
            timeEnd = itemView.findViewById(R.id.timeEnd);
            subject = itemView.findViewById(R.id.subject);
            teacher = itemView.findViewById(R.id.teacher);
            room = itemView.findViewById(R.id.room);
            type = itemView.findViewById(R.id.type_subject);
            view = itemView.findViewById(R.id.left_panel_type);
        }
    }
}
