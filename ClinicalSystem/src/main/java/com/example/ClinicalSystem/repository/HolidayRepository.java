package com.example.ClinicalSystem.repository;

import com.example.ClinicalSystem.model.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {
    Holiday save(Holiday holiday);
    List<Holiday> findAll();
    Holiday findByReason(String reason);
    Long removeByEmail(String email);
    Long removeByReason(String reason);
    Holiday findByEmail(String email);
}
