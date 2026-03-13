package com.rev.app.service.impl;

import com.rev.app.entity.Notification;
import com.rev.app.entity.User;
import com.rev.app.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private User testUser;
    private Notification testNotification;

    @BeforeEach
    void setUp() {
        testUser = User.builder().id(1).email("u@u.com").build();
        testNotification = Notification.builder().id(1).user(testUser).message("M").isRead(false).build();
    }

    @Test
    void getNotificationsByUser_Success() {
        when(notificationRepository.findByUserOrderByCreatedAtDesc(testUser))
                .thenReturn(Arrays.asList(testNotification));

        List<Notification> notifications = notificationService.getNotificationsByUser(testUser);

        assertFalse(notifications.isEmpty());
        assertEquals(1, notifications.size());
    }

    @Test
    void sendNotification_Success() {
        when(notificationRepository.save(any(Notification.class))).thenReturn(testNotification);

        notificationService.sendNotification(testUser, "Test message");

        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void markAsRead_Success() {
        when(notificationRepository.findById(1)).thenReturn(java.util.Optional.of(testNotification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(testNotification);

        notificationService.markAsRead(1);

        assertTrue(testNotification.isRead());
        verify(notificationRepository, times(1)).save(testNotification);
    }
}
