package com.example.demo.service;

import com.example.demo.entity.Status;
import com.example.demo.entity.User;
import com.example.demo.entity.Vacancy;
import com.example.demo.exception.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface VacancyService {
    List<Vacancy> findVacanciesPaginated(int page);
    List<Vacancy> findVacanciesByStatusPaginated(Status status, int page);
    List<Vacancy> findVacanciesByCompanyNamePaginated(String companyName, int page);
    void updateVacancy(Vacancy vacancy, int vacancyId) throws UserNotFoundException, VacancyNotFoundException, PermittedActionException;
    void sendEmails();
    void createVacancy(Vacancy vacancy) throws UserNotFoundException;
}
