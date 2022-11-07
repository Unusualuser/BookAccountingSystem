package ru.example.repository.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import ru.example.exception.UserNotFoundException;
import ru.example.model.User;
import ru.example.model.fieldenum.UserRole;
import ru.example.repository.UserRepository;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@SqlGroup({
        @Sql(value = "classpath:dbscripts/truncateTables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "classpath:dbscripts/insertAndUpdate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
})
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
class UserRepositoryImplTest {
    @Autowired
    UserRepository userRepository;

    @DisplayName("Integration positive test for RequestRepositoryImplTest saveUser method")
    @Test
    void givenUser_whenSaveUserInvoked_thenUserSaved() {
        // arrange
        User user = new User("login", "password", UserRole.ROLE_USER);

        // act
        userRepository.saveUser(user);

        // assert
        User savedUser = userRepository.getUserById(4L);
        assertEquals(user.getLogin(), savedUser.getLogin());
        assertEquals(user.getRole(), savedUser.getRole());
        assertEquals(user.getPassword(), savedUser.getPassword());
    }

    @DisplayName("Integration negative test for UserRepositoryImplTest saveUser method")
    @Test
    void givenUserWithExistentLogin_whenSaveUserInvoked_thenDataIntegrityViolationExceptionThrown() {
        // arrange
        User user = new User("user", "password", UserRole.ROLE_USER);

        // act and assert
        assertThrows(DataIntegrityViolationException.class, () ->  userRepository.saveUser(user));
    }

    @DisplayName("Integration positive test for UserRepositoryImplTest getUserById method")
    @Test
    void givenUserId_whenGetUserByIdInvoked_thenUserReturned() {
        // arrange
        Long userId = 1L;

        // act
        User user = userRepository.getUserById(userId);

        // assert
        assertEquals("Анисимов Андрей Андреевич", user.getName());
        assertEquals("admin", user.getLogin());
        assertEquals(UserRole.ROLE_ADMIN, user.getRole());
        assertEquals("Москва, Ломоносовский проспект, 3", user.getAddress());
        assertEquals("+79998883344", user.getPhoneNumber());
    }

    @DisplayName("Integration negative test for UserRepositoryImplTest getUserById method")
    @Test
    void givenNonexistentUserId_whenGetUserByIdInvoked_thenUserNotFoundExceptionThrown() {
        // arrange
        Long nonexistentUserId = 9999L;

        // act and assert
        assertThrows(UserNotFoundException.class, () ->  userRepository.getUserById(nonexistentUserId));
    }

    @DisplayName("Integration positive test for UserRepositoryImplTest getUserByLogin method")
    @Test
    void givenUserLogin_whenGetUserByLoginInvoked_thenUserReturned() {
        // arrange
        String userLogin = "admin";

        // act
        User user = userRepository.getUserByLogin(userLogin);

        // assert
        assertEquals("Анисимов Андрей Андреевич", user.getName());
        assertEquals(1L, user.getId());
        assertEquals(UserRole.ROLE_ADMIN, user.getRole());
        assertEquals("Москва, Ломоносовский проспект, 3", user.getAddress());
        assertEquals("+79998883344", user.getPhoneNumber());
    }

    @DisplayName("Integration negative test for UserRepositoryImplTest getUserByLogin method")
    @Test
    void givenNonexistentUserLogin_whenGetUserByLoginInvoked_thenUserNotFoundExceptionThrown() {
        // arrange
        String nonexistentUserLogin = "nonexistentUser";

        // act and assert
        assertThrows(UserNotFoundException.class, () ->  userRepository.getUserByLogin(nonexistentUserLogin));
    }

    @DisplayName("Integration positive test for UserRepositoryImplTest containsByLogin method")
    @Test
    void givenExistentUser_whenContainsByLoginInvoked_thenTrueReturned() {
        // arrange
        String userLogin = "admin";

        // act and assert
        assertTrue(userRepository.containsByLogin(userLogin));
    }

    @DisplayName("Integration positive test for UserRepositoryImplTest containsByLogin method")
    @Test
    void givenNonexistentUser_whenContainsByLoginInvoked_thenFalseReturned() {
        // arrange
        String nonexistentUserLogin = "nonexistentUser";

        // act and assert
        assertFalse(userRepository.containsByLogin(nonexistentUserLogin));
    }
}