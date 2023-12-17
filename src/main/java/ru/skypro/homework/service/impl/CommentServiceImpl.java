package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.CommentEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CommentService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private final UserServiceImpl userService;

    public CommentServiceImpl(CommentRepository commentRepository,
                              CommentMapper commentMapper,
                              AdRepository adRepository,
                              UserRepository userRepository,
                              UserServiceImpl userService) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.adRepository = adRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    /**
     * Метод получает список всех комментариев объявления
     *
     * @param id - id объявления
     * @return возвращает DTO - список моделей комментариев
     */
    @Transactional
    @Override
    public Comments getComments(Integer id) {
        log.info("Запущен метод сервиса {}", LoggingMethodImpl.getMethodName());
        List<Comment> comments = commentRepository.findByAdId(id).stream()
                .map(comment -> commentMapper.mapToCommentDto(comment))
                .collect(Collectors.toList());

        return new Comments(comments.size(), comments);
    }

    /**
     * Метод добавляет комментарий к объявлению
     *
     * @param id                    - id объявления
     * @param createOrUpdateComment - DTO модель класса {@link CreateOrUpdateComment}
     * @param username              - логин пользователя
     * @return DTO модель комментария
     */
    @Transactional
    @Override
    public Comment addComment(Integer id, CreateOrUpdateComment createOrUpdateComment, String username) {
        log.info("Запущен метод сервиса {}", LoggingMethodImpl.getMethodName());

        UserEntity author = userService.getUser(username);//todo заменить метод на getUser
        AdEntity ad = adRepository.findById(id).orElse(null);

        //Создаем сущность comment и заполняем поля
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setAuthor(author);
        commentEntity.setAd(ad);
        commentEntity.setText(createOrUpdateComment.getText());
        commentEntity.setCreatedAt(System.currentTimeMillis());

        //Сохраняем сущность commentEntity в БД
        commentRepository.save(commentEntity);

        //Заполняем поле с комментариями у пользователя и сохраняем в БД
        author.getComments().add(commentEntity);
        userRepository.save(author);

        //Создаем возвращаемую сущность ДТО comment и заполняем поля
        Comment commentDTO = new Comment();
        commentDTO.setAuthor(author.getId());

        Integer avatarId = author.getPhoto().getId();
        log.info("id автора комментария - {}", author.getId());
        log.info("URL для получения аватара автора комментария: /photo/image/{}", avatarId);
        commentDTO.setAuthorImage("/photo/image/" + avatarId);

        commentDTO.setAuthorFirstName(author.getFirstName());
        commentDTO.setCreatedAt(commentEntity.getCreatedAt());
        commentDTO.setPk(commentRepository.findFirstByText(createOrUpdateComment.getText()).getId());
        commentDTO.setText(commentRepository.findFirstByText(createOrUpdateComment.getText()).getText());

        return commentDTO;
    }

    /**
     * Метод удаляет комментарий
     *
     * @param commentId - id комментария
     * @param username  - логин пользователя
     * @return строку с результатом выполнения метода
     */
    @Override
    public String deleteComment(Integer commentId, String username) {
        log.info("Запущен метод сервиса {}", LoggingMethodImpl.getMethodName());
        Optional<CommentEntity> comment = commentRepository.findById(commentId);
        if (comment.isPresent()) {
            UserEntity author = userService.getUser(username);
            if (author.getRole().equals(Role.ADMIN)) {
                commentRepository.delete(comment.get());
                return "комментарий удален";
            } else if (author.getRole().equals(Role.USER)) {
                if (comment.get().getAuthor().getUserName().equals(author.getUserName())) {
                    commentRepository.delete(comment.get());
                    return "комментарий удален";
                } else {
                    return "forbidden"; //'403' For the user deletion is forbidden
                }
            }
        }
        return "not found"; //'404' Comment not found
    }

    /**
     * Метод обновляет комментарий
     *
     * @param commentId             - id комментария
     * @param createOrUpdateComment - DTO модель класса {@link CreateOrUpdateComment}
     * @return строку с результатом выполнения метода
     */
    @Override
    public Comment updateComment(Integer adId,
                                 Integer commentId,
                                 CreateOrUpdateComment createOrUpdateComment) {
        log.info("Использован метод сервиса: {}", LoggingMethodImpl.getMethodName());

        CommentEntity comment = commentRepository.findById(commentId).get();
        comment.setText(createOrUpdateComment.getText());
        commentRepository.save(comment);
        return commentMapper.mapToCommentDto(comment);
    }

}

