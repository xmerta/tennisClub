package cz.xmerta.tennisclub.controller;

import cz.xmerta.tennisclub.service.ReservationService;
import cz.xmerta.tennisclub.storage.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    private Reservation reservation1;
    private Reservation reservation2;
    private Reservation updatedReservation;

    @BeforeEach
    void setUp() {
        SurfaceType validSurfaceType = new SurfaceType(1L, "Clay", 0.5);

        Court court = new Court(1L, "Court 1", validSurfaceType);
        User user = new User(1L, "+420123456789", "John Doe");

        reservation1 = new Reservation(
                1L,
                user,
                court,
                LocalDateTime.of(2025, 1, 14, 10, 0),
                LocalDateTime.of(2025, 1, 14, 11, 0),
                GameType.SINGLE,
                30.0
        );

        reservation2 = new Reservation(
                2L,
                user,
                court,
                LocalDateTime.of(2025, 1, 15, 15, 0),
                LocalDateTime.of(2025, 1, 15, 16, 0),
                GameType.SINGLE,
                25.0
        );

        updatedReservation = new Reservation(
                1L,
                user,
                court,
                LocalDateTime.of(2025, 1, 14, 10, 0),
                LocalDateTime.of(2025, 1, 14, 11, 0),
                GameType.SINGLE,
                40.0
        );
    }

    @Test
    void getAll() throws Exception {
        when(reservationService.findAll()).thenReturn(Arrays.asList(reservation1, reservation2));

        mockMvc.perform(get("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].price").value(30.0))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].price").value(25.0));

        verify(reservationService, times(1)).findAll();
    }

    @Test
    void getById_WhenExists() throws Exception {
        when(reservationService.findById(1L)).thenReturn(Optional.of(reservation1));

        mockMvc.perform(get("/api/reservations/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.price").value(30.0));

        verify(reservationService, times(1)).findById(1L);
    }

    @Test
    void getById_WhenNotExists() throws Exception {
        when(reservationService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/reservations/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(reservationService, times(1)).findById(1L);
    }

    @Test
    void create_Ok() throws Exception {
        when(reservationService.save(any(Reservation.class))).thenReturn(reservation1);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"court\": {\"id\": 1, \"name\": \"Court 1\", \"surfaceType\": {\"id\": 1, \"name\": \"Clay\", \"pricePerMinute\": 0.5}}, \"user\": {\"id\": 1, \"name\": \"John Doe\", \"phoneNumber\": \"+123456789123\"}, \"startTime\": \"2025-01-14T10:00\", \"endTime\": \"2025-01-14T11:00\", \"price\": 30.0, \"gameType\": \"SINGLE\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value(30.0));

        verify(reservationService, times(1)).save(any(Reservation.class));
    }

    @Test
    void create_NotOk() throws Exception {
        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"court\": {}, \"user\": {}, \"startTime\": \"\", \"endTime\": \"\", \"price\": -30.0}"))
                .andExpect(status().isBadRequest());

        verify(reservationService, never()).save(any(Reservation.class));
    }

    @Test
    void update_WhenExists() throws Exception {
        when(reservationService.findById(1L)).thenReturn(Optional.of(reservation1));
        when(reservationService.save(any(Reservation.class))).thenReturn(updatedReservation);

        mockMvc.perform(put("/api/reservations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"court\": {\"id\": 1, \"name\": \"Court 1\", \"surfaceType\": {\"id\": 1, \"name\": \"Clay\", \"pricePerMinute\": 0.5}}, \"user\": {\"id\": 1, \"name\": \"John Doe\", \"phoneNumber\": \"+123456789123\"}, \"startTime\": \"2025-01-14T10:00\", \"endTime\": \"2025-01-14T11:00\", \"price\": 40.0, \"gameType\": \"SINGLE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(40.0));

        verify(reservationService, times(1)).findById(1L);
        verify(reservationService, times(1)).save(any(Reservation.class));
    }

    @Test
    void update_WhenNotExists() throws Exception {
        when(reservationService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/reservations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"court\": {\"id\": 1, \"name\": \"Court 1\", \"surfaceType\": {\"id\": 1, \"name\": \"Clay\", \"pricePerMinute\": 0.5}}, \"user\": {\"id\": 1, \"name\": \"John Doe\", \"phoneNumber\": \"+123456789123\"}, \"startTime\": \"2025-01-14T10:00\", \"endTime\": \"2025-01-14T11:00\", \"price\": 40.0, \"gameType\": \"SINGLE\"}"))
                .andExpect(status().isNotFound());

        verify(reservationService, times(1)).findById(1L);
        verify(reservationService, never()).save(any(Reservation.class));
    }

    @Test
    void deleteById_ShouldDeleteReservation_WhenExists() throws Exception {
        when(reservationService.findById(1L)).thenReturn(Optional.of(reservation1));
        doNothing().when(reservationService).deleteById(1L);

        mockMvc.perform(delete("/api/reservations/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(reservationService, times(1)).findById(1L);
        verify(reservationService, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_ShouldReturnNotFound_WhenNotExists() throws Exception {
        when(reservationService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/reservations/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(reservationService, times(1)).findById(1L);
        verify(reservationService, never()).deleteById(anyLong());
    }

    @Test
    void deleteAll() throws Exception {
        doNothing().when(reservationService).deleteAll();

        mockMvc.perform(delete("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(reservationService, times(1)).deleteAll();
    }

    @Test
    void getReservationsByCourt() throws Exception {
        when(reservationService.getReservationsByCourt(1L)).thenReturn(Arrays.asList(reservation1, reservation2));

        mockMvc.perform(get("/api/reservations/court/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].price").value(30.0))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].price").value(25.0));

        verify(reservationService, times(1)).getReservationsByCourt(1L);
    }

    @Test
    void getReservationsByUser_Ok() throws Exception {
        when(reservationService.getUpcomingReservationsByUser("+420123456789"))
                .thenReturn(Arrays.asList(reservation1, reservation2));

        mockMvc.perform(get("/api/reservations/user/+420123456789")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].price").value(30.0))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].price").value(25.0));

        verify(reservationService, times(1)).getUpcomingReservationsByUser("+420123456789");
    }

    @Test
    void getReservationsByUser_UserNotFound() throws Exception {
        when(reservationService.getUpcomingReservationsByUser("+420123456789"))
                .thenThrow(new IllegalArgumentException("User with phone number +420123456789 not found."));

        mockMvc.perform(get("/api/reservations/user/+420123456789")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User with phone number +420123456789 not found."));

        verify(reservationService, times(1)).getUpcomingReservationsByUser("+420123456789");
    }
}
