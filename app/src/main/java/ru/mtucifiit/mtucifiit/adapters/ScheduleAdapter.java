package ru.mtucifiit.mtucifiit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.mtucifiit.mtucifiit.R;
import ru.mtucifiit.mtucifiit.model.schedule.ScheduleModel;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleView> {

    private List<ScheduleModel> scheduleModels;
    private Context context;

    @NonNull
    @Override
    public ScheduleView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.schedule_item,parent,false);
        return new ScheduleView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleView holder, int position) {

    }

    @Override
    public int getItemCount() {
        return scheduleModels.size();
    }

    public class ScheduleView extends RecyclerView.ViewHolder{
        public ScheduleView(@NonNull android.view.View itemView) {
            super(itemView);
        }
    }
}
