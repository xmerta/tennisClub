    package cz.xmerta.tennisclub.service;

    import cz.xmerta.tennisclub.storage.model.SurfaceType;
    import cz.xmerta.tennisclub.storage.dao.SurfaceTypeDao;
    import jakarta.transaction.Transactional;
    import org.springframework.stereotype.Service;

    import java.util.Collection;
    import java.util.Optional;

    @Service
    @Transactional
    public class SurfaceTypeService implements CrudService<SurfaceType> {

        private final SurfaceTypeDao surfaceTypeDao;

        public SurfaceTypeService(SurfaceTypeDao surfaceTypeDao) {
            this.surfaceTypeDao = surfaceTypeDao;
        }

        @Override
        public SurfaceType save(SurfaceType surfaceType) {
            validateUniqueName(surfaceType);
            return surfaceTypeDao.save(surfaceType);
        }

        /**
         *
         * @param surfaceType checks using surfaceTypeDao unique name,
         *                    if the ID is same does not throw exception as it means update
         */
        private void validateUniqueName(SurfaceType surfaceType) {
            Optional<SurfaceType> existing = surfaceTypeDao.findByName(surfaceType.getName());
            if (existing.isPresent() && !existing.get().getId().equals(surfaceType.getId())) {
                throw new IllegalArgumentException("Surface type name must be unique: " + surfaceType.getName());
            }
        }

        @Override
        public Optional<SurfaceType> findById(long id) {
            return surfaceTypeDao.findById(id);
        }

        @Override
        public Collection<SurfaceType> findAll() {
            return surfaceTypeDao.findAll();
        }

        @Override
        public void deleteById(long id) {
            surfaceTypeDao.deleteById(id);
        }

        @Override
        public void deleteAll() {
            surfaceTypeDao.deleteAll();
        }
    }
