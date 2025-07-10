package com.example.test.controllers;

import com.example.test.data.entities.SchoolClass;
import com.example.test.data.entities.Student;
import com.example.test.data.repository.SchoolClassRepository;
import com.example.test.data.repository.SchoolRepository;
import com.example.test.data.repository.StudentRepository;
import com.example.test.dto.student.CreateStudentDto;
import com.example.test.dto.student.UpdateStudentDto;
import com.example.test.models.BaseResponse;
import com.example.test.models.PaginationResponse;
import jakarta.validation.Valid;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/students")
public class StudentController {
    private final StudentRepository studentRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final SchoolRepository schoolRepository;

    StudentController(StudentRepository studentRepository, SchoolClassRepository schoolClassRepository, SchoolRepository schoolRepository){
        this.studentRepository = studentRepository;
        this.schoolClassRepository = schoolClassRepository;
        this.schoolRepository = schoolRepository;
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<Student>>> getAllStudents(){
        return ResponseEntity.ok(BaseResponse.success(studentRepository.findAll(), "Get all students successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<Student>> getStudentById(@PathVariable UUID id){
        return studentRepository.findById(id)
                .map(existing->ResponseEntity.ok(BaseResponse.success(existing, "Get student successfully")))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/pagination-list")
    public ResponseEntity<PaginationResponse<List<Student>>> getAllStudentByClassAndSchool(
            @RequestParam("schoolId") String schoolId,
            @RequestParam("classId") String classId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size){
//        if(page == null) page=0;
//        if(size == null) size=10;
//        if(schoolId == null) return ResponseEntity.badRequest().body(BaseResponse.error(400, "schoolId"))

        SchoolClass schoolClas = schoolClassRepository.findByClassId(classId).orElse(null);

        if(schoolClas == null || !schoolId.equals(schoolClas.getSchoolId())) return ResponseEntity.badRequest().body(
                PaginationResponse.paginationError(400, "Fail to get data")
        );
        System.out.println(schoolClas.getSchoolId());
        System.out.println(schoolId);
        Pageable pageable = PageRequest.of(page, size);
        Page<Student> studentPage = studentRepository.findBySclass_ClassIdAndSclass_School_SchoolId(classId, schoolId, pageable);
        if (studentPage.getContent().isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(
                PaginationResponse.success(
                        studentPage.getContent(),
                        studentPage.getNumber(),
                        studentPage.getNumberOfElements(),
                        studentPage.getTotalPages(),
                        studentPage.getTotalElements(),
                        "Get data successfully"
                )
        );
    }

    @GetMapping("/by-class/{classId}")
    public ResponseEntity<BaseResponse<List<Student>>> getAllStudentByClassId(@PathVariable String classId){
        if(!(classId instanceof  String)){
            return ResponseEntity.badRequest().body(BaseResponse.error(400, "Invalid classId"));
        }
        SchoolClass schoolClass = schoolClassRepository.findByClassId(classId).orElse(null);
        if(schoolClass == null) return ResponseEntity.badRequest().body(BaseResponse.error(400, "Invalid classId"));

        return studentRepository.findBySclass_ClassId(classId)
                .map((List<Student> studentlist)->
                        {
                            if(studentlist.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponse.<List<Student>>error(404,"Not Found"));
                            return ResponseEntity.ok(BaseResponse.success(studentlist, "Get student list successfully"));
                        })
                            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<PaginationResponse<List<Student>>> getAllStudentByName(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
            ){
        if(page == null || page<0) page = 0;
        if(size == null || size<0) size = 10;

        if(name.equals("")) return ResponseEntity.badRequest().body(
                PaginationResponse.paginationError(400, "Name is required")
        );

        Pageable pageable = PageRequest.of(page, size);

        Page<Student> studentPage = studentRepository.findByNameContaining(name, pageable);
        if(studentPage.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                PaginationResponse.paginationError(404, "Not found")
        );
        else return ResponseEntity.ok(
                PaginationResponse.success(
                        studentPage.getContent(),
                        studentPage.getNumber(),
                        studentPage.getNumberOfElements(),
                        studentPage.getTotalPages(),
                        studentPage.getTotalElements(),
                        "Get data successfully")
        );
    }

    @GetMapping("/count-by-class/{classId}")
    public ResponseEntity<BaseResponse<Long>> getNumberOfStudentByClassId(@PathVariable String classId){
        if(classId == null) return ResponseEntity.badRequest().body(BaseResponse.error(400, "Class Id is required"));

        SchoolClass schoolClass = schoolClassRepository.findByClassId(classId).orElse(null);
        if(schoolClass == null) return ResponseEntity.badRequest().body(BaseResponse.error(400, "Invalid Class Id"));

        long res = studentRepository.countBySclass_ClassId(classId);
        if(res==0) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponse.error(404, "Not found"));
        return ResponseEntity.ok(BaseResponse.success(res, "Get data successfully"));
    }


    @PostMapping
    public ResponseEntity<BaseResponse<Student>> createStudent(@RequestBody @Valid CreateStudentDto studentDto){
        Student student = new Student();
        if(studentDto.getId() == null){
            studentDto.setId(UUID.randomUUID());
        }
        student.setId(studentDto.getId());
        student.setBirthday(studentDto.getBirthday());
        student.setName(studentDto.getName());

        return schoolClassRepository.findByClassId(studentDto.getClassId())
                .map(
                        sclass->{
                            student.setSclass(sclass);
                            URI location = URI.create("/" + studentDto.getId());
                            Student savedStudent = studentRepository.save(student);
                            return ResponseEntity.created(location).body(BaseResponse.success(savedStudent, "Student is created"));
                        }
                )
                .orElse(ResponseEntity.badRequest().body(BaseResponse.error(400, "Class id is not exist")));
    }


    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<Student>> updateStudent(@PathVariable UUID id, @RequestBody @Valid CreateStudentDto updatedStudentDto){
        return studentRepository.findById(id)
                .map(
                        existing->{
                            existing.setName(updatedStudentDto.getName());
                            existing.setBirthday(updatedStudentDto.getBirthday());

                            return schoolClassRepository.findByClassId(updatedStudentDto.getClassId())
                                            .map(sclass->{
                                                existing.setSclass(sclass);
                                                studentRepository.save(existing);
                                                return ResponseEntity.ok(BaseResponse.success(existing, "Student is updated"));
                                            }).orElse(ResponseEntity.badRequest().body(BaseResponse.error(400, "Class id is not exist")));
                        }
                )
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BaseResponse<Student>> modifyStudent(@PathVariable UUID id, @RequestBody UpdateStudentDto modifiedStudentDto){
        return studentRepository.findById(id)
                .map(
                        existing-> {
                            if (modifiedStudentDto.getBirthday() != null) existing.setBirthday(modifiedStudentDto.getBirthday());
                            if (modifiedStudentDto.getName() != null) existing.setName(modifiedStudentDto.getName());
                            if (modifiedStudentDto.getClassId() != null){
                                SchoolClass sclass = schoolClassRepository.findByClassId(modifiedStudentDto.getClassId()).orElse(null);
                                if(sclass == null){
                                    return ResponseEntity.badRequest().body(BaseResponse.<Student>error(400, "Class id is not exist"));
                                }
                                existing.setSclass(sclass);
                            }
                            studentRepository.save(existing);
                            return ResponseEntity.ok(BaseResponse.success(existing, "Modified student successful"));
                        }
                )
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Student>> deleteStudent(@PathVariable UUID id){
        try{
            studentRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }catch(EmptyResultDataAccessException e){
            return ResponseEntity.notFound().build();
        }
    }

}
