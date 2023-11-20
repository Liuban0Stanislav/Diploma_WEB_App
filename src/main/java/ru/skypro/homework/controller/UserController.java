package ru.skypro.homework.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.service.impl.AuthServiceImpl;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    private Authentication authentication;
    private AuthServiceImpl authService;

    public UserController(UserService userService, AuthServiceImpl authService) {
        this.userService = userService;
        this.authService = authService;
    }

//    public UserController(UserService userService, Authentication authentication, AuthServiceImpl authService) {
//        this.userService = userService;
//        this.authentication = authentication;
//        this.authService = authService;
//    }

    @PostMapping("/set_password") // http://localhost:8080/users/set_password
    public ResponseEntity setPassword(@RequestBody NewPassword newPass) {
        userService.setPassword(newPass);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me") // http://localhost:8080/users/me
    public ResponseEntity<User> getUser() {
        UserEntity user = userService.getUser();
        if (authService.getLogin() != null) {
            return ResponseEntity.ok(UserMapper.mapFromUserEntityToUser(user));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PatchMapping("/me") // http://localhost:8080/users/me
    public ResponseEntity<UpdateUser> updateUser(@RequestBody UpdateUser updateUser) {
        UserEntity user = userService.updateUser(updateUser);
        if (user != null) {
            return ResponseEntity.ok(UserMapper.mapFromUserEntityToUpdateUser(user));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    //    @PatchMapping("/me") // http://localhost:8080/users/me
//    public ResponseEntity<UpdateUser> updateUser(@RequestBody UpdateUser updateUser, Authentication authentication) {
//        UserEntity user = userService.updateUser(updateUser, authentication);
//        if (user != null) {
//            return ResponseEntity.ok(UserMapper.mapFromUserEntityToUpdateUser(user));
//        }else{
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//    }
    @PatchMapping("me/image")
    public ResponseEntity<Ad> updateUserImage(@RequestParam MultipartFile image) {

        if (authService.getLogin() != null) {
            return ResponseEntity.ok(userService.updateUserImage(image));
        } else if (authService.getLogin() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return null; //todo разобраться с ошибками 403 и 404, как и в остальных методах выше, если есть
    }

}
