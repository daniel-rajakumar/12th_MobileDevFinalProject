package com.example.a12th_mobiledevfinalproject.home.data_class;

public class CalendarEventTable {
    String name;
    String date;


    public CalendarEventTable(String name, String date) {
        this.name = name;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "CalendarEventTable{" +
                "name='" + name + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
