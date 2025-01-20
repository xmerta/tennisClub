package cz.xmerta.tennisclub.storage.dao;

import cz.xmerta.tennisclub.storage.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Transactional
class UserDaoTest {

    @PersistenceContext
    private EntityManager entityManager;

    private UserDao userDao;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        userDao = new UserDao(entityManager);

        user1 = new User(null, "+420123456789", "John Doe");
        user2 = new User(null, "+420123456788", "Jane Smith");
        user2.setDeleted(true);

        user1 = userDao.save(user1);
        user2 = userDao.save(user2);
    }

    @Test
    void save_Ok() {
        var newUser = new User(null, "+420123456787", "Alice Brown");

        User savedUser = userDao.save(newUser);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("Alice Brown");
        assertThat(savedUser.getPhoneNumber()).isEqualTo("+420123456787");
    }

    @Test
    void save_NotOk() {
        var duplicateUser = new User(null, "+420123456788", "Duplicate User");

        assertThrows(jakarta.persistence.PersistenceException.class, () -> {
            userDao.save(duplicateUser);
            entityManager.flush();
        });
    }

    @Test
    void findAll() {
        Collection<User> users = userDao.findAll();

        assertThat(users).hasSize(1);
        assertThat(users).extracting(User::getName)
                .containsExactlyInAnyOrder("John Doe");
    }

    @Test
    void findById_Ok() {
        Optional<User> foundUser = userDao.findById(user1.getId());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("John Doe");
    }

    @Test
    void findById_Deleted() {
        Optional<User> foundUser = userDao.findById(user2.getId());

        assertThat(foundUser).isEmpty();
    }

    @Test
    void findByPhoneNumber_Ok() {
        Optional<User> foundUser = userDao.findByPhoneNumber("+420123456789");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("John Doe");
    }

    @Test
    void findByPhoneNumber_Deleted() {
        Optional<User> foundUser = userDao.findByPhoneNumber("+420123456788");

        assertThat(foundUser).isEmpty();
    }

    @Test
    void deleteById() {
        userDao.deleteById(user1.getId());

        Optional<User> foundUser = userDao.findById(user1.getId());
        assertThat(foundUser).isEmpty();

        Collection<User> users = userDao.findAll();
        assertThat(users).isEmpty();
    }

    @Test
    void deleteAll() {
        userDao.deleteAll();

        Collection<User> users = userDao.findAll();
        assertThat(users).isEmpty();
    }
}
