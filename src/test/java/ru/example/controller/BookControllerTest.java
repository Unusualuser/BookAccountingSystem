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
import ru.example.service.BookService;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
class BookControllerTest {
    @Autowired
    private BookService bookService;

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("Integration positive test for BookControllerTest saveBook method")
    @Test
    @WithMockUser(username = "moder", password = "moder", roles = {"MODERATOR"})
    void givenNewBook_whenSaveBookInvoked_thenStatusOkAndBookInfoReturnedAndNewBookSaved() throws Exception {
        final Path newBookJsonPath = new ClassPathResource("bookcontrollerjson/newBook.json").getFile().toPath();
        final String bookToSave = Files.readString(newBookJsonPath);

        mockMvc.perform(post("/moder/book")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(bookToSave))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(5))
                        .andExpect(jsonPath("$.name").value("newBook"))
                        .andExpect(jsonPath("$.publicationYear").value(2022))
                        .andExpect(jsonPath("$.author").value("author"))
                        .andExpect(jsonPath("$.description").value("description"));

        assertNotNull(bookService.getBookById(5L));
    }

    @DisplayName("Integration negative test for BookControllerTest saveBook method")
    @Test
    @WithMockUser(username = "moder", password = "moder", roles = {"MODERATOR"})
    void givenInvalidNewBook_whenSaveBookInvoked_thenStatusBadRequestAndErrorMessageReturned() throws Exception {
        final Path invalidNewBookJsonPath = new ClassPathResource("bookcontrollerjson/invalidNewBook.json").getFile().toPath();
        final String invalidBookToSave = Files.readString(invalidNewBookJsonPath);

        mockMvc.perform(post("/moder/book")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidBookToSave))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.error").value("Некорректные входные данные. [Имя автора должно быть не меньше 4 и не больше 100 символов]"));
    }

    @DisplayName("Integration positive test for BookControllerTest updateBookInfo method")
    @Test
    @WithMockUser(username = "moder", password = "moder", roles = {"MODERATOR"})
    void givenBookInfo_whenUpdateBookInfoInvoked_thenStatusOkAndBookInfoReturnedAndBookUpdated() throws Exception {
        final Path bookInfoJsonPath= new ClassPathResource("bookcontrollerjson/updateBookInfo.json").getFile().toPath();
        final String bookInfoToUpdate = Files.readString(bookInfoJsonPath);

        mockMvc.perform(patch("/moder/book")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(bookInfoToUpdate))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(3))
                        .andExpect(jsonPath("$.description").value("Очень интересная книга"));

        assertEquals(bookService.getBookById(3L).getDescription(), "Очень интересная книга");
    }

    @DisplayName("Integration negative test for BookControllerTest updateBookInfo method")
    @Test
    @WithMockUser(username = "moder", password = "moder", roles = {"MODERATOR"})
    void givenInvalidBookInfo_whenUpdateBookInfoInvoked_thenStatusBadRequestAndErrorMessageReturned() throws Exception {
        final Path invalidBookInfoJsonPath = new ClassPathResource("bookcontrollerjson/invalidUpdateBookInfo.json").getFile().toPath();
        final String invalidBookInfoToUpdate= Files.readString(invalidBookInfoJsonPath);

        mockMvc.perform(patch("/moder/book")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidBookInfoToUpdate))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.error").value("Некорректные входные данные. [Описание должно быть не больше 70 символов]"));
    }

    @DisplayName("Integration positive test for BookControllerTest deleteBookById method")
    @Test
    @WithMockUser(username = "moder", password = "moder", roles = {"MODERATOR"})
    void givenBookId_whenDeleteBookByIdInvoked_thenStatusOkReturnedAndBookDeleted() throws Exception {
        Long bookIdToDelete = 1L;

        mockMvc.perform(delete("/moder/book/{id}", bookIdToDelete))
                        .andExpect(status().isOk());

        assertEquals(bookService.getAllBooks().stream().filter(book -> book.getId().equals(bookIdToDelete)).count(), 0);
    }

    @DisplayName("Integration negative test for BookControllerTest deleteBookById method")
    @Test
    @WithMockUser(username = "moder", password = "moder", roles = {"MODERATOR"})
    void givenNonexistentBookId_whenDeleteBookByIdInvoked_thenStatusNotFoundAndErrorMessageReturned() throws Exception {
        Long nonexistentBookId = 99999L;

        mockMvc.perform(delete("/moder/book/{id}", nonexistentBookId))
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.error").value(String.format("Книга с id %d не найдена", nonexistentBookId)));
    }

    @DisplayName("Integration positive test for BookControllerTest getBookById method")
    @Test
    @WithMockUser(username = "user", password = "user", roles = {"USER"})
    void givenBookId_whenGetBookByIdInvoked_thenStatusOkAndBookReturned() throws Exception {
        Long bookId = 1L;

        mockMvc.perform(get("/book/{id}", bookId))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(1))
                        .andExpect(jsonPath("$.name").value("451 градус по Фаренгейту"))
                        .andExpect(jsonPath("$.publicationYear").value(1953))
                        .andExpect(jsonPath("$.author").value("Рэй Дуглас Брэдбери"))
                        .andExpect(jsonPath("$.description").value("Антиутопия"));
    }

    @DisplayName("Integration negative test for BookControllerTest getBookById method")
    @Test
    @WithMockUser(username = "user", password = "user", roles = {"USER"})
    void givenNonexistentBookId_whenGetBookByIdInvoked_thenStatusNotFoundAndErrorMessageReturned() throws Exception {
        Long nonexistentBookId = 99999L;

        mockMvc.perform(get("/book/{id}", nonexistentBookId))
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.error").value(String.format("Книга с id %d не найдена", nonexistentBookId)));
    }

    @DisplayName("Integration positive test for BookControllerTest getAllBooks method")
    @Test
    @WithMockUser(username = "user", password = "user", roles = {"USER"})
    void whenGetAllBooksInvoked_thenStatusOkAndAllBooksReturned() throws Exception {
        mockMvc.perform(get("/all-books"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$").isArray())
                        .andExpect(jsonPath("$", hasSize(4)))
                        .andExpect(jsonPath("$.[0].id").value(1))
                        .andExpect(jsonPath("$.[1].id").value(2))
                        .andExpect(jsonPath("$.[2].id").value(3))
                        .andExpect(jsonPath("$.[3].id").value(4));
    }
}