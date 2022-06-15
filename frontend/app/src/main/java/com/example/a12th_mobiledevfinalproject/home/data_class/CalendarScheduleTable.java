package com.example.a12th_mobiledevfinalproject.home.data_class;

public class CalendarScheduleTable {
    String period;
    String time;
    String name;
    String teacher;
    String room;
    String date;
    String type;

    public CalendarScheduleTable(String date, String type, String period, String time, String name, String teacher, String room) {
        this.period = period;
        this.time = time;
        this.name = name;
        this.teacher = teacher;
        this.room = room;
        this.date = date;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return "CalendarScheduleTable{" +
                "period='" + period + '\'' +
                ", time='" + time + '\'' +
                ", name='" + name + '\'' +
                ", teacher='" + teacher + '\'' +
                ", room='" + room + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
