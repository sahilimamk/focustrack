package com.focustrack.repository;

import com.focustrack.model.Activity;
import com.focustrack.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    
    List<Activity> findBySession(Session session);
    
    @Query("SELECT a FROM Activity a WHERE a.session = :session ORDER BY a.startTime ASC")
    List<Activity> findBySessionOrderByStartTime(@Param("session") Session session);
    
    @Query("SELECT a.appName, SUM(a.durationSeconds) as totalSeconds " +
           "FROM Activity a WHERE a.session.startTime >= :startDate AND a.session.startTime <= :endDate " +
           "GROUP BY a.appName ORDER BY totalSeconds DESC")
    List<Object[]> getAppUsageStats(@Param("startDate") LocalDateTime startDate, 
                                     @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT a.type, SUM(a.durationSeconds) as totalSeconds " +
           "FROM Activity a WHERE a.session.startTime >= :startDate AND a.session.startTime <= :endDate " +
           "GROUP BY a.type")
    List<Object[]> getActivityTypeStats(@Param("startDate") LocalDateTime startDate, 
                                         @Param("endDate") LocalDateTime endDate);
}

