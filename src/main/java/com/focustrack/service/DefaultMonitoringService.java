package com.focustrack.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Default implementation of MonitoringService.
 * 
 * This is a placeholder implementation that returns mock data.
 * For production, this should be replaced with:
 * 1. A native integration (using JNA for Windows/Mac/Linux)
 * 2. An external Python script that sends data via REST API
 * 3. A browser extension for tab tracking
 * 
 * See the monitoring/ directory for example Python scripts.
 */
@Slf4j
@Service
public class DefaultMonitoringService implements MonitoringService {
    
    private boolean monitoringActive = false;
    private Long currentSessionId = null;
    
    @Override
    public String getActiveApplication() {
        // Mock implementation - replace with actual monitoring
        log.debug("Getting active application (mock: 'Unknown')");
        return "Unknown";
    }
    
    @Override
    public String getActiveWindowTitle() {
        // Mock implementation - replace with actual monitoring
        log.debug("Getting active window title (mock: 'FocusTrack Dashboard')");
        return "FocusTrack Dashboard";
    }
    
    @Override
    public boolean isUserFocused() {
        // Mock implementation - always returns true
        // In production, this would use webcam/eye-tracking
        return true;
    }
    
    @Override
    public void startMonitoring(Long sessionId) {
        log.info("Starting monitoring for session: {}", sessionId);
        this.currentSessionId = sessionId;
        this.monitoringActive = true;
        // In production, start a background thread that:
        // 1. Polls for active window/app every few seconds
        // 2. Sends updates to ActivityController
        // 3. Handles focus detection if enabled
    }
    
    @Override
    public void stopMonitoring() {
        log.info("Stopping monitoring");
        this.monitoringActive = false;
        this.currentSessionId = null;
        // In production, stop background monitoring thread
    }
    
    @Override
    public boolean isMonitoringActive() {
        return monitoringActive;
    }
}

