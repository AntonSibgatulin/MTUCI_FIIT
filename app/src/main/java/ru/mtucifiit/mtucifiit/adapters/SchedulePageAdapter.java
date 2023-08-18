package ru.mtucifiit.mtucifiit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

import ru.mtucifiit.mtucifiit.R;
import ru.mtucifiit.mtucifiit.model.schedule.DaySchedule;

public class SchedulePageAdapter extends PagerAdapter {

    private Context context;
    private List<DaySchedule> schedules;

    private RecyclerView.Adapter<ScheduleAdapter.ScheduleView> [] adapters = new RecyclerView.Adapter[2];

    public SchedulePageAdapter(Context context, List<DaySchedule> schedules,ScheduleAdapter scheduleAdapter,ProjectsAdapter projectsAdapter) {
        this.context = context;
        this.schedules = schedules;
        adapters[0] = scheduleAdapter;
       // adapters[1] = projectsAdapter;
    }

    String[] type={"Чёт","Не чет"};


    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout)object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater.from(context).inflate(R.layout.even_not_even_item,container,false);
        return null;
    }
}
