package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class CreateOrUpdateAd {
    private String title; // заголовок объявления
    private Integer price; // цена объявления
    private String description; // описание объявления
}
