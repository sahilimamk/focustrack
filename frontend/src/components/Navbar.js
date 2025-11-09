import React from 'react';
import './Navbar.css';

function Navbar({ activeTab, setActiveTab }) {
  return (
    <nav className="navbar">
      <div className="navbar-brand">
        <h1>🧠 FocusTrack</h1>
      </div>
      <div className="navbar-tabs">
        <button
          className={activeTab === 'dashboard' ? 'active' : ''}
          onClick={() => setActiveTab('dashboard')}
        >
          Dashboard
        </button>
        <button
          className={activeTab === 'pomodoro' ? 'active' : ''}
          onClick={() => setActiveTab('pomodoro')}
        >
          Pomodoro
        </button>
        <button
          className={activeTab === 'reports' ? 'active' : ''}
          onClick={() => setActiveTab('reports')}
        >
          Reports
        </button>
      </div>
    </nav>
  );
}

export default Navbar;

