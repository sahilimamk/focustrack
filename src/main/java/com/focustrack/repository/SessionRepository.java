package com.focustrack.repository;

import com.focustrack.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    
    List<Session> findByStatus(Session.SessionStatus status);
    
    Optional<Session> findFirstByStatusOrderByStartTimeDesc(Session.SessionStatus status);
    
    @Query("SELECT s FROM Session s WHERE s.startTime >= :startDate AND s.startTime <= :endDate ORDER BY s.startTime DESC")
    List<Session> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                                   @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT s FROM Session s WHERE DATE(s.startTime) = DATE(:date) ORDER BY s.startTime DESC")
    List<Session> findByDate(@Param("date") LocalDateTime date);
}

