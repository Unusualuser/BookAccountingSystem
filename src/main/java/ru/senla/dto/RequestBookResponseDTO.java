package ru.senla.dto;

import java.io.Serializable;

public class RequestBookResponseDTO implements Serializable {
    private Long requestedBook;
    private String userLogin;

    public RequestBookResponseDTO() {
    }

    public RequestBookResponseDTO(Long requestedBook, String userLogin) {
        this.requestedBook = requestedBook;
        this.userLogin = userLogin;
    }

    public Long getRequestedBook() {
        return requestedBook;
    }

    public void setRequestedBook(Long requestedBook) {
        this.requestedBook = requestedBook;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }
}
