package com.focustrack.service;

import com.focustrack.dto.SessionDTO;
import com.focustrack.model.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PomodoroService {
    
    private final SessionService sessionService;
    
    private static final long POMODORO_WORK_DURATION_SECONDS = 25 * 60; // 25 minutes
    private static final long POMODORO_BREAK_DURATION_SECONDS = 5 * 60; // 5 minutes
    private static final long POMODORO_LONG_BREAK_DURATION_SECONDS = 15 * 60; // 15 minutes
    
    public SessionDTO startPomodoroWork(String sessionName) {
        return sessionService.createSession(
                sessionName != null ? sessionName : "Pomodoro Work Session",
                Session.SessionType.POMODORO_WORK
        );
    }
    
    public SessionDTO startPomodoroBreak(boolean isLongBreak) {
        String breakType = isLongBreak ? "Long Break" : "Short Break";
        return sessionService.createSession(
                "Pomodoro " + breakType,
                Session.SessionType.POMODORO_BREAK
        );
    }
    
    public long getWorkDuration() {
        return POMODORO_WORK_DURATION_SECONDS;
    }
    
    public long getBreakDuration() {
        return POMODORO_BREAK_DURATION_SECONDS;
    }
    
    public long getLongBreakDuration() {
        return POMODORO_LONG_BREAK_DURATION_SECONDS;
    }
}

