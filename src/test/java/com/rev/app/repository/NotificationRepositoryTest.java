package com.rev.app.repository;

import com.rev.app.entity.Notification;
import com.rev.app.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUserOrderByCreatedAtDesc_Success() {
        User user = userRepository.save(User.builder().email("n@e.com").password("p").role("E").build());
        Notification n = Notification.builder().user(user).message("M").isRead(false).build();
        notificationRepository.save(n);

        List<Notification> notes = notificationRepository.findByUserOrderByCreatedAtDesc(user);
        assertEquals(1, notes.size());
    }
}
