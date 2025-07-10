package com.example.test.controllers;

import com.example.test.data.entities.School;
import com.example.test.data.repository.SchoolRepository;
import com.example.test.models.BaseResponse;
import jakarta.validation.Valid;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/schools")
public class SchoolController {
    private final SchoolRepository schoolRepository;

    SchoolController(SchoolRepository schoolRepository){
        this.schoolRepository = schoolRepository;
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<School>>> getAllSchools(){
        return ResponseEntity.ok(BaseResponse.success(schoolRepository.findAll(), "Get schools successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<School>> getSchoolById(@PathVariable UUID id){
        return schoolRepository.findById(id)
                .map(school->ResponseEntity.ok(BaseResponse.success(school, "Get school info successfully")))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/with-more-than/{number}-classes")
    public ResponseEntity<BaseResponse<List<School>>> getAllSchoolHaveMoreNClasses(@PathVariable Long number){

        List<School> schools = schoolRepository.findSchoolHaveMoreNClasses(number);

        if(schools.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponse.error(400, "Not found"));

        return ResponseEntity.ok(BaseResponse.success(schools, "Get data successfully"));
    }


    @PostMapping
    public ResponseEntity<BaseResponse<School>> createSchool(@RequestBody @Valid School school){
        if(school.getId()==null){
            school.setId(UUID.randomUUID());
        }
        URI location = URI.create("/"+school.getId().toString());
        School savedSchool = schoolRepository.save(school);
        return ResponseEntity.created(location).body(BaseResponse.success(savedSchool, "School is created"));
    }

    @PostMapping("/batch")
    public ResponseEntity<BaseResponse<List<School>>> createListSchool(@RequestBody List<School> schools){
        System.out.print(schools);
        schools.forEach(school -> {
            if (school.getId() == null) {
                school.setId(UUID.randomUUID());
            }
        });
        List<School> savedSchools = schoolRepository.saveAll(schools);
        return ResponseEntity.ok(BaseResponse.success(savedSchools));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<School>> updateSchool(@PathVariable UUID id, @RequestBody @Valid School updatedSchool){
        return schoolRepository.findById(id)
                .map(
                        existing->{
                            existing.setName(updatedSchool.getName());
                            existing.setSchoolId(updatedSchool.getSchoolId());
                            existing.setAddress(updatedSchool.getAddress());
                            schoolRepository.save(existing);
                            return ResponseEntity.ok(BaseResponse.success(existing, "Update school successfully"));
                        }
                )
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BaseResponse<School>> modifySchool(@PathVariable UUID id, @RequestBody School modifiedSchool){
        return schoolRepository.findById(id)
                .map(
                        existing->{
                            if(modifiedSchool.getName()!=null) existing.setName(modifiedSchool.getName());
                            if(modifiedSchool.getAddress()!=null) existing.setAddress(modifiedSchool.getAddress());
                            if(modifiedSchool.getSchoolId()!=null) existing.setSchoolId(modifiedSchool.getSchoolId());
                            schoolRepository.save(existing);
                            return ResponseEntity.ok(BaseResponse.success(existing, "School is modified"));
                        }
                )
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<School>> deleteSchool(@PathVariable UUID id){
        try{
            schoolRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }catch(EmptyResultDataAccessException e){
            return ResponseEntity.notFound().build();
        }
    }
}
