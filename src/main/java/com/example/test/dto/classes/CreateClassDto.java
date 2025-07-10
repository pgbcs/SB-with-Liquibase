package com.example.test.dto.classes;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class CreateClassDto {
    private UUID id;
    private String name;
    @Min(value = 1, message = "Grade must be in [1;12]")
    @Max(value = 10, message = "Grade must be in [1;12]")
    private Integer grade;

    @NotNull(message = "SchoolId is required")
    private String schoolId;
    @NotNull(message = "ClassId is required")
    private String classId;

    public void setClassId(String class_id) {
        this.classId = class_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public void setSchoolId(String school_id) {
        this.schoolId = school_id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getClassId() {
        return classId;
    }

    public String getName() {
        return name;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public Integer getGrade(){
        return grade;
    }

    public UUID getId() {
        return id;
    }
}

