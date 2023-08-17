package ru.mtucifiit.mtucifiit.config;

public class NetConfig {
    public String ip = "192.168.0.108:8080";
    public String base_url = "http://"+ip+"/";

    public String getScheduleByGroup = base_url+"getGroup/";

    public String getGroups = base_url+"getGroups";
    public String getMain = base_url+"main";
    public String getProjects = base_url+"history/getHistoriesForUser?page=";
    public String reg = base_url+"history/reg";
    public String auth = base_url+"history/auth";
    public String url_create_history=base_url+"history/create";
    public String like=base_url+"history/like";
}
