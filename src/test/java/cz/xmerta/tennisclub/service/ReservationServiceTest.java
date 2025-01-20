package cz.xmerta.tennisclub.service;

import cz.xmerta.tennisclub.storage.dao.ReservationDao;
import cz.xmerta.tennisclub.storage.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    @Mock
    private ReservationDao reservationDao;

    @Mock
    private CourtService courtService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ReservationService reservationService;
    private SurfaceType surfaceType;
    private Court court;
    private User user;
    private User newUser;
    private Reservation reservation1;
    private Reservation reservation2;
    private Reservation reservationNewUser;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        surfaceType = new SurfaceType(1L, "Spikes", 57.0);
        court = new Court(1L, "Court 1", surfaceType);
        user = new User(1L, "+420123456789", "John Doe");
        newUser = new User(null, "+420123456789", "John Doe");

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
                LocalDateTime.of(2025, 1, 14, 12, 0),
                LocalDateTime.of(2025, 1, 14, 13, 0),
                GameType.DOUBLE,
                45.0
        );
    }

    @Test
    void save_Valid() {
        when(courtService.findById(court.getId())).thenReturn(Optional.of(court));
        when(userService.findByPhoneNumber(user.getPhoneNumber())).thenReturn(Optional.of(user));
        when(reservationDao.findAll()).thenReturn(Collections.singletonList(reservation1));
        when(reservationDao.save(any(Reservation.class))).thenReturn(reservation2);

        Reservation savedReservation = reservationService.save(reservation2);

        assertThat(savedReservation).isNotNull();
        assertThat(savedReservation.getId()).isEqualTo(2L);
        assertThat(savedReservation.getPrice()).isEqualTo(5130.0);
        verify(reservationDao, times(1)).save(reservation2);
    }

    @Test
    void save_CourtDoesNotExist() {
        when(courtService.findById(court.getId())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> reservationService.save(reservation1));
        verify(reservationDao, never()).save(any(Reservation.class));
    }

    @Test
    void save_ReservationTimeOverlaps() {
        when(courtService.findById(court.getId())).thenReturn(Optional.of(court));
        when(userService.findByPhoneNumber(user.getPhoneNumber())).thenReturn(Optional.of(user));
        when(reservationDao.findAll()).thenReturn(Collections.singletonList(reservation1));

        Reservation overlappingReservation = new Reservation(
                null,
                user,
                court,
                LocalDateTime.of(2025, 1, 14, 10, 30),
                LocalDateTime.of(2025, 1, 14, 11, 30),
                GameType.SINGLE,
                30.0
        );

        assertThrows(IllegalArgumentException.class, () -> reservationService.save(overlappingReservation));
        verify(reservationDao, never()).save(any(Reservation.class));
    }

    @Test
    void findById_WhenExists() {
        when(reservationDao.findById(1L)).thenReturn(Optional.of(reservation1));

        Optional<Reservation> foundReservation = reservationService.findById(1L);

        assertThat(foundReservation).isPresent();
        assertThat(foundReservation.get().getId()).isEqualTo(1L);
        verify(reservationDao, times(1)).findById(1L);
    }

    @Test
    void findById_WhenNotExists() {
        when(reservationDao.findById(1L)).thenReturn(Optional.empty());

        Optional<Reservation> foundReservation = reservationService.findById(1L);

        assertThat(foundReservation).isEmpty();
        verify(reservationDao, times(1)).findById(1L);
    }

    @Test
    void getReservationsByCourt_ShouldReturnReservations() {
        when(reservationDao.findByCourtId(1L)).thenReturn(Arrays.asList(reservation1, reservation2));

        Collection<Reservation> reservations = reservationService.getReservationsByCourt(1L);

        assertThat(reservations).hasSize(2);
        verify(reservationDao, times(1)).findByCourtId(1L);
    }

    @Test
    void deleteById() {
        doNothing().when(reservationDao).deleteById(1L);

        reservationService.deleteById(1L);

        verify(reservationDao, times(1)).deleteById(1L);
    }

    @Test
    void deleteAll() {
        doNothing().when(reservationDao).deleteAll();

        reservationService.deleteAll();

        verify(reservationDao, times(1)).deleteAll();
    }

    @Test
    void getReservationsByUser_WhenUserExists() {
        when(userService.findByPhoneNumber(user.getPhoneNumber())).thenReturn(Optional.of(user));
        when(reservationDao.findByUserId(user.getId())).thenReturn(Arrays.asList(reservation1, reservation2));

        Collection<Reservation> reservations = reservationService.getReservationsByUser(user.getPhoneNumber());

        assertThat(reservations).hasSize(2);
        assertThat(reservations).extracting(Reservation::getId).containsExactlyInAnyOrder(1L, 2L);
        verify(userService, times(1)).findByPhoneNumber(user.getPhoneNumber());
        verify(reservationDao, times(1)).findByUserId(user.getId());
    }

    @Test
    void getReservationsByCourt_WhenCourtExists() {
        when(reservationDao.findByCourtId(court.getId())).thenReturn(Arrays.asList(reservation1, reservation2));

        Collection<Reservation> reservations = reservationService.getReservationsByCourt(court.getId());

        assertThat(reservations).hasSize(2);
        assertThat(reservations).extracting(Reservation::getId).containsExactlyInAnyOrder(1L, 2L);
        verify(reservationDao, times(1)).findByCourtId(court.getId());
    }

    @Test
    void save_UserDoesNotExist() {
        when(courtService.findById(court.getId())).thenReturn(Optional.of(court));
        when(userService.findByPhoneNumber(newUser.getPhoneNumber())).thenReturn(Optional.empty());
        when(userService.save(any(User.class))).thenReturn(new User(2L, newUser.getPhoneNumber(), newUser.getName()));
        when(reservationDao.findAll()).thenReturn(Collections.singletonList(reservation2));
        when(reservationDao.save(any(Reservation.class))).thenReturn(reservation1);

        Reservation newReservation = new Reservation(
                null,
                newUser,
                court,
                LocalDateTime.of(2025, 1, 15, 10, 0),
                LocalDateTime.of(2025, 1, 15, 11, 0),
                GameType.SINGLE,
                30.0
        );

        Reservation savedReservation = reservationService.save(newReservation);

        assertThat(savedReservation).isNotNull();
        assertThat(savedReservation.getUser().getId()).isEqualTo(1L);
        verify(userService, times(1)).save(any(User.class));
        verify(reservationDao, times(1)).save(newReservation);
    }
}
