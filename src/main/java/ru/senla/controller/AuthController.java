package ru.senla.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.senla.dto.AuthRequestDto;
import ru.senla.dto.AuthResponseDto;
import ru.senla.dto.RegisterRequestDto;
import ru.senla.dto.RegisterResponseDto;
import ru.senla.model.User;
import ru.senla.model.fieldenum.UserRole;
import ru.senla.security.JwtTokenProvider;
import ru.senla.service.UserService;

import javax.validation.Valid;

@RestController
@Api(tags = "Методы для регистрации и аутентификации")
public class AuthController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    private ResponseEntity<?> register(RegisterRequestDto registerRequestDto,
                                       UserRole userRole) {
        User u = new User(registerRequestDto.getLogin(), registerRequestDto.getPassword(), userRole);

        userService.registerUser(u);

        return new ResponseEntity<>(new RegisterResponseDto(u.getId(), u.getLogin(), u.getRole()), HttpStatus.OK);
    }

    @PostMapping("/register")
    @ApiOperation(value = "Метод для регистрации пользователя с правами USER")
    public ResponseEntity<?> registerUser(@ApiParam(value = "Модель запроса RegisterRequestDto", required = true)
                                          @Valid
                                          @RequestBody
                                          RegisterRequestDto registerRequestDto) {
        return register(registerRequestDto, UserRole.ROLE_USER);
    }

    @PostMapping("/admin/register-moderator")
    @ApiOperation(value = "Метод для регистрации пользователя с правами MODERATOR")
    public ResponseEntity<?> registerModerator(@ApiParam(value = "Модель запроса RegisterRequestDto", required = true)
                                               @Valid
                                               @RequestBody
                                               RegisterRequestDto registerRequestDto) {
        return register(registerRequestDto, UserRole.ROLE_MODERATOR);
    }

    @PostMapping("/login")
    @ApiOperation(value = "Метод для аутентификации пользователя")
    public ResponseEntity<?> auth(@ApiParam(value = "Модель запроса AuthRequestDto", required = true)
                                  @Valid
                                  @RequestBody
                                  AuthRequestDto request) {
        User user = userService.getUserByLoginAndPassword(request.getLogin(), request.getPassword());
        String token = jwtTokenProvider.generateAccessToken(user);

        return new ResponseEntity<>(new AuthResponseDto(user.getLogin(), token), HttpStatus.OK);
    }
}
