package ru.example.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import ru.example.security.JwtTokenProvider;
import ru.example.service.UserService;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@SqlGroup({
        @Sql(value = "classpath:dbscripts/truncateTables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "classpath:dbscripts/insertAndUpdate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
})
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
class AuthControllerTest {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("JUnit positive test for AuthControllerTest registerUser method")
    @Test
    void givenNewUser_whenRegisterUserInvoked_thenStatusOkAndUserInfoReturnedAndNewUserSaved() throws Exception {
        final Path registerJsonPath = new ClassPathResource("authcontrollerjson/registerUser5.json").getFile().toPath();
        final String userToRegister = Files.readString(registerJsonPath);

        mockMvc.perform(post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(userToRegister))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(4))
                        .andExpect(jsonPath("$.login").value("user5"))
                        .andExpect(jsonPath("$.role").value("ROLE_USER"));

        assertNotNull(userService.getUserByLogin("user5"));
    }

    @DisplayName("JUnit negative test for AuthControllerTest registerUser method")
    @Test
    void givenInvalidUser_whenRegisterUserInvoked_thenStatusBadRequestAndErrorMessageReturned() throws Exception {
        final Path invalidRegisterJsonPath = new ClassPathResource("authcontrollerjson/invalidRegisterUser.json").getFile().toPath();
        final String invalidUserToRegister = Files.readString(invalidRegisterJsonPath);

        mockMvc.perform(post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidUserToRegister))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.error").value("Некорректные входные данные. [Логин должен быть не меньше 4 и не больше 30 символов]"));
    }

    @DisplayName("JUnit positive test for AuthControllerTest registerModerator method")
    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    void givenNewModerator_whenRegisterModeratorInvoked_thenStatusOkAndUserInfoReturnedAndNewModeratorSaved() throws Exception {
        final Path registerJsonPath = new ClassPathResource("authcontrollerjson/registerModer5.json").getFile().toPath();
        final String userToRegister = Files.readString(registerJsonPath);

        mockMvc.perform(post("/admin/register-moderator")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(userToRegister))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(4))
                        .andExpect(jsonPath("$.login").value("moder5"))
                        .andExpect(jsonPath("$.role").value("ROLE_MODERATOR"));

        assertNotNull(userService.getUserByLogin("moder5"));
    }

    @DisplayName("JUnit negative test for AuthControllerTest registerModerator method")
    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    void givenInvalidModerator_whenRegisterModeratorInvoked_thenStatusBadRequestAndErrorMessageReturned() throws Exception {
        final Path invalidRegisterJsonPath = new ClassPathResource("authcontrollerjson/invalidRegisterModerator.json").getFile().toPath();
        final String invalidUserToRegister = Files.readString(invalidRegisterJsonPath);

        mockMvc.perform(post("/admin/register-moderator")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidUserToRegister))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.error").value("Некорректные входные данные. [Пароль должен быть не меньше 4 символов]"));
    }

    @DisplayName("JUnit positive test for AuthControllerTest auth method")
    @Test
    void givenUserAdmin_whenAuthInvoked_thenStatusOkAndJWTReturned() throws Exception {
        final Path authJsonPath = new ClassPathResource("authcontrollerjson/loginAdmin.json").getFile().toPath();
        final String userToAuth = Files.readString(authJsonPath);

        mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(userToAuth))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.login").value("admin"))
                        .andExpect(jsonPath("$.accessToken").exists());
    }

    @DisplayName("JUnit negative test for AuthControllerTest auth method")
    @Test
    void givenInvalidUserAdmin_whenAuthInvoked_thenStatusBadRequestAndErrorMessageReturned() throws Exception {
        final Path invalidAuthJsonPath = new ClassPathResource("authcontrollerjson/invalidLoginAdmin.json").getFile().toPath();
        final String invalidUserToAuth = Files.readString(invalidAuthJsonPath);

        mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidUserToAuth))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.error").value("Ошибка аутентификации. Пользователь с указанными данными не найден."));
    }
}