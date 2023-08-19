package ru.mtucifiit.mtucifiit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

import ru.mtucifiit.mtucifiit.R;
import ru.mtucifiit.mtucifiit.model.schedule.DaySchedule;

public class SchedulePageAdapter extends PagerAdapter {

    private Context context;

    private ScheduleAdapter[] scheduleAdapters = new ScheduleAdapter[2];

    private RecyclerView[] recyclerViews = new RecyclerView[2];


    public SchedulePageAdapter(Context context, ScheduleAdapter scheduleAdapter, ScheduleAdapter scheduleAdapter2) {
        this.context = context;

        scheduleAdapters[0] = scheduleAdapter;
        scheduleAdapters[1] = scheduleAdapter2;

    }

    String[] type = {"Чётная неделя", "Нечётная неделя"};


    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.even_not_even_item, container, false);

        ScheduleAdapter scheduleAdapter = scheduleAdapters[position];


        RecyclerView recyclerView = view.findViewById(R.id.recycleView);
        TextView even_not_even = view.findViewById(R.id.type);

        recyclerView.setAdapter(scheduleAdapter);
        even_not_even.setText(type[position]);

        recyclerViews[position] = recyclerView;
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }

}
