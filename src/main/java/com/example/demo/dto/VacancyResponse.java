package com.example.demo.dto;

import com.example.demo.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacancyResponse {
    private String companyName;
    private String post;
    private int expectedSalary;
    private String vacancyLink;
    private String recruiterInfo;
    private Status status;
    private LocalDateTime requestDate;
}
