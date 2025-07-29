package com.petverse.service;

import com.petverse.client.PetServiceClient;
import com.petverse.client.WeatherClient;
import com.petverse.dto.NotificationEvent;
import com.petverse.dto.PetDTO;
import com.petverse.dto.WeatherResponse;
import com.petverse.model.Activity;
import com.petverse.model.ActivityType;
import com.petverse.producer.ActivityEventPublisher;
import com.petverse.repository.ActivityRepository;
import com.petverse.service.impl.ActivityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import com.petverse.model.ActivityType;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class ActivityServiceImplTest {

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private ActivityEventPublisher publisher;

    @Mock
    private PetServiceClient petServiceClient;

    @Mock
    private WeatherClient weatherClient;

    @InjectMocks
    private ActivityServiceImpl activityService;

    @Mock
    private WeatherAdviceService weatherAdviceService; 

    @Mock
    private PetMoodService petMoodService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

@Test
void shouldCreateActivityIfUserOwnsPet() {
    Activity activity = Activity.builder()
            .type(ActivityType.WALK)
            .description("Akşam yürüyüşü")
            .petId(1L)
            .build();

    PetDTO petDTO = new PetDTO(1L, "Loki", "Cat", 2, 42L);
    Activity savedActivity = Activity.builder()
            .id(100L)
            .type(ActivityType.WALK)
            .description("Akşam yürüyüşü")
            .petId(1L)
            .userId(42L)
            .build();

    WeatherResponse.CurrentWeather mockWeather = new WeatherResponse.CurrentWeather();
    mockWeather.setTemperature(25.0);
    mockWeather.setWindspeed(3.5);
    mockWeather.setWeathercode(1);

    WeatherResponse weatherResponse = new WeatherResponse();
    weatherResponse.setCurrentWeather(mockWeather);

    when(petServiceClient.getPetById(1L)).thenReturn(petDTO);
    when(weatherClient.getWeather(anyString())).thenReturn(weatherResponse);
    when(weatherAdviceService.generateAdvice(mockWeather)).thenReturn("Hava güzel, dışarı çıkabilirsiniz.");

    when(activityRepository.save(any(Activity.class))).thenReturn(savedActivity);

    Activity result = activityService.createActivity(activity, "42");

    assertNotNull(result);
    assertEquals(100L, result.getId());
    assertEquals(42L, result.getUserId());
    verify(petServiceClient).getPetById(1L);
    verify(publisher).publish(any(NotificationEvent.class));
    verify(activityRepository, times(2)).save(any(Activity.class));

}


    @Test
    void shouldThrowExceptionIfUserDoesNotOwnPet() {
        Activity activity = Activity.builder()
                .type(ActivityType.WALK)
                .description("deneme")
                .petId(1L)
                .build();

        PetDTO petDTO = new PetDTO(1L, "Loki", "Cat", 2, 999L); // başka kullanıcı

        when(petServiceClient.getPetById(1L)).thenReturn(petDTO);

        var ex = assertThrows(RuntimeException.class, () ->
                activityService.createActivity(activity, "42")
        );

        assertTrue(ex.getMessage().contains("Bu pet bu kullanıcıya ait değil"));
    }

    @Test
    void shouldThrowFallbackExceptionIfPetServiceFails() {
        Activity activity = Activity.builder()
                .type(ActivityType.RUN)
                .description("koşu")
                .petId(1L)
                .build();

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                activityService.handlePetServiceFailure(activity, "42", new RuntimeException("PetService patladı!"))
        );

        assertTrue(ex.getMessage().contains("PetService şu anda ulaşılamıyor"));
    }
}
