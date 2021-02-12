package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vacancies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vacancy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String companyName;

    @Column
    private String post;

    @Column
    private int expectedSalary;

    @Column
    private String vacancyLink;

    @Column
    private String recruiterInfo;

    @Column
    private Status status;

    @Column
    private LocalDateTime requestDate;

    @ManyToOne
    private User user;
}
