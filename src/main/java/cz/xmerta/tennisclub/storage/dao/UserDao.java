package cz.xmerta.tennisclub.storage.dao;

import cz.xmerta.tennisclub.storage.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDao implements DataAccessObject<User> {

    @PersistenceContext
    private EntityManager entityManager;

    public UserDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            entityManager.persist(user);
        } else {
            user = entityManager.merge(user);
        }
        return user;
    }

    @Override
    public Collection<User> findAll() {
        return entityManager.createQuery(
                        "SELECT u FROM User u WHERE u.isDeleted = false", User.class)
                .getResultList();
    }

    @Override
    public Optional<User> findById(Long id) {
        return entityManager.createQuery(
                        "SELECT u FROM User u WHERE u.id = :id AND u.isDeleted = false", User.class)
                .setParameter("id", id)
                .getResultStream()
                .findFirst();
    }

    @Override
    public void deleteById(Long id) {
        entityManager.createQuery("UPDATE User u SET u.isDeleted = true WHERE u.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public void deleteAll() {
        entityManager.createQuery("UPDATE User u SET u.isDeleted = true")
                .executeUpdate();
    }

    public Optional<User> findByPhoneNumber(String phoneNumber) {
        List<User> result = entityManager.createQuery(
                        "SELECT u FROM User u WHERE u.phoneNumber = :phoneNumber AND u.isDeleted = false", User.class)
                .setParameter("phoneNumber", phoneNumber)
                .getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }
}