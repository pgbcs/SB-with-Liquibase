package com.example.test.dto.classes;

import jakarta.validation.constraints.NotNull;


public class UpdateClassDto {
    private String name;

    private Integer grade;

    private String schoolId;

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
}
