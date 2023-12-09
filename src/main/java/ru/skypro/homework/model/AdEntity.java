package ru.skypro.homework.model;

import lombok.*;
import ru.skypro.homework.dto.Comment;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ads")
public class AdEntity extends ModelEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private Integer price;
    @Column(nullable = false, length = 250)
    private String description;

    @OneToOne
    private PhotoEntity photo;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;

    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL)
    private Collection<CommentEntity> comments;

    private String filePath; //путь на ПК
}
