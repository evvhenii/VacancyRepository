package com.example.demo.repository;

import com.example.demo.entity.Status;
import com.example.demo.entity.Vacancy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VacancyRepository extends JpaRepository<Vacancy, Integer> {
    List<Vacancy> findAllByUserId(Integer id, Pageable pageable);
    List<Vacancy> findAllByUserIdAndStatus(Integer id, Status status, Pageable pageable);
    List<Vacancy> findAllByUserIdAndCompanyName(Integer id, String companyName, Pageable pageable);
    List<Vacancy> findByUserId(int id);
}
