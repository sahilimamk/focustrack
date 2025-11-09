package com.focustrack.service;

import com.focustrack.dto.SessionDTO;
import com.focustrack.model.Activity;
import com.focustrack.model.Session;
import com.focustrack.repository.ActivityRepository;
import com.focustrack.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionService {
    
    private final SessionRepository sessionRepository;
    private final ActivityRepository activityRepository;
    
    @Transactional
    public SessionDTO createSession(String sessionName, Session.SessionType type) {
        Session session = new Session();
        session.setSessionName(sessionName != null ? sessionName : "Session " + LocalDateTime.now());
        session.setType(type != null ? type : Session.SessionType.FOCUS);
        session.setStatus(Session.SessionStatus.ACTIVE);
        session.setStartTime(LocalDateTime.now());
        
        Session saved = sessionRepository.save(session);
        return SessionDTO.fromEntity(saved);
    }
    
    @Transactional
    public SessionDTO pauseSession(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        
        session.setStatus(Session.SessionStatus.PAUSED);
        updateSessionDuration(session);
        
        return SessionDTO.fromEntity(sessionRepository.save(session));
    }
    
    @Transactional
    public SessionDTO resumeSession(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        
        session.setStatus(Session.SessionStatus.ACTIVE);
        
        return SessionDTO.fromEntity(sessionRepository.save(session));
    }
    
    @Transactional
    public SessionDTO endSession(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        
        session.setStatus(Session.SessionStatus.COMPLETED);
        session.setEndTime(LocalDateTime.now());
        updateSessionDuration(session);
        
        return SessionDTO.fromEntity(sessionRepository.save(session));
    }
    
    @Transactional
    public Activity addActivity(Long sessionId, String appName, String windowTitle) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        
        // End previous activity if exists
        Optional<Activity> lastActivity = activityRepository.findBySessionOrderByStartTime(session)
                .stream()
                .filter(a -> a.getEndTime() == null)
                .findFirst();
        
        if (lastActivity.isPresent()) {
            Activity prev = lastActivity.get();
            prev.setEndTime(LocalDateTime.now());
            activityRepository.save(prev);
        }
        
        // Create new activity
        Activity activity = new Activity();
        activity.setSession(session);
        activity.setAppName(appName);
        activity.setWindowTitle(windowTitle);
        activity.setStartTime(LocalDateTime.now());
        
        return activityRepository.save(activity);
    }
    
    @Transactional
    public void endActivity(Long activityId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activity not found"));
        
        activity.setEndTime(LocalDateTime.now());
        activityRepository.save(activity);
    }
    
    public Optional<SessionDTO> getActiveSession() {
        return sessionRepository.findFirstByStatusOrderByStartTimeDesc(Session.SessionStatus.ACTIVE)
                .map(SessionDTO::fromEntity);
    }
    
    public List<SessionDTO> getAllSessions() {
        return sessionRepository.findAll().stream()
                .map(SessionDTO::fromEntity)
                .collect(Collectors.toList());
    }
    
    public SessionDTO getSessionById(Long id) {
        return sessionRepository.findById(id)
                .map(SessionDTO::fromEntity)
                .orElseThrow(() -> new RuntimeException("Session not found"));
    }
    
    public List<SessionDTO> getSessionsByDateRange(LocalDateTime start, LocalDateTime end) {
        return sessionRepository.findByDateRange(start, end).stream()
                .map(SessionDTO::fromEntity)
                .collect(Collectors.toList());
    }
    
    private void updateSessionDuration(Session session) {
        List<Activity> activities = activityRepository.findBySession(session);
        
        long totalSeconds = 0;
        long focusedSeconds = 0;
        long distractedSeconds = 0;
        
        for (Activity activity : activities) {
            if (activity.getDurationSeconds() != null) {
                totalSeconds += activity.getDurationSeconds();
                
                if (activity.getType() == Activity.ActivityType.PRODUCTIVE) {
                    focusedSeconds += activity.getDurationSeconds();
                } else if (activity.getType() == Activity.ActivityType.DISTRACTING) {
                    distractedSeconds += activity.getDurationSeconds();
                }
            }
        }
        
        session.setTotalDurationSeconds(totalSeconds);
        session.setFocusedDurationSeconds(focusedSeconds);
        session.setDistractedDurationSeconds(distractedSeconds);
    }
}

