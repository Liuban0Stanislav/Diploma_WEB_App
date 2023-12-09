package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class UpdateUser {

    private String firstName; // имя пользователя
    private String lastName; // фамилия пользователя
    private String phone; // телефон пользователя
}
