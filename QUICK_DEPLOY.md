# Quick Deploy Guide for FocusTrack

## 🚀 Quick Steps to Deploy

### 1. Create GitHub Repository

1. Go to https://github.com/new
2. Repository name: `focustrack`
3. Set to Public or Private
4. **Don't** initialize with README
5. Click "Create repository"

### 2. Push to GitHub

```bash
git remote add origin https://github.com/sahilimamk/focustrack.git
git branch -M main
git push -u origin main
```

### 3. Deploy Backend to Railway (Free Tier Available)

1. Go to https://railway.app
2. Sign up with GitHub
3. Click "New Project" → "Deploy from GitHub repo"
4. Select `focustrack` repository
5. Railway auto-detects Java/Maven
6. Wait for deployment
7. Copy the URL (e.g., `https://focustrack.railway.app`)

### 4. Deploy Frontend to Vercel

1. Go to https://vercel.com
2. Sign up with GitHub
3. Click "Add New Project"
4. Import `focustrack` repository
5. Configure:
   - **Framework Preset**: Create React App
   - **Root Directory**: `frontend`
   - **Build Command**: `cd frontend && npm install && npm run build`
   - **Output Directory**: `frontend/build`
6. Add Environment Variable:
   - **Key**: `REACT_APP_API_URL`
   - **Value**: `https://your-railway-url.railway.app/api` (from step 3)
7. Click "Deploy"

### 5. Update Backend CORS (if needed)

The backend already allows all origins, so it should work. If you want to restrict it later, update `CorsConfig.java`.

### 6. Test Your Deployment

- Frontend: Visit your Vercel URL
- Backend: Visit `https://your-railway-url.railway.app/api/sessions`

## 📝 Notes

- **Backend**: Must be hosted on Railway, Render, or similar (Java support)
- **Frontend**: Hosted on Vercel ✅
- **Monitoring Script**: Runs locally (not deployed)

## 🔧 Troubleshooting

### CORS Errors
- Check that backend URL is correct in Vercel environment variables
- Verify backend is accessible

### API Connection Errors
- Verify `REACT_APP_API_URL` is set correctly in Vercel
- Check backend logs on Railway
- Test backend URL directly in browser

### Build Errors
- Check Node.js version (React 18 needs Node 14+)
- Check Java version (Spring Boot 3 needs Java 17+)
- Review build logs

## 📚 Full Documentation

See `DEPLOYMENT.md` for detailed deployment instructions.

