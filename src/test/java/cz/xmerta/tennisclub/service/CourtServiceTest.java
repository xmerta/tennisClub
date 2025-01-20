package cz.xmerta.tennisclub.service;

import cz.xmerta.tennisclub.storage.dao.CourtDao;
import cz.xmerta.tennisclub.storage.model.Court;
import cz.xmerta.tennisclub.storage.model.SurfaceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CourtServiceTest {

    private CourtDao courtDao;
    private SurfaceTypeService surfaceTypeService;
    private CourtService courtService;

    private SurfaceType surfaceType;
    private Court court1;
    private Court newCourt1;
    private Court court2;

    @BeforeEach
    void setUp() {
        courtDao = mock(CourtDao.class);
        surfaceTypeService = mock(SurfaceTypeService.class);
        courtService = new CourtService(courtDao, surfaceTypeService);

        surfaceType = new SurfaceType(1L, "Clay", 5.0);
        court1 = new Court(1L, "Court 1", surfaceType);
        newCourt1 = new Court(null, "Court 1", surfaceType);
        court2 = new Court(2L, "Court 2", surfaceType);
    }

    @Test
    void save_Ok() {
        when(surfaceTypeService.findById(surfaceType.getId())).thenReturn(Optional.of(surfaceType));
        when(courtDao.save(newCourt1)).thenReturn(court1);

        Court savedCourt = courtService.save(newCourt1);

        assertThat(savedCourt).isEqualTo(court1);
        verify(courtDao, times(1)).save(newCourt1);
    }

    @Test
    void save_InvalidSurfaceType() {
        when(surfaceTypeService.findById(surfaceType.getId())).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> courtService.save(court1));

        assertThat(exception.getMessage()).isEqualTo("Invalid SurfaceType ID: " + surfaceType.getId());
        verify(courtDao, never()).save(any(Court.class));
    }

    @Test
    void save_DuplicateName() {
        when(surfaceTypeService.findById(surfaceType.getId())).thenReturn(Optional.of(surfaceType));
        when(courtDao.findByName(court1.getName())).thenReturn(Optional.ofNullable(court1));

        court2.setName(court1.getName());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> courtService.save(court2));

        assertThat(exception.getMessage()).isEqualTo("Court name must be unique: " + court1.getName());
        verify(courtDao, never()).save(any(Court.class));
    }

    @Test
    void findById_WhenExists() {
        when(courtDao.findById(court1.getId())).thenReturn(Optional.of(court1));

        Optional<Court> foundCourt = courtService.findById(court1.getId());

        assertThat(foundCourt).isPresent();
        assertThat(foundCourt.get()).isEqualTo(court1);
        verify(courtDao, times(1)).findById(court1.getId());
    }

    @Test
    void findById_WhenNotExists() {
        when(courtDao.findById(court1.getId())).thenReturn(Optional.empty());

        Optional<Court> foundCourt = courtService.findById(court1.getId());

        assertThat(foundCourt).isEmpty();
        verify(courtDao, times(1)).findById(court1.getId());
    }

    @Test
    void findAll() {
        when(courtDao.findAll()).thenReturn(Arrays.asList(court1, court2));

        Collection<Court> courts = courtService.findAll();

        assertThat(courts).hasSize(2);
        assertThat(courts).containsExactlyInAnyOrder(court1, court2);
        verify(courtDao, times(1)).findAll();
    }

    @Test
    void deleteById() {
        doNothing().when(courtDao).deleteById(court1.getId());

        courtService.deleteById(court1.getId());

        verify(courtDao, times(1)).deleteById(court1.getId());
    }

    @Test
    void deleteAll() {
        doNothing().when(courtDao).deleteAll();

        courtService.deleteAll();

        verify(courtDao, times(1)).deleteAll();
    }
}
