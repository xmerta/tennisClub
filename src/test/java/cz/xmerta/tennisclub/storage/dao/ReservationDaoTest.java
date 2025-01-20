package cz.xmerta.tennisclub.storage.dao;

import cz.xmerta.tennisclub.storage.dao.CourtDao;
import cz.xmerta.tennisclub.storage.dao.ReservationDao;
import cz.xmerta.tennisclub.storage.dao.SurfaceTypeDao;
import cz.xmerta.tennisclub.storage.dao.UserDao;
import cz.xmerta.tennisclub.storage.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Transactional
class ReservationDaoTest {

    @PersistenceContext
    private EntityManager entityManager;

    private ReservationDao reservationDao;

    private Court court;
    private User user;
    private Reservation reservation1;
    private Reservation reservation2;

    @BeforeEach
    void setUp() {
        SurfaceTypeDao surfaceTypeDao = new SurfaceTypeDao(entityManager);
        CourtDao courtDao = new CourtDao(entityManager);
        UserDao userDao = new UserDao(entityManager);
        reservationDao = new ReservationDao(entityManager);

        SurfaceType surfaceType = new SurfaceType(null, "Clay", 5.0);
        surfaceTypeDao.save(surfaceType);

        court = new Court(null, "Court 1", surfaceType);
        courtDao.save(court);

        user = new User(null, "+420123456789", "John Doe");
        userDao.save(user);

        reservation1 = new Reservation(
                null,
                user,
                court,
                LocalDateTime.of(2025, 1, 14, 10, 0),
                LocalDateTime.of(2025, 1, 14, 11, 0),
                GameType.SINGLE,
                30.0
        );

        reservation2 = new Reservation(
                null,
                user,
                court,
                LocalDateTime.of(2025, 1, 15, 15, 0),
                LocalDateTime.of(2025, 1, 15, 16, 0),
                GameType.SINGLE,
                25.0
        );
        reservation2.setDeleted(true);

        reservation1 = reservationDao.save(reservation1);
        reservation2 = reservationDao.save(reservation2);
    }

    @Test
    void save_Ok() {
        Reservation newReservation = new Reservation(
                null,
                user,
                court,
                LocalDateTime.of(2025, 1, 16, 10, 0),
                LocalDateTime.of(2025, 1, 16, 11, 0),
                GameType.DOUBLE,
                45.0
        );

        Reservation savedReservation = reservationDao.save(newReservation);

        assertThat(savedReservation.getId()).isNotNull();
        assertThat(savedReservation.getPrice()).isEqualTo(45.0);
    }

    @Test
    void findAll() {
        Collection<Reservation> reservations = reservationDao.findAll();

        assertThat(reservations).hasSize(1);
        assertThat(reservations).extracting(Reservation::getPrice)
                .containsExactlyInAnyOrder(30.0);
    }

    @Test
    void findById_Ok() {
        Optional<Reservation> foundReservation = reservationDao.findById(reservation1.getId());

        assertThat(foundReservation).isPresent();
        assertThat(foundReservation.get().getPrice()).isEqualTo(30.0);
    }

    @Test
    void findById_Deleted() {
        Optional<Reservation> foundReservation = reservationDao.findById(reservation2.getId());

        assertThat(foundReservation).isEmpty();
    }

    @Test
    void findByCourtId() {
        Collection<Reservation> reservations = reservationDao.findByCourtId(court.getId());

        assertThat(reservations).hasSize(1);
        assertThat(reservations).extracting(Reservation::getPrice)
                .containsExactlyInAnyOrder(30.0);
    }

    @Test
    void findByUserId() {
        Collection<Reservation> reservations = reservationDao.findByUserId(user.getId());

        assertThat(reservations).hasSize(1);
        assertThat(reservations).extracting(Reservation::getPrice)
                .containsExactlyInAnyOrder(30.0);
    }

    @Test
    void deleteById() {
        reservationDao.deleteById(reservation1.getId());

        Optional<Reservation> foundReservation = reservationDao.findById(reservation1.getId());
        assertThat(foundReservation).isEmpty();

        Collection<Reservation> reservations = reservationDao.findAll();
        assertThat(reservations).isEmpty();
    }

    @Test
    void deleteAll() {
        reservationDao.deleteAll();

        Collection<Reservation> reservations = reservationDao.findAll();
        assertThat(reservations).isEmpty();
    }
}
