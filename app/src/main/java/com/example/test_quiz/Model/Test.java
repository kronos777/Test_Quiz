package com.example.test_quiz.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Test implements Serializable{
    String Name;
    String TutorId;
    String GroupId;
    ArrayList<Question> Questions;
    Long time;

    public Test() {
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setTutorId(String TutorId1) {
        TutorId = TutorId1;
    }

    public String getTutorId() {
        return TutorId;
    }

    public void setGroupId(String GroupId1) {
        GroupId = GroupId1;
    }

    public String getGroupId() {
        return GroupId;
    }


    public ArrayList<Question> getQuestions() {
        return Questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        Questions = questions;
    }
}