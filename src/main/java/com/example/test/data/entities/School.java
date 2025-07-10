package com.example.test.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "school")
public class School {
    @Id
    private UUID id;

    private String name;

    private String address;

    @Column(unique = true, nullable = false, name="school_id")
    @NotNull(message = "School id is required")
    private String schoolId;

    @OneToMany(mappedBy = "school")
    @JsonIgnore
    private List<SchoolClass> schoolClasses = new ArrayList<SchoolClass>();

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSchoolId(String school_id) {
        this.schoolId = school_id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setSchoolClasses(List<SchoolClass> schoolClasses) {
        this.schoolClasses = schoolClasses;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public List<SchoolClass> getSchoolClasses() {
        return schoolClasses;
    }
}
