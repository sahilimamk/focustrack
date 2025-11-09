package com.focustrack.controller;

import com.focustrack.dto.SessionDTO;
import com.focustrack.model.Session;
import com.focustrack.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SessionController {
    
    private final SessionService sessionService;
    
    @PostMapping
    public ResponseEntity<SessionDTO> createSession(
            @RequestParam(required = false) String sessionName,
            @RequestParam(required = false) Session.SessionType type) {
        SessionDTO session = sessionService.createSession(sessionName, type);
        return ResponseEntity.status(HttpStatus.CREATED).body(session);
    }
    
    @GetMapping("/active")
    public ResponseEntity<SessionDTO> getActiveSession() {
        return sessionService.getActiveSession()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<SessionDTO>> getAllSessions() {
        return ResponseEntity.ok(sessionService.getAllSessions());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SessionDTO> getSessionById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(sessionService.getSessionById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}/pause")
    public ResponseEntity<SessionDTO> pauseSession(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(sessionService.pauseSession(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}/resume")
    public ResponseEntity<SessionDTO> resumeSession(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(sessionService.resumeSession(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}/end")
    public ResponseEntity<SessionDTO> endSession(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(sessionService.endSession(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

