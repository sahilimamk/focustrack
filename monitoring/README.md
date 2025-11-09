# FocusTrack Activity Monitor

This directory contains native monitoring scripts that track user activity and send data to the FocusTrack backend.

## Overview

The activity monitor tracks:
- **Active Application**: Which application is currently in focus (e.g., Chrome, VS Code)
- **Window Title**: The title of the active window/tab (e.g., "YouTube - Google Chrome")
- **Activity Duration**: How long each app/window is active

## Setup

### 1. Install Python Dependencies

```bash
pip install -r requirements.txt
```

### 2. Platform-Specific Setup

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
# Install xdotool (optional, for better window detection)
sudo apt-get install xdotool  # Ubuntu/Debian
sudo yum install xdotool      # RHEL/CentOS
```

## Usage

### Step 1: Start a Focus Session

First, start a session using the FocusTrack API or web interface. Note the session ID.

### Step 2: Run the Monitor

```bash
python activity_monitor.py --session-id <SESSION_ID> --api-url http://localhost:8080
```

Example:
```bash
python activity_monitor.py --session-id 1 --api-url http://localhost:8080 --poll-interval 5
```

### Parameters

- `--session-id` (required): The session ID from the FocusTrack backend
- `--api-url` (optional): Backend API URL (default: http://localhost:8080)
- `--poll-interval` (optional): How often to check for activity changes in seconds (default: 5)

## How It Works

1. The monitor polls the system every N seconds (default: 5) to detect the active window
2. When the active application or window title changes, it sends a POST request to:
   ```
   POST /api/activities/session/{sessionId}
   Body: {
     "appName": "Chrome",
     "windowTitle": "YouTube - Google Chrome"
   }
   ```
3. The backend automatically:
   - Ends the previous activity
   - Creates a new activity record
   - Calculates duration and activity type (productive/distracting/neutral)

## Integration with FocusTrack

The monitor works seamlessly with the FocusTrack backend:

1. **Start Session**: User starts a focus session via the web UI
2. **Start Monitor**: Run the Python script with the session ID
3. **Track Activity**: Monitor automatically sends activity updates
4. **View Reports**: Check the FocusTrack dashboard for productivity insights

## Future Enhancements

- **Webcam Integration**: Add eye-tracking for focus detection
- **Browser Extension**: More accurate tab tracking for browsers
- **Auto-start**: Automatically start monitoring when a session begins
- **Background Service**: Run as a system service/daemon
- **Multiple Monitor Support**: Track activity across multiple monitors

## Troubleshooting

### "psutil not installed"
```bash
pip install psutil
```

### "Cannot detect window title"
- **Windows**: Install `pywin32`
- **macOS**: Install `pyobjc`
- **Linux**: Install `xdotool` or run with appropriate permissions

### "Connection refused"
- Ensure the FocusTrack backend is running on the specified URL
- Check firewall settings
- Verify the session ID is correct

### "Permission denied" (Linux)
- Run with appropriate permissions
- Some systems require running as the same user as the desktop session

## Security Notes

- The monitor only tracks which applications are active
- No sensitive data (keystrokes, passwords, file contents) is captured
- All data is stored locally on your machine
- The monitor communicates only with your local FocusTrack backend

