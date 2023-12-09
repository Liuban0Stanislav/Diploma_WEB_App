package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Comments {

    private int count; // общее количество комментариев
    private List<Comment> results;
}
