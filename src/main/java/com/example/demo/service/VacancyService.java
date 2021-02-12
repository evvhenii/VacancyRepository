package com.example.demo.service;

import com.example.demo.entity.Status;
import com.example.demo.entity.User;
import com.example.demo.entity.Vacancy;
import com.example.demo.exception.ValidationException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface VacancyService {
    Vacancy saveVacancy(Vacancy pet);
    List<Vacancy> findByUserIdPaginated(int id, int page);
    List<Vacancy> findByUserId(int id);
    List<Vacancy> findByUserIdAndStatusPaginated(int id, Status status, int page);
    List<Vacancy> findByUserIdAndCompanyNamePaginated(int id, String companyName, int page);
    void updateVacancy(Vacancy vacancy);
    Optional<Vacancy> findById(Integer id);
    void sendEmails(int userId);
}
