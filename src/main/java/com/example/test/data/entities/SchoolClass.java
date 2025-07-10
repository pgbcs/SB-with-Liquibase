package com.example.test.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="classes")
public class SchoolClass {
    @Id
    private UUID id;

    private String name;

    @Min(1)
    @Max(12)
    private Integer grade;

    @ManyToOne
    @JoinColumn(referencedColumnName = "school_id", name="school_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private School school;


    @Column(unique = true, nullable = false)
    @NotNull(message = "Class id is required")
    private String classId;

    @OneToMany(mappedBy = "sclass")
    @JsonIgnore
    private List<Student> students = new ArrayList<Student>();

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }


    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getGrade() {
        return grade;
    }

    public School getSchool() {
        return school;
    }

    public String getClassId() {
        return classId;
    }

    public List<Student> getStudents() {
        return students;
    }

    public String getSchoolId(){
        return school != null? school.getSchoolId(): null;
    }
}
