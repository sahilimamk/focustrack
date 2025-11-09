# Test Railway Backend
# Replace YOUR_RAILWAY_URL with your actual Railway URL

$railwayUrl = "YOUR_RAILWAY_URL"  # e.g., https://focustrack-production.up.railway.app

Write-Host "Testing Railway Backend..."
Write-Host "Backend URL: $railwayUrl"
Write-Host ""

# Test 1: Get all sessions
Write-Host "Test 1: Getting all sessions..."
try {
    $response = Invoke-WebRequest -Uri "$railwayUrl/api/sessions" -Method GET -UseBasicParsing
    Write-Host "✅ Backend is working! Status: $($response.StatusCode)"
    Write-Host "Response: $($response.Content)"
} catch {
    Write-Host "❌ Error: $_"
}

Write-Host ""

# Test 2: Create a session
Write-Host "Test 2: Creating a session..."
try {
    $sessionData = @{
        sessionName = "Test Session"
        targetUrl = "https://github.com"
    }
    $response = Invoke-WebRequest -Uri "$railwayUrl/api/sessions?sessionName=Test%20Session&targetUrl=https://github.com" -Method POST -UseBasicParsing
    Write-Host "✅ Session created! Status: $($response.StatusCode)"
    Write-Host "Response: $($response.Content)"
} catch {
    Write-Host "❌ Error: $_"
}

Write-Host ""
Write-Host "If both tests pass, your backend is working correctly!"
Write-Host "Next step: Update your frontend's REACT_APP_API_URL to: $railwayUrl/api"

