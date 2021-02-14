package com.example.demo.service.impl;

import com.example.demo.entity.Status;
import com.example.demo.entity.Vacancy;
import com.example.demo.exception.NotPermittedActionException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.exception.VacancyNotFoundException;
import com.example.demo.repository.VacancyRepository;
import com.example.demo.service.UserService;
import com.example.demo.service.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class VacancyServiceImpl implements VacancyService {
    private final VacancyRepository vacancyRepository;
    private final JavaMailSender javaMailSender;
    private final UserService userService;
    private final HttpServletRequest httpServletRequest;

    @Override
    public void createVacancy(Vacancy vacancy) throws UserNotFoundException {
        vacancy.setUser(userService.getCurrentUser());
        saveVacancy(vacancy);
    }

    @Override
    public List<Vacancy> findVacanciesPaginated(int page) {
        int id = getUserIdFromSession();
        Pageable paging = PageRequest.of(page, 5);
        return vacancyRepository.findAllByUserId(id, paging);
    }

    @Override
    public List<Vacancy> findVacanciesByStatusPaginated(Status status, int page) {
        int id = getUserIdFromSession();
        Pageable paging = PageRequest.of(page, 5);
        return vacancyRepository.findAllByUserIdAndStatus(id, status, paging);
    }

    @Override
    public List<Vacancy> findVacanciesByCompanyNamePaginated(String companyName, int page) {
        int id = getUserIdFromSession();
        Pageable paging = PageRequest.of(page, 5);
        return vacancyRepository.findAllByUserIdAndCompanyName(id, companyName, paging);
    }

    @Override
    public void updateVacancy(Vacancy vacancy, int vacancyId) throws UserNotFoundException, VacancyNotFoundException, NotPermittedActionException {
        vacancy.setId(vacancyId);

        int currentUserId = getUserIdFromSession();
        vacancy.setUser(userService.getCurrentUser());

        Optional<Vacancy> optOldVacancyVersion = findById(vacancyId);
        Vacancy oldVacancyVersion = optOldVacancyVersion.orElseThrow(VacancyNotFoundException::new);
        int vacancyOwnerId = oldVacancyVersion.getUser().getId();
        if (vacancyOwnerId != currentUserId) throw new NotPermittedActionException();

        if(oldVacancyVersion.getStatus() == vacancy.getStatus()){
            vacancy.setRequestDate(oldVacancyVersion.getRequestDate());
        }else{
            vacancy.setRequestDate(LocalDateTime.now());
        }

        vacancyRepository.save(vacancy);
    }

    @Override
    public void sendEmails() {
        int userId = getUserIdFromSession();
        List<Vacancy> vacancies = findByUserId(userId);
        vacancies.stream()
                .filter(vac -> vac.getStatus() == Status.WAITING_FOR_FEEDBACK && Duration.between(vac.getRequestDate(), LocalDateTime.now()).toDays() > 7)
                .map(Vacancy::getRecruiterInfo)
                .forEach(this::sendEmail);
    }

    public Optional<Vacancy> findById(int id) {
        return vacancyRepository.findById(id);
    }

    public int getUserIdFromSession(){
        Principal principal = httpServletRequest.getUserPrincipal();
        return Integer.parseInt(principal.getName());
    }

    public void saveVacancy(Vacancy vacancy) {
        vacancy.setRequestDate(LocalDateTime.now());
        vacancyRepository.save(vacancy);
    }

    public List<Vacancy> findByUserId(int id) {
        return vacancyRepository.findByUserId(id);
    }

    public void sendEmail(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("testing.test1121@gmail.com");
        message.setTo(email);
        message.setSubject("Please, answer");
        message.setText("Im waiting for your feedback for a week. Answer, please!");
        javaMailSender.send(message);
    }
}
