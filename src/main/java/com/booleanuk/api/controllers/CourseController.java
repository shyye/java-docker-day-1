package com.booleanuk.api.controllers;

import com.booleanuk.api.models.Course;
import com.booleanuk.api.repositories.CourseRepository;
import com.booleanuk.api.responses.ErrorResponse;
import com.booleanuk.api.responses.Response;
import com.booleanuk.api.responses.CourseListResponse;
import com.booleanuk.api.responses.CourseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("courses")
public class CourseController {
    @Autowired
    private CourseRepository repository;

    @PostMapping
    public ResponseEntity<CourseResponse> create(@RequestBody Course course) {
        this.repository.save(course);
        CourseResponse response = new CourseResponse();
        response.set(course);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<CourseListResponse> getAll() {
        CourseListResponse response = new CourseListResponse();
        response.set(this.repository.findAll());
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<Response<?>> getSpecific(@PathVariable int id) {
        Course course = this.repository.findById(id).orElse(null);
        if (course == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("A course with this id was not found.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        CourseResponse response = new CourseResponse();
        response.set(course);
        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<Response<?>> update(@PathVariable int id, @RequestBody Course course) {
        Course originalCourse = this.repository.findById(id).orElse(null);
        if (originalCourse == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("Not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        course.setId(id);
        try {
            this.repository.save(course);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        CourseResponse response = new CourseResponse();
        response.set(course);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Response<?>> delete(@PathVariable int id) {
        Course course = this.repository.findById(id).orElse(null);
        if (course == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("Not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        this.repository.delete(course);
        CourseResponse response = new CourseResponse();
        response.set(course);
        return ResponseEntity.ok(response);
    }
}
