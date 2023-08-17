package ru.mtucifiit.mtucifiit.model.project;

import ru.mtucifiit.mtucifiit.model.user.HUser;

public class HistoryModel {

    public Long id;
    public String name;
    public HistoryType historyType;
    public String description;
    public HUser user;
    public Long time;

    public Integer count_like;
    public Integer count_dislike;


    public boolean ilike = false;
    public boolean idisLike = false;


    public HistoryModel() {
    }
}
