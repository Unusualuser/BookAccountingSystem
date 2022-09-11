package ru.senla.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.senla.dto.UpdateUserPersonalInfoRequestDTO;
import ru.senla.dto.UpdateUserPersonalInfoResponseDTO;
import ru.senla.dto.UserDTO;
import ru.senla.model.User;
import ru.senla.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@Validated
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PatchMapping("/user/update-personal-info")
    public ResponseEntity<?> updateUserPersonalInfo(@Valid
                                                    @RequestBody
                                                    UpdateUserPersonalInfoRequestDTO updateUserPersonalInfoRequestDTO,
                                                    Authentication authentication) {
        String userLogin = authentication.getName();
        this.userService.updateUserPersonalInfo(
                                                userLogin,
                                                updateUserPersonalInfoRequestDTO.getEmail(),
                                                updateUserPersonalInfoRequestDTO.getName(),
                                                updateUserPersonalInfoRequestDTO.getAddress(),
                                                updateUserPersonalInfoRequestDTO.getPhoneNumber());
        return new ResponseEntity<>(new UpdateUserPersonalInfoResponseDTO(
                                                              userLogin,
                                                              updateUserPersonalInfoRequestDTO.getEmail(),
                                                              updateUserPersonalInfoRequestDTO.getName(),
                                                              updateUserPersonalInfoRequestDTO.getAddress(),
                                                              updateUserPersonalInfoRequestDTO.getPhoneNumber()),
                                    HttpStatus.OK);
    }

    @Transactional
    @GetMapping("/moder/user/{id}")
    public ResponseEntity<?> getUserById(@Min(value = 0L, message = "Значение id должно быть положительным")
                                         @PathVariable
                                         Long id) {
        User user = this.userService.getUserById(id);
        return new ResponseEntity<>(new UserDTO(
                                                user.getId(),
                                                user.getLogin(),
                                                user.getRole(),
                                                user.getEmail(),
                                                user.getName(),
                                                user.getAddress(),
                                                user.getPhoneNumber()),
                                    HttpStatus.OK);
    }
}
