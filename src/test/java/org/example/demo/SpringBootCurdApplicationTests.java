package org.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")   // ← activates the "test" profile
class SpringBootCurdApplicationTests {

    @Test
    void contextLoads() {
    }
}