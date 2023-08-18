package ru.mtucifiit.mtucifiit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.mtucifiit.mtucifiit.R;
import ru.mtucifiit.mtucifiit.model.group.Group;
import ru.mtucifiit.mtucifiit.view.home.activity.GroupSelectActivity;

public class SettingGroupAdapter extends RecyclerView.Adapter<SettingGroupAdapter.SettingGroupView> {

    private List<Group> groupList;
    private Context context;

    private GroupSelectActivity groupSelectActivity;

    public SettingGroupAdapter(List<Group> groupList, Context context, GroupSelectActivity groupSelectActivity) {
        this.groupList = groupList;
        this.context = context;
        this.groupSelectActivity = groupSelectActivity;
    }

    @NonNull
    @Override
    public SettingGroupView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.group_item,parent,false);
        return new SettingGroupView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingGroupView holder, int position) {
        Group group = groupList.get(position);
        if(group.name.startsWith("БФИ")){
            holder.name.setText("ФИИТ "+group.name);
            holder.name.setTextColor(context.getColor(R.color.black));
        }else{
            holder.name.setText(group.name);
            holder.name.setTextColor(context.getColor(R.color.grey_500));

        }
        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                groupSelectActivity.checkGroup(group);

            }
        });
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public class SettingGroupView extends RecyclerView.ViewHolder {
        public TextView name;
        public AppCompatButton check;

        public SettingGroupView(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.group_name);
            check = itemView.findViewById(R.id.button);

        }
    }
}
