package ru.senla.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.senla.dto.AuthRequestDTO;
import ru.senla.dto.AuthResponseDTO;
import ru.senla.dto.RegisterRequestDTO;
import ru.senla.dto.RegisterResponseDTO;
import ru.senla.model.User;
import ru.senla.model.fieldenum.UserRole;
import ru.senla.security.JwtTokenProvider;
import ru.senla.service.UserService;

@RestController
public class AuthController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public ResponseEntity<?> register(RegisterRequestDTO registerRequestDTO, UserRole userRole) {
        User u = new User(registerRequestDTO.getLogin(), registerRequestDTO.getPassword(), userRole);
        this.userService.saveUser(u);
        return new ResponseEntity<>(new RegisterResponseDTO(u.getId(), u.getLogin(), u.getRole()), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequestDTO registerRequestDTO) {
        return register(registerRequestDTO, UserRole.ROLE_USER);
    }

    @PostMapping("/admin/register-moderator")
    public ResponseEntity<?> registerModerator(@RequestBody RegisterRequestDTO registerRequestDTO) {
        return register(registerRequestDTO, UserRole.ROLE_MODERATOR);
    }

    @PostMapping("/login")
    public ResponseEntity<?> auth(@RequestBody AuthRequestDTO request) {
        User user = this.userService.getUserByLoginAndPassword(request.getLogin(), request.getPassword());
        String token = this.jwtTokenProvider.generateAccessToken(user);
        return new ResponseEntity<>(new AuthResponseDTO(user.getLogin(), token), HttpStatus.OK);
    }
}
