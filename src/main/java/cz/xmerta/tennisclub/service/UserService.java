package cz.xmerta.tennisclub.service;

import cz.xmerta.tennisclub.storage.dao.UserDao;
import cz.xmerta.tennisclub.storage.model.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

/**
 * Service class for managing {@link User} entities. Provides CRUD operations and ensures unique phone numbers for users.
 */
@Service
@Transactional
public class UserService implements CrudService<User> {

    private final UserDao userDao;

    /**
     * Constructor for {@link UserService}.
     *
     * @param userDao the DAO for managing {@link User} entities
     */
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Saves a {@link User} entity after validating the uniqueness of its phone number.
     *
     * @param user the User entity to save
     * @return the saved User entity
     * @throws IllegalArgumentException if the phone number is not unique
     */
    @Override
    public User save(User user) {
        validateUniquePhoneNumber(user);
        return userDao.save(user);
    }

    /**
     * Validates that the phone number of the given {@link User} is unique. If the ID matches an existing entry,
     * it is considered an update and no exception is thrown.
     *
     * @param user the User entity to validate
     * @throws IllegalArgumentException if the phone number is not unique
     */
    private void validateUniquePhoneNumber(User user) {
        Optional<User> existingUser = userDao.findByPhoneNumber(user.getPhoneNumber());
        if (existingUser.isPresent() && !existingUser.get().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Phone number must be unique: " + user.getPhoneNumber());
        }
    }

    /**
     * Finds a {@link User} by its ID.
     *
     * @param id the ID of the User to find
     * @return an {@link Optional} containing the found User, or empty if not found
     */
    @Override
    public Optional<User> findById(long id) {
        return userDao.findById(id);
    }

    /**
     * Retrieves all {@link User} entities.
     *
     * @return a collection of all User entities
     */
    @Override
    public Collection<User> findAll() {
        return userDao.findAll();
    }

    /**
     * Deletes a {@link User} by its ID.
     *
     * @param id the ID of the User to delete
     */
    @Override
    public void deleteById(long id) {
        userDao.deleteById(id);
    }

    /**
     * Deletes all {@link User} entities.
     */
    @Override
    public void deleteAll() {
        userDao.deleteAll();
    }

    /**
     * Finds a {@link User} by their phone number.
     *
     * @param phoneNumber the phone number of the User to find
     * @return an {@link Optional} containing the found User, or empty if not found
     */
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return userDao.findByPhoneNumber(phoneNumber);
    }
}
