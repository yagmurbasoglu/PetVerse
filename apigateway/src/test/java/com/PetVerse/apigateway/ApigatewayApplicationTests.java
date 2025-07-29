package com.petverse.apigateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
@SpringBootTest(properties = {
  "spring.datasource.url=jdbc:postgresql://localhost:5432/petcaredb",
  "spring.datasource.username=admin",
  "spring.datasource.password=admin123",
  "spring.jpa.hibernate.ddl-auto=update"
})
@ActiveProfiles("test")
class ApigatewayApplicationTests {

	@Test
	void contextLoads() {
	}

}
