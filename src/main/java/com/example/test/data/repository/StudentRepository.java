package com.example.test.data.repository;

import com.example.test.data.entities.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {
    Page<Student> findBySclass_ClassIdAndSclass_School_SchoolId(String classId, String schoolId, Pageable pageable);
    Optional<List<Student>> findBySclass_ClassId(String classId);
    Page<Student> findByNameContaining(String name, Pageable pageable);
    long countBySclass_ClassId(String classId);
}
