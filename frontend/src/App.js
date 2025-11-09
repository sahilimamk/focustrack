import React, { useState, useEffect } from 'react';
import './App.css';
import Dashboard from './components/Dashboard';
import PomodoroTimer from './components/PomodoroTimer';
import Reports from './components/Reports';
import Navbar from './components/Navbar';

function App() {
  const [activeTab, setActiveTab] = useState('dashboard');
  const [activeSession, setActiveSession] = useState(null);

  useEffect(() => {
    fetchActiveSession();
    const interval = setInterval(fetchActiveSession, 5000); // Poll every 5 seconds
    return () => clearInterval(interval);
  }, []);

  const fetchActiveSession = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/sessions/active');
      if (response.ok) {
        const session = await response.json();
        setActiveSession(session);
      } else {
        setActiveSession(null);
      }
    } catch (error) {
      console.error('Error fetching active session:', error);
    }
  };

  return (
    <div className="app">
      <Navbar activeTab={activeTab} setActiveTab={setActiveTab} />
      <div className="container">
        {activeTab === 'dashboard' && (
          <Dashboard activeSession={activeSession} onSessionUpdate={fetchActiveSession} />
        )}
        {activeTab === 'pomodoro' && (
          <PomodoroTimer activeSession={activeSession} onSessionUpdate={fetchActiveSession} />
        )}
        {activeTab === 'reports' && <Reports />}
      </div>
    </div>
  );
}

export default App;

