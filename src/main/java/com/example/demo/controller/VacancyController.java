package com.example.demo.controller;

import com.example.demo.dto.CreateVacancyRequest;
import com.example.demo.dto.UpdateVacancyRequest;
import com.example.demo.dto.VacancyResponse;
import com.example.demo.entity.Status;
import com.example.demo.entity.User;
import com.example.demo.entity.Vacancy;
import com.example.demo.service.UserService;
import com.example.demo.service.VacancyService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@Log
public class VacancyController {
    private final VacancyService vacancyService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @PostMapping("/vacancies")
    public ResponseEntity<String> createVacancy(@RequestBody CreateVacancyRequest createVacancyRequest,
                                         Principal principal) {
        log.info("Handling creating vacancy request: ");
        int userId = Integer.parseInt(principal.getName());
        Optional<User> optUser = userService.findById(userId);
        Vacancy vacancy = modelMapper.map(createVacancyRequest, Vacancy.class);
        optUser.ifPresentOrElse(
                vacancy::setUser,
                ResponseEntity.unprocessableEntity()::build);
        vacancyService.saveVacancy(vacancy);
        return ResponseEntity.ok().build();
    }

    @GetMapping("vacancies")
    public List<VacancyResponse> findVacancies(Principal principal, @RequestParam("page") int page) {
        log.info("Handling find all vacancies");
        int id = Integer.parseInt(principal.getName());
        List<Vacancy> vacancies = vacancyService.findByUserIdPaginated(id, page - 1);
        return vacancies
                .stream()
                .map(pet -> modelMapper.map(pet, VacancyResponse.class))
                .collect(Collectors.toList());
    }

    @GetMapping("vacancies/status/{statusValue}")
    public List<VacancyResponse> findVacanciesByStatus(Principal principal, @PathVariable String statusValue, @RequestParam("page") int page) {
        log.info("Handling find vacancies by status");
        Status status = Status.valueOf(statusValue.toUpperCase());
        int id = Integer.parseInt(principal.getName());
        List<Vacancy> vacancies = vacancyService.findByUserIdAndStatusPaginated(id, status, page - 1);
        return vacancies
                .stream()
                .map(pet -> modelMapper.map(pet, VacancyResponse.class))
                .collect(Collectors.toList());
    }

    @GetMapping("vacancies/company/{companyName}")
    public List<VacancyResponse> findVacanciesByCompanyName(Principal principal, @PathVariable String companyName, @RequestParam("page") int page) {
        log.info("Handling find all vacancies by company name");
        int id = Integer.parseInt(principal.getName());
        List<Vacancy> vacancies = vacancyService.findByUserIdAndCompanyNamePaginated(id, companyName, page - 1);
        return vacancies
                .stream()
                .map(pet -> modelMapper.map(pet, VacancyResponse.class))
                .collect(Collectors.toList());
    }

    @PostMapping("/send_mails")
    public ResponseEntity<String> sendMails(Principal principal){
        log.info("Handling sending emails request: ");
        Integer userId = Integer.parseInt(principal.getName());
        Optional<User> optUser = userService.findById(userId);
        optUser.ifPresentOrElse(
                (user) -> vacancyService.sendEmails(user.getId()),
                ResponseEntity.unprocessableEntity()::build);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/vacancy/{id}")
    public ResponseEntity<?> updateUser(@RequestBody UpdateVacancyRequest updateVacancyRequest, Principal principal, @PathVariable int id) {
        log.info("Handling updating user");
        Integer userId = Integer.parseInt(principal.getName());
        Optional<User> optUser = userService.findById(userId);
        Vacancy vacancy = modelMapper.map(updateVacancyRequest, Vacancy.class);
        optUser.ifPresentOrElse(
                vacancy::setUser,
                ResponseEntity.unprocessableEntity()::build);
        vacancy.setId(id);
        Vacancy oldVacancyVersion = vacancyService.findById(vacancy.getId()).get();
        if (oldVacancyVersion.getUser().getId() != userId) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        vacancyService.updateVacancy(vacancy);
        return ResponseEntity.ok().build();
    }
}