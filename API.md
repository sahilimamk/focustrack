# FocusTrack API Documentation

## Base URL
```
http://localhost:8080/api
```

## Authentication
Currently, the API does not require authentication. Future versions may include user authentication.

---

## Sessions API

### Create Session
Create a new focus session.

**Endpoint**: `POST /api/sessions`

**Parameters**:
- `sessionName` (optional, query param): Name of the session
- `type` (optional, query param): Session type (`FOCUS`, `POMODORO_WORK`, `POMODORO_BREAK`)

**Example Request**:
```bash
curl -X POST "http://localhost:8080/api/sessions?sessionName=My%20Focus%20Session&type=FOCUS"
```

**Response** (201 Created):
```json
{
  "id": 1,
  "sessionName": "My Focus Session",
  "startTime": "2024-01-15T10:00:00",
  "endTime": null,
  "status": "ACTIVE",
  "type": "FOCUS",
  "totalDurationSeconds": null,
  "focusedDurationSeconds": null,
  "distractedDurationSeconds": null,
  "activities": []
}
```

---

### Get Active Session
Get the currently active session.

**Endpoint**: `GET /api/sessions/active`

**Response** (200 OK):
```json
{
  "id": 1,
  "sessionName": "My Focus Session",
  "startTime": "2024-01-15T10:00:00",
  "status": "ACTIVE",
  "type": "FOCUS",
  ...
}
```

**Response** (404 Not Found):
If no active session exists.

---

### Get All Sessions
Get all sessions (active, paused, and completed).

**Endpoint**: `GET /api/sessions`

**Response** (200 OK):
```json
[
  {
    "id": 1,
    "sessionName": "My Focus Session",
    "startTime": "2024-01-15T10:00:00",
    "status": "ACTIVE",
    ...
  },
  {
    "id": 2,
    "sessionName": "Previous Session",
    "startTime": "2024-01-14T09:00:00",
    "status": "COMPLETED",
    ...
  }
]
```

---

### Get Session by ID
Get a specific session by its ID.

**Endpoint**: `GET /api/sessions/{id}`

**Path Parameters**:
- `id` (required): Session ID

**Response** (200 OK):
```json
{
  "id": 1,
  "sessionName": "My Focus Session",
  "startTime": "2024-01-15T10:00:00",
  "status": "ACTIVE",
  "activities": [
    {
      "id": 1,
      "appName": "Chrome",
      "windowTitle": "VS Code - Google Chrome",
      "type": "PRODUCTIVE",
      "durationSeconds": 300
    }
  ],
  ...
}
```

---

### Pause Session
Pause an active session.

**Endpoint**: `PUT /api/sessions/{id}/pause`

**Path Parameters**:
- `id` (required): Session ID

**Response** (200 OK):
```json
{
  "id": 1,
  "status": "PAUSED",
  ...
}
```

---

### Resume Session
Resume a paused session.

**Endpoint**: `PUT /api/sessions/{id}/resume`

**Path Parameters**:
- `id` (required): Session ID

**Response** (200 OK):
```json
{
  "id": 1,
  "status": "ACTIVE",
  ...
}
```

---

### End Session
End a session (marks it as completed).

**Endpoint**: `PUT /api/sessions/{id}/end`

**Path Parameters**:
- `id` (required): Session ID

**Response** (200 OK):
```json
{
  "id": 1,
  "status": "COMPLETED",
  "endTime": "2024-01-15T11:00:00",
  "totalDurationSeconds": 3600,
  "focusedDurationSeconds": 3000,
  "distractedDurationSeconds": 600,
  ...
}
```

---

## Activities API

### Add Activity
Add an activity to a session. This automatically ends the previous activity.

**Endpoint**: `POST /api/activities/session/{sessionId}`

**Path Parameters**:
- `sessionId` (required): Session ID

**Request Body**:
```json
{
  "appName": "Chrome",
  "windowTitle": "YouTube - Google Chrome",
  "type": "DISTRACTING"
}
```

**Note**: The `type` field is optional and will be auto-determined if not provided.

**Response** (201 Created):
```json
{
  "id": 1,
  "sessionId": 1,
  "appName": "Chrome",
  "windowTitle": "YouTube - Google Chrome",
  "startTime": "2024-01-15T10:05:00",
  "endTime": null,
  "type": "DISTRACTING",
  "durationSeconds": null
}
```

---

### End Activity
End an activity (sets the end time and calculates duration).

**Endpoint**: `PUT /api/activities/{id}/end`

**Path Parameters**:
- `id` (required): Activity ID

**Response** (200 OK):
```json
{
  "id": 1,
  "endTime": "2024-01-15T10:10:00",
  "durationSeconds": 300
}
```

---

## Pomodoro API

### Start Pomodoro Work Session
Start a Pomodoro work session (25 minutes by default).

**Endpoint**: `POST /api/pomodoro/start`

**Parameters**:
- `sessionName` (optional, query param): Name of the session

**Example Request**:
```bash
curl -X POST "http://localhost:8080/api/pomodoro/start?sessionName=Pomodoro%20Session%201"
```

**Response** (200 OK):
```json
{
  "id": 1,
  "sessionName": "Pomodoro Session 1",
  "type": "POMODORO_WORK",
  "status": "ACTIVE",
  ...
}
```

---

### Start Pomodoro Break
Start a Pomodoro break session.

**Endpoint**: `POST /api/pomodoro/break`

**Parameters**:
- `longBreak` (optional, query param, default: false): Whether this is a long break (15 min) or short break (5 min)

**Example Request**:
```bash
curl -X POST "http://localhost:8080/api/pomodoro/break?longBreak=false"
```

**Response** (200 OK):
```json
{
  "id": 2,
  "sessionName": "Pomodoro Short Break",
  "type": "POMODORO_BREAK",
  "status": "ACTIVE",
  ...
}
```

---

### Get Pomodoro Durations
Get the default Pomodoro durations.

**Endpoint**: `GET /api/pomodoro/durations`

**Response** (200 OK):
```json
{
  "workDuration": 1500,
  "breakDuration": 300,
  "longBreakDuration": 900
}
```

**Note**: Durations are in seconds.
- Work: 1500 seconds (25 minutes)
- Break: 300 seconds (5 minutes)
- Long Break: 900 seconds (15 minutes)

---

## Reports API

### Get Daily Report
Get a productivity report for a specific day.

**Endpoint**: `GET /api/reports/daily`

**Parameters**:
- `date` (optional, query param): Date in ISO format (YYYY-MM-DD). Defaults to today.

**Example Request**:
```bash
curl -X GET "http://localhost:8080/api/reports/daily?date=2024-01-15"
```

**Response** (200 OK):
```json
{
  "reportDate": "2024-01-15T12:00:00",
  "totalFocusTimeSeconds": 10800,
  "totalDistractedTimeSeconds": 3600,
  "totalNeutralTimeSeconds": 1800,
  "productivityScore": 66.67,
  "distractionScore": 22.22,
  "consistencyRating": 3,
  "topApps": [
    {
      "appName": "VS Code",
      "durationSeconds": 7200,
      "percentage": 44.44
    },
    {
      "appName": "Chrome",
      "durationSeconds": 5400,
      "percentage": 33.33
    }
  ],
  "topDistractingApps": [
    {
      "appName": "YouTube",
      "durationSeconds": 3600,
      "percentage": 22.22
    }
  ],
  "topProductiveApps": [
    {
      "appName": "VS Code",
      "durationSeconds": 7200,
      "percentage": 44.44
    }
  ]
}
```

---

### Get Weekly Report
Get a productivity report for the past week.

**Endpoint**: `GET /api/reports/weekly`

**Parameters**:
- `startDate` (optional, query param): Start date in ISO format (YYYY-MM-DD). Defaults to 7 days ago.

**Example Request**:
```bash
curl -X GET "http://localhost:8080/api/reports/weekly?startDate=2024-01-08"
```

**Response** (200 OK):
Same format as daily report, but aggregated over the week.

---

### Get Custom Report
Get a productivity report for a custom date range.

**Endpoint**: `GET /api/reports/custom`

**Parameters**:
- `startDate` (required, query param): Start date and time in ISO format (YYYY-MM-DDTHH:mm:ss)
- `endDate` (required, query param): End date and time in ISO format (YYYY-MM-DDTHH:mm:ss)

**Example Request**:
```bash
curl -X GET "http://localhost:8080/api/reports/custom?startDate=2024-01-01T00:00:00&endDate=2024-01-31T23:59:59"
```

**Response** (200 OK):
Same format as daily report, but for the custom date range.

---

## Activity Types

Activities are automatically classified into three types:

### PRODUCTIVE
Apps and websites that are considered productive, such as:
- Code editors (VS Code, IntelliJ, Eclipse)
- Documentation sites (GitHub, Stack Overflow)
- Note-taking apps (Notion, Obsidian)
- Office apps (Word, Excel, PowerPoint)
- PDF readers

### DISTRACTING
Apps and websites that are considered distracting, such as:
- Social media (YouTube, Instagram, Facebook, Twitter, TikTok)
- Entertainment (Netflix, Reddit)
- Gaming apps

### NEUTRAL
Apps that don't fall into either category (default classification).

---

## Error Responses

### 400 Bad Request
```json
{
  "error": "Invalid request parameters"
}
```

### 404 Not Found
```json
{
  "error": "Resource not found"
}
```

### 500 Internal Server Error
```json
{
  "error": "Internal server error"
}
```

---

## Rate Limiting

Currently, there is no rate limiting. Future versions may include rate limiting for API endpoints.

---

## CORS

CORS is enabled for the following origins:
- `http://localhost:3000` (React development server)
- `http://localhost:3001` (Alternative React port)

---

## Examples

### Complete Workflow

1. **Start a session**:
```bash
curl -X POST "http://localhost:8080/api/sessions?sessionName=Morning%20Focus"
```

2. **Add activities** (typically done by the monitoring script):
```bash
curl -X POST "http://localhost:8080/api/activities/session/1" \
  -H "Content-Type: application/json" \
  -d '{"appName": "VS Code", "windowTitle": "FocusTrack.java - VS Code"}'
```

3. **End the session**:
```bash
curl -X PUT "http://localhost:8080/api/sessions/1/end"
```

4. **Get daily report**:
```bash
curl -X GET "http://localhost:8080/api/reports/daily"
```

---

## Testing with Postman

1. Import the API collection (if available)
2. Set the base URL to `http://localhost:8080/api`
3. Test each endpoint individually
4. Check the responses and verify the data

---

## WebSocket Support (Future)

Future versions may include WebSocket support for real-time activity updates. This would allow the frontend to receive live updates without polling.

---

## Versioning

Current API version: **v1**

API versioning may be implemented in future releases using URL versioning:
- `/api/v1/sessions`
- `/api/v2/sessions`

---

## Support

For API issues or questions, please open an issue on GitHub or contact the project maintainers.

