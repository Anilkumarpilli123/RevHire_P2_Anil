package com.rev.app;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
class RevHireP2ApplicationTests {

    @MockitoBean
    private JavaMailSender javaMailSender;

    @Test
    void contextLoads() {
    }

}
