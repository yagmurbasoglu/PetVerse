package com.petservice.petservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
  "spring.datasource.url=jdbc:postgresql://localhost:5432/petcaredb",
  "spring.datasource.username=admin",
  "spring.datasource.password=admin123",
  "spring.jpa.hibernate.ddl-auto=update"
})
class PetserviceApplicationTests {
    @Test
    void contextLoads() {
    }
}
