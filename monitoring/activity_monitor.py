#!/usr/bin/env python3
"""
FocusTrack Activity Monitor
===========================

A Python script that monitors active applications and browser tabs,
then sends the data to the FocusTrack Spring Boot backend.

Requirements:
    pip install psutil requests

Usage:
    python activity_monitor.py --session-id <session_id> --api-url http://localhost:8080

For Windows:
    pip install pywin32
    python activity_monitor.py --session-id <session_id>

For macOS:
    pip install pyobjc
    python activity_monitor.py --session-id <session_id>

For Linux:
    python activity_monitor.py --session-id <session_id>
"""

import time
import sys
import argparse
import requests
import platform
import webbrowser
from datetime import datetime
from urllib.parse import urlparse

try:
    import psutil
except ImportError:
    print("Error: psutil not installed. Run: pip install psutil")
    sys.exit(1)

# Platform-specific imports
if platform.system() == "Windows":
    try:
        import win32gui
        import win32process
    except ImportError:
        print("Warning: pywin32 not installed. Window title detection may be limited.")
        print("Install with: pip install pywin32")
        win32gui = None
elif platform.system() == "Darwin":  # macOS
    try:
        from AppKit import NSWorkspace
    except ImportError:
        print("Warning: PyObjC not installed. Install with: pip install pyobjc")
        NSWorkspace = None
elif platform.system() == "Linux":
    import subprocess


class ActivityMonitor:
    def __init__(self, session_id, api_url="http://localhost:8080", poll_interval=5):
        self.session_id = session_id
        self.api_url = api_url
        self.poll_interval = poll_interval
        self.last_app = None
        self.last_title = None
        self.running = False
        self.target_url = None
        self.off_target_start_time = None
        self.distraction_video_url = "https://www.youtube.com/watch?v=kDgVEjvnxu8&list=RDkDgVEjvnxu8&start_radio=1"
        self.distraction_threshold_seconds = 20 * 60  # 20 minutes
        self.video_played = False
        self.session_fetched = False
        
    def get_active_window_windows(self):
        """Get active window on Windows."""
        if win32gui is None:
            return None, None
            
        try:
            hwnd = win32gui.GetForegroundWindow()
            title = win32gui.GetWindowText(hwnd)
            _, pid = win32process.GetWindowThreadProcessId(hwnd)
            process = psutil.Process(pid)
            app_name = process.name()
            return app_name, title
        except Exception as e:
            print(f"Error getting Windows window: {e}")
            return None, None
    
    def get_active_window_macos(self):
        """Get active window on macOS."""
        if NSWorkspace is None:
            return None, None
            
        try:
            workspace = NSWorkspace.sharedWorkspace()
            active_app = workspace.activeApplication()
            app_name = active_app.get('NSApplicationName', 'Unknown')
            # Getting window title on macOS requires more complex API calls
            # For MVP, we'll just return the app name
            return app_name, f"{app_name} Window"
        except Exception as e:
            print(f"Error getting macOS window: {e}")
            return None, None
    
    def get_active_window_linux(self):
        """Get active window on Linux."""
        try:
            # Try using xdotool if available
            result = subprocess.run(
                ['xdotool', 'getactivewindow', 'getwindowname'],
                capture_output=True,
                text=True,
                timeout=1
            )
            if result.returncode == 0:
                title = result.stdout.strip()
                # Get process name
                result = subprocess.run(
                    ['xdotool', 'getactivewindow', 'getwindowpid'],
                    capture_output=True,
                    text=True,
                    timeout=1
                )
                if result.returncode == 0:
                    pid = int(result.stdout.strip())
                    process = psutil.Process(pid)
                    app_name = process.name()
                    return app_name, title
        except (FileNotFoundError, subprocess.TimeoutExpired, Exception) as e:
            pass
        
        # Fallback: get foreground process
        try:
            for proc in psutil.process_iter(['pid', 'name']):
                try:
                    if proc.info['name']:
                        return proc.info['name'], "Unknown Window"
                except (psutil.NoSuchProcess, psutil.AccessDenied):
                    continue
        except Exception:
            pass
        
        return None, None
    
    def get_active_window(self):
        """Get the currently active window based on the OS."""
        system = platform.system()
        
        if system == "Windows":
            return self.get_active_window_windows()
        elif system == "Darwin":
            return self.get_active_window_macos()
        elif system == "Linux":
            return self.get_active_window_linux()
        else:
            return None, None
    
    def fetch_session_info(self):
        """Fetch session information including target URL."""
        try:
            url = f"{self.api_url}/api/sessions/{self.session_id}"
            response = requests.get(url, timeout=5)
            if response.status_code == 200:
                session_data = response.json()
                self.target_url = session_data.get('targetUrl')
                self.session_fetched = True
                if self.target_url:
                    print(f"✓ Target URL set: {self.target_url}")
                else:
                    print("ℹ No target URL set for this session")
                return True
            else:
                print(f"✗ Failed to fetch session: {response.status_code}")
                return False
        except Exception as e:
            print(f"✗ Error fetching session info: {e}")
            return False
    
    def is_on_target(self, window_title):
        """Check if the current window title matches the target URL."""
        if not self.target_url:
            return True  # No target URL means no restriction
        
        if not window_title:
            return False
        
        # Extract domain from target URL
        try:
            parsed_target = urlparse(self.target_url)
            target_domain = parsed_target.netloc.lower()
            
            # Remove www. prefix for matching
            if target_domain.startswith('www.'):
                target_domain = target_domain[4:]
            
            # Get the main domain (e.g., 'github.com' from 'www.github.com' or 'github.com')
            domain_parts = target_domain.split('.')
            if len(domain_parts) >= 2:
                main_domain = '.'.join(domain_parts[-2:])  # Get last two parts (e.g., 'github.com')
            else:
                main_domain = target_domain
            
            window_title_lower = window_title.lower()
            
            # Check for full URL match (most precise)
            if self.target_url.lower() in window_title_lower:
                return True
            
            # Check for domain match (e.g., 'github.com' in title)
            if target_domain in window_title_lower or main_domain in window_title_lower:
                return True
            
            # Check if it's a browser window - browsers often show URL in title
            # Some browsers format: "Page Title - Domain" or "Domain - Page Title"
            # Check if the main domain parts appear together
            if len(domain_parts) >= 2:
                # Check if both main parts appear (e.g., 'github' and 'com')
                if all(part in window_title_lower for part in domain_parts[-2:]):
                    return True
            
            return False
        except Exception as e:
            print(f"Error checking target match: {e}")
            return True  # Default to on-target if we can't parse to avoid false positives
    
    def play_distraction_video(self):
        """Open the distraction video in the default browser."""
        if not self.video_played:
            print(f"\n⚠️  WARNING: You've been off-target for more than 20 minutes!")
            print(f"📺 Playing distraction video: {self.distraction_video_url}\n")
            try:
                webbrowser.open(self.distraction_video_url)
                self.video_played = True
            except Exception as e:
                print(f"✗ Error opening video: {e}")
    
    def check_target_compliance(self, window_title):
        """Check if user is on target and track off-target time."""
        if not self.target_url:
            return  # No target URL, skip checking
        
        is_on_target = self.is_on_target(window_title)
        
        if is_on_target:
            # Reset off-target tracking if we're back on target
            if self.off_target_start_time is not None:
                off_target_duration = (datetime.now() - self.off_target_start_time).total_seconds()
                print(f"✓ Back on target after {off_target_duration:.0f} seconds")
                self.off_target_start_time = None
                self.video_played = False  # Reset video flag when back on target
        else:
            # We're off target
            if self.off_target_start_time is None:
                # Start tracking off-target time
                self.off_target_start_time = datetime.now()
                print(f"⚠️  Off-target detected. Window: {window_title}")
            else:
                # Calculate how long we've been off-target
                off_target_duration = (datetime.now() - self.off_target_start_time).total_seconds()
                
                if off_target_duration >= self.distraction_threshold_seconds and not self.video_played:
                    self.play_distraction_video()
                else:
                    # Calculate remaining time until video plays
                    remaining = self.distraction_threshold_seconds - off_target_duration
                    # Print status every minute or when close to threshold
                    minutes_off = int(off_target_duration / 60)
                    if minutes_off > 0 and off_target_duration % 60 < self.poll_interval:
                        if remaining > 0:
                            print(f"⏱️  Off-target for {off_target_duration/60:.1f} minutes. Video will play in {remaining/60:.1f} minutes if not on target.")
                        else:
                            # Should have triggered video already, but just in case
                            if not self.video_played:
                                self.play_distraction_video()
    
    def send_activity(self, app_name, window_title):
        """Send activity data to the backend API."""
        try:
            url = f"{self.api_url}/api/activities/session/{self.session_id}"
            data = {
                "appName": app_name,
                "windowTitle": window_title
            }
            response = requests.post(url, json=data, timeout=5)
            if response.status_code == 201:
                print(f"✓ Sent activity: {app_name} - {window_title}")
            else:
                print(f"✗ Failed to send activity: {response.status_code}")
        except Exception as e:
            print(f"✗ Error sending activity: {e}")
    
    def monitor(self):
        """Main monitoring loop."""
        print(f"Starting activity monitor for session {self.session_id}")
        print(f"Polling every {self.poll_interval} seconds...")
        print("Press Ctrl+C to stop monitoring\n")
        
        # Fetch session info including target URL
        if not self.fetch_session_info():
            print("Warning: Could not fetch session info. Continuing without target URL checking...")
        
        self.running = True
        
        try:
            while self.running:
                app_name, window_title = self.get_active_window()
                
                if app_name and window_title:
                    # Check target URL compliance
                    self.check_target_compliance(window_title)
                    
                    # Only send if the app or title changed
                    if app_name != self.last_app or window_title != self.last_title:
                        self.send_activity(app_name, window_title)
                        self.last_app = app_name
                        self.last_title = window_title
                elif window_title:
                    # Even if app_name is None, check compliance with window_title
                    self.check_target_compliance(window_title)
                
                time.sleep(self.poll_interval)
                
        except KeyboardInterrupt:
            print("\n\nStopping monitor...")
            self.running = False


def main():
    parser = argparse.ArgumentParser(description="FocusTrack Activity Monitor")
    parser.add_argument("--session-id", type=int, required=True,
                       help="Session ID from FocusTrack backend")
    parser.add_argument("--api-url", type=str, default="http://localhost:8080",
                       help="FocusTrack API URL (default: http://localhost:8080)")
    parser.add_argument("--poll-interval", type=int, default=5,
                       help="Polling interval in seconds (default: 5)")
    
    args = parser.parse_args()
    
    monitor = ActivityMonitor(
        session_id=args.session_id,
        api_url=args.api_url,
        poll_interval=args.poll_interval
    )
    
    monitor.monitor()


if __name__ == "__main__":
    main()

