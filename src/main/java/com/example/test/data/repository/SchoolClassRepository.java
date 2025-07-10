package com.example.test.data.repository;

import com.example.test.data.entities.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SchoolClassRepository extends JpaRepository<SchoolClass, UUID> {
    Optional<SchoolClass> findByClassId(String classId);
    Optional<List<SchoolClass>> findBySchool_SchoolId(String schoolId);


}
