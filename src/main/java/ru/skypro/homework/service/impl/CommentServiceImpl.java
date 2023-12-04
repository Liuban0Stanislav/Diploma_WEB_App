package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.CommentEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    private final AdRepository adRepository;
    private final UserService userService;

    public CommentServiceImpl(CommentRepository commentRepository, CommentMapper commentMapper, AdRepository adRepository, UserService userService) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.adRepository = adRepository;
        this.userService = userService;
    }

    @Override
    public Comments getComments(Integer id) {
        log.info("Запущен метод сервиса {}", LoggingMethodImpl.getMethodName());
        List<Comment> comments = commentRepository.findByAdId(id).stream()
                .map(comment -> commentMapper.mapToCommentDto(comment))
                .collect(Collectors.toList());

        return new Comments(comments.size(), comments);
    }

    @Override
    public Comment addComment(Integer id, CreateOrUpdateComment createOrUpdateComment, String username) {
        log.info("Запущен метод сервиса {}", LoggingMethodImpl.getMethodName());
        AdEntity ad = adRepository.findById(id).get();
        UserEntity author = userService.checkUserByUsername(username);

        CommentEntity comment = new CommentEntity();
        comment.setAd(ad);
        comment.setAuthor(author);
        comment.setCreatedAt(System.currentTimeMillis());
        comment.setText(createOrUpdateComment.getText());

        return commentMapper.mapToCommentDto(comment);
    }

    @Override
    public String deleteComment(Integer commentId, String username) {
        log.info("Запущен метод сервиса {}", LoggingMethodImpl.getMethodName());
        Optional<CommentEntity> comment = commentRepository.findById(commentId);
        if (comment.isPresent()) {
            UserEntity author = userService.checkUserByUsername(username);
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

    @Override
    public Comment updateComment(Integer commentId, CreateOrUpdateComment createOrUpdateComment, String username) {
        log.info("Запущен метод сервиса {}", LoggingMethodImpl.getMethodName());
        Optional<CommentEntity> commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isPresent()) {
            CommentEntity comment = commentOptional.get();
            UserEntity author = userService.checkUserByUsername(username);
            if (author.getComments().contains(comment)) {
                comment.setText(createOrUpdateComment.getText());
                commentRepository.save(comment);
                return commentMapper.mapToCommentDto(comment); //'200' Ok, comment updated
            } else {
                return commentMapper.mapToCommentDto(comment); //'403' For the user update is forbidden
            }
        }
        return null; //'404' Comment not found
    }

}

