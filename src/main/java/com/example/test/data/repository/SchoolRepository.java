package com.example.test.data.repository;

import com.example.test.data.entities.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SchoolRepository extends JpaRepository<School, UUID> {
    Optional<School> findBySchoolId(String SchoolId);
    @Query("""
           SELECT s FROM School s JOIN s.schoolClasses c GROUP BY s HAVING COUNT(c) > :minC
            """)
    List<School> findSchoolHaveMoreNClasses(@Param("minC") long  minC);
}
