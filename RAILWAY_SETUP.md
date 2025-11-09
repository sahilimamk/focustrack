# Railway Backend Setup Guide

## ✅ Backend is Deployed!

Your Spring Boot backend is now running on Railway. Here's how to find and use your deployment URL:

## Finding Your Railway URL

1. **Go to Railway Dashboard**: https://railway.app/dashboard
2. **Click on your project** (focustrack)
3. **Click on your service** (the backend service)
4. **Go to Settings tab**
5. **Click on "Networking" or "Domains"**
6. **Copy the Public Domain URL**

The URL will look like one of these:
- `https://focustrack-production.up.railway.app`
- `https://focustrack-xxxxx.up.railway.app`
- Or a custom domain if you set one up

## Using Your Backend URL

### 1. Test Your Backend

Open your browser and visit:
```
https://your-railway-url.railway.app/api/sessions
```

You should see an empty array `[]` or get a response, which means the backend is working!

### 2. Update Frontend Environment Variable

Now you need to update your frontend to use this backend URL:

#### Option A: Update for Vercel Deployment

1. Go to Vercel Dashboard: https://vercel.com/dashboard
2. Select your project (or create a new one)
3. Go to **Settings** → **Environment Variables**
4. Add a new variable:
   - **Key**: `REACT_APP_API_URL`
   - **Value**: `https://your-railway-url.railway.app/api`
   - **Environment**: Production, Preview, and Development
5. **Redeploy** your frontend

#### Option B: Update Local Development

Create a file `frontend/.env.local`:
```
REACT_APP_API_URL=https://your-railway-url.railway.app/api
```

### 3. Update Monitoring Script

When running the monitoring script locally, use:
```bash
python monitoring/activity_monitor.py --session-id 1 --api-url https://your-railway-url.railway.app
```

## Common Railway URLs

- **Dashboard**: https://railway.app/dashboard
- **Project Settings**: Click on your project → Settings
- **Service Settings**: Click on your service → Settings → Networking
- **Logs**: Click on your service → View Logs

## Testing Your Deployment

### Test Backend Endpoints

1. **Health Check**: `https://your-railway-url.railway.app/api/sessions`
2. **Create Session**: 
   ```bash
   curl -X POST "https://your-railway-url.railway.app/api/sessions?sessionName=Test&targetUrl=https://github.com"
   ```
3. **Get Active Session**: `https://your-railway-url.railway.app/api/sessions/active`

### Test Frontend Connection

1. Deploy frontend to Vercel with the `REACT_APP_API_URL` environment variable
2. Open your Vercel app URL
3. Try creating a session
4. Check browser console for any CORS or connection errors

## Troubleshooting

### Backend Not Accessible

- Check Railway logs for errors
- Verify the service is running (green status)
- Check if port is correctly configured (Railway auto-detects port 8080)

### CORS Errors

- The backend CORS is configured to allow all origins
- If you still get CORS errors, check that the backend URL is correct
- Verify the frontend is making requests to the correct backend URL

### Database Issues

- Railway uses the H2 database by default (in-memory)
- Data will be lost on redeployment
- For persistent storage, consider adding a PostgreSQL database on Railway

## Next Steps

1. ✅ Backend deployed on Railway
2. ⏭️ Copy your Railway URL
3. ⏭️ Deploy frontend to Vercel
4. ⏭️ Set `REACT_APP_API_URL` environment variable in Vercel
5. ⏭️ Test the full application

## Need Help?

- Check Railway logs: Railway Dashboard → Your Service → Logs
- Check backend health: Visit `https://your-railway-url.railway.app/api/sessions`
- Verify environment variables in Vercel are set correctly

