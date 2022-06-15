package com.example.a12th_mobiledevfinalproject.home.data_class;

public class ListTable {
    String mp;
    String aClass;
    String teacher;
    String date;
    String type;
    String name;
    String score;
    String comment;

    public ListTable(String mp, String aClass, String teacher, String date, String type, String name, String score, String comment) {
        this.mp = mp;
        this.aClass = aClass;
        this.teacher = teacher;
        this.date = date;
        this.type = type;
        this.name = name;
        this.score = score;
        this.comment = comment;
    }

    public String getMp() {
        return mp;
    }

    public String getaClass() {
        return aClass;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getScore() {
        return score;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public String toString() {
        return "ListTable{" +
                "mp='" + mp + '\'' +
                ", aClass='" + aClass + '\'' +
                ", teacher='" + teacher + '\'' +
                ", date='" + date + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", score='" + score + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
