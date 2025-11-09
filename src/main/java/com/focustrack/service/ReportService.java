package com.focustrack.service;

import com.focustrack.dto.ProductivityReportDTO;
import com.focustrack.model.Activity;
import com.focustrack.model.Session;
import com.focustrack.repository.ActivityRepository;
import com.focustrack.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {
    
    private final SessionRepository sessionRepository;
    private final ActivityRepository activityRepository;
    
    public ProductivityReportDTO generateDailyReport(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        
        return generateReport(startOfDay, endOfDay);
    }
    
    public ProductivityReportDTO generateWeeklyReport(LocalDate startDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = start.plusDays(7).minusSeconds(1);
        
        return generateReport(start, end);
    }
    
    public ProductivityReportDTO generateReport(LocalDateTime startDate, LocalDateTime endDate) {
        List<Session> sessions = sessionRepository.findByDateRange(startDate, endDate);
        List<Activity> allActivities = new ArrayList<>();
        
        for (Session session : sessions) {
            allActivities.addAll(activityRepository.findBySession(session));
        }
        
        // Calculate totals
        long totalFocusTime = allActivities.stream()
                .filter(a -> a.getType() == Activity.ActivityType.PRODUCTIVE && a.getDurationSeconds() != null)
                .mapToLong(Activity::getDurationSeconds)
                .sum();
        
        long totalDistractedTime = allActivities.stream()
                .filter(a -> a.getType() == Activity.ActivityType.DISTRACTING && a.getDurationSeconds() != null)
                .mapToLong(Activity::getDurationSeconds)
                .sum();
        
        long totalNeutralTime = allActivities.stream()
                .filter(a -> a.getType() == Activity.ActivityType.NEUTRAL && a.getDurationSeconds() != null)
                .mapToLong(Activity::getDurationSeconds)
                .sum();
        
        long totalTime = totalFocusTime + totalDistractedTime + totalNeutralTime;
        
        // Calculate scores
        double productivityScore = totalTime > 0 ? (double) totalFocusTime / totalTime * 100 : 0;
        double distractionScore = totalTime > 0 ? (double) totalDistractedTime / totalTime * 100 : 0;
        
        // Get app usage stats
        Map<String, Long> appUsageMap = allActivities.stream()
                .filter(a -> a.getDurationSeconds() != null)
                .collect(Collectors.groupingBy(
                        Activity::getAppName,
                        Collectors.summingLong(Activity::getDurationSeconds)
                ));
        
        List<ProductivityReportDTO.AppUsageDTO> topApps = appUsageMap.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .map(entry -> {
                    ProductivityReportDTO.AppUsageDTO dto = new ProductivityReportDTO.AppUsageDTO();
                    dto.setAppName(entry.getKey());
                    dto.setDurationSeconds(entry.getValue());
                    dto.setPercentage(totalTime > 0 ? (double) entry.getValue() / totalTime * 100 : 0);
                    return dto;
                })
                .collect(Collectors.toList());
        
        // Get distracting apps
        List<ProductivityReportDTO.AppUsageDTO> topDistractingApps = allActivities.stream()
                .filter(a -> a.getType() == Activity.ActivityType.DISTRACTING && a.getDurationSeconds() != null)
                .collect(Collectors.groupingBy(
                        Activity::getAppName,
                        Collectors.summingLong(Activity::getDurationSeconds)
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .map(entry -> {
                    ProductivityReportDTO.AppUsageDTO dto = new ProductivityReportDTO.AppUsageDTO();
                    dto.setAppName(entry.getKey());
                    dto.setDurationSeconds(entry.getValue());
                    dto.setPercentage(totalTime > 0 ? (double) entry.getValue() / totalTime * 100 : 0);
                    return dto;
                })
                .collect(Collectors.toList());
        
        // Get productive apps
        List<ProductivityReportDTO.AppUsageDTO> topProductiveApps = allActivities.stream()
                .filter(a -> a.getType() == Activity.ActivityType.PRODUCTIVE && a.getDurationSeconds() != null)
                .collect(Collectors.groupingBy(
                        Activity::getAppName,
                        Collectors.summingLong(Activity::getDurationSeconds)
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .map(entry -> {
                    ProductivityReportDTO.AppUsageDTO dto = new ProductivityReportDTO.AppUsageDTO();
                    dto.setAppName(entry.getKey());
                    dto.setDurationSeconds(entry.getValue());
                    dto.setPercentage(totalTime > 0 ? (double) entry.getValue() / totalTime * 100 : 0);
                    return dto;
                })
                .collect(Collectors.toList());
        
        // Calculate consistency (sessions per day)
        Map<LocalDate, Long> sessionsPerDay = sessions.stream()
                .collect(Collectors.groupingBy(
                        session -> session.getStartTime().toLocalDate(),
                        Collectors.counting()
                ));
        
        long consistencyRating = sessionsPerDay.isEmpty() ? 0 : 
                (long) sessionsPerDay.values().stream()
                        .mapToLong(Long::longValue)
                        .average()
                        .orElse(0);
        
        ProductivityReportDTO report = new ProductivityReportDTO();
        report.setReportDate(LocalDateTime.now());
        report.setTotalFocusTimeSeconds(totalFocusTime);
        report.setTotalDistractedTimeSeconds(totalDistractedTime);
        report.setTotalNeutralTimeSeconds(totalNeutralTime);
        report.setProductivityScore(Math.round(productivityScore * 100.0) / 100.0);
        report.setDistractionScore(Math.round(distractionScore * 100.0) / 100.0);
        report.setTopApps(topApps);
        report.setTopDistractingApps(topDistractingApps);
        report.setTopProductiveApps(topProductiveApps);
        report.setConsistencyRating(consistencyRating);
        
        return report;
    }
}

