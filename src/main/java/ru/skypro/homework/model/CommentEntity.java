package ru.skypro.homework.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String text;
    private Long createdAt;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "author_id")
    private UserEntity author;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "ad_id")
    private AdEntity ad;
}
