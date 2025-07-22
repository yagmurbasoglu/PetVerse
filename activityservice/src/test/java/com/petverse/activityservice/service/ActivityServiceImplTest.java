package com.petverse.service;

import com.petverse.client.PetServiceClient;
import com.petverse.dto.NotificationEvent;
import com.petverse.dto.PetDTO;
import com.petverse.model.Activity;
import com.petverse.producer.ActivityEventPublisher;
import com.petverse.repository.ActivityRepository;
import com.petverse.service.impl.ActivityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

class ActivityServiceImplTest {

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private ActivityEventPublisher publisher;

    @Mock
    private PetServiceClient petServiceClient;

    @InjectMocks
    private ActivityServiceImpl activityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateActivityIfUserOwnsPet() {
        // arrange
        Activity activity = Activity.builder()
                .type("walk")
                .description("Akşam yürüyüşü")
                .petId(1L)
                .build();

        PetDTO petDTO = new PetDTO(1L, "Loki", "Cat", 2, 42L);
        Activity savedActivity = Activity.builder()
                .id(100L)
                .type("walk")
                .description("Akşam yürüyüşü")
                .petId(1L)
                .userId(42L)
                .build();

        when(petServiceClient.getPetById(1L)).thenReturn(petDTO);
        when(activityRepository.save(any(Activity.class))).thenReturn(savedActivity);

        // act
        Activity result = activityService.createActivity(activity, "42");

        // assert
        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals(42L, result.getUserId());
        verify(petServiceClient).getPetById(1L);
        verify(publisher).publish(any(NotificationEvent.class));
        verify(activityRepository).save(any(Activity.class));
    }

    @Test
    void shouldThrowExceptionIfUserDoesNotOwnPet() {
        Activity activity = Activity.builder()
                .type("walk")
                .description("deneme")
                .petId(1L)
                .build();

        PetDTO petDTO = new PetDTO(1L, "Loki", "Cat", 2, 999L); // başkasına ait

        when(petServiceClient.getPetById(1L)).thenReturn(petDTO);

        var ex = assertThrows(RuntimeException.class, () ->
                activityService.createActivity(activity, "42")
        );

        assertTrue(ex.getMessage().contains("Bu pet bu kullanıcıya ait değil"));
    }

    @Test
    void shouldThrowFallbackExceptionIfPetServiceFails() {
        Activity activity = Activity.builder()
                .type("run")
                .description("koşu")
                .petId(1L)
                .build();

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                activityService.handlePetServiceFailure(activity, "42", new RuntimeException("PetService patladı!"))
        );

        assertTrue(ex.getMessage().contains("PetService şu anda ulaşılamıyor"));
    }
}
