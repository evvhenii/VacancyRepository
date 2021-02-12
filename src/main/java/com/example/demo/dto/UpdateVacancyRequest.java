package com.example.demo.dto;

import com.example.demo.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateVacancyRequest {
    private String companyName;
    private String post;
    private int expectedSalary;
    private String vacancyLink;
    private String recruiterInfo;
    private Status status;
}
