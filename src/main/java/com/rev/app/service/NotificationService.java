package com.rev.app.service;

import com.rev.app.entity.Notification;
import com.rev.app.entity.User;
import java.util.List;

public interface NotificationService {
    Notification sendNotification(User user, String message);

    List<Notification> getNotificationsByUser(User user);

    void markAsRead(int notificationId);

    long getUnreadCount(User user);
}
