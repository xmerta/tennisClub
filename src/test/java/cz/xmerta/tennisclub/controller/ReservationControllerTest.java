package cz.xmerta.tennisclub.controller;

import cz.xmerta.tennisclub.service.CourtService;
import cz.xmerta.tennisclub.service.ReservationService;
import cz.xmerta.tennisclub.storage.model.Court;
import cz.xmerta.tennisclub.storage.model.Reservation;
import cz.xmerta.tennisclub.storage.model.SurfaceType;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ReservationController.class)
public class ReservationTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ReservationService reservationService;

    private Reservation reservation1;
    private Reservation reservation2;
    private Court court1;
    private Court court2;
    private SurfaceType surfaceType1;


    @BeforeEach
    void setUp() {

        surfaceType1 = new SurfaceType();
        surfaceType1.setId(1L);
        surfaceType1.setName("Clay");
        surfaceType1.setPricePerMinute(0.5);

        court1 = new Court();
        court1.setId(1L);
        court1.setName("Court 1");
        court1.setSurfaceType(surfaceType1);

        court2 = new Court();
        court2.setId(2L);
        court2.setName("Court 2");
        court2.setSurfaceType(surfaceType1);

        reservation1 = new Reservation();
        reservation1.setId(1L);
        reservation1.setCourt(court1);
        reservation2 = new Reservation();
    }
}
