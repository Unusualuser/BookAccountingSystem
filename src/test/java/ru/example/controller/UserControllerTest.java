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
import ru.example.model.User;
import ru.example.service.UserService;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
class UserControllerTest {
    @Autowired
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("JUnit positive test for UserControllerTest updateUserPersonalInfo method")
    @Test
    @WithMockUser(username = "user", password = "user", roles = {"USER"})
    void givenUserPersonalInfo_whenUpdateUserPersonalInfoInvoked_thenStatusOkAndUserPersonalInfoUpdated() throws Exception {
        String userLogin = "user";
        final Path updateUserPersonalInfoJsonPath = new ClassPathResource("usercontrollerjson/updateUserPersonalInfo.json").getFile().toPath();
        final String updateUserPersonalInfoBody = Files.readString(updateUserPersonalInfoJsonPath);
        User oldUser = userService.getUserByLogin(userLogin);

        mockMvc.perform(patch("/user/update-personal-info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateUserPersonalInfoBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value(userLogin))
                .andExpect(jsonPath("$.email").value("danil@mail.ru"))
                .andExpect(jsonPath("$.name").value("Лежебоков Данил Артурович"))
                .andExpect(jsonPath("$.address").isEmpty())
                .andExpect(jsonPath("$.phoneNumber").isEmpty());

        User updatedUser = userService.getUserByLogin(userLogin);
        assertNotEquals(oldUser.getEmail(), updatedUser.getEmail());
        assertNotEquals(oldUser.getName(), updatedUser.getName());
        assertEquals(oldUser.getAddress(), updatedUser.getAddress());
        assertEquals(oldUser.getPhoneNumber(), updatedUser.getPhoneNumber());
    }

    @DisplayName("JUnit negative test for UserControllerTest updateUserPersonalInfo method")
    @Test
    @WithMockUser(username = "user", password = "user", roles = {"USER"})
    void givenInvalidUserPersonalInfo_whenUpdateUserPersonalInfoInvoked_thenStatusBadRequestAndErrorMessageReturned() throws Exception {
        final Path invalidUpdateUserPersonalInfoJsonPath = new ClassPathResource("usercontrollerjson/invalidUpdateUserPersonalInfo.json").getFile().toPath();
        final String invalidUpdateUserPersonalInfoBody = Files.readString(invalidUpdateUserPersonalInfoJsonPath);

        mockMvc.perform(patch("/user/update-personal-info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUpdateUserPersonalInfoBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Некорректные входные данные. [Email должен быть корректен]"));
    }

    @DisplayName("JUnit positive test for UserControllerTest getUserById method")
    @Test
    @WithMockUser(username = "moder", password = "moder", roles = {"MODERATOR"})
    void givenUserId_whenGetUserByIdInvoked_thenStatusOkAndUserReturned() throws Exception {
        Long userId = 1L;

        mockMvc.perform(get("/moder/user/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.login").value("admin"))
                .andExpect(jsonPath("$.userRole").value("ROLE_ADMIN"))
                .andExpect(jsonPath("$.email").value("anisimov.andrew.hi@gmail.com"))
                .andExpect(jsonPath("$.name").value("Анисимов Андрей Андреевич"))
                .andExpect(jsonPath("$.address").value("Москва, Ломоносовский проспект, 3"))
                .andExpect(jsonPath("$.phoneNumber").value("+79998883344"));
    }

    @DisplayName("JUnit negative test for UserControllerTest getUserById method")
    @Test
    @WithMockUser(username = "moder", password = "moder", roles = {"MODERATOR"})
    void givenNonexistentUserId_whenGetUserByIdInvoked_thenStatusNotFoundAndErrorMessageReturned() throws Exception {
        Long nonexistentUserId = 9999L;

        mockMvc.perform(get("/moder/user/{id}", nonexistentUserId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(String.format("Пользователь с id %d не найден.", nonexistentUserId)));
    }
}