package com.focustrack.dto;

import com.focustrack.model.Activity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityRequest {
    private String appName;
    private String windowTitle;
    private Activity.ActivityType type;
}

