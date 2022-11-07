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
import ru.example.service.BookStorageService;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
class BookStorageControllerTest {
    @Autowired
    private BookStorageService bookStorageService;

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("Integration positive test for BookStorageControllerTest addQuantity method")
    @Test
    @WithMockUser(username = "moder", password = "moder", roles = {"MODERATOR"})
    void givenBookIdAndQuantityToAdd_whenAddQuantityInvoked_thenStatusOkAndBookQuantityIncreased() throws Exception {
        final Path addQuantityJsonPath = new ClassPathResource("bookstoragecontrollerjson/addQuantity.json").getFile().toPath();
        final String addQuantityBody = Files.readString(addQuantityJsonPath);
        Long oldBookQuantity = bookStorageService.getQuantityByBookId(4L);

        mockMvc.perform(patch("/moder/add-quantity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addQuantityBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(4))
                .andExpect(jsonPath("$.addedQuantity").value(1));

        assertEquals(oldBookQuantity + 1, bookStorageService.getQuantityByBookId(4L));
    }

    @DisplayName("Integration negative test for BookHistoryControllerTest addQuantity method")
    @Test
    @WithMockUser(username = "moder", password = "moder", roles = {"MODERATOR"})
    void givenNonexistentBookIdAndQuantityToAdd_whenAddQuantityInvoked_thenStatusNotFoundAndErrorMessageReturned() throws Exception {
        final Path invalidAddQuantityJsonPath = new ClassPathResource("bookstoragecontrollerjson/invalidAddQuantity.json").getFile().toPath();
        final String invalidAddQuantityBody = Files.readString(invalidAddQuantityJsonPath);

        mockMvc.perform(patch("/moder/add-quantity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidAddQuantityBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(String.format("Книга с id %d не найдена", 9999)));
    }

    @DisplayName("Integration positive test for BookStorageControllerTest reduceQuantity method")
    @Test
    @WithMockUser(username = "moder", password = "moder", roles = {"MODERATOR"})
    void givenBookIdAndQuantityToReduce_whenReduceQuantityInvoked_thenStatusOkAndBookQuantityReduced() throws Exception {
        final Path reduceQuantityJsonPath = new ClassPathResource("bookstoragecontrollerjson/reduceQuantity.json").getFile().toPath();
        final String reduceQuantityBody = Files.readString(reduceQuantityJsonPath);
        Long oldBookQuantity = bookStorageService.getQuantityByBookId(1L);

        mockMvc.perform(patch("/moder/reduce-quantity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reduceQuantityBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(1))
                .andExpect(jsonPath("$.reducedQuantity").value(1));

        assertEquals(oldBookQuantity - 1, bookStorageService.getQuantityByBookId(1L));
    }

    @DisplayName("Integration negative test for BookHistoryControllerTest reduceQuantity method")
    @Test
    @WithMockUser(username = "moder", password = "moder", roles = {"MODERATOR"})
    void givenNonexistentBookIdAndQuantityToReduce_whenReduceQuantityInvoked_thenStatusNotFoundAndErrorMessageReturned() throws Exception {
        final Path invalidReduceQuantityJsonPath = new ClassPathResource("bookstoragecontrollerjson/invalidReduceQuantity.json").getFile().toPath();
        final String invalidReduceQuantityBody = Files.readString(invalidReduceQuantityJsonPath);

        mockMvc.perform(patch("/moder/reduce-quantity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidReduceQuantityBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(String.format("Книга с id %d не найдена", 9999)));
    }

    @DisplayName("Integration positive test for BookStorageControllerTest getQuantityByBookId method")
    @Test
    @WithMockUser(username = "user", password = "user", roles = {"USER"})
    void givenBookId_whenGetQuantityByBookIdInvoked_thenStatusOkAndBookQuantityReturned() throws Exception {;
        Long bookId = 1L;

        mockMvc.perform(get("/quantity-by-book-id/{bookId}", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(1))
                .andExpect(jsonPath("$.quantity").value(3));
    }

    @DisplayName("Integration negative test for BookHistoryControllerTest getQuantityByBookId method")
    @Test
    @WithMockUser(username = "moder", password = "moder", roles = {"MODERATOR"})
    void givenNonexistentBookId_whenGetQuantityByBookIdInvoked_thenStatusNotFoundAndErrorMessageReturned() throws Exception {
        Long nonexistentBookId = 9999L;

        mockMvc.perform(get("/quantity-by-book-id/{bookId}", nonexistentBookId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(String.format("Книга с id %d не найдена", nonexistentBookId)));
    }
}