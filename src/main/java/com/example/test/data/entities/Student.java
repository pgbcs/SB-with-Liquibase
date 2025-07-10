package com.example.test.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name="student")
public class Student {
    @Id
    private UUID id;

    private String name;

    private Date birthday;

    public String getABC() {
        return ABC;
    }

    public void setABC(String ABC) {
        this.ABC = ABC;
    }

    private String ABC;

    public String getCDE() {
        return CDE;
    }

    public void setCDE(String CDE) {
        this.CDE = CDE;
    }

    private String CDE;

    public String getDEF() {
        return DEF;
    }

    public void setDEF(String DEF) {
        this.DEF = DEF;
    }

    private String DEF;

    @ManyToOne
    @JoinColumn(referencedColumnName = "classId", name="class_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private SchoolClass sclass;

    public void setId(UUID id) {
        this.id = id;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSclass(SchoolClass sclass) {
        this.sclass = sclass;
    }


    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public SchoolClass getSclass() {
        return sclass;
    }

    public String getClassId(){
        return sclass !=null? sclass.getClassId():null;
    }
}
