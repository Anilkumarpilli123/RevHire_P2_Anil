package com.rev.app.rest;

import com.rev.app.dto.ApiResponse;
import com.rev.app.dto.NotificationDto;
import com.rev.app.entity.Notification;
import com.rev.app.entity.User;
import com.rev.app.mapper.NotificationMapper;
import com.rev.app.repository.UserRepository;
import com.rev.app.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
public class NotificationRestController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationMapper notificationMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationDto>>> getMyNotifications(Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).get();
        List<NotificationDto> notifications = notificationService.getNotificationsByUser(user)
                .stream().map(notificationMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Notifications fetched", notifications));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<String>> markAsRead(@PathVariable int id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok(ApiResponse.success("Notification marked as read", null));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).get();
        long count = notificationService.getUnreadCount(user);
        return ResponseEntity.ok(ApiResponse.success("Unread count fetched", count));
    }
}
