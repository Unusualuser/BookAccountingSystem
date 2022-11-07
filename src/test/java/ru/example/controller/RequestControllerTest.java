package ru.example.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import ru.example.service.RequestService;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
class RequestControllerTest {
    @Autowired
    private RequestService requestService;

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("Integration positive test for RequestControllerTest requestBook method")
    @Test
    @WithMockUser(username = "user", password = "user", roles = {"USER"})
    void givenBookId_whenRequestBookInvoked_thenStatusOkAndBookRequested() throws Exception {
        Long bookIdToRequest = 4L;

        mockMvc.perform(post("/request-book-by-id")
                        .param("bookId", String.valueOf(bookIdToRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestedBook").value(4))
                .andExpect(jsonPath("$.userLogin").value("user"));

        assertEquals(2, requestService.getAllRequestsByBookId(bookIdToRequest).size());
    }

    @DisplayName("Integration negative test for RequestControllerTest requestBook method")
    @Test
    @WithMockUser(username = "user", password = "user", roles = {"USER"})
    void givenNonexistentBookId_whenRequestBookInvoked_thenStatusNotFoundAndErrorMessageReturned() throws Exception {
        Long nonexistentBookIdToRequest = 9999L;

        mockMvc.perform(post("/request-book-by-id")
                        .param("bookId", String.valueOf(nonexistentBookIdToRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(String.format("Книга с id %d не найдена", nonexistentBookIdToRequest)));
    }

    @DisplayName("Integration positive test for RequestControllerTest getRequestsByBookIdForPeriod method")
    @Test
    @WithMockUser(username = "moder", password = "moder", roles = {"MODERATOR"})
    void givenBookIdAndPeriod_whenGetRequestsByBookIdForPeriodInvoked_thenStatusOkAndRequestsForPeriodByBookIdReturned() throws Exception {
        Long bookId = 4L;
        LocalDateTime beginDttm = LocalDateTime.now().minusMinutes(1);
        LocalDateTime endDttm = LocalDateTime.now();

        mockMvc.perform(get("/moder/requests-by-book-id-for-period")
                        .param("bookId", String.valueOf(bookId))
                        .param("beginDttm", String.valueOf(beginDttm))
                        .param("endDttm", String.valueOf(endDttm)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].bookId").value(bookId));
    }

    @DisplayName("Integration negative test for RequestControllerTest getRequestsByBookIdForPeriod method")
    @Test
    @WithMockUser(username = "moder", password = "moder", roles = {"MODERATOR"})
    void givenNonexistentBookIdAndPeriod_whenGetRequestsByBookIdForPeriodInvoked_thenStatusNotFoundAndErrorMessageReturned() throws Exception {
        Long nonexistentBookId = 9999L;
        LocalDateTime beginDttm = LocalDateTime.now().minusMinutes(1);
        LocalDateTime endDttm = LocalDateTime.now();

        mockMvc.perform(get("/moder/requests-by-book-id-for-period")
                        .param("bookId", String.valueOf(nonexistentBookId))
                        .param("beginDttm", String.valueOf(beginDttm))
                        .param("endDttm", String.valueOf(endDttm)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(String.format("Книга с id %d не найдена", nonexistentBookId)));
    }

    @DisplayName("Integration positive test for RequestControllerTest getAllRequestsByBookId method")
    @Test
    @WithMockUser(username = "moder", password = "moder", roles = {"MODERATOR"})
    void givenBookId_whenGetAllRequestsByBookIdInvoked_thenStatusOkAndAllRequestsByBookIdReturned() throws Exception {
        Long bookId = 4L;

        mockMvc.perform(get("/moder/requests-by-book-id")
                        .param("bookId", String.valueOf(bookId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].bookId").value(bookId));
    }

    @DisplayName("Integration negative test for RequestControllerTest getAllRequestsByBookId method")
    @Test
    @WithMockUser(username = "moder", password = "moder", roles = {"MODERATOR"})
    void givenNonexistentBookId_whenGetAllRequestsByBookIdInvoked_thenStatusNotFoundAndErrorMessageReturned() throws Exception {
        Long nonexistentBookId = 9999L;

        mockMvc.perform(get("/moder/requests-by-book-id")
                        .param("bookId", String.valueOf(nonexistentBookId)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(String.format("Книга с id %d не найдена", nonexistentBookId)));
    }
}