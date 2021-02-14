package com.example.demo.controller;

import com.example.demo.dto.CreateVacancyRequest;
import com.example.demo.dto.UpdateVacancyRequest;
import com.example.demo.dto.VacancyResponse;
import com.example.demo.entity.Status;
import com.example.demo.entity.Vacancy;
import com.example.demo.exception.PermittedActionException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.exception.VacancyNotFoundException;
import com.example.demo.service.VacancyService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@Log
public class VacancyController {
    private final VacancyService vacancyService;
    private final ModelMapper modelMapper;

    @PostMapping("/vacancies")
    public ResponseEntity<String> createVacancy(@RequestBody CreateVacancyRequest createVacancyRequest) {
        log.info("Handling creating vacancy request: ");
        try{
            Vacancy vacancy = modelMapper.map(createVacancyRequest, Vacancy.class);
            vacancyService.createVacancy(vacancy);
            return ResponseEntity.ok().build();
        } catch(UserNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not found", ex);
        }
    }

    @GetMapping("vacancies")
    public List<VacancyResponse> findVacancies(@RequestParam("page") int page) {
        log.info("Handling find all vacancies");
        List<Vacancy> vacancies = vacancyService.findVacanciesPaginated(page - 1);
        return vacancies
                .stream()
                .map(pet -> modelMapper.map(pet, VacancyResponse.class))
                .collect(Collectors.toList());
    }

    @GetMapping("vacancies/status/{statusValue}")
    public List<VacancyResponse> findVacanciesByStatus(@PathVariable String statusValue, @RequestParam("page") int page) {
        log.info("Handling find vacancies by status");
        Status status = Status.valueOf(statusValue.toUpperCase());
        List<Vacancy> vacancies = vacancyService.findVacanciesByStatusPaginated(status, page - 1);
        return vacancies
                .stream()
                .map(pet -> modelMapper.map(pet, VacancyResponse.class))
                .collect(Collectors.toList());
    }

    @GetMapping("vacancies/company/{companyName}")
    public List<VacancyResponse> findVacanciesByCompanyName(@PathVariable String companyName, @RequestParam("page") int page) {
        log.info("Handling find all vacancies by company name");
        List<Vacancy> vacancies = vacancyService.findVacanciesByCompanyNamePaginated(companyName, page - 1);
        return vacancies
                .stream()
                .map(pet -> modelMapper.map(pet, VacancyResponse.class))
                .collect(Collectors.toList());
    }

    @PostMapping("/send_mails")
    public ResponseEntity<String> sendMails(){
        log.info("Handling sending emails request: ");
        vacancyService.sendEmails();
        return ResponseEntity.ok().build();
    }

    @PutMapping("/vacancy/{vacancyId}")
    public ResponseEntity<?> updateVacancy(@RequestBody UpdateVacancyRequest updateVacancyRequest, @PathVariable int vacancyId) {
        log.info("Handling updating user");
        try{
            Vacancy vacancy = modelMapper.map(updateVacancyRequest, Vacancy.class);
            vacancyService.updateVacancy(vacancy, vacancyId);
            return ResponseEntity.ok().build();
        } catch(UserNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not found", ex);
        } catch(VacancyNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Vacancy is not found", ex);
        } catch(PermittedActionException ex){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Action is permitted", ex);
        }
    }
}