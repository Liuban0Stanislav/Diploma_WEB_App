package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class User {

    private Integer id; // id пользователя
    private String email; // логин пользователя
    private String firstName; // имя пользователя
    private String lastName; // фамилия пользователя
    private String phone; // телефон пользователя
    private String image; // ссылка на аватар пользователя
}
