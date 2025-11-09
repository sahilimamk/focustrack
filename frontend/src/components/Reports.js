import React, { useState, useEffect } from 'react';
import './Reports.css';
import axios from 'axios';
import {
  PieChart,
  Pie,
  Cell,
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer
} from 'recharts';

const API_BASE_URL = 'http://localhost:8080/api';

const COLORS = ['#667eea', '#764ba2', '#f093fb', '#4facfe', '#00f2fe'];

function Reports() {
  const [report, setReport] = useState(null);
  const [loading, setLoading] = useState(true);
  const [reportType, setReportType] = useState('daily');

  useEffect(() => {
    fetchReport();
  }, [reportType]);

  const fetchReport = async () => {
    setLoading(true);
    try {
      let response;
      if (reportType === 'daily') {
        response = await axios.get(`${API_BASE_URL}/reports/daily`);
      } else {
        response = await axios.get(`${API_BASE_URL}/reports/weekly`);
      }
      setReport(response.data);
    } catch (error) {
      console.error('Error fetching report:', error);
    } finally {
      setLoading(false);
    }
  };

  const formatDuration = (seconds) => {
    if (!seconds) return '0h 0m';
    const hours = Math.floor(seconds / 3600);
    const minutes = Math.floor((seconds % 3600) / 60);
    return `${hours}h ${minutes}m`;
  };

  if (loading) {
    return <div className="loading">Loading report...</div>;
  }

  if (!report) {
    return <div className="error">No report data available</div>;
  }

  const pieData = [
    { name: 'Productive', value: report.totalFocusTimeSeconds || 0 },
    { name: 'Distracting', value: report.totalDistractedTimeSeconds || 0 },
    { name: 'Neutral', value: report.totalNeutralTimeSeconds || 0 }
  ];

  const topAppsData = (report.topApps || []).slice(0, 5).map(app => ({
    name: app.appName.length > 15 ? app.appName.substring(0, 15) + '...' : app.appName,
    time: app.durationSeconds || 0,
    hours: ((app.durationSeconds || 0) / 3600).toFixed(1)
  }));

  return (
    <div className="reports">
      <div className="card">
        <div className="report-header">
          <h2>Productivity Report</h2>
          <div className="report-type-selector">
            <button
              className={reportType === 'daily' ? 'active' : ''}
              onClick={() => setReportType('daily')}
            >
              Daily
            </button>
            <button
              className={reportType === 'weekly' ? 'active' : ''}
              onClick={() => setReportType('weekly')}
            >
              Weekly
            </button>
          </div>
        </div>

        <div className="report-stats">
          <div className="stat-item">
            <label>Productivity Score</label>
            <value>{report.productivityScore?.toFixed(1)}%</value>
          </div>
          <div className="stat-item">
            <label>Distraction Score</label>
            <value>{report.distractionScore?.toFixed(1)}%</value>
          </div>
          <div className="stat-item">
            <label>Focus Time</label>
            <value>{formatDuration(report.totalFocusTimeSeconds)}</value>
          </div>
          <div className="stat-item">
            <label>Consistency</label>
            <value>{report.consistencyRating || 0} sessions/day</value>
          </div>
        </div>
      </div>

      <div className="card">
        <h3>Time Distribution</h3>
        <ResponsiveContainer width="100%" height={300}>
          <PieChart>
            <Pie
              data={pieData}
              cx="50%"
              cy="50%"
              labelLine={false}
              label={({ name, percent }) => `${name}: ${(percent * 100).toFixed(0)}%`}
              outerRadius={80}
              fill="#8884d8"
              dataKey="value"
            >
              {pieData.map((entry, index) => (
                <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
              ))}
            </Pie>
            <Tooltip />
          </PieChart>
        </ResponsiveContainer>
      </div>

      {topAppsData.length > 0 && (
        <div className="card">
          <h3>Top Apps by Usage Time</h3>
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={topAppsData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="name" />
              <YAxis />
              <Tooltip />
              <Legend />
              <Bar dataKey="hours" fill="#667eea" name="Hours" />
            </BarChart>
          </ResponsiveContainer>
        </div>
      )}

      {report.topDistractingApps && report.topDistractingApps.length > 0 && (
        <div className="card">
          <h3>Top Distracting Apps</h3>
          <div className="apps-list">
            {report.topDistractingApps.map((app, index) => (
              <div key={index} className="app-item distracting">
                <span className="app-name">{app.appName}</span>
                <span className="app-time">{formatDuration(app.durationSeconds)}</span>
                <span className="app-percentage">{app.percentage?.toFixed(1)}%</span>
              </div>
            ))}
          </div>
        </div>
      )}

      {report.topProductiveApps && report.topProductiveApps.length > 0 && (
        <div className="card">
          <h3>Top Productive Apps</h3>
          <div className="apps-list">
            {report.topProductiveApps.map((app, index) => (
              <div key={index} className="app-item productive">
                <span className="app-name">{app.appName}</span>
                <span className="app-time">{formatDuration(app.durationSeconds)}</span>
                <span className="app-percentage">{app.percentage?.toFixed(1)}%</span>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}

export default Reports;

