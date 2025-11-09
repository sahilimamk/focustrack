package com.focustrack.service;

/**
 * Service interface for monitoring user activity.
 * 
 * This interface defines methods that can be implemented by native monitoring modules
 * (e.g., Python scripts using psutil, Java Native Access, etc.) to track:
 * - Active application/window
 * - Browser tabs
 * - Screen attention detection (optional)
 * 
 * For MVP, this can be implemented as a REST client that receives data from
 * an external monitoring script, or as a native Java service using JNA.
 */
public interface MonitoringService {
    
    /**
     * Gets the currently active application name.
     * @return The name of the active application (e.g., "Chrome", "VS Code")
     */
    String getActiveApplication();
    
    /**
     * Gets the title of the currently active window/tab.
     * @return The window title (e.g., "YouTube - Google Chrome", "FocusTrack.java - VS Code")
     */
    String getActiveWindowTitle();
    
    /**
     * Checks if the user is currently focused on the screen.
     * This is an optional feature that can use webcam/eye-tracking.
     * @return true if user is focused, false otherwise
     */
    boolean isUserFocused();
    
    /**
     * Starts monitoring activity in the background.
     * This should periodically send activity updates to the backend.
     */
    void startMonitoring(Long sessionId);
    
    /**
     * Stops monitoring activity.
     */
    void stopMonitoring();
    
    /**
     * Checks if monitoring is currently active.
     * @return true if monitoring is running
     */
    boolean isMonitoringActive();
}

