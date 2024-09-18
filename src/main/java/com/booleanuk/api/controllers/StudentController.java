package com.booleanuk.api.controllers;

import com.booleanuk.api.models.Course;
import com.booleanuk.api.models.Student;
import com.booleanuk.api.repositories.CourseRepository;
import com.booleanuk.api.repositories.StudentRepository;
import com.booleanuk.api.responses.ErrorResponse;
import com.booleanuk.api.responses.Response;
import com.booleanuk.api.responses.StudentListResponse;
import com.booleanuk.api.responses.StudentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("students")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @PostMapping
    public ResponseEntity<StudentResponse> create(@RequestBody Student student) {
        this.studentRepository.save(student);
        StudentResponse response = new StudentResponse();
        response.set(student);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<StudentListResponse> getAll() {
        StudentListResponse response = new StudentListResponse();
        response.set(this.studentRepository.findAll());
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<Response<?>> getSpecific(@PathVariable int id) {
        Student student = this.studentRepository.findById(id).orElse(null);
        if (student == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("A student with this id was not found.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        StudentResponse response = new StudentResponse();
        response.set(student);
        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<Response<?>> update(@PathVariable int id, @RequestBody Student student) {
        Student originalStudent = this.studentRepository.findById(id).orElse(null);
        if (originalStudent == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("Not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        student.setId(id);
        try {
            this.studentRepository.save(student);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        StudentResponse response = new StudentResponse();
        response.set(student);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Response<?>> delete(@PathVariable int id) {
        Student student = this.studentRepository.findById(id).orElse(null);
        if (student == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("Not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        this.studentRepository.delete(student);
        StudentResponse response = new StudentResponse();
        response.set(student);
        return ResponseEntity.ok(response);
    }

    // Create course for student
    @PostMapping("{studentId}/courses/{courseId}")
    public ResponseEntity<Response<?>> addCourseToStudent(
            @PathVariable int studentId,
            @PathVariable int courseId) {

        Student student = this.studentRepository.findById(studentId).orElse(null);
        Course course = this.courseRepository.findById(courseId).orElse(null);

        if (student == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("A student with this id was not found.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        } else if (course == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("A course with this id was not found.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        student.getCourses().add(course);
        this.studentRepository.save(student);

        StudentResponse response = new StudentResponse();
        response.set(student);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("{studentId}/courses/{courseId}")
    public ResponseEntity<Response<?>> deleteCourseFromStudent(
            @PathVariable int studentId,
            @PathVariable int courseId) {

        Student student = this.studentRepository.findById(studentId).orElse(null);
        Course course = this.courseRepository.findById(courseId).orElse(null);

        // TODO: Duplicate code, should refactor
        if (student == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("A student with this id was not found.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        } else if (course == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("A course with this id was not found.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        student.getCourses().remove(course);
        this.studentRepository.save(student);

        StudentResponse response = new StudentResponse();
        response.set(student);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
