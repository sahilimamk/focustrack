# 🧠 FocusTrack – Intelligent Pomodoro & Activity Monitoring System

> **A privacy-focused productivity tracker that not only times your focus — but truly knows where your attention goes.**

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18.2.0-blue.svg)](https://reactjs.org/)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Architecture](#-architecture)
- [Prerequisites](#-prerequisites)
- [Installation](#-installation)
- [Usage](#-usage)
- [API Documentation](#-api-documentation)
- [Project Structure](#-project-structure)
- [Monitoring Setup](#-monitoring-setup)
- [Screenshots](#-screenshots)
- [Future Enhancements](#-future-enhancements)
- [Contributing](#-contributing)
- [License](#-license)

---

## 🎯 Overview

FocusTrack is a modern productivity monitoring system that tracks **actual user engagement** rather than just time intervals. Unlike traditional Pomodoro timers, FocusTrack:

- ✅ **Tracks Real Activity**: Monitors which applications and websites you're using
- ✅ **Detects Distractions**: Automatically categorizes activities as productive, distracting, or neutral
- ✅ **Provides Insights**: Generates daily/weekly reports with productivity scores
- ✅ **Privacy-First**: All data stored locally on your machine
- ✅ **Pomodoro Integration**: Enhanced Pomodoro timer with activity tracking

---

## ✨ Features

### Core Features

- **🎯 Session Management**: Start, pause, resume, and end focus sessions
- **⏱️ Pomodoro Timer**: 25-minute work sessions with 5-minute breaks (15-minute long breaks after 4 Pomodoros)
- **📊 Activity Tracking**: Monitors active applications and browser tabs in real-time
- **📈 Productivity Reports**: Daily and weekly reports with:
  - Total focused time
  - Productivity score
  - Distraction score
  - Top apps (productive and distracting)
  - Consistency rating
- **🎨 Modern Dashboard**: React-based UI with real-time stats and visualizations
- **💾 Local Storage**: H2 database for local data storage (privacy-focused)

### Advanced Features (MVP)

- **🔄 Real-time Monitoring**: Native Python script for activity tracking (Windows/macOS/Linux)
- **📱 Cross-Platform**: Works on Windows, macOS, and Linux
- **🔍 Activity Classification**: Automatically categorizes apps as productive/distracting/neutral
- **📉 Progress Visualization**: Charts and graphs for productivity trends

---

## 🛠️ Tech Stack

### Backend
- **Spring Boot 3.2.0**: REST API framework
- **Spring Data JPA**: Database abstraction
- **H2 Database**: Embedded database for local storage
- **Lombok**: Reduces boilerplate code
- **Maven**: Build and dependency management

### Frontend
- **React 18.2.0**: UI framework
- **Axios**: HTTP client
- **Recharts**: Data visualization
- **CSS3**: Modern styling

### Monitoring
- **Python 3**: Activity monitoring scripts
- **psutil**: Process and system utilities
- **requests**: HTTP client for API communication
- **Platform-specific**: pywin32 (Windows), PyObjC (macOS), xdotool (Linux)

---

## 🏗️ Architecture

```
┌─────────────────┐
│   React Frontend │
│   (Port 3000)    │
└────────┬─────────┘
         │ REST API
         ▼
┌─────────────────┐
│  Spring Boot    │
│  Backend API    │
│  (Port 8080)    │
└────────┬─────────┘
         │
         ▼
┌─────────────────┐
│   H2 Database   │
│  (Local Storage)│
└─────────────────┘

         │
         ▼
┌─────────────────┐
│ Python Monitor  │
│  (Background)   │
└─────────────────┘
```

### Components

1. **Frontend (React)**: User interface for session management, Pomodoro timer, and reports
2. **Backend (Spring Boot)**: REST API for session management, activity tracking, and reporting
3. **Database (H2)**: Local storage for sessions, activities, and reports
4. **Monitor (Python)**: Native script that tracks active applications and sends data to the backend

---

## 📦 Prerequisites

### Required

- **Java 17+**: [Download Java](https://www.oracle.com/java/technologies/downloads/)
- **Maven 3.6+**: [Download Maven](https://maven.apache.org/download.cgi)
- **Node.js 16+**: [Download Node.js](https://nodejs.org/)
- **npm or yarn**: Comes with Node.js

### Optional (for Monitoring)

- **Python 3.8+**: [Download Python](https://www.python.org/downloads/)
- **pip**: Python package manager

---

## 🚀 Installation

### 1. Clone the Repository

```bash
git clone <repository-url>
cd focustrack
```

### 2. Backend Setup

```bash
# Navigate to project root
cd focustrack

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

### 3. Frontend Setup

```bash
# Navigate to frontend directory
cd frontend

# Install dependencies
npm install

# Start the development server
npm start
```

The frontend will start on `http://localhost:3000`

### 4. Database

The H2 database is automatically created in the `data/` directory. You can access the H2 console at:
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:file:./data/focustrack`
- Username: `sa`
- Password: (empty)

### 5. Monitoring Setup (Optional)

See [Monitoring Setup](#-monitoring-setup) for detailed instructions.

---

## 💻 Usage

### Starting a Focus Session

1. Open the FocusTrack dashboard at `http://localhost:3000`
2. Click **"Start Focus Session"** on the Dashboard tab
3. Optionally enter a session name
4. The session will start tracking your activity

### Using Pomodoro Timer

1. Navigate to the **"Pomodoro"** tab
2. Click **"Start"** to begin a 25-minute work session
3. When the timer completes, take a 5-minute break
4. After 4 Pomodoros, take a 15-minute long break

### Viewing Reports

1. Navigate to the **"Reports"** tab
2. View daily or weekly productivity reports
3. See your:
   - Focus time
   - Productivity score
   - Top apps
   - Distraction analysis

### Activity Monitoring

1. Start a focus session
2. Note the session ID from the backend
3. Run the Python monitor:
   ```bash
   python monitoring/activity_monitor.py --session-id <SESSION_ID>
   ```
4. The monitor will automatically track your activity and send updates to the backend

---

## 📚 API Documentation

### Sessions

#### Create Session
```http
POST /api/sessions
Content-Type: application/json

Parameters:
- sessionName (optional): Name of the session
- type (optional): Session type (FOCUS, POMODORO_WORK, POMODORO_BREAK)
```

#### Get Active Session
```http
GET /api/sessions/active
```

#### Get All Sessions
```http
GET /api/sessions
```

#### Get Session by ID
```http
GET /api/sessions/{id}
```

#### Pause Session
```http
PUT /api/sessions/{id}/pause
```

#### Resume Session
```http
PUT /api/sessions/{id}/resume
```

#### End Session
```http
PUT /api/sessions/{id}/end
```

### Activities

#### Add Activity
```http
POST /api/activities/session/{sessionId}
Content-Type: application/json

Body:
{
  "appName": "Chrome",
  "windowTitle": "YouTube - Google Chrome"
}
```

#### End Activity
```http
PUT /api/activities/{id}/end
```

### Pomodoro

#### Start Pomodoro Work Session
```http
POST /api/pomodoro/start?sessionName=OptionalName
```

#### Start Pomodoro Break
```http
POST /api/pomodoro/break?longBreak=false
```

#### Get Pomodoro Durations
```http
GET /api/pomodoro/durations
```

### Reports

#### Get Daily Report
```http
GET /api/reports/daily?date=2024-01-01
```

#### Get Weekly Report
```http
GET /api/reports/weekly?startDate=2024-01-01
```

#### Get Custom Report
```http
GET /api/reports/custom?startDate=2024-01-01T00:00:00&endDate=2024-01-07T23:59:59
```

---

## 📁 Project Structure

```
focustrack/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── focustrack/
│       │           ├── config/          # Configuration classes
│       │           ├── controller/      # REST controllers
│       │           ├── dto/             # Data Transfer Objects
│       │           ├── model/           # Entity models
│       │           ├── repository/      # Data repositories
│       │           ├── service/         # Business logic
│       │           └── FocusTrackApplication.java
│       └── resources/
│           └── application.properties   # Application configuration
├── frontend/
│   ├── public/
│   ├── src/
│   │   ├── components/                  # React components
│   │   ├── App.js
│   │   └── index.js
│   └── package.json
├── monitoring/
│   ├── activity_monitor.py              # Python monitoring script
│   ├── requirements.txt                 # Python dependencies
│   └── README.md                        # Monitoring setup guide
├── pom.xml                              # Maven configuration
├── .gitignore
└── README.md                            # This file
```

---

## 🔍 Monitoring Setup

### Step 1: Install Python Dependencies

```bash
cd monitoring
pip install -r requirements.txt
```

### Step 2: Platform-Specific Setup

#### Windows
```bash
pip install pywin32
```

#### macOS
```bash
pip install pyobjc
```

#### Linux
```bash
sudo apt-get install xdotool  # Ubuntu/Debian
```

### Step 3: Run the Monitor

1. Start a focus session via the web UI
2. Note the session ID from the backend logs or API response
3. Run the monitor:
   ```bash
   python monitoring/activity_monitor.py --session-id <SESSION_ID> --api-url http://localhost:8080
   ```

### Step 4: Verify Monitoring

- Check the backend logs for activity updates
- View the Dashboard to see tracked activities
- Check the Reports tab for productivity insights

For more details, see [monitoring/README.md](monitoring/README.md).

---

## 📊 Example User Flow

### Scenario: Sahil's Focus Session

1. **Start Session**: Sahil opens FocusTrack and starts a focus session
2. **Work on VS Code**: Spends 50 minutes coding
3. **Take Break**: Takes a 5-minute Pomodoro break
4. **Switch to YouTube**: Watches YouTube for 15 minutes (tracked as distracting)
5. **Return to Study**: Goes back to VS Code for another hour
6. **End Session**: Ends the session and views the report

### Report Generated:

- **Total Focus Time**: 3.5 hours
- **YouTube Time**: 1.2 hours
- **Productivity Score**: 75%
- **Top Apps**: VS Code (3.5h), YouTube (1.2h)
- **Suggestion**: "Limit YouTube under 30 mins tomorrow"

---

## 🎨 Screenshots

### Dashboard
![Dashboard](screenshots/dashboard.png)

### Pomodoro Timer
![Pomodoro](screenshots/pomodoro.png)

### Reports
![Reports](screenshots/reports.png)

---

## 🚀 Future Enhancements

### Planned Features

- [ ] **AI-Based Attention Score**: Webcam integration for eye-tracking
- [ ] **Cloud Sync**: Optional cloud sync across devices
- [ ] **Browser Extension**: Precise tab tracking for browsers
- [ ] **Smart Suggestions**: AI-powered productivity recommendations
- [ ] **Calendar Integration**: Integration with Google Calendar and Notion
- [ ] **Mobile App**: iOS and Android apps for tracking on the go
- [ ] **Team Features**: Shared productivity goals and leaderboards
- [ ] **Export Reports**: PDF/CSV export for reports
- [ ] **Custom Categories**: User-defined productive/distracting apps
- [ ] **Focus Zones**: Define time-based focus zones

### Technical Improvements

- [ ] **Native Monitoring**: Java Native Access (JNA) for better performance
- [ ] **Real-time Updates**: WebSocket support for live activity updates
- [ ] **Database Migration**: Support for PostgreSQL/MySQL
- [ ] **Authentication**: User authentication and multi-user support
- [ ] **API Rate Limiting**: Rate limiting for API endpoints
- [ ] **Logging**: Enhanced logging and monitoring
- [ ] **Testing**: Comprehensive unit and integration tests
- [ ] **Docker**: Docker containerization for easy deployment

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Development Guidelines

- Follow Java and React coding conventions
- Write meaningful commit messages
- Add tests for new features
- Update documentation as needed
- Ensure all tests pass before submitting

---

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🙏 Acknowledgments

- **Spring Boot**: For the excellent framework
- **React**: For the powerful UI library
- **psutil**: For cross-platform process monitoring
- **H2 Database**: For lightweight local storage

---

## 📧 Contact

For questions or support, please open an issue on GitHub or contact the project maintainers.

---

## 🎓 Project Information

**Project Title**: FocusTrack – Intelligent Pomodoro & Activity Monitoring System

**Problem Statement**: In an era of digital distraction, maintaining focus while working or studying has become increasingly difficult. Traditional Pomodoro timers only track time intervals, not actual productivity or user engagement.

**Solution**: FocusTrack is a modern focus-monitoring system that tracks user activity in real-time, analyzes where their attention is spent, and provides data-driven productivity insights — all while keeping user data stored locally for privacy.

**Technology Stack**: Spring Boot, React, H2 Database, Python, psutil

**Key Features**:
- Real-time activity monitoring
- Pomodoro timer integration
- Productivity analytics and reporting
- Privacy-first local storage
- Cross-platform support

---

**Made with ❤️ for productivity enthusiasts**

