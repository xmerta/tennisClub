package cz.xmerta.tennisclub.storage.util;

import cz.xmerta.tennisclub.service.CourtService;
import cz.xmerta.tennisclub.service.SurfaceTypeService;
import cz.xmerta.tennisclub.storage.model.Court;
import cz.xmerta.tennisclub.storage.model.SurfaceType;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class DataInitializer {

    private final SurfaceTypeService surfaceTypeService;
    private final CourtService courtService;

    @Value("${app.data.initialize:false}")
    private boolean initializeData;

    public DataInitializer(SurfaceTypeService surfaceTypeService, CourtService courtService) {
        this.surfaceTypeService = surfaceTypeService;
        this.courtService = courtService;
    }

    @PostConstruct
    public void initialize() {
        if (!initializeData) {
            return;
        }

        SurfaceType clay = new SurfaceType(null, "Clay", 5.0);
        SurfaceType grass = new SurfaceType(null, "Grass", 7.0);

        clay = surfaceTypeService.save(clay);
        grass = surfaceTypeService.save(grass);

        courtService.save(new Court(null, "Court 1", clay));
        courtService.save(new Court(null, "Court 2", clay));
        courtService.save(new Court(null, "Court 3", grass));
        courtService.save(new Court(null, "Court 4", grass));

        System.out.println("Data initialization complete.");
    }
}
