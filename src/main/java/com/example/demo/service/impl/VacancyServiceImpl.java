package com.example.demo.service.impl;

import com.example.demo.entity.Status;
import com.example.demo.entity.Vacancy;
import com.example.demo.repository.VacancyRepository;
import com.example.demo.service.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class VacancyServiceImpl implements VacancyService {
    private final VacancyRepository vacancyRepository;
    private final JavaMailSender javaMailSender;

    @Override
    public Vacancy saveVacancy(Vacancy vacancy) {
        vacancy.setRequestDate(LocalDateTime.now());
        vacancyRepository.save(vacancy);
        return vacancy;
    }

    @Override
    public List<Vacancy> findByUserIdPaginated(int id, int page) {
        Pageable paging = PageRequest.of(page, 5);
        return vacancyRepository.findAllByUserId(id, paging);
    }

    @Override
    public List<Vacancy> findByUserId(int id) {
        return vacancyRepository.findByUserId(id);
    }

    @Override
    public List<Vacancy> findByUserIdAndStatusPaginated(int id, Status status, int page) {
        Pageable paging = PageRequest.of(page, 5);
        return vacancyRepository.findAllByUserIdAndStatus(id, status, paging);
    }

    @Override
    public List<Vacancy> findByUserIdAndCompanyNamePaginated(int id, String companyName, int page) {
        Pageable paging = PageRequest.of(page, 5);
        return vacancyRepository.findAllByUserIdAndCompanyName(id, companyName, paging);
    }

    @Override
    public void updateVacancy(Vacancy vacancy){
        Vacancy oldVacancyVersion = findById(vacancy.getId()).get();
        if(oldVacancyVersion.getStatus() == vacancy.getStatus()){
            vacancy.setRequestDate(oldVacancyVersion.getRequestDate());
        }else{
            vacancy.setRequestDate(LocalDateTime.now());
        }
        vacancyRepository.save(vacancy);
    }

    @Override
    public Optional<Vacancy> findById(Integer id) {
        return vacancyRepository.findById(id);
    }

    @Override
    public void sendEmails(int userId){
        List<Vacancy> vacancies = findByUserId(userId);
        vacancies.stream()
                .filter(vac -> vac.getStatus() == Status.WAITING_FOR_FEEDBACK && Duration.between(vac.getRequestDate(), LocalDateTime.now()).toDays() > 7)
                .map(Vacancy::getRecruiterInfo)
                .forEach(this::sendEmail);
    }

    public void sendEmail(String email){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("testing.test1121@gmail.com");
        message.setTo(email);
        message.setSubject("Please, answer");
        message.setText("Im waiting for your feedback for a week. Answer, please!");
        javaMailSender.send(message);
    }
}
