package ru.senla.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.senla.dto.RequestBookResponseDTO;
import ru.senla.dto.RequestDTO;
import ru.senla.model.Request;
import ru.senla.service.RequestService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class RequestController {
    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping("/request-book-by-id")
    public ResponseEntity<?> requestBook(@RequestParam Long bookId, Authentication authentication) {
        String userLogin = authentication.getName();

        this.requestService.requestBookByIdAndUserLogin(bookId, userLogin);
        return new ResponseEntity<>(new RequestBookResponseDTO(bookId, userLogin), HttpStatus.OK);
    }

    @GetMapping("/moder/requests-by-book-id-for-period")
    public ResponseEntity<?> getRequestsByBookIdForPeriod(@RequestParam Long bookId,
                                                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime beginDttm,
                                                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDttm) {
        List<Request> requests = this.requestService.getRequestsByBookIdForPeriod(bookId, beginDttm, endDttm);
        List<RequestDTO> requestsDTO = requests.stream()
                                               .map(request -> new RequestDTO(
                                                       request.getId(),
                                                       request.getBook().getId(),
                                                       request.getUser().getId(),
                                                       request.getCreateDttm(),
                                                       request.getRequestStatus()))
                                               .collect(Collectors.toList());
        return new ResponseEntity<>(requestsDTO, HttpStatus.OK);
    }

    @GetMapping("/moder/requests-by-book-id")
    public ResponseEntity<?> getAllRequestsByBookId(@RequestParam Long bookId) {
        List<Request> allRequests = this.requestService.getAllRequestsByBookId(bookId);
        List<RequestDTO> allRequestsDTO = allRequests.stream()
                .map(request -> new RequestDTO(
                        request.getId(),
                        request.getBook().getId(),
                        request.getUser().getId(),
                        request.getCreateDttm(),
                        request.getRequestStatus()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(allRequestsDTO, HttpStatus.OK);
    }
}
