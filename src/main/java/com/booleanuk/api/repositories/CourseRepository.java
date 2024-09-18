package com.booleanuk.api.repositories;

import com.booleanuk.api.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Integer> {
}
