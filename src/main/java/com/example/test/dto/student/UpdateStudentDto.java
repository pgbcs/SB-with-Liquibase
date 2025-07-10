package com.example.test.dto.student;

import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.UUID;

public class UpdateStudentDto {
    private String name;
    private Date birthday;

    private String classId;

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassId() {
        return classId;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getName() {
        return name;
    }
}
