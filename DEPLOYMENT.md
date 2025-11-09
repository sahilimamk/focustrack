# FocusTrack Deployment Guide

## Overview

FocusTrack consists of three main components:
1. **Spring Boot Backend** (Java) - REST API
2. **React Frontend** - Web UI (can be hosted on Vercel)
3. **Python Monitoring Script** - Runs locally on user's machine

## Important Notes

⚠️ **Vercel can only host the frontend (React app)**. The Spring Boot backend needs to be hosted on a platform that supports Java applications.

### Recommended Hosting Options:
- **Backend**: Railway, Render, Heroku, AWS Elastic Beanstalk, or DigitalOcean
- **Frontend**: Vercel (as requested)
- **Monitoring Script**: Runs locally (not deployed)

## Step 1: Create GitHub Repository

1. Go to https://github.com/sahilimamk
2. Click "New repository"
3. Name it: `focustrack`
4. Set it to Public or Private
5. **Do NOT** initialize with README, .gitignore, or license (we already have these)
6. Click "Create repository"

## Step 2: Push to GitHub

After creating the repository, run:
```bash
git remote add origin https://github.com/sahilimamk/focustrack.git
git branch -M main
git push -u origin main
```

## Step 3: Deploy Backend (Spring Boot)

### Option A: Railway (Recommended - Easy & Free Tier Available)

1. Go to https://railway.app
2. Sign up with GitHub
3. Click "New Project" → "Deploy from GitHub repo"
4. Select the `focustrack` repository
5. Railway will auto-detect it's a Java/Maven project
6. Add environment variables if needed
7. Railway will provide a URL like: `https://your-app.railway.app`

### Option B: Render

1. Go to https://render.com
2. Sign up with GitHub
3. Click "New" → "Web Service"
4. Connect your GitHub repository
5. Settings:
   - **Build Command**: `mvn clean install`
   - **Start Command**: `java -jar target/focustrack-*.jar`
   - **Environment**: Java
6. Render will provide a URL like: `https://your-app.onrender.com`

### Option C: Heroku

1. Install Heroku CLI
2. Login: `heroku login`
3. Create app: `heroku create focustrack-backend`
4. Set Java buildpack: `heroku buildpacks:set heroku/java`
5. Deploy: `git push heroku main`

## Step 4: Update Frontend API URL

Before deploying to Vercel, update the API URL in the frontend:

1. Open `frontend/src/components/Dashboard.js`
2. Change `API_BASE_URL` to your backend URL:
   ```javascript
   const API_BASE_URL = 'https://your-backend-url.railway.app/api';
   ```

3. Do the same for:
   - `frontend/src/components/PomodoroTimer.js`
   - `frontend/src/components/Reports.js`

Or better yet, use environment variables:

1. Create `frontend/.env.production`:
   ```
   REACT_APP_API_URL=https://your-backend-url.railway.app/api
   ```

2. Update components to use:
   ```javascript
   const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';
   ```

## Step 5: Deploy Frontend to Vercel

### Option 1: Vercel CLI

1. Install Vercel CLI:
   ```bash
   npm i -g vercel
   ```

2. Navigate to frontend directory:
   ```bash
   cd frontend
   ```

3. Deploy:
   ```bash
   vercel
   ```

4. Follow the prompts

### Option 2: Vercel Dashboard (Recommended)

1. Go to https://vercel.com
2. Sign up/login with GitHub
3. Click "Add New Project"
4. Import your GitHub repository
5. Configure project:
   - **Framework Preset**: Create React App
   - **Root Directory**: `frontend`
   - **Build Command**: `npm run build`
   - **Output Directory**: `build`
   - **Install Command**: `npm install`

6. Add environment variable:
   - **Key**: `REACT_APP_API_URL`
   - **Value**: `https://your-backend-url.railway.app/api`

7. Click "Deploy"

## Step 6: Update CORS Settings

Update `src/main/java/com/focustrack/config/CorsConfig.java` to allow your Vercel domain:

```java
config.addAllowedOrigin("https://your-app.vercel.app");
config.addAllowedOrigin("http://localhost:3000"); // For local development
```

## Step 7: Database Configuration

For production, you may want to switch from H2 to a persistent database:

### Option: PostgreSQL on Railway/Render

1. Add PostgreSQL database on Railway/Render
2. Update `application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://your-db-url:5432/focustrack
   spring.datasource.username=your-username
   spring.datasource.password=your-password
   spring.jpa.hibernate.ddl-auto=update
   ```

3. Add PostgreSQL dependency to `pom.xml`:
   ```xml
   <dependency>
       <groupId>org.postgresql</groupId>
       <artifactId>postgresql</artifactId>
   </dependency>
   ```

## Environment Variables for Backend

If using environment variables for database:

```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://...
SPRING_DATASOURCE_USERNAME=...
SPRING_DATASOURCE_PASSWORD=...
```

## Testing the Deployment

1. **Backend**: Visit `https://your-backend-url.railway.app/api/sessions`
2. **Frontend**: Visit `https://your-app.vercel.app`
3. **Monitor**: Run locally: `python monitoring/activity_monitor.py --session-id 1 --api-url https://your-backend-url.railway.app`

## Troubleshooting

### CORS Errors
- Make sure CORS config includes your Vercel domain
- Check that backend is accessible from browser

### API Connection Errors
- Verify backend URL is correct in frontend
- Check backend logs for errors
- Ensure backend is running and accessible

### Build Errors
- Check Node.js version (React 18 requires Node 14+)
- Check Java version (Spring Boot 3 requires Java 17+)
- Review build logs for specific errors

## Local Development Setup

1. **Backend**: `mvn spring-boot:run`
2. **Frontend**: `cd frontend && npm start`
3. **Monitor**: `python monitoring/activity_monitor.py --session-id 1`

## Production Checklist

- [ ] Backend deployed and accessible
- [ ] Frontend API URL updated
- [ ] CORS configured for Vercel domain
- [ ] Environment variables set
- [ ] Database configured (if using PostgreSQL)
- [ ] Frontend deployed to Vercel
- [ ] Test end-to-end functionality

## Support

For issues or questions, check:
- Backend logs on hosting platform
- Vercel deployment logs
- Browser console for frontend errors
- Network tab for API calls

