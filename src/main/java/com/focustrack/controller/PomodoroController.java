package com.focustrack.controller;

import com.focustrack.dto.SessionDTO;
import com.focustrack.service.PomodoroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pomodoro")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PomodoroController {
    
    private final PomodoroService pomodoroService;
    
    @PostMapping("/start")
    public ResponseEntity<SessionDTO> startPomodoro(
            @RequestParam(required = false) String sessionName) {
        return ResponseEntity.ok(pomodoroService.startPomodoroWork(sessionName));
    }
    
    @PostMapping("/break")
    public ResponseEntity<SessionDTO> startBreak(
            @RequestParam(required = false, defaultValue = "false") boolean longBreak) {
        return ResponseEntity.ok(pomodoroService.startPomodoroBreak(longBreak));
    }
    
    @GetMapping("/durations")
    public ResponseEntity<PomodoroDurations> getDurations() {
        PomodoroDurations durations = new PomodoroDurations();
        durations.setWorkDuration(pomodoroService.getWorkDuration());
        durations.setBreakDuration(pomodoroService.getBreakDuration());
        durations.setLongBreakDuration(pomodoroService.getLongBreakDuration());
        return ResponseEntity.ok(durations);
    }
    
    public static class PomodoroDurations {
        private long workDuration;
        private long breakDuration;
        private long longBreakDuration;
        
        public long getWorkDuration() { return workDuration; }
        public void setWorkDuration(long workDuration) { this.workDuration = workDuration; }
        public long getBreakDuration() { return breakDuration; }
        public void setBreakDuration(long breakDuration) { this.breakDuration = breakDuration; }
        public long getLongBreakDuration() { return longBreakDuration; }
        public void setLongBreakDuration(long longBreakDuration) { this.longBreakDuration = longBreakDuration; }
    }
}

