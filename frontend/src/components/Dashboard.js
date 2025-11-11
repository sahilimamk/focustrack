import React, { useState, useEffect } from 'react';
import './Dashboard.css';
import axios from 'axios';
// REMOVED: const { spawn } = require('child_process');

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

function Dashboard({ activeSession, onSessionUpdate }) {
  const [sessionName, setSessionName] = useState('');
  const [activities, setActivities] = useState([]);
  const [stats, setStats] = useState(null);
  // REMOVED: monitorProc and isMonitoring states

  useEffect(() => {
    if (activeSession) {
      fetchActivities();
      fetchStats();

      // Set up an interval to refresh activities and stats
      const interval = setInterval(() => {
        fetchActivities();
        fetchStats();
      }, 5000); // Refresh every 5 seconds

      return () => clearInterval(interval); // Clean up on unmount
    }
  }, [activeSession]);

  const fetchActivities = async () => {
    if (!activeSession) return;
    try {
      // Fetch session details which include activities
      const response = await axios.get(`${API_BASE_URL}/sessions/${activeSession.id}`);
      // Ensure we have an array, even if activities is null
      setActivities(response.data.activities || []);
    } catch (error) {
      console.error('Error fetching activities:', error);
    }
  };

  const fetchStats = async () => {
    try {
      // Get stats for today's date
      const today = new Date().toISOString().split('T')[0];
      const response = await axios.get(`${API_BASE_URL}/reports/daily`, {
        params: { date: today }
      });
      setStats(response.data);
    } catch (error) {
      console.error('Error fetching stats:', error);
    }
  };

  const startSession = async () => {
    try {
      const response = await axios.post(`${API_BASE_URL}/sessions`, null, {
        params: { sessionName: sessionName || 'Focus Session' }
      });
      // Pass the *new* session data (which is response.data) to the parent
      onSessionUpdate(response.data);
      setSessionName('');
    } catch (error) {
      console.error('Error starting session:', error);
      alert('Failed to start session');
    }
  };

  const endSession = async () => {
    if (!activeSession) return;
    try {
      await axios.put(`${API_BASE_URL}/sessions/${activeSession.id}/end`);
      // REMOVED: stopMonitor();
      onSessionUpdate(null); // Clear the active session in App.js
      setActivities([]);
      setStats(null);
    } catch (error) {
      console.error('Error ending session:', error);
      alert('Failed to end session');
    }
  };

  const pauseSession = async () => {
    if (!activeSession) return;
    try {
      const response = await axios.put(`${API_BASE_URL}/sessions/${activeSession.id}/pause`);
      onSessionUpdate(response.data); // Update parent with new session status
    } catch (error) {
      console.error('Error pausing session:', error);
    }
  };

  const resumeSession = async () => {
    if (!activeSession) return;
    try {
      const response = await axios.put(`${API_BASE_URL}/sessions/${activeSession.id}/resume`);
      onSessionUpdate(response.data); // Update parent with new session status
    } catch (error) {
      console.error('Error resuming session:', error);
    }
  };

  // REMOVED: startMonitor and stopMonitor functions

  const formatDuration = (seconds) => {
    if (seconds === null || seconds === undefined) return '0s';
    const totalSeconds = Math.floor(seconds);
    const hours = Math.floor(totalSeconds / 3600);
    const minutes = Math.floor((totalSeconds % 3600) / 60);
    const secs = totalSeconds % 60;

    if (hours > 0) {
      return `${hours}h ${minutes}m ${secs}s`;
    } else if (minutes > 0) {
      return `${minutes}m ${secs}s`;
    } else {
      return `${secs}s`;
    }
  };

  const formatTime = (dateString) => {
    if (!dateString) return '';
    return new Date(dateString).toLocaleTimeString();
  };

  // Helper to find if the phone monitor has been active
  const isPhoneMonitorActive = activities.some(
    (act) => act.appName === "Phone (Webcam Detected)"
  );

  return (
    <div className="dashboard">
      <div className="card">
        <h2>Session Control</h2>
        {!activeSession ? (
          <div>
            <div className="form-group">
              <label>Session Name (optional)</label>
              <input
                type="text"
                value={sessionName}
                onChange={(e) => setSessionName(e.target.value)}
                placeholder="Enter session name"
              />
            </div>
            <button className="button button-primary" onClick={startSession}>
              Start Focus Session
            </button>
          </div>
        ) : (
          <div>
            <div className="session-info">
              <h3>{activeSession.sessionName}</h3>
              <span className={`session-status ${activeSession.status?.toLowerCase()}`}>
                {activeSession.status}
              </span>
              <p>Started: {formatTime(activeSession.startTime)}</p>
            </div>
            <div className="timer-controls">
              {activeSession.status === 'ACTIVE' ? (
                <button className="button button-secondary" onClick={pauseSession}>
                  Pause Session
                </button>
              ) : (
                <button className="button button-success" onClick={resumeSession}>
                  Resume Session
                </button>
              )}
              <button className="button button-danger" onClick={endSession}>
                End Session
              </button>
            </div>

            {/* --- REVISED MONITOR SECTION --- */}
            {activeSession.status !== 'ENDED' && (
              <div className="monitor-instructions card-inset">
                <h4><span role="img" aria-label="webcam">📹</span> Webcam Phone Monitor</h4>
                <p>To detect phone distractions, run this command in your terminal:</p>
                <code>
                  python monitoring/activity_monitor.py --session-id {activeSession.id}
                </code>
                {isPhoneMonitorActive && (
                    <p className="monitor-active-note">
                      ✓ Phone distraction logged. Monitor appears to be working.
                    </p>
                )}
              </div>
            )}
            {/* --- END REVISED SECTION --- */}

          </div>
        )}
      </div>

      {/* --- Stats Grid --- */}
      {stats && (
        <div className="stats-grid">
          <div className="stat-card">
            <h3>Focus Time</h3>
            <div className="value">{formatDuration(stats.totalFocusTimeSeconds)}</div>
            <div className="label">Today</div>
          </div>
          <div className="stat-card">
            <h3>Productivity Score</h3>
            <div className="value">{stats.productivityScore?.toFixed(1)}%</div>
            <div className="label">Efficiency</div>
          </div>
          <div className="stat-card">
            <h3>Distraction Time</h3>
            <div className="value">{formatDuration(stats.totalDistractedTimeSeconds)}</div>
            <div className="label">Today</div>
          </div>
          <div className="stat-card">
            <h3>Distraction Score</h3>
            <div className="value">{stats.distractionScore?.toFixed(1)}%</div>
            <div className="label">Needs Improvement</div>
          </div>
        </div>
      )}

      {/* --- Activities List --- */}
      {activeSession && activities.length > 0 && (
        <div className="card">
          <h2>Recent Activities</h2>
          <div className="activities-list">
            {/* Show last 10, reversed (newest first) */}
            {activities.slice(-10).reverse().map((activity) => (
              <div key={activity.id} className="activity-item">
                <div className="activity-info">
                  <strong>{activity.appName}</strong>
                  {/* Updated: Check for phoneDetected flag or category */}
                  <span className={`activity-type ${activity.category?.toLowerCase()}`}>
                    {activity.phoneDetected ? "PHONE" : activity.category}
                  </span>
                </div>
                <div className="activity-details">
                  <span>{activity.windowTitle}</span>
                  <span>{formatDuration(activity.durationSeconds)}</span>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}

export default Dashboard;