package com.focustrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductivityReportDTO {
    private LocalDateTime reportDate;
    private Long totalFocusTimeSeconds;
    private Long totalDistractedTimeSeconds;
    private Long totalNeutralTimeSeconds;
    private Double productivityScore;
    private Double distractionScore;
    private List<AppUsageDTO> topApps;
    private List<AppUsageDTO> topDistractingApps;
    private List<AppUsageDTO> topProductiveApps;
    private Map<String, Long> dailyFocusStreak;
    private Long consistencyRating;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AppUsageDTO {
        private String appName;
        private Long durationSeconds;
        private Double percentage;
    }
}

