package com.petverse.configserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
    "spring.cloud.config.enabled=false",
    "spring.cloud.config.server.git.uri=none"
})
class ConfigserverApplicationTests {

    @Test
    void contextLoads() {
    }

}
