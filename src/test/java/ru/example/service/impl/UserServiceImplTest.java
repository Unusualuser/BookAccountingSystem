package ru.example.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.example.exception.UserAlreadyExistException;
import ru.example.exception.UserNotFoundException;
import ru.example.model.User;
import ru.example.model.fieldenum.UserRole;
import ru.example.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;
    private User user;
    private User invalidUser;

    @BeforeEach
    public void init() {
        user = new User("андрей", "адрес", "+79999999999",
                1L, "andrew", "andrew", UserRole.ROLE_USER, "email@mail.ru");
        invalidUser = new User("", "", "",
                -1L, "", "", UserRole.ROLE_USER, "");
    }

    @DisplayName("JUnit positive test for UserServiceImplTest registerUser method")
    @Test
    void givenUser_whenRegisterUserInvoked_thenRepoContainsByLoginAndRepoSaveUserMethodCalledPasswordEncoded() {
        // arrange
        User userToRegister = user;
        String userLogin = user.getLogin();
        String oldUserPassword = userToRegister.getPassword();

        when(userRepository.containsByLogin(userLogin)).thenReturn(false);

        // act
        userService.registerUser(userToRegister);

        // assert
        verify(userRepository, times(1)).containsByLogin(userLogin);
        assertNotEquals(oldUserPassword, userToRegister.getPassword());
        verify(passwordEncoder, times(1)).encode(oldUserPassword);
        verify(userRepository, times(1)).saveUser(userToRegister);
    }

    @DisplayName("JUnit negative test for UserServiceImplTest registerUser method")
    @Test
    void givenExistingUser_whenRegisterUserInvoked_thenUserAlreadyExistExceptionThrown() {
        // arrange
        User existingUser = invalidUser;

        when(userRepository.containsByLogin(existingUser.getLogin())).thenReturn(true);

        // act and assert
        assertThrows(UserAlreadyExistException.class, () -> userService.registerUser(existingUser));
    }

    @DisplayName("JUnit positive test for UserServiceImplTest updateUserPersonalInfo method")
    @Test
    void givenUserPersonalInfoToUpdate_whenUpdateUserPersonalInfoInvoked_thenUserPersonalInfoUpdated() {
        // arrange
        User oldUser = new User(user.getName(),
                                user.getAddress(),
                                user.getPhoneNumber(),
                                user.getId(),
                                user.getLogin(),
                                user.getPassword(),
                                user.getRole(),
                                user.getEmail());
        String userLogin = user.getLogin();
        String newEmail = "newEmail";
        String newName = "newName";
        String newAddress = "newAdress";
        String newPhoneNumber = "newPhoneNumber";

        when(userRepository.getUserByLogin(userLogin)).thenReturn(user);

        // act
        this.userService.updateUserPersonalInfo(userLogin, newEmail, newName, newAddress, newPhoneNumber);

        // assert
        verify(userRepository, times(1)).getUserByLogin(userLogin);
        assertEquals(oldUser.getId(), user.getId());
        assertEquals(oldUser.getLogin(), user.getLogin());
        assertEquals(oldUser.getPassword(), user.getPassword());
        assertEquals(oldUser.getRole(), user.getRole());
        assertNotEquals(oldUser.getEmail(), user.getEmail());
        assertNotEquals(oldUser.getName(), user.getName());
        assertNotEquals(oldUser.getAddress(), user.getAddress());
        assertNotEquals(oldUser.getPhoneNumber(), user.getPhoneNumber());
    }

    @DisplayName("JUnit negative test for UserServiceImplTest updateUserPersonalInfo method")
    @Test
    void givenNonexistentUserLoginAndUserPersonalInfo_whenUpdateUserPersonalInfoInvoked_thenUserNotFoundExceptionThrown() {
        // arrange
        String nonexistentUserLogin = invalidUser.getLogin();

        doThrow(UserNotFoundException.class).when(userRepository).getUserByLogin(nonexistentUserLogin);

        // act and assert
        assertThrows(UserNotFoundException.class, () -> userService.updateUserPersonalInfo(nonexistentUserLogin, null, null, null, null));
        verify(userRepository, times(1)).getUserByLogin(nonexistentUserLogin);
    }

    @DisplayName("JUnit positive test for UserServiceImplTest getUserById method")
    @Test
    void givenUserId_whenGetUserByIdInvoked_thenUserReturnedAndRepoGetUserByIdMethodCalled() {
        // arrange
        Long userId = user.getId();

        when(userRepository.getUserById(anyLong())).thenReturn(user);

        // act
        User obtainedUser = userService.getUserById(userId);

        // assert
        assertEquals(user.getId(), obtainedUser.getId());
        assertEquals(user.getName(), obtainedUser.getName());
        assertEquals(user.getEmail(), obtainedUser.getEmail());
        assertEquals(user.getLogin(), obtainedUser.getLogin());
        assertEquals(user.getRole(), obtainedUser.getRole());
        assertEquals(user.getAddress(), obtainedUser.getAddress());
        assertEquals(user.getPassword(), obtainedUser.getPassword());
        verify(userRepository, times(1)).getUserById(userId);
    }

    @DisplayName("JUnit negative test for UserServiceImplTest getUserById method")
    @Test
    void givenNonexistentUserId_whenGetUserByIdInvoked_thenUserNotFoundExceptionThrown() {
        // arrange
        Long nonexistentUserId = invalidUser.getId();

        doThrow(UserNotFoundException.class).when(userRepository).getUserById(nonexistentUserId);

        // act and assert
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(nonexistentUserId));
    }

    @DisplayName("JUnit positive test for UserServiceImplTest getUserByLogin method")
    @Test
    void givenUserLogin_whenGetUserByLoginInvoked_thenRepoGetUserByLoginMethodCalled() {
        // arrange
        String userLogin = user.getLogin();

        // act
        userService.getUserByLogin(userLogin);

        // assert
        verify(userRepository, times(1)).getUserByLogin(userLogin);
    }

    @DisplayName("JUnit negative test for UserServiceImplTest getUserByLogin method")
    @Test
    void givenNonexistentUserLogin_whenGetUserByLoginInvoked_thenUserNotFoundExceptionThrown() {
        // arrange
        String nonexistentUserLogin = invalidUser.getLogin();

        doThrow(UserNotFoundException.class).when(userRepository).getUserByLogin(nonexistentUserLogin);

        // act and assert
        assertThrows(UserNotFoundException.class, () -> userService.getUserByLogin(nonexistentUserLogin));
    }

    @DisplayName("JUnit positive test for UserServiceImplTest getUserByLoginAndPassword method")
    @Test
    void givenUserLoginAndPassword_whenGetUserByLoginAndPasswordInvoked_thenRepoGetUserByLoginMethodCalled() {
        // arrange
        String userLogin = user.getLogin();
        String userPassword = user.getPassword();

        when(userRepository.getUserByLogin(userLogin)).thenReturn(user);
        when(passwordEncoder.matches(userPassword, userPassword)).thenReturn(true);

        // act
        userService.getUserByLoginAndPassword(userLogin, userPassword);

        // assert
        verify(userRepository, times(1)).getUserByLogin(userLogin);
        verify(passwordEncoder, times(1)).matches(userPassword, userPassword);
    }

    @DisplayName("JUnit negative test for UserServiceImplTest getUserByLoginAndPassword method")
    @Test
    void givenInvalidUserLoginAndPassword_whenGetUserByLoginAndPasswordInvoked_thenUserNotFoundExceptionThrown() {
        // arrange
        String nonexistentUserLogin = invalidUser.getLogin();
        String invalidUserPassword = invalidUser.getPassword();

        doThrow(UserNotFoundException.class).when(userRepository).getUserByLogin(nonexistentUserLogin);

        // act and assert
        assertThrows(UserNotFoundException.class, () -> userService.getUserByLoginAndPassword(nonexistentUserLogin, invalidUserPassword));
    }
}