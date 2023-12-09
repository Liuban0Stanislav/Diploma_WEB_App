package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class Ad {

    private Integer author; // id автора объявления
    private String image; // ссылка на картинку объявления
    private Integer pk; // id объявления
    private Integer price; // цена объявления
    private String title; // заголовок объявления
}
