package com.example.test.dto.student;

import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.UUID;

public class CreateStudentDto {
    private UUID id;
    private String name;
    private Date birthday;

    @NotNull(message = "Class id is required")
    private String classId;

    public void setId(UUID id) {
        this.id = id;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
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
