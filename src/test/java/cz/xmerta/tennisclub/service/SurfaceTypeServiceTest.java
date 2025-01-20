package cz.xmerta.tennisclub.service;

import cz.xmerta.tennisclub.storage.dao.SurfaceTypeDao;
import cz.xmerta.tennisclub.storage.model.SurfaceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class SurfaceTypeServiceTest {

    @Mock
    private SurfaceTypeDao surfaceTypeDao;

    @InjectMocks
    private SurfaceTypeService surfaceTypeService;

    private SurfaceType newSurfaceType1;
    private SurfaceType surfaceType1;
    private SurfaceType surfaceType2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        newSurfaceType1 = new SurfaceType(null, "Clay", 5.0);
        surfaceType1 = new SurfaceType(1L, "Clay", 5.0);
        surfaceType2 = new SurfaceType(2L, "Grass", 7.0);
    }

    @Test
    void save_Unique() {
        when(surfaceTypeDao.findByName(newSurfaceType1.getName())).thenReturn(Optional.empty());
        when(surfaceTypeDao.save(newSurfaceType1)).thenReturn(surfaceType1);

        SurfaceType savedSurfaceType = surfaceTypeService.save(newSurfaceType1);

        assertThat(savedSurfaceType).isNotNull();
        assertThat(savedSurfaceType.getName()).isEqualTo("Clay");
        verify(surfaceTypeDao, times(1)).findByName(newSurfaceType1.getName());
        verify(surfaceTypeDao, times(1)).save(newSurfaceType1);
    }

    @Test
    void save_Update() {
        when(surfaceTypeDao.save(surfaceType1)).thenReturn(surfaceType1);

        SurfaceType saved = surfaceTypeService.save(surfaceType1);

        assertThat(saved.getId()).isEqualTo(1L);
        verify(surfaceTypeDao, times(1)).save(surfaceType1);
    }

    @Test
    void findById_Exists() {
        when(surfaceTypeDao.findById(1L)).thenReturn(Optional.of(surfaceType1));

        Optional<SurfaceType> result = surfaceTypeService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Clay");
        verify(surfaceTypeDao, times(1)).findById(1L);
    }

    @Test
    void findById_NotExists() {
        when(surfaceTypeDao.findById(1L)).thenReturn(Optional.empty());

        Optional<SurfaceType> result = surfaceTypeService.findById(1L);

        assertThat(result).isEmpty();
        verify(surfaceTypeDao, times(1)).findById(1L);
    }

    @Test
    void findAll() {
        when(surfaceTypeDao.findAll()).thenReturn(Arrays.asList(surfaceType1, surfaceType2));

        Collection<SurfaceType> surfaceTypes = surfaceTypeService.findAll();

        assertThat(surfaceTypes).hasSize(2);
        assertThat(surfaceTypes).extracting(SurfaceType::getName).containsExactlyInAnyOrder("Clay", "Grass");
        verify(surfaceTypeDao, times(1)).findAll();
    }

    @Test
    void deleteById() {
        doNothing().when(surfaceTypeDao).deleteById(1L);

        surfaceTypeService.deleteById(1L);

        verify(surfaceTypeDao, times(1)).deleteById(1L);
    }

    @Test
    void deleteAll() {
        doNothing().when(surfaceTypeDao).deleteAll();

        surfaceTypeService.deleteAll();

        verify(surfaceTypeDao, times(1)).deleteAll();
    }
}
