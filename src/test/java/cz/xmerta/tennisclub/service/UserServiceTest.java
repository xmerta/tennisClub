package cz.xmerta.tennisclub.service;

import cz.xmerta.tennisclub.storage.dao.UserDao;
import cz.xmerta.tennisclub.storage.model.User;
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

class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    private User newUser;
    private User existingUser;
    private User updatedUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        newUser = new User(null, "+420123456789", "John Doe");
        existingUser = new User(1L, "+420123456789", "John Doe");
        updatedUser = new User(1L, "+420123456787", "John Smith");
    }

    @Test
    void save_UniquePhoneNumber() {
        when(userDao.findByPhoneNumber(newUser.getPhoneNumber())).thenReturn(Optional.empty());
        when(userDao.save(newUser)).thenReturn(existingUser);

        User savedUser = userService.save(newUser);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getPhoneNumber()).isEqualTo("+420123456789");
        assertThat(savedUser.getName()).isEqualTo("John Doe");

        verify(userDao, times(1)).findByPhoneNumber(newUser.getPhoneNumber());
        verify(userDao, times(1)).save(newUser);
    }

    @Test
    void save_DuplicatePhoneNumber() {
        when(userDao.findByPhoneNumber(newUser.getPhoneNumber())).thenReturn(Optional.of(existingUser));

        assertThrows(IllegalArgumentException.class, () -> userService.save(newUser));

        verify(userDao, times(1)).findByPhoneNumber(newUser.getPhoneNumber());
        verify(userDao, never()).save(any(User.class));
    }

    @Test
    void save_UpdateExistingUser() {
        when(userDao.findByPhoneNumber(updatedUser.getPhoneNumber())).thenReturn(Optional.empty());
        when(userDao.save(updatedUser)).thenReturn(updatedUser);

        User savedUser = userService.save(updatedUser);

        assertThat(savedUser.getId()).isEqualTo(1L);
        assertThat(savedUser.getPhoneNumber()).isEqualTo("+420123456787");
        assertThat(savedUser.getName()).isEqualTo("John Smith");

        verify(userDao, times(1)).findByPhoneNumber(updatedUser.getPhoneNumber());
        verify(userDao, times(1)).save(updatedUser);
    }

    @Test
    void findById_Exists() {
        when(userDao.findById(1L)).thenReturn(Optional.of(existingUser));

        Optional<User> result = userService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getPhoneNumber()).isEqualTo("+420123456789");

        verify(userDao, times(1)).findById(1L);
    }

    @Test
    void findById_NotExists() {
        when(userDao.findById(1L)).thenReturn(Optional.empty());

        Optional<User> result = userService.findById(1L);

        assertThat(result).isEmpty();

        verify(userDao, times(1)).findById(1L);
    }

    @Test
    void findAll() {
        when(userDao.findAll()).thenReturn(Arrays.asList(existingUser, updatedUser));

        Collection<User> users = userService.findAll();

        assertThat(users).hasSize(2);
        assertThat(users).extracting(User::getPhoneNumber).containsExactlyInAnyOrder("+420123456789", "+420123456787");

        verify(userDao, times(1)).findAll();
    }

    @Test
    void deleteById() {
        doNothing().when(userDao).deleteById(1L);

        userService.deleteById(1L);

        verify(userDao, times(1)).deleteById(1L);
    }

    @Test
    void deleteAll() {
        doNothing().when(userDao).deleteAll();

        userService.deleteAll();

        verify(userDao, times(1)).deleteAll();
    }

    @Test
    void findByPhoneNumber_Exists() {
        when(userDao.findByPhoneNumber("+420123456789")).thenReturn(Optional.of(existingUser));

        Optional<User> result = userService.findByPhoneNumber("+420123456789");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("John Doe");

        verify(userDao, times(1)).findByPhoneNumber("+420123456789");
    }

    @Test
    void findByPhoneNumber_NotExists() {
        when(userDao.findByPhoneNumber("+420123456789")).thenReturn(Optional.empty());

        Optional<User> result = userService.findByPhoneNumber("+420123456789");

        assertThat(result).isEmpty();

        verify(userDao, times(1)).findByPhoneNumber("+420123456789");
    }
}
