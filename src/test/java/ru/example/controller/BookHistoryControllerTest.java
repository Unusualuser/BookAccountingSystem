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
import ru.example.service.BookHistoryService;
import ru.example.service.BookStorageService;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
class BookHistoryControllerTest {
    @Autowired
    private BookHistoryService bookHistoryService;

    @Autowired
    private BookStorageService bookStorageService;

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("Integration positive test for BookHistoryControllerTest rentBook method")
    @Test
    @WithMockUser(username = "user", password = "user", roles = {"USER"})
    void givenBookId_whenRentBookInvoked_thenStatusOkAndBookRented() throws Exception {
        Long bookId = 1L;
        Long oldBookQuantity = bookStorageService.getQuantityByBookId(bookId);

        mockMvc.perform(post("/rent-book")
                            .param("bookId", String.valueOf(bookId)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.rentedBookId").value(1));

        assertTrue(bookHistoryService.getBookHistoriesByBookIdForPeriod(bookId, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1)).size() > 0);
        assertEquals(oldBookQuantity - 1, bookStorageService.getQuantityByBookId(bookId));
    }

    @DisplayName("Integration negative test for BookHistoryControllerTest rentBook method")
    @Test
    @WithMockUser(username = "user", password = "user", roles = {"USER"})
    void givenNonexistentBookId_whenRentBookInvoked_thenStatusNotFoundAndErrorMessageReturned() throws Exception {
        Long nonexistentBookId = 9999L;

        mockMvc.perform(post("/rent-book")
                            .param("bookId", String.valueOf(nonexistentBookId)))
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.error").value(String.format("Книга с id %d не найдена", nonexistentBookId)));
    }

    @DisplayName("Integration positive test for BookHistoryControllerTest returnRentedBook method")
    @Test
    @WithMockUser(username = "moder", password = "moder", roles = {"MODERATOR"})
    void givenBookHistoryId_whenReturnRentedBookInvoked_thenStatusOkAndBookRentClosed() throws Exception {
        Long bookHistoryId = 3L;

        mockMvc.perform(patch("/moder/return-rented-book")
                            .param("bookHistoryId", String.valueOf(bookHistoryId)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.closedBookHistoryId").value(3));

        assertEquals(bookHistoryService.getBookHistoryById(bookHistoryId).getReturnDate(), LocalDate.now());
    }

    @DisplayName("Integration negative test for BookHistoryControllerTest returnRentedBook method")
    @Test
    @WithMockUser(username = "moder", password = "moder", roles = {"MODERATOR"})
    void givenNonexistentBookHistoryId_whenReturnRentedBookInvoked_thenStatusNotFoundAndErrorMessageReturned() throws Exception {
        Long nonexistentBookHistoryId = 9999L;

        mockMvc.perform(patch("/moder/return-rented-book")
                            .param("bookHistoryId", String.valueOf(nonexistentBookHistoryId)))
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.error").value(String.format("Запись истории аренды с id %d не найдена.", nonexistentBookHistoryId)));
    }

    @DisplayName("Integration positive test for BookHistoryControllerTest getFullBookHistoryByBookId method")
    @Test
    @WithMockUser(username = "moder", password = "moder", roles = {"MODERATOR"})
    void givenBookId_whenGetFullBookHistoryByBookIdInvoked_thenStatusOkAndFullBookHistoryReturned() throws Exception {
        Long bookId = 1L;

        mockMvc.perform(get("/moder/full-book-history-by-book-id")
                            .param("bookId", String.valueOf(bookId)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$").isArray())
                        .andExpect(jsonPath("$", hasSize(3)))
                        .andExpect(jsonPath("$.[0].id").value(1))
                        .andExpect(jsonPath("$.[1].id").value(2))
                        .andExpect(jsonPath("$.[2].id").value(3));
    }

    @DisplayName("Integration negative test for BookHistoryControllerTest getFullBookHistoryByBookId method")
    @Test
    @WithMockUser(username = "moder", password = "moder", roles = {"MODERATOR"})
    void givenNonexistentBookId_whenGetFullBookHistoryByBookIdInvoked_thenStatusNotFoundAndErrorMessageReturned() throws Exception {
        Long nonexistentBookId = 9999L;

        mockMvc.perform(get("/moder/full-book-history-by-book-id")
                            .param("bookId", String.valueOf(nonexistentBookId)))
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.error").value(String.format("Книга с id %d не найдена", nonexistentBookId)));
    }

    @DisplayName("Integration positive test for BookHistoryControllerTest getBookHistoriesByBookIdForPeriod method")
    @Test
    @WithMockUser(username = "moder", password = "moder", roles = {"MODERATOR"})
    void givenBookIdAndPeriod_whenGetBookHistoriesByBookIdForPeriodInvoked_thenStatusOkAndBookHistoriesByBookIdForPeriodReturned() throws Exception {
        Long bookId = 1L;
        LocalDate beginDate = LocalDate.of(2022, 6, 1);
        LocalDate endDate = LocalDate.of(2022, 8, 1);

        mockMvc.perform(get("/moder/book-histories-by-book-id-for-period")
                            .param("bookId", String.valueOf(bookId))
                            .param("beginDate", String.valueOf(beginDate))
                            .param("endDate", String.valueOf(endDate)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$").isArray())
                        .andExpect(jsonPath("$", hasSize(2)))
                        .andExpect(jsonPath("$.[0].id").value(1))
                        .andExpect(jsonPath("$.[1].id").value(2));
    }

    @DisplayName("Integration negative test for BookHistoryControllerTest getFullBookHistoryByBookId method")
    @Test
    @WithMockUser(username = "moder", password = "moder", roles = {"MODERATOR"})
    void givenNonexistentBookIdAndPeriod_whenGetBookHistoriesByBookIdForPeriodInvoked_thenStatusNotFoundAndErrorMessageReturned() throws Exception {
        Long nonexistentBookId = 9999L;
        LocalDate beginDate = LocalDate.of(2022, 6, 1);
        LocalDate endDate = LocalDate.of(2022, 8, 1);

        mockMvc.perform(get("/moder/book-histories-by-book-id-for-period")
                            .param("bookId", String.valueOf(nonexistentBookId))
                            .param("beginDate", String.valueOf(beginDate))
                            .param("endDate", String.valueOf(endDate)))
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.error").value(String.format("Книга с id %d не найдена", nonexistentBookId)));
    }

    @DisplayName("Integration positive test for BookHistoryControllerTest findAndGetExpiredRent method")
    @Test
    @WithMockUser(username = "moder", password = "moder", roles = {"MODERATOR"})
    void whenFindAndGetExpiredRentInvoked_thenStatusOkAndExpiredRentsReturned() throws Exception {
        mockMvc.perform(get("/moder/expired-rent"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$").isArray())
                        .andExpect(jsonPath("$", hasSize(1)))
                        .andExpect(jsonPath("$.[0].id").value(3));
    }
}