package com.focustrack.dto;

import com.focustrack.model.Session;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionDTO {
    private Long id;
    private String sessionName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Session.SessionStatus status;
    private Session.SessionType type;
    private Long totalDurationSeconds;
    private Long focusedDurationSeconds;
    private Long distractedDurationSeconds;
    private List<ActivityDTO> activities;
    
    public static SessionDTO fromEntity(Session session) {
        SessionDTO dto = new SessionDTO();
        dto.setId(session.getId());
        dto.setSessionName(session.getSessionName());
        dto.setStartTime(session.getStartTime());
        dto.setEndTime(session.getEndTime());
        dto.setStatus(session.getStatus());
        dto.setType(session.getType());
        dto.setTotalDurationSeconds(session.getTotalDurationSeconds());
        dto.setFocusedDurationSeconds(session.getFocusedDurationSeconds());
        dto.setDistractedDurationSeconds(session.getDistractedDurationSeconds());
        
        if (session.getActivities() != null) {
            dto.setActivities(session.getActivities().stream()
                    .map(ActivityDTO::fromEntity)
                    .toList());
        }
        
        return dto;
    }
}

