package cz.xmerta.tennisclub.controller;

import cz.xmerta.tennisclub.service.CourtService;
import cz.xmerta.tennisclub.storage.model.Court;
import cz.xmerta.tennisclub.storage.model.SurfaceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourtController.class)
class CourtControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CourtService courtService;

    private Court court1;
    private Court court2;
    private Court updatedCourt1;

    @BeforeEach
    void setUp() {
        SurfaceType surfaceType1 = new SurfaceType();
        surfaceType1.setId(1L);
        surfaceType1.setName("Bricks");
        surfaceType1.setPricePerMinute(0.5);

        court1 = new Court();
        court1.setId(1L);
        court1.setName("Court 1");
        court1.setSurfaceType(surfaceType1);

        court2 = new Court();
        court2.setId(2L);
        court2.setName("Court 2");
        court2.setSurfaceType(surfaceType1);

        updatedCourt1 = new Court();
        updatedCourt1.setId(1L);
        updatedCourt1.setName("Updated Court");
        updatedCourt1.setSurfaceType(surfaceType1);
    }

    @Test
    void getAll() throws Exception {
        when(courtService.findAll()).thenReturn(Arrays.asList(court1, court2));

        mockMvc.perform(get("/api/courts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Court 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Court 2"));

        verify(courtService, times(1)).findAll();
    }

    @Test
    void getById_WhenExists() throws Exception {
        when(courtService.findById(1L)).thenReturn(Optional.of(court1));

        mockMvc.perform(get("/api/courts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Court 1"));

        verify(courtService, times(1)).findById(1L);
    }

    @Test
    void getById_WhenNotExists() throws Exception {
        when(courtService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/courts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(courtService, times(1)).findById(1L);
    }

    @Test
    void create_Ok() throws Exception {
        when(courtService.save(any(Court.class))).thenReturn(court1);

        mockMvc.perform(post("/api/courts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Court 1\", \"surfaceType\": {\"id\": 1}}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Court 1"));

        verify(courtService, times(1)).save(any(Court.class));
    }

    @Test
    void create_NotOK() throws Exception {
        when(courtService.save(any(Court.class))).thenReturn(court1);

        mockMvc.perform(post("/api/courts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"C\", \"surfaceType\": {\"id\": 1}}"))
                .andExpect(status().isBadRequest());

        verify(courtService, times(0)).save(any(Court.class));
    }

    @Test
    void update_WhenExists() throws Exception {
        when(courtService.findById(1L)).thenReturn(Optional.of(court1));
        when(courtService.save(any(Court.class))).thenReturn(updatedCourt1);

        mockMvc.perform(put("/api/courts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"name\": \"Updated Court\", \"surfaceType\": {\"id\": 1}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Court"));

        verify(courtService, times(1)).findById(1L);
        verify(courtService, times(1)).save(any(Court.class));
    }

    @Test
    void update_WhenExistsMissingSurface() throws Exception {
        when(courtService.findById(1L)).thenReturn(Optional.of(court1));
        when(courtService.save(any(Court.class))).thenReturn(court1);

        mockMvc.perform(put("/api/courts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"name\": \"Updated Court\"}"))
                .andExpect(status().isBadRequest());

        verify(courtService, times(0)).findById(1L);
        verify(courtService, times(0)).save(any(Court.class));
    }

    @Test
    void update_NotFound() throws Exception {
        when(courtService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/courts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"name\": \"Updated Court\", \"surfaceType\": {\"id\": 1}}"))
                .andExpect(status().isNotFound());

        verify(courtService, times(1)).findById(1L);
        verify(courtService, never()).save(any(Court.class));
    }

    @Test
    void deleteById_WhenExists() throws Exception {
        when(courtService.findById(1L)).thenReturn(Optional.of(court1));
        doNothing().when(courtService).deleteById(1L);

        mockMvc.perform(delete("/api/courts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(courtService, times(1)).findById(1L);
        verify(courtService, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_WhenCourtNotExists() throws Exception {
        when(courtService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/courts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(courtService, times(1)).findById(1L);
        verify(courtService, never()).deleteById(anyLong());
    }

    @Test
    void deleteAll() throws Exception {
        doNothing().when(courtService).deleteAll();

        mockMvc.perform(delete("/api/courts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(courtService, times(1)).deleteAll();
    }
}
