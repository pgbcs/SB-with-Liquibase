package com.example.test.controllers;

import com.example.test.data.entities.SchoolClass;
import com.example.test.data.entities.School;
import com.example.test.data.repository.SchoolClassRepository;
import com.example.test.data.repository.SchoolRepository;
import com.example.test.dto.classes.CreateClassDto;
import com.example.test.dto.classes.UpdateClassDto;
import com.example.test.models.BaseResponse;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/classes")
public class SchoolClassController {
    private final SchoolClassRepository schoolClassRepository;
    private final SchoolRepository schoolRepository;
    SchoolClassController(SchoolClassRepository schoolClassRepository, SchoolRepository schoolRepository){
        this.schoolClassRepository = schoolClassRepository;
        this.schoolRepository = schoolRepository;
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<SchoolClass>>> getAllClasses(){
        return ResponseEntity.ok(BaseResponse.success(schoolClassRepository.findAll(), "Get classes successful"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<SchoolClass>> getClassById(@PathVariable UUID id){
        return schoolClassRepository.findById(id)
                .map(classEntity-> ResponseEntity.ok(BaseResponse.success(classEntity, "Get class info successful")))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-school/{schoolId}")
    public ResponseEntity<BaseResponse<List<SchoolClass>>>  getAllSchoolClassBySchoolId(@PathVariable String schoolId){
        School school = schoolRepository.findBySchoolId(schoolId).orElse(null);
        if(school == null) return ResponseEntity.badRequest().body(BaseResponse.error(400, "Invalid school id"));
        return schoolClassRepository.findBySchool_SchoolId(schoolId)
                .map(
                        classList-> {
                            if(classList.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponse.<List<SchoolClass>>error(404, "No classes found"));
                            return ResponseEntity.ok(BaseResponse.success(classList, "Get class by school id successful"));
                        }
                )
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BaseResponse<SchoolClass>> createClass(@RequestBody @Valid CreateClassDto newClass){
        SchoolClass nSchoolClass = new SchoolClass();
        if(newClass.getId() == null){
            newClass.setId(UUID.randomUUID());
        }

        nSchoolClass.setId(newClass.getId());
        nSchoolClass.setName(newClass.getName());
        nSchoolClass.setGrade(newClass.getGrade());
        nSchoolClass.setClassId(newClass.getClassId());

        return schoolRepository.findBySchoolId(newClass.getSchoolId())
                        .map(school->{
                            nSchoolClass.setSchool(school);
                            URI location = URI.create("/"+ nSchoolClass.getId());
                            schoolClassRepository.save(nSchoolClass);
                            return ResponseEntity.created(location).body(BaseResponse.success(nSchoolClass, "Class is created"));
                        })
                .orElse(
                        ResponseEntity.badRequest().body(BaseResponse.error(400, "School id is not exist"))
                );
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<SchoolClass>> updateClass(@PathVariable UUID id, @RequestBody @Valid CreateClassDto updatedClassDto){
        return schoolClassRepository.findById(id)
                .map(
                        existing->{
                            existing.setName(updatedClassDto.getName());
                            existing.setClassId(updatedClassDto.getClassId());
                            existing.setGrade(updatedClassDto.getGrade());

                            return schoolRepository.findBySchoolId(updatedClassDto.getSchoolId())
                                        .map(school->{
                                            existing.setSchool(school);
                                            schoolClassRepository.save(existing);
                                            return ResponseEntity.ok(BaseResponse.success(existing, "Class is updated"));
                                        })
                                    .orElse(ResponseEntity.badRequest().body(BaseResponse.error(400, "School id is not exist")));
                        }
                )
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BaseResponse<SchoolClass>> modifyClass(@PathVariable UUID id, @RequestBody UpdateClassDto modifiedClassDto){
        return schoolClassRepository.findById(id)
                .map(
                        existing-> {
                            if (modifiedClassDto.getClassId() != null) existing.setClassId(modifiedClassDto.getClassId());
                            if (modifiedClassDto.getName() != null) existing.setName(modifiedClassDto.getName());
                            if (modifiedClassDto.getGrade() != null) existing.setGrade(modifiedClassDto.getGrade());

                            if(modifiedClassDto.getSchoolId() != null){
                                School school = schoolRepository.findBySchoolId(modifiedClassDto.getSchoolId()).orElse(null);
                                if(school == null) return ResponseEntity.badRequest().body(BaseResponse.<SchoolClass>error(400, "School id is not exist"));
                                existing.setSchool(school);
                            }
                            schoolClassRepository.save(existing);
                            return ResponseEntity.ok(BaseResponse.success(existing, "Modified class successful"));
                        }
                )
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<SchoolClass>> deleteClass(@PathVariable UUID id){
        try{
            schoolClassRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }catch(EmptyResultDataAccessException e){
            return ResponseEntity.notFound().build();
        }
    }
}
