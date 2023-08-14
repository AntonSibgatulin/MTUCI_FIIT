package ru.mtucifiit.mtucifiit.model.project;

import ru.mtucifiit.mtucifiit.model.user.HUser;

public class HistoryModel {

    public Long id;
    public String name;
    public HistoryType historyType;
    public String description;
    public HUser user;
    public Long time;


    public HistoryModel() {
    }
}
