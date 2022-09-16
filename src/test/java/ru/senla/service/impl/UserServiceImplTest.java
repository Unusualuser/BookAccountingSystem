package ru.senla.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.senla.exception.UserAlreadyExistException;
import ru.senla.model.User;
import ru.senla.model.fieldenum.UserRole;
import ru.senla.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
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
    void givenUser_whenRegisterUserInvoked_thenCheckContainsUserAndRepoSaveUserMethodCalledPasswordEncoded() {
        // arrange
        String oldUserPassword = user.getPassword();

        when(userRepository.containsByLogin(user.getLogin())).thenReturn(false);

        // act
        userService.registerUser(user);

        // assert
        assertNotEquals(oldUserPassword, user.getPassword());
        verify(passwordEncoder, times(1)).encode(oldUserPassword);
        verify(userRepository, times(1)).saveUser(user);
    }

//    @DisplayName("JUnit negative test for UserServiceImplTest registerUser method")
//    @Test
//    void givenInvalidUser_whenRegisterUserInvoked_thenUserAlreadyExistExceptionThrown() {
//        // arrange
//        when(userRepository.containsByLogin(user.getLogin())).thenReturn(true);
//
//        // act and assert
//        assertThrows(UserAlreadyExistException.class, () -> userService.registerUser(user));
//    }

    @DisplayName("JUnit positive test for UserServiceImplTest updateUserPersonalInfo method")
    @Test
    void givenUserPersonalInfoToUpdate_whenUpdateUserPersonalInfoInvoked_thenUserPersonalInfoUpdated() {
        // arrange
        User oldUser = new User(
                                user.getName(),
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

//    @DisplayName("JUnit negative test for UserServiceImplTest updateUserPersonalInfo method")
//    @Test
//    void givenNonexistentUserLogin_whenUpdateUserPersonalInfoInvoked_thenUserServiceOperationExceptionThrown() {
//        // arrange
//        String nonexistentUserLogin = invalidUser.getLogin();
//        String newEmail = "newEmail";
//        String newName = "newName";
//        String newAddress = "newAddress";
//        String newPhoneNumber = "newPhoneNumber";
//        doThrow(ObjectNotFoundException.class).when(userRepository).getUserByLogin(nonexistentUserLogin);
//
//        // act and assert
//        assertThrows(UserServiceOperationException.class,
//                () -> userService.updateUserPersonalInfo(nonexistentUserLogin,
//                                                         newEmail,
//                                                         newName,
//                                                         newAddress,
//                                                         newPhoneNumber));
//        verify(userRepository, times(1)).getUserByLogin(nonexistentUserLogin);
//    }

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

//    @DisplayName("JUnit negative test for UserServiceImplTest getUserById method")
//    @Test
//    void givenInvalidUserId_whenGetUserByIdInvoked_thenUserServiceOperationExceptionThrown() {
//        // arrange
//        Long invalidUserId = invalidUser.getId();
//        doThrow(ObjectNotFoundException.class).when(userRepository).getUserById(argThat(argLong -> argLong < 0));
//
//        // act and assert
//        assertThrows(UserServiceOperationException.class, () -> userService.getUserById(invalidUserId));
//    }

    @DisplayName("JUnit positive test for UserServiceImplTest getUserByLogin method")
    @Test
    void givenUserLogin_whenGetUserByLoginInvoked_thenUserReturnedAndRepoGetUserByLoginMethodCalled() {
        // arrange
        String userLogin = user.getLogin();
        when(userRepository.getUserByLogin(userLogin)).thenReturn(user);

        // act
        User obtainedUser = userService.getUserByLogin(userLogin);

        // assert
        assertEquals(user, obtainedUser);
        verify(userRepository, times(1)).getUserByLogin(userLogin);
    }

//    @DisplayName("JUnit negative test for UserServiceImplTest getUserByLogin method")
//    @Test
//    void givenNonexistentUserLogin_whenGetUserByLoginInvoked_thenUserServiceOperationExceptionThrown() {
//        // arrange
//        String nonexistentUserLogin = invalidUser.getLogin();
//        doThrow(ObjectNotFoundException.class).when(userRepository).getUserByLogin(argThat(argString -> argString.equals("")));
//
//        // act and assert
//        assertThrows(UserServiceOperationException.class, () -> userService.getUserByLogin(nonexistentUserLogin));
//    }

    @DisplayName("JUnit positive test for UserServiceImplTest getUserByLoginAndPassword method")
    @Test
    void givenUserLoginAndPassword_whenGetUserByLoginAndPasswordInvoked_thenUserReturnedAndRepoGetUserByLoginMethodCalled() {
        // arrange
        String userLogin = user.getLogin();
        String userPassword = user.getPassword();
        when(userRepository.getUserByLogin(userLogin)).thenReturn(user);
        doReturn(true).when(passwordEncoder).matches(userPassword, userPassword);

        // act
        User obtainedUser = userService.getUserByLoginAndPassword(userLogin, userPassword);

        // assert
        assertEquals(user, obtainedUser);
        verify(userRepository, times(1)).getUserByLogin(userLogin);
        verify(passwordEncoder, times(1)).matches(userPassword, userPassword);
    }

//    @DisplayName("JUnit negative test for UserServiceImplTest getUserByLoginAndPassword method")
//    @Test
//    void givenInvalidUserLoginAndPassword_whenGetUserByLoginAndPasswordInvoked_thenServiceOperationExceptionThrown() {
//        // arrange
//        String invalidUserLogin = invalidUser.getLogin();
//        String invalidUserPassword = invalidUser.getPassword();
//        doThrow(ObjectNotFoundException.class).when(userRepository).getUserByLogin(argThat(argString -> argString.equals("")));
//
//        // act and assert
//        assertThrows(UserServiceOperationException.class, () -> userService.getUserByLoginAndPassword(invalidUserLogin, invalidUserPassword));
//    }
}