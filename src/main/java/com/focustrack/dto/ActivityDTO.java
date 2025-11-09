package com.focustrack.dto;

import com.focustrack.model.Activity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDTO {
    private Long id;
    private Long sessionId;
    private String appName;
    private String windowTitle;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Activity.ActivityType type;
    private Long durationSeconds;
    
    public static ActivityDTO fromEntity(Activity activity) {
        ActivityDTO dto = new ActivityDTO();
        dto.setId(activity.getId());
        dto.setSessionId(activity.getSession().getId());
        dto.setAppName(activity.getAppName());
        dto.setWindowTitle(activity.getWindowTitle());
        dto.setStartTime(activity.getStartTime());
        dto.setEndTime(activity.getEndTime());
        dto.setType(activity.getType());
        dto.setDurationSeconds(activity.getDurationSeconds());
        return dto;
    }
}

