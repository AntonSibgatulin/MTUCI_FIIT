package ru.mtucifiit.mtucifiit.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.App;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ru.mtucifiit.mtucifiit.R;
import ru.mtucifiit.mtucifiit.model.project.History;
import ru.mtucifiit.mtucifiit.model.project.HistoryModel;
import ru.mtucifiit.mtucifiit.model.project.HistoryType;
import ru.mtucifiit.mtucifiit.service.RequestService;
import ru.mtucifiit.mtucifiit.view.home.activity.HistoryViewActivity;
import ru.mtucifiit.mtucifiit.view.home.fragments.ProjectFragment;

public class ProjectsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_LOAD_MORE = 1;

    private int page = 0;
    private Context context;
    public List<HistoryModel> projectModels;

    public RequestService requestService;

    public ProjectsAdapter(Context context, List<HistoryModel> projectModels, RequestService requestService, int page) {
        this.projectModels = projectModels;
        this.context = context;
        this.requestService = requestService;
        this.page = page;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.project_item, parent, false);
            return new ProjectView(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.load_more_item, parent, false);
            return new LoadMore(view);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ProjectView) {
            ProjectView holder = (ProjectView) viewHolder;
            HistoryModel projectModel = projectModels.get(position);
            HistoryType projectType = (projectModel.historyType);
            int textColor = context.getColor(getColorText(projectType));
            if (holder == null || holder.left_panel == null) return;
            holder.left_panel.setBackgroundTintList(context.getColorStateList(getColorText(projectType)));
            holder.header.setTextColor(textColor);
            holder.header.setBackgroundTintList(context.getColorStateList(getBgColorText(projectType)));

            holder.author.setText("@" + projectModel.user.username);

            holder.name.setText(projectModel.name);
            holder.description.setText(resizeText(projectModel.description, 0, 35) + "...");


            loadLikeByObject(holder, projectModel);

            holder.const_.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, HistoryViewActivity.class);
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        intent.putExtra("history",objectMapper.writeValueAsString(projectModel));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    context.startActivity(intent);
                }
            });

            holder.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int type = 1;
                    if (projectModel.ilike) {
                        type = 0;
                    }
                    requestService.like(projectModel, type, listener -> {
                        updateHistoryData(listener, projectModel, holder);
                    }, error -> {
                        error.printStackTrace();
                    });
                }
            });


            holder.dislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int type = -1;
                    if (projectModel.idisLike) {
                        type = 0;
                    }
                    requestService.like(projectModel, type, listener -> {
                        updateHistoryData(listener, projectModel, holder);

                    }, error -> {
                        error.printStackTrace();
                    });
                }
            });

        } else {
            LoadMore holder = (LoadMore) viewHolder;
            holder.load_more_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    holder.loading.setVisibility(View.VISIBLE);
                    holder.load_more_button.setVisibility(View.GONE);
                    loadMore(holder);

                }
            });
        }
    }

    private void updateHistoryData(String listener, HistoryModel projectModel, @NonNull ProjectView holder) {
        Log.e("DATA", listener);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            HistoryModel historyModel = objectMapper.readValue(listener, HistoryModel.class);
            projectModel.ilike = historyModel.ilike;
            projectModel.idisLike = historyModel.idisLike;

            projectModel.count_dislike = historyModel.count_dislike;
            projectModel.count_like = historyModel.count_like;

            loadLikeByObject(holder, projectModel);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void loadLikeByObject(@NonNull ProjectView holder, HistoryModel projectModel) {
        if (projectModel.ilike) {
            holder.like.setImageDrawable(context.getDrawable(R.drawable.like_solid));
        } else {
            holder.like.setImageDrawable(context.getDrawable(R.drawable.like));

        }

        if (projectModel.idisLike) {
            holder.dislike.setImageDrawable(context.getDrawable(R.drawable.dislike_solid));
        } else {
            holder.dislike.setImageDrawable(context.getDrawable(R.drawable.dislike));
        }
        holder.count_like_text.setText(String.valueOf(projectModel.count_like));
        holder.count_dislike_text.setText(String.valueOf(projectModel.count_dislike));


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
        return projectModels == null ? 0 : projectModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return projectModels.get(position) != null ? VIEW_TYPE_ITEM : VIEW_TYPE_LOAD_MORE;
    }

    public class ProjectView extends RecyclerView.ViewHolder {

        public View left_panel;
        public AppCompatButton header;

        public TextView name, description, author, count_like_text, count_dislike_text;
        private ImageView like, dislike;

        private ConstraintLayout const_;


        public ProjectView(@NonNull View itemView) {
            super(itemView);

            left_panel = itemView.findViewById(R.id.left_panel);
            header = itemView.findViewById(R.id.header);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.desc);
            author = itemView.findViewById(R.id.author);
            count_dislike_text = itemView.findViewById(R.id.count_dislike_text);
            count_like_text = itemView.findViewById(R.id.count_like_text);
            like = itemView.findViewById(R.id.like_image);
            dislike = itemView.findViewById(R.id.dislike_image);
            const_ = itemView.findViewById(R.id.constrain);

        }


    }


    public class LoadMore extends RecyclerView.ViewHolder {
        public AppCompatButton load_more_button;
        public ProgressBar loading;

        public LoadMore(@NonNull View itemView) {
            super(itemView);

            load_more_button = itemView.findViewById(R.id.load_more);
            loading = itemView.findViewById(R.id.loading);

        }
    }


    public void loadMore(LoadMore loadMore) {
        page+=1;
        requestService.post(requestService.netConfig.getProjects + page, listener -> {
            try {
                JSONArray jsonArray = new JSONArray(listener);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("list", jsonArray);
                ObjectMapper objectMapper = new ObjectMapper();
                History history = objectMapper.readValue(jsonObject.toString(), History.class);

                projectModels.remove(projectModels.size() - 1);

                if(history.list.size() == 0)page-=1;

                for (int i = 0; i < history.list.size(); i++) {
                    projectModels.add(history.list.get(i));
                }

                projectModels.add(null);
                loadMore.load_more_button.setVisibility(View.VISIBLE);
                loadMore.loading.setVisibility(View.GONE);
                notifyDataSetChanged();

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
