package ru.skypro.homework.mapper;

import org.springframework.stereotype.Service;
import ru.skypro.homework.constants.Constants;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.model.CommentEntity;


@Service
public class CommentMapper {

    /**
     * Entity -> dto mapping
     *
     * @param entity input entity class
     * @return dto class
     */
    public Comment mapToCommentDto(CommentEntity entity) {
        Comment dto = new Comment();
        dto.setAuthor(entity.getAuthor().getId());
        dto.setAuthorImage(Constants.URL_PHOTO_CONSTANT + entity.getAuthor().getPhoto().getId());
        dto.setAuthorFirstName(entity.getAuthor().getFirstName());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setPk(entity.getId());
        dto.setText(entity.getText());
        return dto;
    }
}
