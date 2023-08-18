package ru.mtucifiit.mtucifiit.model.schedule;

import java.util.List;

public class DaySchedule {


    public String dayOfWeek;
    public int lessonNumber;
    public String time;

    public boolean dataDay = false;
    public boolean haveLessons = true;

    public String timeStart;
    public String timeEnd;
    public String lessonType;
    public String classroom;
    public List<String> teachers;
    public List<String> subjects;

    public DaySchedule() {
    }

    public DaySchedule(String dayOfWeek, boolean dataDay) {
        this.dayOfWeek = dayOfWeek;
        this.dataDay = dataDay;
    }
    public DaySchedule(String dayOfWeek, boolean dataDay,boolean haveLessons) {
        this.dayOfWeek = dayOfWeek;
        this.dataDay = dataDay;
        this.haveLessons = haveLessons;
    }
}
