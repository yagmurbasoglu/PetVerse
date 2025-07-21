package com.petverse.dto;

import com.petverse.model.ActivityType;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent implements Serializable {
    private ActivityType type;
    private String description;
    private String userId;
}
