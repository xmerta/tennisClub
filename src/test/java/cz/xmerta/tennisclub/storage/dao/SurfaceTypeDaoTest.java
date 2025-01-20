package cz.xmerta.tennisclub.storage.dao;

import cz.xmerta.tennisclub.storage.dao.SurfaceTypeDao;
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

@DataJpaTest
@Transactional
class SurfaceTypeDaoTest {

    @PersistenceContext
    private EntityManager entityManager;

    private SurfaceTypeDao surfaceTypeDao;

    private SurfaceType surfaceType1;
    private SurfaceType surfaceType2;

    @BeforeEach
    void setUp() {
        surfaceTypeDao = new SurfaceTypeDao(entityManager);
        surfaceType1 = new SurfaceType(null, "Clay", 5.0);
        surfaceType2 = new SurfaceType(null, "Grass", 10.0);
        surfaceType2.setDeleted(true);

        surfaceTypeDao.save(surfaceType1);
        surfaceTypeDao.save(surfaceType2);
    }

    @Test
    void save() {
        SurfaceType newSurfaceType = new SurfaceType(null, "Hard", 0.5);

        SurfaceType savedSurfaceType = surfaceTypeDao.save(newSurfaceType);

        assertThat(savedSurfaceType.getId()).isNotNull();
        assertThat(savedSurfaceType.getName()).isEqualTo("Hard");
    }

    @Test
    void findAll() {
        Collection<SurfaceType> surfaceTypes = surfaceTypeDao.findAll();

        assertThat(surfaceTypes).hasSize(1);
        assertThat(surfaceTypes).extracting(SurfaceType::getName)
                .containsExactlyInAnyOrder("Clay");
    }

    @Test
    void findById() {
        Optional<SurfaceType> foundSurfaceType = surfaceTypeDao.findById(surfaceType1.getId());

        assertThat(foundSurfaceType).isPresent();
        assertThat(foundSurfaceType.get().getName()).isEqualTo("Clay");
    }

    @Test
    void findById_Deleted() {
        Optional<SurfaceType> foundSurfaceType = surfaceTypeDao.findById(surfaceType2.getId());

        assertThat(foundSurfaceType).isEmpty();
    }

    @Test
    void deleteById() {
        surfaceTypeDao.deleteById(surfaceType1.getId());
        Optional<SurfaceType> foundSurfaceType = surfaceTypeDao.findById(surfaceType1.getId());
        assertThat(foundSurfaceType).isEmpty();

        Collection<SurfaceType> surfaceTypes = surfaceTypeDao.findAll();
        assertThat(surfaceTypes).hasSize(0);
    }

    @Test
    void deleteAll() {
        surfaceTypeDao.deleteAll();

        Collection<SurfaceType> surfaceTypes = surfaceTypeDao.findAll();
        assertThat(surfaceTypes).isEmpty();
    }
}
