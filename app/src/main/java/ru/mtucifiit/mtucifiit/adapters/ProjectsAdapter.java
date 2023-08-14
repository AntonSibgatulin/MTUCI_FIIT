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
import ru.mtucifiit.mtucifiit.model.project.HistoryModel;
import ru.mtucifiit.mtucifiit.model.project.HistoryType;

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.ProjectView> {

    private Context context;
    private List<HistoryModel> projectModels;

    public ProjectsAdapter(Context context, List<HistoryModel> projectModels) {
        this.projectModels = projectModels;
        this.context = context;
    }


    @NonNull
    @Override
    public ProjectView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.project_item, parent, false);
        return new ProjectView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectView holder, int position) {
        HistoryModel projectModel = projectModels.get(position);
        HistoryType projectType = (projectModel.historyType);
        int textColor = context.getColor(getColorText(projectType));
        holder.left_panel.setBackgroundTintList(context.getColorStateList(getColorText(projectType)));
        holder.header.setTextColor(textColor);
        holder.header.setBackgroundTintList(context.getColorStateList(getBgColorText(projectType)));


        holder.name.setText(projectModel.name);
        holder.description.setText(resizeText(projectModel.description, 0, 35) + "...");
    }

    public String resizeText(String text, int start, int end) {
        if (end >= text.length()) {
            return text;
        } else {
            return text.substring(start, end);
        }
    }

    public int getColorText(HistoryType projectType) {
        if (projectType == HistoryType.TICK) {
            return (R.color.tick);
        } else if (projectType == HistoryType.HISTORY) {
            return (R.color.history);
        } else if (projectType == HistoryType.SLOW_HISTORY) {
            return (R.color.slow_history);
        } else if (projectType == HistoryType.IMPORTANT) {
            return (R.color.important);
        } else {
            return (R.color.important);
        }

    }

    public int getBgColorText(HistoryType projectType) {
        if (projectType == HistoryType.TICK) {
            return (R.color.bg_tick);
        } else if (projectType == HistoryType.HISTORY) {
            return (R.color.bg_history);
        } else if (projectType == HistoryType.SLOW_HISTORY) {
            return (R.color.bg_slow_history);
        } else if (projectType == HistoryType.IMPORTANT) {
            return (R.color.bg_important);
        } else {
            return (R.color.bg_important);
        }

    }

    @Override
    public int getItemCount() {
        return projectModels.size();
    }

    public class ProjectView extends RecyclerView.ViewHolder {

        private View left_panel;
        private AppCompatButton header;
        private TextView name, description, author;

        public ProjectView(@NonNull View itemView) {
            super(itemView);
            left_panel = itemView.findViewById(R.id.left_panel);
            header = itemView.findViewById(R.id.header);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.desc);
            author = itemView.findViewById(R.id.author);
        }
    }

}
