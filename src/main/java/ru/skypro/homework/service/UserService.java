package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.model.UserEntity;

import java.io.IOException;

public interface UserService {

    /**
     * Метод обновляет пароль для авторизованного пользователя.
     *
     * @param oldPass        старый пароль
     * @param newPass        новый пароль
     * @param authentication
     */
    void setPassword(NewPassword newPass, Authentication authentication);

    /**
     * Метод возвращает информацию об авторизованном пользователе.
     * @param authentication
     * @return объект {@link UserEntity}, содержащий информацию о пользователе.
     */
    UserEntity getUser(String userName);

    /**
     * Метод обновляет информацию об авторизованном пользователе.
     * @param updateUser объект содержащий поля с именем, фамилией и номером телефона.
     * @param authentication
     * @return объект {@link UserEntity}
     */
    UserEntity updateUser(UpdateUser updateUser, Authentication authentication);

    /**
     * Метод обновляет аватар авторизованного пользователя.
     * @param image
     * @return true или false
     * @throws IOException
     */
    void updateUserImage(MultipartFile image, Authentication authentication) throws IOException;
}
