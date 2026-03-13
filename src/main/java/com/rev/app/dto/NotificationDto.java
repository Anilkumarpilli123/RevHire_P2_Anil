package com.rev.app.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDto {
    private int id;
    private String message;
    private boolean isRead;
    private LocalDateTime createdAt;
}
