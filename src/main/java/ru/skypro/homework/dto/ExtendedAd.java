package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class ExtendedAd {

    private Integer pk; // id объявления
    private String authorFirstName; // имя автора объявления
    private String authorLastName; // фамилия автора объявления
    private String description; // описание объявления
    private String email; // логин автора объявления
    private String image; // ссылка на картинку объявления
    private String phone; // телефон автора объявления
    private Integer price; // цена объявления
    private String title; // заголовок объявления
}
