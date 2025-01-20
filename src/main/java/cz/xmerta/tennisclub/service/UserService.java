package cz.xmerta.tennisclub.service;

import cz.xmerta.tennisclub.storage.dao.UserDao;
import cz.xmerta.tennisclub.storage.model.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@Transactional
public class UserService implements CrudService<User> {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User save(User user) {
        validateUniquePhoneNumber(user);
        return userDao.save(user);
    }

    private void validateUniquePhoneNumber(User user) {
        Optional<User> existingUser = userDao.findByPhoneNumber(user.getPhoneNumber());
        if (existingUser.isPresent() && !existingUser.get().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Phone number must be unique: " + user.getPhoneNumber());
        }
    }

    @Override
    public Optional<User> findById(long id) {
        return userDao.findById(id);
    }

    @Override
    public Collection<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public void deleteById(long id) {
        userDao.deleteById(id);
    }

    @Override
    public void deleteAll() {
        userDao.deleteAll();
    }

    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return userDao.findByPhoneNumber(phoneNumber);
    }
}
