# FocusTrack - Project Summary

## Project Overview

**FocusTrack** is an intelligent productivity monitoring system that tracks user activity in real-time, analyzes where attention is spent, and provides data-driven productivity insights. Unlike traditional Pomodoro timers, FocusTrack monitors actual user engagement rather than just time intervals.

## Key Features

### 1. Session Management
- Start, pause, resume, and end focus sessions
- Track session duration and status
- Support for different session types (Focus, Pomodoro Work, Pomodoro Break)

### 2. Activity Tracking
- Monitor active applications and browser tabs
- Automatically classify activities as productive, distracting, or neutral
- Track duration spent on each activity
- Real-time activity updates via Python monitoring script

### 3. Pomodoro Timer
- 25-minute work sessions
- 5-minute short breaks
- 15-minute long breaks (after 4 Pomodoros)
- Automatic session creation and tracking

### 4. Productivity Reports
- Daily and weekly reports
- Productivity score calculation
- Distraction score analysis
- Top apps by usage time
- Consistency rating (sessions per day)
- Visual charts and graphs

### 5. Privacy-First Design
- All data stored locally (H2 database)
- No cloud synchronization
- No data sharing with third parties
- User controls all data

## Technology Stack

### Backend
- **Spring Boot 3.2.0**: REST API framework
- **Spring Data JPA**: Database abstraction layer
- **H2 Database**: Embedded database for local storage
- **Lombok**: Reduces boilerplate code
- **Maven**: Build and dependency management

### Frontend
- **React 18.2.0**: UI framework
- **Axios**: HTTP client for API communication
- **Recharts**: Data visualization library
- **CSS3**: Modern styling

### Monitoring
- **Python 3**: Activity monitoring scripts
- **psutil**: Cross-platform process and system utilities
- **requests**: HTTP client for API communication
- **Platform-specific libraries**: pywin32 (Windows), PyObjC (macOS), xdotool (Linux)

## Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    React Frontend                        │
│                    (Port 3000)                          │
│  - Dashboard                                             │
│  - Pomodoro Timer                                        │
│  - Reports                                               │
└────────────────────┬────────────────────────────────────┘
                     │ REST API
                     ▼
┌─────────────────────────────────────────────────────────┐
│               Spring Boot Backend                        │
│                   (Port 8080)                           │
│  - Session Controller                                    │
│  - Activity Controller                                   │
│  - Pomodoro Controller                                   │
│  - Report Controller                                     │
│  - Session Service                                       │
│  - Report Service                                        │
│  - Monitoring Service                                    │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│                   H2 Database                            │
│              (Local File Storage)                        │
│  - Sessions Table                                        │
│  - Activities Table                                      │
│  - Focus Entries Table                                   │
└─────────────────────────────────────────────────────────┘

                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│              Python Monitoring Script                    │
│                 (Background Process)                     │
│  - Active Window Detection                               │
│  - Activity Tracking                                     │
│  - API Communication                                     │
└─────────────────────────────────────────────────────────┘
```

## Database Schema

### Sessions Table
- `id`: Primary key
- `session_name`: Session name
- `start_time`: Session start timestamp
- `end_time`: Session end timestamp
- `status`: Session status (ACTIVE, PAUSED, COMPLETED)
- `type`: Session type (FOCUS, POMODORO_WORK, POMODORO_BREAK)
- `total_duration_seconds`: Total session duration
- `focused_duration_seconds`: Focused time duration
- `distracted_duration_seconds`: Distracted time duration
- `created_at`: Creation timestamp

### Activities Table
- `id`: Primary key
- `session_id`: Foreign key to sessions table
- `app_name`: Application name
- `window_title`: Window/tab title
- `start_time`: Activity start timestamp
- `end_time`: Activity end timestamp
- `type`: Activity type (PRODUCTIVE, DISTRACTING, NEUTRAL)
- `duration_seconds`: Activity duration
- `created_at`: Creation timestamp

### Focus Entries Table
- `id`: Primary key
- `session_id`: Foreign key to sessions table
- `timestamp`: Focus entry timestamp
- `is_focused`: Focus status (true/false)
- `notes`: Optional notes
- `created_at`: Creation timestamp

## API Endpoints

### Sessions
- `POST /api/sessions` - Create session
- `GET /api/sessions/active` - Get active session
- `GET /api/sessions` - Get all sessions
- `GET /api/sessions/{id}` - Get session by ID
- `PUT /api/sessions/{id}/pause` - Pause session
- `PUT /api/sessions/{id}/resume` - Resume session
- `PUT /api/sessions/{id}/end` - End session

### Activities
- `POST /api/activities/session/{sessionId}` - Add activity
- `PUT /api/activities/{id}/end` - End activity

### Pomodoro
- `POST /api/pomodoro/start` - Start Pomodoro work session
- `POST /api/pomodoro/break` - Start Pomodoro break
- `GET /api/pomodoro/durations` - Get Pomodoro durations

### Reports
- `GET /api/reports/daily` - Get daily report
- `GET /api/reports/weekly` - Get weekly report
- `GET /api/reports/custom` - Get custom date range report

## Activity Classification

### Productive Apps
- Code editors (VS Code, IntelliJ, Eclipse)
- Documentation sites (GitHub, Stack Overflow)
- Note-taking apps (Notion, Obsidian)
- Office apps (Word, Excel, PowerPoint)
- PDF readers

### Distracting Apps
- Social media (YouTube, Instagram, Facebook, Twitter, TikTok)
- Entertainment (Netflix, Reddit)
- Gaming apps

### Neutral Apps
- Default classification for apps that don't fall into either category

## Installation & Setup

### Backend Setup
```bash
mvn clean install
mvn spring-boot:run
```

### Frontend Setup
```bash
cd frontend
npm install
npm start
```

### Monitoring Setup
```bash
cd monitoring
pip install -r requirements.txt
python activity_monitor.py --session-id <SESSION_ID>
```

## Usage Workflow

1. **Start Backend**: Run Spring Boot application
2. **Start Frontend**: Run React development server
3. **Start Session**: Create a focus session via web UI
4. **Start Monitor**: Run Python monitoring script with session ID
5. **Work**: Use computer normally - activities are tracked automatically
6. **View Reports**: Check productivity reports in the web UI
7. **End Session**: End session when done

## Key Metrics

### Productivity Score
- Calculated as: (Focused Time / Total Time) × 100
- Represents percentage of time spent on productive activities

### Distraction Score
- Calculated as: (Distracted Time / Total Time) × 100
- Represents percentage of time spent on distracting activities

### Consistency Rating
- Average number of sessions per day
- Indicates how consistently the user tracks their productivity

## Future Enhancements

### Planned Features
- AI-based attention score using webcam data
- Cloud sync across devices
- Browser extension for precise tab tracking
- Smart focus suggestions
- Calendar integration (Google Calendar, Notion)
- Mobile app (iOS and Android)
- Team features (shared goals, leaderboards)
- Export reports (PDF/CSV)
- Custom activity categories
- Focus zones (time-based)

### Technical Improvements
- Native monitoring using Java Native Access (JNA)
- WebSocket support for real-time updates
- Database migration to PostgreSQL/MySQL
- User authentication and multi-user support
- API rate limiting
- Enhanced logging and monitoring
- Comprehensive testing (unit and integration)
- Docker containerization

## Project Structure

```
focustrack/
├── src/main/java/com/focustrack/
│   ├── config/          # Configuration classes
│   ├── controller/      # REST controllers
│   ├── dto/             # Data Transfer Objects
│   ├── model/           # Entity models
│   ├── repository/      # Data repositories
│   ├── service/         # Business logic
│   └── FocusTrackApplication.java
├── src/main/resources/
│   └── application.properties
├── frontend/
│   ├── src/
│   │   ├── components/  # React components
│   │   ├── App.js
│   │   └── index.js
│   └── package.json
├── monitoring/
│   ├── activity_monitor.py
│   ├── requirements.txt
│   └── README.md
├── pom.xml
├── README.md
├── API.md
├── QUICKSTART.md
└── PROJECT_SUMMARY.md
```

## Testing

### Backend Testing
- Unit tests for services
- Integration tests for controllers
- Repository tests

### Frontend Testing
- Component tests
- Integration tests
- E2E tests

### API Testing
- Postman collection
- cURL examples
- API documentation

## Deployment

### Development
- Backend: `mvn spring-boot:run`
- Frontend: `npm start`
- Database: H2 (embedded)

### Production
- Backend: Spring Boot JAR file
- Frontend: Built React app (static files)
- Database: H2 (file-based) or PostgreSQL/MySQL
- Monitoring: Python script as system service

## Security Considerations

- No authentication required (MVP)
- All data stored locally
- No sensitive data collection
- CORS enabled for localhost only
- Input validation on API endpoints

## Performance Considerations

- H2 database for fast local storage
- Efficient activity tracking (polling every 5 seconds)
- Optimized database queries
- React component optimization
- Chart rendering optimization

## Limitations

### Current Limitations
- No user authentication
- Single-user only
- No cloud sync
- Basic activity classification
- No webcam/eye-tracking
- No browser extension
- Limited to desktop platforms

### Future Improvements
- Multi-user support
- Cloud synchronization
- Advanced AI classification
- Webcam integration
- Browser extension
- Mobile app support

## Conclusion

FocusTrack is a comprehensive productivity monitoring system that provides real-time activity tracking, productivity insights, and Pomodoro timer functionality. The system is designed with privacy in mind, storing all data locally and providing users with complete control over their data.

The project is suitable for:
- College end-semester projects
- Portfolio projects
- Personal productivity tracking
- Research on productivity patterns
- Learning Spring Boot and React

## Contact & Support

For questions, issues, or contributions, please:
- Open an issue on GitHub
- Contact the project maintainers
- Review the documentation (README.md, API.md, QUICKSTART.md)

---

**Project Status**: MVP Complete ✅
**Version**: 1.0.0
**Last Updated**: 2024-01-15

