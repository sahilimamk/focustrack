# Fix Vercel Deployment Error

## Problem
Vercel can't find the `frontend` directory because the root directory is not set correctly.

## Solution: Set Root Directory in Vercel

### Option 1: Update Vercel Project Settings (Recommended)

1. Go to Vercel Dashboard: https://vercel.com/dashboard
2. Click on your **focustrack** project
3. Go to **Settings** → **General**
4. Scroll down to **Root Directory**
5. Click **Edit**
6. Set Root Directory to: `frontend`
7. Click **Save**
8. **Redeploy** your project

### Option 2: Use Updated vercel.json

The `vercel.json` has been updated. After setting the root directory to `frontend` in Vercel settings, the build should work.

## Updated Configuration

After setting root directory to `frontend`:
- **Build Command**: `npm install && npm run build` (runs in frontend directory)
- **Output Directory**: `build` (relative to frontend directory)
- **Install Command**: `npm install` (runs in frontend directory)

## Steps to Fix

1. ✅ Go to Vercel Dashboard
2. ✅ Open your project settings
3. ✅ Set Root Directory to `frontend`
4. ✅ Save settings
5. ✅ Add environment variable: `REACT_APP_API_URL=https://focustrack-production-68bf.up.railway.app/api`
6. ✅ Redeploy

## Alternative: Deploy from frontend directory

If the above doesn't work, you can also:
1. Create a separate Vercel project
2. Point it to a branch or subdirectory
3. Or move the frontend files to root (not recommended)

## Verify

After setting root directory and redeploying:
- Build should complete successfully
- No "cd frontend: No such file or directory" error
- Frontend should be deployed and accessible

