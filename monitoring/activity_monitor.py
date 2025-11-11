import cv2
import numpy as np
from ultralytics import YOLO
import os
import webbrowser  # For opening YouTube
import time  # For cooldown timer

# Load the pre-trained YOLOv8 model (downloads automatically if needed)
model = YOLO('yolov8n.pt')  # 'n' for nano (fastest); use 's' or 'm' for better accuracy

# Open webcam (0 for default laptop camera)
cap = cv2.VideoCapture(0)

# Check if camera opened successfully
if not cap.isOpened():
    print("Error: Could not open webcam. Check if it's connected and permissions are granted.")
    exit()

# Confidence threshold for detections
conf_threshold = 0.5

# COCO class ID for 'cell phone' (67)
PHONE_CLASS_ID = 67

# YouTube URL to play
YOUTUBE_URL = 'https://www.youtube.com/watch?v=kDgVEjvnxu8&list=RDkDgVEjvnxu8&start_radio=1'

# Cooldown timer (seconds) to prevent spamming video opens
COOLDOWN_SECONDS = 10
last_trigger_time = 0

# Snapshot counter
snapshot_count = 0

print("Phone Detector started! Press 's' to save snapshot, 'ESC' or 'q' to quit.")
print("Hold a phone in front of the camera to trigger the yellow alert + YouTube video.")
print(f"Video will open: {YOUTUBE_URL}")

while True:
    ret, frame = cap.read()
    if not ret:
        print("Error: Failed to capture frame.")
        break

    current_time = time.time()

    # Run YOLOv8 inference
    results = model(frame, verbose=False)  # Suppress verbose output

    # Check for phone detection
    phone_detected = False
    annotated_frame = results[0].plot()  # Draw boxes/labels on frame

    for result in results:
        boxes = result.boxes
        if boxes is not None:
            for box in boxes:
                cls_id = int(box.cls[0])
                conf = float(box.conf[0])
                if cls_id == PHONE_CLASS_ID and conf > conf_threshold:
                    phone_detected = True
                    break  # No need to check further once detected

    if phone_detected and (current_time - last_trigger_time > COOLDOWN_SECONDS):
        print("Phone detected! Showing alert and opening YouTube video...")
        last_trigger_time = current_time  # Set cooldown

        # Create fullscreen yellow alert window
        alert_window = 'Phone Alert'
        cv2.namedWindow(alert_window, cv2.WINDOW_FULLSCREEN)

        # Get rough screen size (adjust if your resolution differs)
        screen_width = 1920  # Change to your screen width if needed
        screen_height = 1080  # Change to your screen height if needed
        alert_img = np.zeros((screen_height, screen_width, 3), dtype=np.uint8)
        alert_img[:] = (0, 255, 255)  # Fixed: Yellow color (BGR: 0,255,255) via broadcasting

        # Add warning text
        font = cv2.FONT_HERSHEY_SIMPLEX
        text = "PHONE DETECTED!"
        text_size = cv2.getTextSize(text, font, 2, 2)[0]
        text_x = (screen_width - text_size[0]) // 2
        text_y = screen_height // 2 - 50
        cv2.putText(alert_img, text, (text_x, text_y), font, 2, (0, 0, 0), 3)  # Black text, thick

        sub_text = "Opening distraction video... Press 'ESC' or 'q' to dismiss alert."
        sub_size = cv2.getTextSize(sub_text, font, 1, 2)[0]
        sub_x = (screen_width - sub_size[0]) // 2
        sub_y = screen_height // 2 + 50
        cv2.putText(alert_img, sub_text, (sub_x, sub_y), font, 1, (0, 0, 0), 2)

        cv2.imshow(alert_window, alert_img)

        # Wait for key to dismiss alert (non-blocking for video open)
        key = cv2.waitKey(0) & 0xFF
        if key == 27 or key == ord('q'):  # ESC or q
            cv2.destroyWindow(alert_window)

        # Open YouTube video in default browser (after alert dismiss)
        webbrowser.open(YOUTUBE_URL)
        print(f"Opened YouTube video: {YOUTUBE_URL}")

    else:
        # No phone: Show normal annotated frame
        cv2.imshow('Phone Detector', annotated_frame)

    # Handle key presses
    key = cv2.waitKey(1) & 0xFF
    if key == 27 or key == ord('q'):  # ESC or q to quit
        break
    elif key == ord('s'):  # Save snapshot
        filename = f"snapshot_{snapshot_count}.jpg"
        cv2.imwrite(filename, annotated_frame)
        print(f"Saved {filename}")
        snapshot_count += 1

# Cleanup
cap.release()
cv2.destroyAllWindows()
print("Phone Detector stopped.")