package com.petverse.notificationservice;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import com.petverse.notificationservice.OpenAPIConfig; // ✅ OpenAPIConfig sınıfını içe aktar

@EnableRabbit
@SpringBootApplication
@Import(OpenAPIConfig.class)  // ✅ OpenAPIConfig manuel dahil ediliyor
public class NotificationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }
}
