package cz.xmerta.tennisclub.controller;

import cz.xmerta.tennisclub.service.SurfaceTypeService;
import cz.xmerta.tennisclub.storage.model.SurfaceType;
import cz.xmerta.tennisclub.controller.dto.mapper.SurfaceTypeDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SurfaceTypeController.class)
@Import(SurfaceTypeDtoMapper.class)
class SurfaceTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SurfaceTypeService surfaceTypeService;

    private SurfaceType surfaceType1;
    private SurfaceType surfaceType2;
    private SurfaceType updatedSurfaceType1;

    @BeforeEach
    void setUp() {
        surfaceType1 = new SurfaceType(1L, "Clay", 0.5);
        surfaceType2 = new SurfaceType(2L, "Grass", 0.8);
        updatedSurfaceType1 = new SurfaceType(1L, "Updated Clay", 0.5);
    }

    @Test
    void getAll() throws Exception {
        when(surfaceTypeService.findAll()).thenReturn(Arrays.asList(surfaceType1, surfaceType2));

        mockMvc.perform(get("/api/surfacetypes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Clay"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Grass"));

        verify(surfaceTypeService, times(1)).findAll();
    }

    @Test
    void getById_WhenExists() throws Exception {
        when(surfaceTypeService.findById(1L)).thenReturn(Optional.of(surfaceType1));

        mockMvc.perform(get("/api/surfacetypes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Clay"));

        verify(surfaceTypeService, times(1)).findById(1L);
    }

    @Test
    void getById_WhenNotExists() throws Exception {
        when(surfaceTypeService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/surfacetypes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(surfaceTypeService, times(1)).findById(1L);
    }

    @Test
    void create_Ok() throws Exception {
        when(surfaceTypeService.save(any(SurfaceType.class))).thenReturn(surfaceType1);

        mockMvc.perform(post("/api/surfacetypes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Clay\", \"pricePerMinute\": 0.5}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Clay"));

        verify(surfaceTypeService, times(1)).save(any(SurfaceType.class));
    }

    @Test
    void create_NotOk() throws Exception {
        mockMvc.perform(post("/api/surfacetypes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Apollo\", \"pricePerMinute\": -0.5}"))
                .andExpect(status().isBadRequest());

        verify(surfaceTypeService, never()).save(any(SurfaceType.class));
    }

    @Test
    void update_WhenExists() throws Exception {
        when(surfaceTypeService.findById(1L)).thenReturn(Optional.of(surfaceType1));
        when(surfaceTypeService.save(any(SurfaceType.class))).thenReturn(updatedSurfaceType1);

        mockMvc.perform(put("/api/surfacetypes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"name\": \"Updated Clay\", \"pricePerMinute\": 0.6}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Clay"));

        verify(surfaceTypeService, times(1)).findById(1L);
        verify(surfaceTypeService, times(1)).save(any(SurfaceType.class));
    }

    @Test
    void update_WhenNotExists() throws Exception {
        when(surfaceTypeService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/surfacetypes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"name\": \"Updated Clay\", \"pricePerMinute\": 0.6}"))
                .andExpect(status().isNotFound());

        verify(surfaceTypeService, times(1)).findById(1L);
        verify(surfaceTypeService, never()).save(any(SurfaceType.class));
    }

    @Test
    void deleteById_WhenExists() throws Exception {
        when(surfaceTypeService.findById(1L)).thenReturn(Optional.of(surfaceType1));
        doNothing().when(surfaceTypeService).deleteById(1L);

        mockMvc.perform(delete("/api/surfacetypes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(surfaceTypeService, times(1)).findById(1L);
        verify(surfaceTypeService, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_WhenNotExists() throws Exception {
        when(surfaceTypeService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/surfacetypes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(surfaceTypeService, times(1)).findById(1L);
        verify(surfaceTypeService, never()).deleteById(anyLong());
    }
}
