package cz.xmerta.tennisclub.storage.dao;

import cz.xmerta.tennisclub.storage.dao.CourtDao;
import cz.xmerta.tennisclub.storage.dao.SurfaceTypeDao;
import cz.xmerta.tennisclub.storage.model.Court;
import cz.xmerta.tennisclub.storage.model.SurfaceType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Transactional
class CourtDaoTest {

    @PersistenceContext
    private EntityManager entityManager;

    private CourtDao courtDao;

    private SurfaceType surfaceType;
    private Court court1;
    private Court court2;

    @BeforeEach
    void setUp() {
        SurfaceTypeDao surfaceTypeDao = new SurfaceTypeDao(entityManager);
        courtDao = new CourtDao(entityManager);

        surfaceType = new SurfaceType(null, "Clay", 5.0);
        court1 = new Court(null, "Court 1", surfaceType);
        court2 = new Court(null, "Court 2", surfaceType);
        court2.setDeleted(true);

        surfaceTypeDao.save(surfaceType);
        court1 = courtDao.save(court1);
        court2 = courtDao.save(court2);
    }

    @Test
    void save_Ok() {
        var newCourt = new Court(null, "Court 3", surfaceType);

        Court savedCourt = courtDao.save(newCourt);

        assertThat(savedCourt.getId()).isNotNull();
        assertThat(savedCourt.getName()).isEqualTo("Court 3");
    }

    @Test
    void save_NotOk() {
        var surfaceType1 = new SurfaceType(1L, "Clay", 5.0);
        var newCourt = new Court(null, "Court 1", surfaceType1);

        assertThrows(jakarta.persistence.PersistenceException.class, () -> {
            courtDao.save(newCourt);
            entityManager.flush();
        });
    }

    @Test
    void findAll() {
        Collection<Court> courts = courtDao.findAll();

        assertThat(courts).hasSize(1);
        assertThat(courts).extracting(Court::getName)
                .containsExactlyInAnyOrder("Court 1");
    }

    @Test
    void findById_Ok() {
        Optional<Court> foundCourt = courtDao.findById(court1.getId());

        assertThat(foundCourt).isPresent();
        assertThat(foundCourt.get().getName()).isEqualTo("Court 1");
    }

    @Test
    void findById_Deleted() {
        Optional<Court> foundCourt = courtDao.findById(court2.getId());

        assertThat(foundCourt).isEmpty();
    }

    @Test
    void deleteById(){
        courtDao.deleteById(court1.getId());

        Optional<Court> foundCourt = courtDao.findById(court1.getId());
        assertThat(foundCourt).isEmpty();

        Collection<Court> courts = courtDao.findAll();
        assertThat(courts).hasSize(0);
    }

    @Test
    void deleteAll() {
        courtDao.deleteAll();

        Collection<Court> courts = courtDao.findAll();
        assertThat(courts).isEmpty();
    }
}
