package ru.senla.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.senla.dto.RequestBookResponseDto;
import ru.senla.dto.RequestDto;
import ru.senla.model.Request;
import ru.senla.service.RequestService;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
@Api(tags = "Методы для взаимодействия с сущностью Request")
public class RequestController {
    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping("/request-book-by-id")
    @ApiOperation(value = "Метод для добавления запроса на книгу по id книги")
    public ResponseEntity<?> requestBook(@ApiParam(value = "Идентификатор книги", required = true)
                                         @Min(value = 0L, message = "Значение bookId должно быть положительным")
                                         @RequestParam
                                         Long bookId,
                                         @ApiIgnore Authentication authentication) {
        String userLogin = authentication.getName();

        requestService.requestBookByIdAndUserLogin(bookId, userLogin);

        return new ResponseEntity<>(new RequestBookResponseDto(bookId, userLogin), HttpStatus.OK);
    }

    @GetMapping("/moder/requests-by-book-id-for-period")
    @ApiOperation(value = "Метод для получения запросов на книгу по id книги за период")
    public ResponseEntity<?> getRequestsByBookIdForPeriod(@ApiParam(value = "Идентификатор книги", required = true)
                                                          @Min(value = 0L, message = "Значение bookId должно быть положительным")
                                                          @RequestParam
                                                          Long bookId,
                                                          @ApiParam(value = "Дата и время начала периода", required = true)
                                                          @RequestParam
                                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                          LocalDateTime beginDttm,
                                                          @ApiParam(value = "Дата и время конца периода", required = true)
                                                          @RequestParam
                                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                          LocalDateTime endDttm) {
        List<Request> requests = requestService.getRequestsByBookIdForPeriod(bookId, beginDttm, endDttm);

        List<RequestDto> requestsDto = requests.stream()
                                               .map(request -> new RequestDto(request.getId(),
                                                                              request.getBook().getId(),
                                                                              request.getUser().getId(),
                                                                              request.getCreateDttm(),
                                                                              request.getRequestStatus()))
                                               .collect(Collectors.toList());

        return new ResponseEntity<>(requestsDto, HttpStatus.OK);
    }

    @GetMapping("/moder/requests-by-book-id")
    @ApiOperation(value = "Метод для получения всех запросов на книгу по id книги")
    public ResponseEntity<?> getAllRequestsByBookId(@ApiParam(value = "Идентификатор книги", required = true)
                                                    @Min(value = 0L, message = "Значение bookId должно быть положительным")
                                                    @RequestParam
                                                    Long bookId) {
        List<Request> allRequests = requestService.getAllRequestsByBookId(bookId);

        List<RequestDto> allRequestsDto = allRequests.stream()
                                                     .map(request -> new RequestDto(request.getId(),
                                                                                    request.getBook().getId(),
                                                                                    request.getUser().getId(),
                                                                                    request.getCreateDttm(),
                                                                                    request.getRequestStatus()))
                                                     .collect(Collectors.toList());

        return new ResponseEntity<>(allRequestsDto, HttpStatus.OK);
    }
}
