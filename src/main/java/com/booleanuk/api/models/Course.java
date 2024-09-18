package com.booleanuk.api.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String title;

    @Column
    private LocalDateTime startDate;

    @Column
    private double credits;

    public Course(String title, LocalDateTime startDate, double credits) {
        this.title = title;
        this.startDate = startDate;
        this.credits = credits;
    }
}
