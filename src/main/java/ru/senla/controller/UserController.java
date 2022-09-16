package ru.senla.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.senla.dto.UpdateUserPersonalInfoRequestDto;
import ru.senla.dto.UpdateUserPersonalInfoResponseDto;
import ru.senla.dto.UserDto;
import ru.senla.model.User;
import ru.senla.service.UserService;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@Validated
@Api(tags = "Методы для взаимодействия с сущностью User")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PatchMapping("/user/update-personal-info")
    @ApiOperation(value = "Метод для обновления персональной информации пользователя")
    public ResponseEntity<?> updateUserPersonalInfo(@ApiParam(value = "Модель запроса UpdateUserPersonalInfoRequestDto", required = true)
                                                    @Valid
                                                    @RequestBody
                                                    UpdateUserPersonalInfoRequestDto updateUserPersonalInfoRequestDto,
                                                    @ApiIgnore Authentication authentication) {
        String userLogin = authentication.getName();
        userService.updateUserPersonalInfo(userLogin,
                                           updateUserPersonalInfoRequestDto.getEmail(),
                                           updateUserPersonalInfoRequestDto.getName(),
                                           updateUserPersonalInfoRequestDto.getAddress(),
                                           updateUserPersonalInfoRequestDto.getPhoneNumber());

        return new ResponseEntity<>(new UpdateUserPersonalInfoResponseDto(userLogin,
                                                                          updateUserPersonalInfoRequestDto.getEmail(),
                                                                          updateUserPersonalInfoRequestDto.getName(),
                                                                          updateUserPersonalInfoRequestDto.getAddress(),
                                                                          updateUserPersonalInfoRequestDto.getPhoneNumber()),
                                    HttpStatus.OK);
    }

    @GetMapping("/moder/user/{id}")
    @ApiOperation(value = "Метод для получения пользователя по id")
    public ResponseEntity<?> getUserById(@ApiParam(value = "Идентификатор пользователя", required = true)
                                         @Min(value = 0L, message = "Значение id должно быть положительным")
                                         @PathVariable
                                         Long id) {
        User user = userService.getUserById(id);

        return new ResponseEntity<>(new UserDto(user.getId(),
                                                user.getLogin(),
                                                user.getRole(),
                                                user.getEmail(),
                                                user.getName(),
                                                user.getAddress(),
                                                user.getPhoneNumber()),
                                    HttpStatus.OK);
    }
}
