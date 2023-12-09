package ru.skypro.homework.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Comment {

    private Integer author; // id автора комментария
    private String authorImage; // ссылка на аватар автора комментария
    private String authorFirstName; // имя создателя комментария
    private Long createdAt; // дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970
    private Integer pk; // id комментария
    private String text; // текст комментария

}
