package com.example.a12th_mobiledevfinalproject.home.data_class;

public class HomeTable {
    private String name,
            teacher,
            grade,
            mail;

    public HomeTable(String name, String teacher, String grade, String mail) {
        this.name = name;
        this.teacher = teacher;
        this.grade = grade;
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public String getTeacher() {
        return teacher;
    }


    public String getMail() {
        return mail;
    }

    public String getGrade() {
        return grade.replace("%-", "% ").replace("PROJECTED", "");
    }

    @Override
    public String toString() {
        return "GradeTable{" +
                "name='" + name + '\'' +
                ", teacher='" + teacher + '\'' +
                ", grade='" + grade + '\'' +
                ", mail='" + mail + '\'' +
                '}';
    }
}
