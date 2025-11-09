import React, { useState, useEffect } from 'react';
import './Dashboard.css';
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

function Dashboard({ activeSession, onSessionUpdate }) {
  const [sessionName, setSessionName] = useState('');
  const [activities, setActivities] = useState([]);
  const [stats, setStats] = useState(null);

  useEffect(() => {
    if (activeSession) {
      fetchActivities();
      fetchStats();
    }
  }, [activeSession]);

  const fetchActivities = async () => {
    if (!activeSession) return;
    try {
      const response = await axios.get(`${API_BASE_URL}/sessions/${activeSession.id}`);
      setActivities(response.data.activities || []);
    } catch (error) {
      console.error('Error fetching activities:', error);
    }
  };

  const fetchStats = async () => {
    try {
      const response = await axios.get(`${API_BASE_URL}/reports/daily`);
      setStats(response.data);
    } catch (error) {
      console.error('Error fetching stats:', error);
    }
  };

  const startSession = async () => {
    try {
      await axios.post(`${API_BASE_URL}/sessions`, null, {
        params: { sessionName: sessionName || 'Focus Session' }
      });
      onSessionUpdate();
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
      onSessionUpdate();
      setActivities([]);
    } catch (error) {
      console.error('Error ending session:', error);
      alert('Failed to end session');
    }
  };

  const pauseSession = async () => {
    if (!activeSession) return;
    try {
      await axios.put(`${API_BASE_URL}/sessions/${activeSession.id}/pause`);
      onSessionUpdate();
    } catch (error) {
      console.error('Error pausing session:', error);
    }
  };

  const resumeSession = async () => {
    if (!activeSession) return;
    try {
      await axios.put(`${API_BASE_URL}/sessions/${activeSession.id}/resume`);
      onSessionUpdate();
    } catch (error) {
      console.error('Error resuming session:', error);
    }
  };

  const formatDuration = (seconds) => {
    if (!seconds) return '0s';
    const hours = Math.floor(seconds / 3600);
    const minutes = Math.floor((seconds % 3600) / 60);
    const secs = seconds % 60;
    
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
              <span className={`session-status ${activeSession.status.toLowerCase()}`}>
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
          </div>
        )}
      </div>

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

      {activeSession && activities.length > 0 && (
        <div className="card">
          <h2>Recent Activities</h2>
          <div className="activities-list">
            {activities.slice(-10).reverse().map((activity) => (
              <div key={activity.id} className="activity-item">
                <div className="activity-info">
                  <strong>{activity.appName}</strong>
                  <span className={`activity-type ${activity.type?.toLowerCase()}`}>
                    {activity.type}
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

