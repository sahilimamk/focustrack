package com.focustrack.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "activities")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Activity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;
    
    @Column(nullable = false)
    private String appName;
    
    @Column(nullable = false)
    private String windowTitle;
    
    @Column(nullable = false)
    private LocalDateTime startTime;
    
    private LocalDateTime endTime;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityType type;
    
    private Long durationSeconds;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (startTime == null) {
            startTime = LocalDateTime.now();
        }
        if (type == null) {
            type = determineActivityType(appName, windowTitle);
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        if (endTime != null && startTime != null) {
            durationSeconds = java.time.Duration.between(startTime, endTime).getSeconds();
        }
    }
    
    private ActivityType determineActivityType(String appName, String windowTitle) {
        String lowerApp = appName.toLowerCase();
        String lowerTitle = windowTitle.toLowerCase();
        
        // Distracting apps
        if (lowerApp.contains("youtube") || lowerTitle.contains("youtube") ||
            lowerApp.contains("instagram") || lowerTitle.contains("instagram") ||
            lowerApp.contains("facebook") || lowerTitle.contains("facebook") ||
            lowerApp.contains("twitter") || lowerTitle.contains("twitter") ||
            lowerApp.contains("tiktok") || lowerTitle.contains("tiktok") ||
            lowerApp.contains("netflix") || lowerTitle.contains("netflix") ||
            lowerTitle.contains("reddit")) {
            return ActivityType.DISTRACTING;
        }
        
        // Productive apps
        if (lowerApp.contains("code") || lowerApp.contains("ide") ||
            lowerApp.contains("intellij") || lowerApp.contains("eclipse") ||
            lowerApp.contains("vs code") || lowerApp.contains("visual studio") ||
            lowerTitle.contains("github") || lowerTitle.contains("stackoverflow") ||
            lowerApp.contains("notion") || lowerApp.contains("obsidian") ||
            lowerApp.contains("word") || lowerApp.contains("excel") ||
            lowerApp.contains("powerpoint") || lowerApp.contains("pdf")) {
            return ActivityType.PRODUCTIVE;
        }
        
        return ActivityType.NEUTRAL;
    }
    
    public enum ActivityType {
        PRODUCTIVE, DISTRACTING, NEUTRAL
    }
}

