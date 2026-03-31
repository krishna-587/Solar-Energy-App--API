package com.example.Energy_Dashboard.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.Energy_Dashboard.model.EnergyRecord;
import com.example.Energy_Dashboard.model.User;

public interface EnergyRecordRepository extends JpaRepository<EnergyRecord, Long> {
    List<EnergyRecord> findByUserAndRecordDateBetween(User user, LocalDate startDate, LocalDate endDate);
    
    List<EnergyRecord> findByUser(User user);
    
    @Query("SELECT DISTINCT YEAR(e.recordDate) FROM EnergyRecord e WHERE e.user = :user ORDER BY YEAR(e.recordDate) DESC")
    List<Integer> findAvailableYearsForUser(@Param("user") User user);
}

