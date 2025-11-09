import React, { useState, useEffect } from 'react';
import './PomodoroTimer.css';
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

function PomodoroTimer({ activeSession, onSessionUpdate }) {
  const [timeLeft, setTimeLeft] = useState(25 * 60); // 25 minutes in seconds
  const [isRunning, setIsRunning] = useState(false);
  const [isBreak, setIsBreak] = useState(false);
  const [workDuration, setWorkDuration] = useState(25 * 60);
  const [breakDuration, setBreakDuration] = useState(5 * 60);
  const [completedPomodoros, setCompletedPomodoros] = useState(0);

  useEffect(() => {
    fetchDurations();
  }, []);

  useEffect(() => {
    let interval = null;
    if (isRunning && timeLeft > 0) {
      interval = setInterval(() => {
        setTimeLeft((prev) => prev - 1);
      }, 1000);
    } else if (timeLeft === 0) {
      handleTimerComplete();
    }
    return () => clearInterval(interval);
  }, [isRunning, timeLeft]);

  const fetchDurations = async () => {
    try {
      const response = await axios.get(`${API_BASE_URL}/pomodoro/durations`);
      setWorkDuration(response.data.workDuration);
      setBreakDuration(response.data.breakDuration);
      setTimeLeft(response.data.workDuration);
    } catch (error) {
      console.error('Error fetching durations:', error);
    }
  };

  const handleTimerComplete = async () => {
    setIsRunning(false);
    if (activeSession) {
      try {
        await axios.put(`${API_BASE_URL}/sessions/${activeSession.id}/end`);
      } catch (error) {
        console.error('Error ending session:', error);
      }
    }

    if (!isBreak) {
      setCompletedPomodoros((prev) => prev + 1);
      setIsBreak(true);
      setTimeLeft(breakDuration);
      // Start break session
      try {
        await axios.post(`${API_BASE_URL}/pomodoro/break`, null, {
          params: { longBreak: completedPomodoros > 0 && completedPomodoros % 4 === 0 }
        });
      } catch (error) {
        console.error('Error starting break:', error);
      }
    } else {
      setIsBreak(false);
      setTimeLeft(workDuration);
      // Start work session
      try {
        await axios.post(`${API_BASE_URL}/pomodoro/start`);
      } catch (error) {
        console.error('Error starting work session:', error);
      }
    }
    onSessionUpdate();
  };

  const startTimer = async () => {
    if (!activeSession && !isBreak) {
      try {
        await axios.post(`${API_BASE_URL}/pomodoro/start`);
        onSessionUpdate();
      } catch (error) {
        console.error('Error starting Pomodoro:', error);
        return;
      }
    }
    setIsRunning(true);
  };

  const pauseTimer = () => {
    setIsRunning(false);
    if (activeSession) {
      axios.put(`${API_BASE_URL}/sessions/${activeSession.id}/pause`);
    }
  };

  const resetTimer = () => {
    setIsRunning(false);
    setTimeLeft(isBreak ? breakDuration : workDuration);
    if (activeSession) {
      axios.put(`${API_BASE_URL}/sessions/${activeSession.id}/end`);
      onSessionUpdate();
    }
  };

  const formatTime = (seconds) => {
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
  };

  return (
    <div className="pomodoro-timer">
      <div className="card">
        <h2>Pomodoro Timer</h2>
        <div className="pomodoro-info">
          <div className="completed-count">
            Completed: {completedPomodoros} Pomodoros
          </div>
          <div className={`timer-mode ${isBreak ? 'break' : 'work'}`}>
            {isBreak ? '☕ Break Time' : '🍅 Focus Time'}
          </div>
        </div>
        <div className="timer-display">{formatTime(timeLeft)}</div>
        <div className="timer-controls">
          {!isRunning ? (
            <button className="button button-primary" onClick={startTimer}>
              Start
            </button>
          ) : (
            <button className="button button-secondary" onClick={pauseTimer}>
              Pause
            </button>
          )}
          <button className="button button-danger" onClick={resetTimer}>
            Reset
          </button>
        </div>
        <div className="pomodoro-tips">
          <h3>Pomodoro Technique Tips:</h3>
          <ul>
            <li>Work for 25 minutes, then take a 5-minute break</li>
            <li>After 4 Pomodoros, take a longer 15-minute break</li>
            <li>Focus on one task at a time</li>
            <li>Track your progress to improve productivity</li>
          </ul>
        </div>
      </div>
    </div>
  );
}

export default PomodoroTimer;

