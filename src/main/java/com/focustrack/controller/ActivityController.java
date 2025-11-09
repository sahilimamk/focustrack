package com.focustrack.controller;

import com.focustrack.dto.ActivityDTO;
import com.focustrack.dto.ActivityRequest;
import com.focustrack.dto.SessionDTO;
import com.focustrack.model.Activity;
import com.focustrack.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ActivityController {
    
    private final SessionService sessionService;
    
    @PostMapping("/session/{sessionId}")
    public ResponseEntity<ActivityDTO> addActivity(
            @PathVariable Long sessionId,
            @RequestBody ActivityRequest request) {
        try {
            Activity activity = sessionService.addActivity(
                    sessionId,
                    request.getAppName(),
                    request.getWindowTitle()
            );
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ActivityDTO.fromEntity(activity));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}/end")
    public ResponseEntity<Void> endActivity(@PathVariable Long id) {
        try {
            sessionService.endActivity(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<List<ActivityDTO>> getActivitiesBySession(@PathVariable Long sessionId) {
        try {
            SessionDTO session = sessionService.getSessionById(sessionId);
            return ResponseEntity.ok(session.getActivities() != null ? session.getActivities() : List.of());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

