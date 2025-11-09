# Deploy Frontend to Vercel - Quick Guide

## ✅ Your Backend URL
```
https://focustrack-production-68bf.up.railway.app
```

## 🚀 Deploy to Vercel

### Step 1: Go to Vercel
1. Visit: https://vercel.com
2. Sign in with GitHub
3. Click **"Add New Project"**

### Step 2: Import Repository
1. Select your GitHub repository: `sahilimamk/focustrack`
2. Click **"Import"**

### Step 3: Configure Project
1. **Framework Preset**: `Create React App` (auto-detected)
2. **Root Directory**: `frontend`
3. **Build Command**: `cd frontend && npm install && npm run build`
4. **Output Directory**: `frontend/build`
5. **Install Command**: `cd frontend && npm install`

### Step 4: Add Environment Variable
**IMPORTANT**: Before deploying, add this environment variable:

1. Click **"Environment Variables"** section
2. Add new variable:
   - **Key**: `REACT_APP_API_URL`
   - **Value**: `https://focustrack-production-68bf.up.railway.app/api`
   - **Environment**: Select all (Production, Preview, Development)
3. Click **"Save"**

### Step 5: Deploy
1. Click **"Deploy"**
2. Wait for deployment to complete (~2-3 minutes)
3. Your app will be live at: `https://your-app.vercel.app`

## ✅ Testing Your Deployment

### Test Backend Connection
1. Open your Vercel app URL
2. Open browser Developer Tools (F12)
3. Go to Console tab
4. Try creating a session
5. Check Network tab for API calls to your Railway backend

### Test Backend Directly
Visit: https://focustrack-production-68bf.up.railway.app/api/sessions

You should see: `[]` (empty array, which is correct)

## 🔧 Troubleshooting

### Frontend Can't Connect to Backend
- Verify `REACT_APP_API_URL` is set correctly in Vercel
- Check that the value is: `https://focustrack-production-68bf.up.railway.app/api`
- Make sure you selected all environments (Production, Preview, Development)
- Redeploy after adding environment variable

### CORS Errors
- Backend CORS is already configured to allow all origins
- If you still see CORS errors, verify the backend URL is correct
- Check browser console for exact error message

### Build Errors
- Check Vercel build logs
- Verify Node.js version (React 18 needs Node 14+)
- Check that `frontend/package.json` exists

## 📝 Quick Reference

### Backend URLs
- **Base URL**: `https://focustrack-production-68bf.up.railway.app`
- **API URL**: `https://focustrack-production-68bf.up.railway.app/api`
- **Test Endpoint**: `https://focustrack-production-68bf.up.railway.app/api/sessions`

### Environment Variable for Vercel
```
REACT_APP_API_URL=https://focustrack-production-68bf.up.railway.app/api
```

### Monitoring Script (Local)
```bash
python monitoring/activity_monitor.py --session-id 1 --api-url https://focustrack-production-68bf.up.railway.app
```

## 🎉 Next Steps

1. ✅ Backend deployed on Railway
2. ⏭️ Deploy frontend to Vercel
3. ⏭️ Set environment variable in Vercel
4. ⏭️ Test the full application
5. ⏭️ Share your app URL!

## 🆘 Need Help?

- **Vercel Dashboard**: https://vercel.com/dashboard
- **Railway Dashboard**: https://railway.app/dashboard
- **Backend Status**: https://focustrack-production-68bf.up.railway.app/api/sessions
- Check deployment logs in Vercel if something goes wrong

