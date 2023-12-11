package ru.skypro.homework.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private PhotoEntity photo;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;

    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<CommentEntity> comments;

    private String filePath; //путь на ПК

    @Override
    public String toString() {
        return "AdEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", photo=" + (photo != null) +
                ", author=" + author +
                ", comments=" + comments +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
