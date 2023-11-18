package ru.skypro.homework.service;

import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.model.UserEntity;

public interface UserService {

    /**
     * Метод обновляет пароль для пользователя.
     * @param oldPass старый пароль
     * @param newPass новый пароль
     */
    void setNewPassword(NewPassword newPass);

    /**
     * Метод возвращает информацию об авторизованном пользователе.
     * @return объект {@link UserEntity}, содержащий информацию о пользователе.
     */
    UserEntity getUserInfo();

    /**
     * Метод обновляет информацию о пользователе.
     * @param updateUser объект содержащий поля с именем, фамилией и номером телефона.
     * @return объект {@link UserEntity}
     */
    UserEntity updateUserInfo(UpdateUser updateUser);
}
