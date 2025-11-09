# FocusTrack Quick Start Guide

Get up and running with FocusTrack in 5 minutes!

## Prerequisites

- Java 17+
- Maven 3.6+
- Node.js 16+
- Python 3.8+ (optional, for activity monitoring)

## Step 1: Start the Backend

```bash
# Navigate to project root
cd focustrack

# Build and run
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

## Step 2: Start the Frontend

```bash
# In a new terminal, navigate to frontend directory
cd frontend

# Install dependencies (first time only)
npm install

# Start the development server
npm start
```

The frontend will open automatically at `http://localhost:3000`

## Step 3: Start a Focus Session

1. Open the browser at `http://localhost:3000`
2. Click **"Start Focus Session"** on the Dashboard
3. Your session is now active!

## Step 4: (Optional) Start Activity Monitoring

### Install Python Dependencies

```bash
cd monitoring
pip install -r requirements.txt
```

### Platform-Specific Setup

**Windows**:
```bash
pip install pywin32
```

**macOS**:
```bash
pip install pyobjc
```

**Linux**:
```bash
sudo apt-get install xdotool
```

### Get Session ID

1. Check the backend console logs for the session ID, OR
2. Use the API: `GET http://localhost:8080/api/sessions/active`

### Run the Monitor

```bash
python monitoring/activity_monitor.py --session-id 1 --api-url http://localhost:8080
```

Replace `1` with your actual session ID.

## Step 5: Use Pomodoro Timer

1. Navigate to the **"Pomodoro"** tab
2. Click **"Start"** to begin a 25-minute work session
3. Take breaks when the timer completes

## Step 6: View Reports

1. Navigate to the **"Reports"** tab
2. View your daily or weekly productivity report
3. See your focus time, productivity score, and top apps

## Troubleshooting

### Backend won't start
- Check if port 8080 is already in use
- Verify Java 17+ is installed: `java -version`
- Check Maven installation: `mvn -version`

### Frontend won't start
- Check if port 3000 is already in use
- Verify Node.js is installed: `node -version`
- Try deleting `node_modules` and reinstalling: `rm -rf node_modules && npm install`

### Monitor not working
- Verify the session ID is correct
- Check that the backend is running
- Ensure Python dependencies are installed
- Check platform-specific requirements (pywin32, PyObjC, xdotool)

### No activity data
- Ensure the monitor is running
- Check that the session is active
- Verify the monitor is sending data to the correct API URL

## Next Steps

- Read the [Full README](README.md) for detailed documentation
- Check the [API Documentation](API.md) for API endpoints
- Explore the [Monitoring Guide](monitoring/README.md) for advanced monitoring setup

## Need Help?

- Open an issue on GitHub
- Check the troubleshooting section in the main README
- Review the API documentation

## Quick Commands Reference

```bash
# Backend
mvn spring-boot:run                    # Start backend
mvn clean install                      # Build backend

# Frontend
npm start                              # Start frontend
npm install                            # Install dependencies
npm run build                          # Build for production

# Monitoring
python monitoring/activity_monitor.py --session-id 1  # Start monitor
pip install -r monitoring/requirements.txt            # Install Python deps

# Database
# Access H2 console at http://localhost:8080/h2-console
# JDBC URL: jdbc:h2:file:./data/focustrack
# Username: sa
# Password: (empty)
```

## Example Workflow

1. **Start Backend**: `mvn spring-boot:run`
2. **Start Frontend**: `npm start` (in frontend directory)
3. **Start Session**: Click "Start Focus Session" in the web UI
4. **Start Monitor**: `python monitoring/activity_monitor.py --session-id 1`
5. **Work**: Use your computer normally
6. **View Report**: Check the Reports tab for productivity insights
7. **End Session**: Click "End Session" when done

That's it! You're now tracking your productivity with FocusTrack! 🎉

